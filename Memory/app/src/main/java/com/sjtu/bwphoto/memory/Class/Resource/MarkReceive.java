package com.sjtu.bwphoto.memory.Class.Resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

/**
 * Created by Administrator on 2016/8/4.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)  //如果null就不生成
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarkReceive {

    @JsonProperty
    long id;

    @JsonProperty
    long item_id;

    @JsonProperty
    String thisUser;

    @JsonProperty
    String content;

    @JsonProperty
    Timestamp time;

    @JsonProperty
    int mark_on_id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getItem_id() {
        return item_id;
    }

    public void setItem_id(long item_id) {
        this.item_id = item_id;
    }

    public String getThisUser() {
        return thisUser;
    }

    public void setThisUser(String thisUser) {
        this.thisUser = thisUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public int getMark_on_id() {
        return mark_on_id;
    }

    public void setMark_on_id(int mark_on_id) {
        this.mark_on_id = mark_on_id;
    }





}
