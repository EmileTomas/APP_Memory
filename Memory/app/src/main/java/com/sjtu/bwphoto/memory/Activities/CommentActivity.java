package com.sjtu.bwphoto.memory.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sjtu.bwphoto.memory.Class.Adapter.CommentAdapter;
import com.sjtu.bwphoto.memory.Class.Resource.CommentIntent;
import com.sjtu.bwphoto.memory.Class.Resource.MarkReceiveList;
import com.sjtu.bwphoto.memory.Class.RestUtil;
import com.sjtu.bwphoto.memory.Class.Resource.Song;
import com.sjtu.bwphoto.memory.Class.ServerUrl;
import com.sjtu.bwphoto.memory.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/8/2.
 */
public class CommentActivity extends AppCompatActivity {
    private static final ServerUrl url = new ServerUrl();

    private Song song = new Song();
    private MediaPlayer mediaPlayer=new MediaPlayer();
    CommentIntent commentIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_view);

        initialHeader();

    }

    private void initialHeader(){
        Intent intent = this.getIntent();
        commentIntent= (CommentIntent) intent.getSerializableExtra("commentIntent");

        System.out.println("Create commentView");
        ImageView imageView = (ImageView) findViewById(R.id.comment_view_image);
        ImageView musicPlayer = (ImageView) findViewById(R.id.comment_musicPlayer);
        TextView content = (TextView) findViewById(R.id.comment_view_content);
        TextView postername = (TextView) findViewById(R.id.comment_poster_name);

        System.out.println(commentIntent.getImageURL());
        //Set Image
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.loading)
                .showImageOnFail(R.drawable.load_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .extraForDownloader(RestUtil.getAuth())
                .build();
        ImageLoader.getInstance().displayImage(commentIntent.getImageURL(), imageView, options);

        //Set content
        content.setText(commentIntent.getContent());

        //Set posterName
        postername.setText(commentIntent.getName());

        //set Music Player
        if (commentIntent.getMusicURL() != "") {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    String httpUrl = "http://apis.baidu.com/geekery/music/playinfo";
                    String httpArg = "hash=" + commentIntent.getMusicURL();
                    String jsonResult = request(httpUrl, httpArg);
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        song = objectMapper.readValue(jsonResult, Song.class);

                        //Show song Title
                        Bundle bundle = new Bundle();
                        bundle.putString("SongTitle", song.getData().getFileName());

                        System.out.println(song.getData().getFileName());

                        mediaPlayer.reset();
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mediaPlayer.setDataSource(song.getData().getUrl());
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
    private void InitialComment(){
        MarkReceiveList markReceiveList=new MarkReceiveList();

        if(commentIntent.getResourceId()!=-1){
            String markUrl=url.url+"/resources/"+Integer.toString(commentIntent.getResourceId())+"/marks";
            markReceiveList =RestUtil.getForObject(markUrl,MarkReceiveList.class);
        }

        View rootView=findViewById(R.id.comment_view);
        RecyclerView recyclerView;
        CommentAdapter adapter=new CommentAdapter(rootView,markReceiveList);

        //Set Adapter for data
        final LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.comment_view_recycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }
    //request for JSON feedback of Music
    private static String request(String httpUrl, String httpArg) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?" + httpArg;

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            // 填入apikey到HTTP header
            connection.setRequestProperty("apikey", "fad22d042fecdcc0be3244f57faa757f");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
