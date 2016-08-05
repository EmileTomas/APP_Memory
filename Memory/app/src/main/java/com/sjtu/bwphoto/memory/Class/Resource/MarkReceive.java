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
    String this_name;

    @JsonProperty
    String content;

    @JsonProperty
    Timestamp timestamp;

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

    public String getThis_name() {
        return this_name;
    }

    public void setThis_name(String this_name) {
        this.this_name = this_name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public int getMark_on_id() {
        return mark_on_id;
    }

    public void setMark_on_id(int mark_on_id) {
        this.mark_on_id = mark_on_id;
    }


}
