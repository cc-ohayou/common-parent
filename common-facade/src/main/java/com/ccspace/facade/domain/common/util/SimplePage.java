package com.ccspace.facade.domain.common.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CF create on 2017/10/17 14:27
 * @description 简单page对象
 */
public class SimplePage<T> {

    public static final int DEFAULT_PAGE_SIZE = 15;

    private List<T> items;

    private int totalCount;// 总记录数

    private int totalPageCount;// 总页数

    private int pageSize;// 每页记录个数

    private int currentPage;

    public static void main(String[] args) {
        SimplePage<Integer> p = new SimplePage<Integer>(null, 9, 20, 11);

    }

    public SimplePage(List<T> items, int totalCount, int pageSize, int currentPage) {
        if (items == null) {
            this.items = new ArrayList<T>();
        } else {
            this.items = items;
        }
        this.pageSize = pageSize > 1 ? pageSize : 1;
        this.currentPage = currentPage > 0 ? currentPage : 1;
        if (totalCount > 0) {
            this.totalCount = totalCount;
            totalPageCount = totalCount / this.pageSize;
            if (this.totalCount % this.pageSize > 0) {
                totalPageCount++;
            }
        } else {
            this.totalCount = 0;
            totalPageCount = 0;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("totalCount:").append(totalCount);
        sb.append(" totalPageCount:").append(totalPageCount);
        sb.append(" pageSize:").append(pageSize);
        sb.append(" currentPage:").append(currentPage);
        sb.append(" items.size:").append(items.size());
        return sb.toString();
    }

    public int getPrevPage() {
        int pre = this.currentPage - 1;
        pre = pre < 1 ? 1 : pre;
        pre = pre < totalPageCount ? pre : totalPageCount;
        return pre;
    }

    public int getNextPage() {
        int next = this.currentPage + 1;
        next = next > 1 ? next : 1;
        next = next < totalPageCount ? next : totalPageCount;
        return next;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPageCount() {
        return totalPageCount;
    }

    public void setTotalPageCount(int totalPageCount) {
        this.totalPageCount = totalPageCount;
    }


}
