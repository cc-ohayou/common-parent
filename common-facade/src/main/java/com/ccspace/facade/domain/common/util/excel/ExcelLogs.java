package com.ccspace.facade.domain.common.util.excel;

import com.ccspace.facade.domain.common.util.excel.ExcelLog;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;


@Slf4j
public class ExcelLogs {
    private Boolean        hasError;
    private List<ExcelLog> logList;

    /**
     *
     */
    public ExcelLogs() {
        super();
        hasError = false;
    }

    /**
     * @return the hasError
     */
    public Boolean getHasError() {
        return hasError;
    }

    /**
     * @param hasError the hasError to set
     */
    public void setHasError(Boolean hasError) {
        this.hasError = hasError;
    }

    /**
     * @return the logList
     */
    public List<ExcelLog> getLogList() {
        return logList;
    }

    public List<ExcelLog> getErrorLogList() {
        List<ExcelLog> errList = new ArrayList<ExcelLog>();
        for (ExcelLog log : this.logList) {
            if (log != null && StringUtils.isNotBlank(log.getLog())) {
                errList.add(log);
            }
        }
        return errList;
    }

    /**
     * @param logList the logList to set
     */
    public void setLogList(List<ExcelLog> logList) {
        this.logList = logList;
    }

}
