package com.sjtu.bwphoto.memory.Activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.sjtu.bwphoto.memory.Class.ImageGetter;
import com.sjtu.bwphoto.memory.Class.ImagePair;
import com.sjtu.bwphoto.memory.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class PairActivity extends AppCompatActivity {
    private ImagePair imgPair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pair);

        pair();
    }

    public void pair(){
        ImageGetter imgGetter = new ImageGetter();
        String path = "/sdcard/DCIM/Camera/";
        List<String> urilist = imgGetter.getAllFile(path);
        imgPair = new ImagePair();
        System.out.println("Here!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//        for (int i = 0; i < urilist.size(); ++i){
//
//        }
        try {
            String s1 = imgPair.getHash(new FileInputStream(new File(urilist.get(0))));
            String s2 = imgPair.getHash(new FileInputStream(new File(urilist.get(1))));
            System.out.println(s1);
            System.out.println(s2);
            int res = imgPair.distance(s1,s2);
            System.out.println(res);
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
