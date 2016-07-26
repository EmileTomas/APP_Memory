package com.sjtu.bwphoto.memory.Class.Resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Administrator on 2016/7/26.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)  //如果null就不生成
@JsonIgnoreProperties(ignoreUnknown = true)
public class Memory {

    @JsonProperty
    int id;
    @JsonProperty
    String content;
    @JsonProperty
    int timestamp;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }





}
