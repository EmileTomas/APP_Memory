package com.sjtu.bwphoto.memory.Class.Resource;

/**
 * Created by ly on 8/29/2016.
 */
public class Tag {
    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public int getResource_id() {
        return resource_id;
    }

    public void setResource_id(int resource_id) {
        this.resource_id = resource_id;
    }

    private String tag_name;
    private int resource_id;
}
