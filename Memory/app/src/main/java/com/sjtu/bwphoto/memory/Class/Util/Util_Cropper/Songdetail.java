package com.sjtu.bwphoto.memory.Class.Util.Util_Cropper;

/**
 * Created by ly on 8/2/2016.
 */

//          "filename": "陈奕迅 - 十年",
//        "extname": "mp3",
//        "m4afilesize": 851201,
//        "filesize": 3242822,
//        "bitrate": 128,
//        "isnew": 0,
//        "duration": 202,
//        "album_name": "黑白灰",
//        "singername": "陈奕迅",
//        "hash": "936051ea140e3cfacb629d2bfaf430d7"

public class Songdetail {
    private String filename;

    public String getOthername() {
        return othername;
    }

    public void setOthername(String othername) {
        this.othername = othername;
    }

    private String othername;

    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }

    private String songname;

    public String getExtname() {
        return extname;
    }

    public void setExtname(String extname) {
        this.extname = extname;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSingername() {
        return singername;
    }

    public void setSingername(String singername) {
        this.singername = singername;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }


    public int getBitrate() {
        return bitrate;
    }

    public void setBitrate(int bitrate) {
        this.bitrate = bitrate;
    }

    public long getFilesize() {
        return filesize;
    }

    public void setFilesize(long filesize) {
        this.filesize = filesize;
    }

    public int getM4afilesize() {
        return m4afilesize;
    }

    public void setM4afilesize(int m4afilesize) {
        this.m4afilesize = m4afilesize;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    private String extname;
    private int m4afilesize;
    private long filesize;
    private int bitrate;

    public int getIsnew() {
        return isnew;
    }

    public void setIsnew(int isnew) {
        this.isnew = isnew;
    }

    private int isnew;
    private int duration;
    private String album_name;
    private String singername;
    private String hash;
}
