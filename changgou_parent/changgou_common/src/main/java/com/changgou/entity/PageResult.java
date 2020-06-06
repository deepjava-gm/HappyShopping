package com.changgou.entity;
import java.util.List;
public class PageResult<T> {

    private Long total;//总记录数
    private List<T> rows;//记录

    private Integer pages;  // 总页数

    public PageResult(Long total, List<T> rows, Integer pages) {
        this.total = total;
        this.rows = rows;
        this.pages = pages;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public PageResult(Long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public PageResult() {
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
