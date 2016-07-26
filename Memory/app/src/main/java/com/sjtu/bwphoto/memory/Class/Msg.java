package com.sjtu.bwphoto.memory.Class;

/**
 * Created by Administrator on 2016/7/4.
 */
public class Msg {

    private String posterAccount;

    private String content;

    private String tag;

    private String imageUrl;

    private String musicHash;


    public Msg(String posterAccount,String content,String tag,String imageUrl,String musicHash){
        this.posterAccount=posterAccount;
        this.content=content;
        this.tag=tag;
        this.imageUrl=imageUrl;
        this.musicHash=musicHash;
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
