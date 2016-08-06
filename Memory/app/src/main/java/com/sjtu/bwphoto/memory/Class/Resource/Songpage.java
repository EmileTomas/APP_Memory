package com.sjtu.bwphoto.memory.Class.Resource;

import java.util.ArrayList;

/**
 * Created by ly on 8/2/2016.
 */
public class Songpage {
    private int current_page;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getTotal_rows() {
        return total_rows;
    }

    public void setTotal_rows(int total_rows) {
        this.total_rows = total_rows;
    }

    public int getPage_size() {
        return page_size;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }

    public int getTotal_page() {
        return total_page;
    }

    public void setTotal_page(int total_page) {
        this.total_page = total_page;
    }

    public ArrayList<Songdetail> getData() {
        return data;
    }

    public void setData(ArrayList<Songdetail> data) {
        this.data = data;
    }

    private String keyword;
    private int total_rows;
    private int total_page;
    private int page_size;
    private ArrayList<Songdetail> data;
}
