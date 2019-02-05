package com.ccspace.facade.domain.common.util;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @AUTHOR CF
 * @DATE Created on 2017/9/20 13:02.
 */
public class NumberUtil<T> {

    private static BigDecimal PRECISION = BigDecimal.valueOf(0.0001);

    /**
     * 由于BigDecimal精度问题，需要两次取小数位
     * 例如
     * 31.3，0.95跌幅的价格为29.74
     * BigDecimal 31.3，0.95跌幅的价格为29.7349999999999994315658113919198513031005859375
     * 取1次2位的四舍五入为 29.73
     * 所以需要先取一次3位的四舍五入为 29.735， 再取一次2位的四舍五入为29.74
     *
     * @param b
     * @return
     */
    public static BigDecimal decimalHalf2Up(BigDecimal b) {
        return b.setScale(3, 4).setScale(2, 4);
    }

    /**
     * 只入不舍 - 该方法目前只用于计算充值手续费，如果其他场景要用到此方法，请仔细考虑是否适用
     * 如果2者都用ROUND_UP,会出现以下问题
     * 0.3500000000000000072858385991025897965300828218460083007812500
     * 0.351
     * 0.36
     * <p>
     * 所以先用DOWN截断保留位数的前一位，在用ROUND_UP只入不舍
     * <p>
     * RoundingMode.DOWN；如果结果为负，则舍入行为类似于 RoundingMode.UP。注意，此舍入模式始终不会增加计算值
     * 1.6 1
     * -1.1 -2
     * <p>
     * RoundingMode.UP ：远离零方向舍入的舍入模式。始终对非零舍弃部分前面的数字加 1。注意，此舍入模式始终不会减少计算值的绝对值
     *
     * @param b
     * @return
     */
    public static BigDecimal decimalUp(BigDecimal b) {
        BigDecimal temp = b.setScale(6, BigDecimal.ROUND_DOWN);
        temp = temp.setScale(2, BigDecimal.ROUND_UP);
        return temp;
    }

    /**
     * 截两位小数，先取三位（四舍五入）再取两位（不入）小数
     * 例如 1.777直接截成1.77，1.333截取成1.33，不让入
     */
    public static BigDecimal decimalDown(BigDecimal b) {
        BigDecimal temp = b.setScale(3, BigDecimal.ROUND_UP);
        temp = temp.setScale(2, BigDecimal.ROUND_DOWN);
        return temp;
    }

    public static BigDecimal judgeBigDecimalNullReturn(BigDecimal tdDealAmount) {
        return tdDealAmount == null ? BigDecimal.ZERO : tdDealAmount;
    }

    /**
     * 相加
     *
     * @param valueOne
     * @param valueTwo
     * @return
     */
    public static BigDecimal add(BigDecimal valueOne, BigDecimal valueTwo) {
        if (valueOne == null) {
            return valueTwo;
        } else {
            if (valueTwo == null) {
                return valueOne;
            } else {
                return valueOne.add(valueTwo);
            }
        }
    }

    /**
     * 现加多个值
     *
     * @param values
     * @return
     */
    public static BigDecimal addMore(BigDecimal... values) {
        BigDecimal temp = new BigDecimal("0");
        for (BigDecimal value :
                values) {
            if (value != null) {
                temp = temp.add(value);
            }
        }

        return temp;
    }

    /**
     * 相减
     *
     * @param value1
     * @param value2
     * @return
     */
    public static BigDecimal subtract(BigDecimal value1, BigDecimal value2) {
        if (value1 == null) {
            if (value2 == null) {
                return null;
            } else {
                return value2.multiply(new BigDecimal(-1));
            }
        } else {
            if (value2 == null) {
                return value1;
            } else {
                return value1.subtract(value2);
            }
        }

    }

    /**
     * 比较两个字符串数值
     *
     * @param bigDecimalStr1 bigDecimalStr
     * @param bigDecimalStr2 bigDecimalStr
     * @return -1, 0, or 1 as this BigDecimal is numerically less than, equal
     * to, or greater than val.
     */
    public static int compare(String bigDecimalStr1, String bigDecimalStr2) {
        BigDecimal before = new BigDecimal(bigDecimalStr1);
        BigDecimal after = new BigDecimal(bigDecimalStr2);
        return before.compareTo(after);
    }

    /**
     * 默认值为0
     *
     * @param value
     * @return
     */
    public static BigDecimal ConvertToBigDecimal(String value) {
        return ConvertToBigDecimal(value, new BigDecimal(0));
    }

    /**
     * 添加默认值入参
     *
     * @param value
     * @param defaultValue
     * @return
     */
    public static BigDecimal ConvertToBigDecimal(String value, BigDecimal defaultValue) {
        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        }
        // 构造以字符串内容为值的BigDecimal类型的变量
        BigDecimal result = new BigDecimal(value);

        return result;
    }



    public static BigDecimal multily(BigDecimal value1, BigDecimal value2) {
        return decimalHalf2Up(value1.multiply(value2));
    }

    /**
     * 该方法目前只用于计算充值手续费，如果其他场景要用到此方法，请仔细考虑是否适用
     * 这里的使用场景是对小数点后6位只入不舍，所以先截取小数点后6位，再处理只入不舍
     *
     * @param value1
     * @param value2
     * @return
     */
    public static BigDecimal multiplyUp(BigDecimal value1, BigDecimal value2) {
        if (value1 == null || value2 == null) {
            return BigDecimal.ZERO;
        }
        return decimalUp(value1.multiply(value2));
    }

    /**
     * 除法，防止无限循环
     * Non-terminating decimal expansion; no exact representable decimal result
     *
     * @param value1
     * @param value2
     * @return
     */
    public static BigDecimal divide(BigDecimal value1, BigDecimal value2) {
        return value1.divide(value2, 4, BigDecimal.ROUND_HALF_UP);

    }

    /**
     * 比较大小，如果2个值大小相差小于保留的精度要求0.0001, 则认为2值相等
     *
     * @param value1
     * @param value2
     * @return
     */
    public static int compare(BigDecimal value1, BigDecimal value2) {
        // 比较是否相等
        BigDecimal subtractValue = subtract(value1, value2);
        if (null == subtractValue) {
            return 0;
        }
        BigDecimal result = subtractValue.abs();
        int compared = result.compareTo(PRECISION);
        if (compared < 0) {
            return 0;
        }

        // 比较大小
        return value1.compareTo(value2);

    }

    /**
     * 格式化数字
     *
     * @param value
     * @return
     */
    public static String toString(BigDecimal value) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        if (value == null) {
            return "0.00";
        }

        return decimalFormat.format(value);
    }

    /**
     * 根据输入的数字，随机分配生成
     *
     * @param money 输入金额
     * @param num   输入红包数量
     */
    public static List<Double> randomNumber(double money, int num) {
        List<Double> array = new ArrayList<>();
        Random r = new Random();
        double sum = 0;
        for (int i = 0; i < num; i++) {
            //经过小小的计算，这样使最小的钱尽量接近0.01；num越大，此计算越没有用
            array.add(r.nextDouble() * money + 0.01 * num * money);
            sum += array.get(i);
        }
        for (int i = 0; i < array.size(); i++) {
            array.set(i, array.get(i) / sum * money);
        }
        Collections.sort(array);
        //将钱进行分配；
        for (int i = 0; i < array.size(); i++) {
            //不足0.01的直接给0.01；
            if (array.get(i) <= 0.01) {
                //z-=0.01;
                array.set(i, 0.01);
            } else if (i == array.size() - 1) {
                //array.set(i, (int)(z*100)*1.0/100);//将剩余的一点money给最后一个人，因为排序，最后一个人最大份，所以最后分配的肯定是正数
                BigDecimal he = new BigDecimal("0");
                for (int j = 0; j < array.size() - 1; j++) {
                    BigDecimal b = new BigDecimal(Double.toString(array.get(j)));
                    he = he.add(b);
                }
                BigDecimal moneyb = new BigDecimal(Double.toString(money));
                array.set(i, moneyb.subtract(he).doubleValue());
            } else {
                array.set(i, (int) (array.get(i) * 100) * 1.0 / 100);
            }
        }

        // 打乱顺序
        Collections.shuffle(array);

        return array;

    }

    /**
     * 获取指定数据的相反数
     *@author xianghy
     *@date 2018/12/16
     *@param number
     *@return
     */
    public static BigDecimal nuNumber(BigDecimal number) {
        if (number == null || number.compareTo(BigDecimal.ZERO) == 0) {
            return number;
        }
        return number.multiply(new BigDecimal("-1"));
    }


    /**
     * 判断是否为小数
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
        if (null == str) {
            return false;
        }
        Double number = new Double(str);
        return (Math.round(number) != number);
    }
}
