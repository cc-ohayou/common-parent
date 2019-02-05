package com.ccspace.facade.domain.common.util.excel;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.Map;


@Data
@Accessors(chain = true)
public class ExcelSheet<T> {
    private String              sheetName;
    private Map<String, String> headers;
    private Collection<T>       dataset;
}
