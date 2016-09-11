package com.sjtu.bwphoto.memory.Edison;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

/**
 * Created by Administrator on 2016/9/11.
 */
public class EdisonMemory {
    @JsonProperty
    private String tag;
    @JsonProperty
    private String memoryText;
    @JsonProperty
    private String musicHash;
    @JsonProperty
    private Timestamp timestamp;

    @JsonCreator
    public EdisonMemory(@JsonProperty("tag") String tag,
                        @JsonProperty("memoryText") String memoryText,
                        @JsonProperty("musicHash") String musicHash,
                        @JsonProperty("timestamp") Timestamp timestamp){
        this.tag=tag;
        this.memoryText=memoryText;
        this.musicHash=musicHash;
        this.timestamp=timestamp;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getMemoryText() {
        return memoryText;
    }

    public void setMemoryText(String memoryText) {
        this.memoryText = memoryText;
    }

    public String getMusicHash() {
        return musicHash;
    }

    public void setMusicHash(String musicHash) {
        this.musicHash = musicHash;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }



}
