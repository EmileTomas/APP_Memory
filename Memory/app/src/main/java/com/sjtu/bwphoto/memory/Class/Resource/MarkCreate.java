package com.sjtu.bwphoto.memory.Class.Resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;


/**
 * Created by Administrator on 2016/8/1.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)  //如果null就不生成
@JsonIgnoreProperties(ignoreUnknown = true)
public class MarkCreate {
    @JsonProperty
    String content;
    @JsonProperty
    Timestamp timestamp;

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



    public MarkCreate(String content, Timestamp timestamp){
        this.content=content;
        this.timestamp=timestamp;
    }
}
