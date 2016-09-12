package com.sjtu.bwphoto.memory.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sjtu.bwphoto.memory.Class.Resource.Musichash;
import com.sjtu.bwphoto.memory.Class.RestUtil;
import com.sjtu.bwphoto.memory.Class.ServerUrl;
import com.sjtu.bwphoto.memory.Class.Resource.Songresult;
import com.sjtu.bwphoto.memory.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MusicSearchActivity extends AppCompatActivity {
    private EditText SongName;
    private String song_name;
    private String userName;
    private Button BtnSearch;
    private Button BtnBack;
    private ListView ListResult;
    private ArrayList<String> list_result = new ArrayList<String>();
    private ArrayList<String> list_hash = new ArrayList<String>();
    private Songresult song;
    private int res_id;
    private String croppedName;
    private final static ServerUrl url = new ServerUrl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_music_search);

        //Receive Data from last activity
        Bundle bundle = this.getIntent().getExtras();
        userName = bundle.getString("userName");
        res_id = bundle.getInt("res_id");
        croppedName = bundle.getString("croppedName");

        SongName = (EditText) findViewById(R.id.song_name);
        BtnSearch = (Button) findViewById(R.id.search);
        BtnBack = (Button) findViewById(R.id.btn_back);
        ListResult = (ListView) findViewById(R.id.result);

        BtnSearch.setOnClickListener(mListener);
        BtnBack.setOnClickListener(mListener);
    }

    View.OnClickListener mListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.search:
                    search();
                    break;
                case R.id.btn_back:
                    back();
                    break;
                case R.id.result:
                    break;
            }
        }
    };

    AdapterView.OnItemClickListener mListener2 = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            if (id == -1) {
                // 点击的是headerView或者footerView
                return;
            }
            int realPosition = (int) id;
            // 响应代码
            String hash = list_hash.get(realPosition);
            System.out.println("Music Search select hash : " + hash);
            Musichash mhash = new Musichash();
            mhash.setResource_id(res_id);
            mhash.setHashcode(hash);
            String result = RestUtil.postForObject(url.url + "/resources/" + res_id + "/music/" + hash, mhash, String.class);
            System.out.println("Music Search upload hash result: " + result);
            Toast.makeText(MusicSearchActivity.this, "Add Music success", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MusicSearchActivity.this, AddMemoryActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("userName", userName);
            bundle.putString("croppedName", croppedName);
            bundle.putInt("res_id", res_id);
            intent.putExtras(bundle);
            startActivity(intent);
            MusicSearchActivity.this.finish();
        }
    };

    public void back() {
        Intent intent = new Intent(MusicSearchActivity.this, AddMemoryMusicActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userName", userName);
        intent.putExtras(bundle);
        startActivity(intent);
        MusicSearchActivity.this.finish();
    }

    public void search() {
        song_name = SongName.getText().toString().trim();

        String httpUrl = "http://apis.baidu.com/geekery/music/query";
        String httpArg = "s=" + song_name + "&size=10&page=1";
        String jsonResult = request(httpUrl, httpArg);
        if (jsonResult != null) System.out.println("jsonResult != null");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            song = objectMapper.readValue(jsonResult, Songresult.class);
            for (int i = 0; i < song.getData().getData().size(); ++i) {
                String hash = song.getData().getData().get(i).getHash();
                String name = song.getData().getData().get(i).getFilename();
                if (hash == null) System.out.println("song hash is null!!!!!!!!!!");
                if (name == null) System.out.println("song name is null!!!!!!!!!!");
                System.out.println("music hash: " + hash);
                System.out.println("music name: " + name);
                list_result.add(name);
                list_hash.add(hash);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("SearchMusic:IOerror");
        }

        if (list_result == null) System.out.println("list result is null!!!!!!!!!!");
        ListResult = new ListView(this);
        ListResult.setOnItemClickListener(mListener2);
        ListResult.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, list_result));
        setContentView(ListResult);
    }

    public static String request(String httpUrl, String httpArg) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?" + httpArg;
        System.out.println("SearchMusic : " + httpUrl);

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
