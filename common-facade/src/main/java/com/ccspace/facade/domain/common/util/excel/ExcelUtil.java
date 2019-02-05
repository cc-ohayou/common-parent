package com.ccspace.facade.domain.common.util.excel;


import com.ccspace.facade.domain.common.util.MapUtils;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ComparatorUtils;
import org.apache.commons.collections4.comparators.ComparatorChain;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * excel util
 * <p>
 * 依赖poi 3.15
 */
@SuppressWarnings("deprecation")
public class ExcelUtil {
    private static final Logger log = LoggerFactory.getLogger(ExcelUtil.class);

    /**
     * 用来验证excel与Vo中的类型是否一致 <br>
     * Map<栏位类型,只能是哪些Cell类型>
     */
    private static Map<Class<?>, Integer[]> validateMap = new HashMap<>();

    static {
        validateMap.put(String[].class, new Integer[]{CellType.STRING.getCode()});
        validateMap.put(Double[].class, new Integer[]{CellType.NUMERIC.getCode()});
        validateMap.put(String.class, new Integer[]{CellType.STRING.getCode(), CellType.NUMERIC.getCode()});
        validateMap.put(Double.class, new Integer[]{CellType.NUMERIC.getCode()});
        validateMap.put(Date.class, new Integer[]{CellType.NUMERIC.getCode(), CellType.STRING.getCode()});
        validateMap.put(Integer.class, new Integer[]{CellType.NUMERIC.getCode()});
        validateMap.put(Float.class, new Integer[]{CellType.NUMERIC.getCode()});
        validateMap.put(Long.class, new Integer[]{CellType.NUMERIC.getCode()});
        validateMap.put(Boolean.class, new Integer[]{CellType.BOOLEAN.getCode()});
    }

    /**
     * 获取cell类型的文字描述
     *
     * @param cellType
     * @return
     */
    private static String getCellTypeByInt(int cellType) {
        switch (cellType) {
            case Cell.CELL_TYPE_BLANK:
                return "Null type";
            case Cell.CELL_TYPE_BOOLEAN:
                return "Boolean type";
            case Cell.CELL_TYPE_ERROR:
                return "Error type";
            case Cell.CELL_TYPE_FORMULA:
                return "Formula type";
            case Cell.CELL_TYPE_NUMERIC:
                return "Numeric type";
            case Cell.CELL_TYPE_STRING:
                return "String type";
            default:
                return "Unknown type";
        }
    }
    public static List<Map<String, Object>> listToMap(List items) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object item : items) {
            Map map = MapUtils.java2Map(item);
            result.add(map);
        }
        return result;
    }
    /**
     * 获取单元格值
     *
     * @param cell
     * @return
     */
    private static Object getCellValue(Cell cell) {
        if (cell == null
                || (cell.getCellType() == Cell.CELL_TYPE_STRING && StringUtils.isBlank(cell.getStringCellValue()))) {
            return null;
        }
        int cellType = cell.getCellType();
        switch (cellType) {
            case Cell.CELL_TYPE_BLANK:
                return null;
            case Cell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue();
            case Cell.CELL_TYPE_ERROR:
                return cell.getErrorCellValue();
            case Cell.CELL_TYPE_FORMULA:
                return cell.getNumericCellValue();
            case Cell.CELL_TYPE_NUMERIC:
                return cell.getNumericCellValue();
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            default:
                return null;
        }
    }

    /**
     * 导出excel
     * 根据文件后缀判断是2003还是2007及以上
     *
     * @param filePath 文件路径
     * @param headers  文件头
     * @param dataset  数据
     * @param <T>      类型
     * @throws Exception
     */
    public static <T> void exportExcel(String filePath, Map<String, String> headers, Collection<T> dataset) throws Exception {
        Boolean xlsFlag = filePath.toLowerCase().endsWith(ExcelConst.SUFFIX_2003);

        File file = new File(filePath);
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            exportExcel(headers, dataset, out, xlsFlag);

        } finally {
            if (out != null) {
                out.close();
            }
        }
    }


    /**
     * 利用JAVA的反射机制，将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上<br>
     * 用于单个sheet
     *
     * @param <T>
     * @param headers 表格属性列名数组
     * @param dataset 需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
     *                javabean属性的数据类型有基本数据类型及String,Date,String[],Double[]
     * @param out     与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
     * @param xlsFlag 是否是2003及以前，true是2003及以前，false是2007及最新
     */
    public static <T> void exportExcel(Map<String, String> headers, Collection<T> dataset, OutputStream out,
                                       Boolean xlsFlag) {
        ExcelConfig excelConfig = ExcelContext.get();
        excelConfig.setIsExcel2003(xlsFlag);

        // 声明一个工作薄
        Workbook workbook = excelConfig.getIsExcel2003() ? new HSSFWorkbook() : new XSSFWorkbook();

        // 生成一个表格
        Sheet sheet = workbook.createSheet();

        write2Sheet(sheet, headers, dataset);

        try {
            workbook.write(out);
        } catch (IOException e) {
            log.error(e.toString(), e);
        }
    }


    /**
     * 利用JAVA的反射机制，将放置在JAVA集合中并且符号一定条件的数据以EXCEL 的形式输出到指定IO设备上<br>
     * 用于多个sheet
     *
     * @param <T>
     * @param sheets {@link ExcelSheet}的集合
     * @param out    与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
     */
    public static <T> void exportExcel(List<ExcelSheet<T>> sheets, OutputStream out) {
        if (CollectionUtils.isEmpty(sheets)) {
            return;
        }
        // 声明一个工作薄
        ExcelConfig excelConfig = ExcelContext.get();
        Workbook workbook = excelConfig.getIsExcel2003() ? new HSSFWorkbook() : new XSSFWorkbook();

        for (ExcelSheet<T> sheet : sheets) {
            // 生成一个表格
            Sheet hssfSheet = workbook.createSheet(sheet.getSheetName());
            write2Sheet(hssfSheet, sheet.getHeaders(), sheet.getDataset());
        }
        try {
            workbook.write(out);
        } catch (IOException e) {
            log.error(e.toString(), e);
        }
    }

    /**
     * 每个sheet的写入
     *
     * @param sheet   页签
     * @param headers 表头
     * @param dataset 数据集合
     */
    private static <T> void write2Sheet(Sheet sheet, Map<String, String> headers, Collection<T> dataset) {

        ExcelConfig excelConfig = ExcelContext.get();

        //时间格式默认"yyyy-MM-dd"
        String pattern = "yyyy-MM-dd";
        if (StringUtils.isNotEmpty(excelConfig.getDatePattern())) {
            pattern = excelConfig.getDatePattern();
        }


        // 产生表格标题行
        Row row = sheet.createRow(0);
        //todo:标题行转中文
        Set<String> keys = headers.keySet();
        Iterator<String> it1 = keys.iterator();
        String key = "";    //存放临时键变量
        int c = 0;   //标题列数
        while (it1.hasNext()) {
            key = it1.next();
            if (headers.containsKey(key)) {
                Cell cell = row.createCell(c);

                RichTextString text = excelConfig.getIsExcel2003() ? new HSSFRichTextString(headers.get(key))
                        : new XSSFRichTextString(headers.get(key));
                cell.setCellValue(text);
                c++;
            }
        }


        // 遍历集合数据，产生数据行
        Iterator<T> it = dataset.iterator();
        int index = 0;
        while (it.hasNext()) {
            index++;
            row = sheet.createRow(index);
            T t = (T) it.next();
            try {
                if (t instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> map = (Map<String, Object>) t;
                    int cellNum = 0;
                    //todo:遍历列名
                    Iterator<String> it2 = keys.iterator();
                    while (it2.hasNext()) {
                        key = it2.next();
                        if (!headers.containsKey(key)) {
                            log.error("Map 中 不存在 key [" + key + "]");
                            continue;
                        }
                        Object value = map.get(key);
                        Cell cell = row.createCell(cellNum);
//                        cell.setCellValue(String.valueOf(value));
                        String textValue = null;
                        if (value instanceof Integer) {
                            int intValue = (Integer) value;
                            cell.setCellValue(intValue);
                        } else if (value instanceof Float) {
                            float fValue = (Float) value;
                            cell.setCellValue(fValue);
                        } else if (value instanceof Double) {
                            double dValue = (Double) value;
                            cell.setCellValue(dValue);
                        } else if (value instanceof Long) {
                            long longValue = (Long) value;
                            cell.setCellValue(longValue);
                        } else if (value instanceof Boolean) {
                            boolean bValue = (Boolean) value;
                            cell.setCellValue(bValue);
                        } else if (value instanceof Date) {

                            //TODO map 无法处理data
                            Date date = (Date) value;
                            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                            textValue = sdf.format(date);
                        } else if (value instanceof String[]) {
                            String[] strArr = (String[]) value;
                            for (int j = 0; j < strArr.length; j++) {
                                String str = strArr[j];
                                cell.setCellValue(str);
                                if (j != strArr.length - 1) {
                                    cellNum++;
                                    cell = row.createCell(cellNum);
                                }
                            }
                        } else if (value instanceof Double[]) {
                            Double[] douArr = (Double[]) value;
                            for (int j = 0; j < douArr.length; j++) {
                                Double val = douArr[j];
                                // 值不为空则set Value
                                if (val != null) {
                                    cell.setCellValue(val);
                                }

                                if (j != douArr.length - 1) {
                                    cellNum++;
                                    cell = row.createCell(cellNum);
                                }
                            }
                        } else {
                            // 其它数据类型都当作字符串简单处理
                            String empty = StringUtils.EMPTY;
                            textValue = value == null ? empty : value.toString();
                        }
                        if (textValue != null) {
                            RichTextString richString = excelConfig.getIsExcel2003() ?
                                    new HSSFRichTextString(textValue) : new XSSFRichTextString(textValue);
                            cell.setCellValue(richString);
                        }


                        cellNum++;
                    }

                } else {
                    List<FieldForSortting> fields = sortFieldByAnno(t.getClass());
                    int cellNum = 0;
                    for (int i = 0; i < fields.size(); i++) {
                        Cell cell = row.createCell(cellNum);
                        Field field = fields.get(i).getField();
                        field.setAccessible(true);
                        Object value = field.get(t);
                        String textValue = null;
                        if (value instanceof Integer) {
                            int intValue = (Integer) value;
                            cell.setCellValue(intValue);
                        } else if (value instanceof Float) {
                            float fValue = (Float) value;
                            cell.setCellValue(fValue);
                        } else if (value instanceof Double) {
                            double dValue = (Double) value;
                            cell.setCellValue(dValue);
                        } else if (value instanceof Long) {
                            long longValue = (Long) value;
                            cell.setCellValue(longValue);
                        } else if (value instanceof Boolean) {
                            boolean bValue = (Boolean) value;
                            cell.setCellValue(bValue);
                        } else if (value instanceof Date) {
                            Date date = (Date) value;

                            // 如果注解上有日期格式化，则使用指定的格式化格式
                            ExcelCell anno = field.getAnnotation(ExcelCell.class);
                            String dateFormat = pattern;
                            if (anno != null) {
                                dateFormat = StringUtils.isNotBlank(anno.format()) ? anno.format() : pattern;

                            }
                            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
                            textValue = sdf.format(date);

                        } else if (value instanceof String[]) {
                            String[] strArr = (String[]) value;
                            for (int j = 0; j < strArr.length; j++) {
                                String str = strArr[j];
                                cell.setCellValue(str);
                                if (j != strArr.length - 1) {
                                    cellNum++;
                                    cell = row.createCell(cellNum);
                                }
                            }
                        } else if (value instanceof Double[]) {
                            Double[] douArr = (Double[]) value;
                            for (int j = 0; j < douArr.length; j++) {
                                Double val = douArr[j];
                                // 值不为空则set Value
                                if (val != null) {
                                    cell.setCellValue(val);
                                }

                                if (j != douArr.length - 1) {
                                    cellNum++;
                                    cell = row.createCell(cellNum);
                                }
                            }
                        } else {
                            // 其它数据类型都当作字符串简单处理
                            String empty = StringUtils.EMPTY;
                            ExcelCell anno = field.getAnnotation(ExcelCell.class);
                            if (anno != null) {
                                empty = anno.defaultValue();
                            }
                            textValue = value == null ? empty : value.toString();
                        }
                        if (textValue != null) {
                            RichTextString richString = excelConfig.getIsExcel2003() ? new HSSFRichTextString(textValue)
                                    : new XSSFRichTextString(textValue);
                            cell.setCellValue(richString);
                        }

                        cellNum++;
                    }
                }
            } catch (Exception e) {
                log.error(e.toString(), e);
            }
        }
        // 设定自动宽度
        for (int i = 0; i < headers.size(); i++) {
            sheet.autoSizeColumn(i);
        }
    }


    /**
     * 读取excel文件
     *
     * @param clazz
     * @param filePath
     * @param logs
     * @param arrayCount
     * @param <T>
     * @return
     */
    public static <T> List<T> readExcel(Class<T> clazz, String filePath, ExcelLogs logs,
                                        Integer... arrayCount) {

        Boolean xlsFlag = filePath.toLowerCase().endsWith(ExcelConst.SUFFIX_2003);

        // 后缀判断文件类型
        ExcelConfig excelConfig = ExcelContext.get();
        excelConfig.setIsExcel2003(xlsFlag);

        FileInputStream inputStream = null;

        List<T> result = null;
        try {
            try {
                inputStream = new FileInputStream(filePath);

                result = readExcel(clazz, inputStream, logs, arrayCount);
            } finally {
                IOUtils.closeQuietly(inputStream);
            }
        } catch (IOException e) {
            log.error("io exception", e);
        }

        return result;
    }

    /**
     * 把Excel的数据封装成voList
     *
     * @param clazz       vo的Class
     * @param inputStream excel输入流
     * @param logs        错误log集合
     * @param arrayCount  如果vo中有数组类型,那就按照index顺序,把数组应该有几个值写上.
     * @return voList
     * @throws RuntimeException
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> readExcel(Class<T> clazz, InputStream inputStream, ExcelLogs logs, Integer... arrayCount) {
        String pattern = ExcelConst.FORMAT_DATE;

        ExcelConfig excelConfig = ExcelContext.get();
        if (StringUtils.isNotEmpty(excelConfig.getDatePattern())) {
            pattern = excelConfig.getDatePattern();
        }


        Workbook workBook = null;

        try {
            Boolean isExcel2003 = excelConfig.getIsExcel2003();
            if (null == isExcel2003) {
                workBook = WorkbookFactory.create(inputStream);
            } else if (!isExcel2003) {
                workBook = new XSSFWorkbook(inputStream);
            } else {
                workBook = new HSSFWorkbook(inputStream);
            }
        } catch (IOException e) {
            log.error(e.toString(), e);
        } catch (InvalidFormatException e) {
            log.error("导入出错");
            e.printStackTrace();
        }
        List<T> list = new ArrayList<>();
        Sheet sheet = workBook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.rowIterator();
        try {
            List<ExcelLog> logList = new ArrayList<>();
            // Map<title,index>
            Map<String, Integer> titleMap = new HashMap<>();

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getRowNum() == 0) {
                    if (clazz == Map.class) {
                        // 解析map用的key,就是excel标题行
                        Iterator<Cell> cellIterator = row.cellIterator();
                        Integer index = 0;
                        while (cellIterator.hasNext()) {
                            String value = cellIterator.next().getStringCellValue();
                            titleMap.put(value, index);
                            index++;
                        }
                    }
                    continue;
                }
                // 整行都空，就跳过
                boolean allRowIsNull = true;
                Iterator<Cell> cellIterator = row.cellIterator();
                while (cellIterator.hasNext()) {
                    Object cellValue = getCellValue(cellIterator.next());
                    if (cellValue != null) {
                        allRowIsNull = false;
                        break;
                    }
                }
                if (allRowIsNull) {
                    log.warn("Excel row " + row.getRowNum() + " all row value is null!");
                    continue;
                }
                T t = null;
                StringBuilder log = new StringBuilder();
                if (clazz == Map.class) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    for (String k : titleMap.keySet()) {
                        Integer index = titleMap.get(k);
                        String value = row.getCell(index).getStringCellValue();
                        map.put(k, value);
                    }
                    list.add((T) map);

                } else {
                    t = clazz.newInstance();
                    int arrayIndex = 0;// 标识当前第几个数组了
                    int cellIndex = 0;// 标识当前读到这一行的第几个cell了
                    List<FieldForSortting> fields = sortFieldByAnno(clazz);
                    for (FieldForSortting ffs : fields) {
                        Field field = ffs.getField();
                        field.setAccessible(true);
                        if (field.getType().isArray()) {
                            Integer count = arrayCount[arrayIndex];
                            Object[] value = null;
                            if (field.getType().equals(String[].class)) {
                                value = new String[count];
                            } else {
                                // 目前只支持String[]和Double[]
                                value = new Double[count];
                            }
                            for (int i = 0; i < count; i++) {
                                Cell cell = row.getCell(cellIndex);
                                String errMsg = validateCell(cell, field, cellIndex);
                                if (StringUtils.isBlank(errMsg)) {
                                    value[i] = getCellValue(cell);
                                } else {
                                    log.append(errMsg);
                                    log.append(";");
                                    logs.setHasError(true);
                                }
                                cellIndex++;
                            }
                            field.set(t, value);
                            arrayIndex++;
                        } else {
                            Cell cell = row.getCell(cellIndex);
                            String errMsg = validateCell(cell, field, cellIndex);
                            if (StringUtils.isBlank(errMsg)) {
                                Object value = null;
                                // 处理特殊情况,Excel中的String,转换成Bean的Date
                                if (field.getType().equals(Date.class) && cell.getCellType() == Cell.CELL_TYPE_STRING) {
                                    Object strDate = getCellValue(cell);
                                    String dateFormat = pattern;
                                    ExcelCell annoCell = field.getAnnotation(ExcelCell.class);
                                    if (annoCell != null) {
                                        dateFormat = StringUtils.isNotBlank(annoCell.format()) ? annoCell.format() : pattern;
                                    }
                                    try {
                                        value = new SimpleDateFormat(dateFormat).parse(strDate.toString());
                                    } catch (ParseException e) {

                                        errMsg =
                                                MessageFormat.format("the cell [{0}] can not be converted to a date ",
                                                        CellReference.convertNumToColString(cell.getColumnIndex()));
                                    }
                                } else {
                                    value = getCellValue(cell);
                                    // 处理特殊情况,excel的value为String,且bean中为其他,且defaultValue不为空,那就=defaultValue
                                    ExcelCell annoCell = field.getAnnotation(ExcelCell.class);
                                    if (value instanceof String && !field.getType().equals(String.class)
                                            && StringUtils.isNotBlank(annoCell.defaultValue())) {
                                        value = annoCell.defaultValue();
                                    }
                                }

                                // 类型转换
                                if (field.getType().equals(String.class)) {
                                    // 去掉小数点
                                    if (value instanceof Double) {
                                        NumberFormat format = NumberFormat.getInstance();
                                        format.setGroupingUsed(false);
                                        value = format.format(value);
                                    }

                                }

                                field.set(t, value);
                            }
                            if (StringUtils.isNotBlank(errMsg)) {
                                log.append(errMsg);
                                log.append(";");
                                logs.setHasError(true);
                            }
                            cellIndex++;
                        }
                    }
                    list.add(t);
                    logList.add(new ExcelLog(t, log.toString(), row.getRowNum() + 1));
                }
            }
            logs.setLogList(logList);
        } catch (InstantiationException e) {
            throw new RuntimeException(MessageFormat.format("can not instance class:{0}", clazz.getSimpleName()), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(MessageFormat.format("can not instance class:{0}", clazz.getSimpleName()), e);
        }
        return list;
    }


    /**
     * 驗證Cell類型是否正確
     *
     * @param cell    cell單元格
     * @param field   欄位
     * @param cellNum 第幾個欄位,用於errMsg
     * @return
     */

    private static String validateCell(Cell cell, Field field, int cellNum) {
        String columnName = CellReference.convertNumToColString(cellNum);
        String result = null;
        Integer[] integers = validateMap.get(field.getType());
        if (integers == null) {
            result = MessageFormat.format("Unsupported type [{0}]", field.getType().getSimpleName());
            return result;
        }
        ExcelCell annoCell = field.getAnnotation(ExcelCell.class);
        if (cell == null
                || (cell.getCellType() == Cell.CELL_TYPE_STRING && StringUtils.isBlank(cell
                .getStringCellValue()))) {
            if (annoCell != null && annoCell.valid().allowNull() == false) {
                result = MessageFormat.format("the cell [{0}] can not null", columnName);
            }
            ;
        } else if (cell.getCellType() == Cell.CELL_TYPE_BLANK && annoCell.valid().allowNull()) {
            return result;
        } else {
            List<Integer> cellTypes = Arrays.asList(integers);

            // 如果類型不在指定範圍內,並且沒有默認值
            if (!(cellTypes.contains(cell.getCellType()))
                    || StringUtils.isNotBlank(annoCell.defaultValue())
                    && cell.getCellType() == Cell.CELL_TYPE_STRING) {
                StringBuilder strType = new StringBuilder();
                for (int i = 0; i < cellTypes.size(); i++) {
                    Integer intType = cellTypes.get(i);
                    strType.append(getCellTypeByInt(intType));
                    if (i != cellTypes.size() - 1) {
                        strType.append(",");
                    }
                }
                result =
                        MessageFormat.format("the cell [{0}] type must [{1}]", columnName, strType.toString());
            } else {
                // 类型符合验证,但值不在要求范围内的
                // String in
                if (annoCell.valid().in().length != 0 && cell.getCellType() == Cell.CELL_TYPE_STRING) {
                    String[] in = annoCell.valid().in();
                    String cellValue = cell.getStringCellValue();
                    boolean isIn = false;
                    for (String str : in) {
                        if (str.equals(cellValue)) {
                            isIn = true;
                        }
                    }
                    if (!isIn) {
                        result = MessageFormat.format("the cell [{0}] value must in {1}", columnName, in);
                    }
                }
                // 数字型
                if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    double cellValue = cell.getNumericCellValue();
                    // 小于
                    if (!Double.isNaN(annoCell.valid().lt())) {
                        if (!(cellValue < annoCell.valid().lt())) {
                            result =
                                    MessageFormat.format("the cell [{0}] value must less than [{1}]", columnName,
                                            annoCell.valid().lt());
                        }
                    }
                    // 大于
                    if (!Double.isNaN(annoCell.valid().gt())) {
                        if (!(cellValue > annoCell.valid().gt())) {
                            result =
                                    MessageFormat.format("the cell [{0}] value must greater than [{1}]", columnName,
                                            annoCell.valid().gt());
                        }
                    }
                    // 小于等于
                    if (!Double.isNaN(annoCell.valid().le())) {
                        if (!(cellValue <= annoCell.valid().le())) {
                            result =
                                    MessageFormat.format("the cell [{0}] value must less than or equal [{1}]",
                                            columnName, annoCell.valid().le());
                        }
                    }
                    // 大于等于
                    if (!Double.isNaN(annoCell.valid().ge())) {
                        if (!(cellValue >= annoCell.valid().ge())) {
                            result =
                                    MessageFormat.format("the cell [{0}] value must greater than or equal [{1}]",
                                            columnName, annoCell.valid().ge());
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * 根据annotation的seq排序后的栏位
     *
     * @param clazz
     * @return
     */
    private static List<FieldForSortting> sortFieldByAnno(Class<?> clazz) {
        Field[] fieldsArr = clazz.getDeclaredFields();
        List<FieldForSortting> fields = new ArrayList<>();
        List<FieldForSortting> annoNullFields = new ArrayList<>();
        for (Field field : fieldsArr) {
            ExcelCell ec = field.getAnnotation(ExcelCell.class);
            if (ec == null) {
                // 没有ExcelCell Annotation 视为不汇入
                continue;
            }
            int id = ec.index();
            fields.add(new FieldForSortting(field, id));
        }
        fields.addAll(annoNullFields);
        sortByProperties(fields, true, false, "index");
        return fields;
    }

    @SuppressWarnings("unchecked")
    private static void sortByProperties(List<? extends Object> list, boolean isNullHigh,
                                         boolean isReversed, String... props) {
        if (CollectionUtils.isNotEmpty(list)) {
            Comparator<?> typeComp = ComparableComparator.getInstance();
            if (isNullHigh == true) {
                typeComp = ComparatorUtils.nullHighComparator(typeComp);
            } else {
                typeComp = ComparatorUtils.nullLowComparator(typeComp);
            }
            if (isReversed) {
                typeComp = ComparatorUtils.reversedComparator(typeComp);
            }

            List<Object> sortCols = new ArrayList<>();

            if (props != null) {
                for (String prop : props) {
                    sortCols.add(new BeanComparator(prop, typeComp));
                }
            }
            if (sortCols.size() > 0) {
                Comparator<Object> sortChain = new ComparatorChain(sortCols);
                Collections.sort(list, sortChain);
            }
        }
    }

}
