package com.sjtu.bwphoto.memory.Class.Resource;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/2.
 */
public class CommentIntent implements Serializable {

    String name;
    String imageURL;
    String musicURL;
    String content;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getMusicURL() {
        return musicURL;
    }

    public void setMusicURL(String musicURL) {
        this.musicURL = musicURL;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

public CommentIntent(String name,String imageURL,String musicURL,String content){
    this.name=name;
    this.imageURL=imageURL;
    this.musicURL=musicURL;
    this.content=content;
}


}
