package com.sjtu.bwphoto.memory.Class.Adapter;

/**
 * Created by Administrator on 2016/7/4.
 */
public class Msg {



    private int resourceId;

    private String posterAccount;

    private String content;

    private String tag;

    private String imageUrl;

    private String musicHash;


    public Msg(int resourceId,String posterAccount,String content,String tag,String imageUrl,String musicHash){
        this.resourceId=resourceId;
        this.posterAccount=posterAccount;
        this.content=content;
        this.tag=tag;
        this.imageUrl=imageUrl;
        this.musicHash=musicHash;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getPosterAccount() {
        return posterAccount;
    }

    public String getMusicHash() {
        return musicHash;
    }

    public String getContent(){
        return content;
    }

    public String getTag(){
        return tag;
    }

    public String getImageUrl(){return imageUrl;}

}
