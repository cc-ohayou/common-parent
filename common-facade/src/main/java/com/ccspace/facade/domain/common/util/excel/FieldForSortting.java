package com.ccspace.facade.domain.common.util.excel;

import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.reflect.Field;


@Data
@Accessors(chain = true)
public class FieldForSortting {
    private Field field;
    private int   index;

    /**
     * @param field
     */
    public FieldForSortting(Field field) {
        super();
        this.field = field;
    }

    /**
     * @param field
     * @param index
     */
    public FieldForSortting(Field field, int index) {
        super();
        this.field = field;
        this.index = index;
    }

}
