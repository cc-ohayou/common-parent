package com.ccspace.facade.domain.common.util.excel;

import lombok.extern.slf4j.Slf4j;

/**
 * Excel 上下文
 *
 */
@Slf4j
public class ExcelContext {

    private static ThreadLocal<ExcelConfig> ctx;

    public static ExcelConfig get() {
        if (ctx == null) {
            ctx = new ThreadLocal<ExcelConfig>() {
                @Override
                protected ExcelConfig initialValue() {
                    return new ExcelConfig();
                }
            };
        }
        return ctx.get();
    }

    public static void clear() {
        if (ctx != null) {
            ctx.remove();
        }
    }

}
