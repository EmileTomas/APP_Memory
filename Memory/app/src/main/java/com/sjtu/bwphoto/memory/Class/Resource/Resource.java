package com.sjtu.bwphoto.memory.Class.Resource;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

/**
 * Created by ly on 7/21/2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)  //如果null就不生成
@JsonIgnoreProperties(ignoreUnknown = true)



public class Resource {
    @JsonProperty
    private int id;
    @JsonProperty("username")
    private String name;
    @JsonProperty
    private String content;
    @JsonProperty
    private int memory_id;
    @JsonProperty
    private int image_id;
    @JsonProperty
    private Timestamp timestamp;
    @JsonProperty
    private String status;

//    public Resource(@JsonProperty("status") char status,
//                    @JsonProperty("id") int id,
//                @JsonProperty("name") String name) {
//        this.status = status;
//        this.id = id;
//        this.name = name;
//    }
//
//    public Resource(@JsonProperty("status") char status,
//            @JsonProperty("id") int id,
//                    @JsonProperty("name") String name,
//                    @JsonProperty("memory_id") int memory_id,
//                    @JsonProperty("image_id") int image_id) {
//        this.status = status;
//        this.id = id;
//        this.name = name;
//        this.memory_id = memory_id;
//        this.image_id = image_id;
//    }
//
//    public Resource(@JsonProperty("status") char status,
//            @JsonProperty("id") int id,
//                    @JsonProperty("content") String content,
//                    @JsonProperty("timestamp") Timestamp timestamp) {
//        this.status = status;
//        this.id = id;
//        this.content = content;
//        this.timestamp = timestamp;
//    }
//
//    public Resource(@JsonProperty("id") int id,
//                    @JsonProperty("name") String name,
//                    @JsonProperty("memory_id") int memory_id,
//                    @JsonProperty("image_id") int image_id,
//                    @JsonProperty("content") String content,
//                    @JsonProperty("timestamp") Timestamp timestamp) {
//        this.id = id;
//        this.name = name;
//        this.memory_id = memory_id;
//        this.image_id = image_id;
//        this.content = content;
//        this.timestamp = timestamp;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public int getMemory_id() {
        return memory_id;
    }

    public void setMemory_id(int memory_id) {
        this.memory_id = memory_id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
