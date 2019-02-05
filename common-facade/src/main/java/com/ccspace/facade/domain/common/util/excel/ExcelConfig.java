package com.ccspace.facade.domain.common.util.excel;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Data
@Accessors(chain = true)
public class ExcelConfig {

    Boolean isExcel2003;

    String datePattern;

}
