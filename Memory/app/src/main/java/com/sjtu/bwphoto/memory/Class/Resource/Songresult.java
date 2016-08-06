package com.sjtu.bwphoto.memory.Class.Resource;

/**
 * Created by ly on 8/2/2016.
 */
public class Songresult {
    private int code;
    private String status;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Songpage getData() {
        return data;
    }

    public void setData(Songpage data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    private String msg;
    private Songpage data;
}
