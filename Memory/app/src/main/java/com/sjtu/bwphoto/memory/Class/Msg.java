package com.sjtu.bwphoto.memory.Class;

/**
 * Created by Administrator on 2016/7/4.
 */
public class Msg {
    private String content;

    private String map_position;

    private String imageUrl;

    public Msg(String content,String map_position,String imageUrl){
        this.content=content;
        this.map_position=map_position;
        this.imageUrl=imageUrl;
    }

    public String getContent(){
        return content;
    }

    public String getMap_position(){
        return map_position;
    }

    public String getImageUrl(){return imageUrl;}

}
