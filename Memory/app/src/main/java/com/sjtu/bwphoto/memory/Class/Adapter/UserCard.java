package com.sjtu.bwphoto.memory.Class.Adapter;

import com.sjtu.bwphoto.memory.Class.Resource.FriendRequest;

/**
 * Created by Administrator on 2016/7/31.
 */
public class UserCard {
    private String name;
    private String content;
    private String profile;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public UserCard(String name, String content, String profile) {
        this.name = name;
        this.content = content;
        this.profile = profile;
    }


}
