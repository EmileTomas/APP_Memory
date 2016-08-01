package com.sjtu.bwphoto.memory.Class.Resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Administrator on 2016/7/30.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)  //如果null就不生成
@JsonIgnoreProperties(ignoreUnknown = true)
public class FriendRequest {
    @JsonProperty
    String applyer;

    @JsonProperty
    String applyee;

    @JsonProperty
    String status;

    public String getApplyer() {
        return applyer;
    }

    public void setApplyer(String applyer) {
        this.applyer = applyer;
    }

    public String getApplyee() {
        return applyee;
    }

    public void setApplyee(String applyee) {
        this.applyee = applyee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
