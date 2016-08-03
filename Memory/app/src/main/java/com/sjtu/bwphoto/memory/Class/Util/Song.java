package com.sjtu.bwphoto.memory.Class.Util;

/**
 * Created by Administrator on 2016/7/23.
 */
public class Song {
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public SongInfo getData() {
        return data;
    }

    public void setData(SongInfo data) {
        this.data = data;
    }

    private SongInfo data;

    private int code;
    private String status;
    private String msg;




}
