package com.sjtu.bwphoto.memory.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.sjtu.bwphoto.memory.Class.Adapter.Msg;
import com.sjtu.bwphoto.memory.Class.Adapter.MsgRecycleAdapter;
import com.sjtu.bwphoto.memory.Class.Util.Util_Cropper.Handle;
import com.sjtu.bwphoto.memory.Edison.Edison;
import com.sjtu.bwphoto.memory.Edison.EdisonError;
import com.sjtu.bwphoto.memory.Edison.EdisonMemory;
import com.sjtu.bwphoto.memory.R;

import java.util.ArrayList;
import java.util.List;

import androidviewhover.BlurLayout;

/**
 * Created by Administrator on 2016/9/11.
 */
public class LocalServerActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private final int INITIAL_VIEW = 1;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MsgRecycleAdapter msgRecycleAdapter;
    private RecyclerView recyclerView;
    private List<Msg> Cards = new ArrayList<Msg>();
    private String posterName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_server);

        //Set the duration of animation
        BlurLayout.setGlobalDefaultDuration(400);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        posterName = bundle.getString("userAccount");

        initial_swipeRefreshLayout();
        fetch_data();


    }


    private void initial_swipeRefreshLayout() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_Refresh_remote_server);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.GoogleBlue,
                R.color.GoogleGreen,
                R.color.GoogleRed,
                R.color.GoogleYellow
        );
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
    }

    //Refresh listeners for swipeRefreshLayout
    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        //Fetch data again
        new FeatchDataThread().start();
        swipeRefreshLayout.setRefreshing(false);
        msgRecycleAdapter.notifyDataSetChanged();
    }

    //Fetch
    private void fetch_data() {
        System.out.println("Featch begin");
        //fill data to Cards
        new FeatchDataThread().start();


    }

    private void fill_recyclerView() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecycleAdapter = new MsgRecycleAdapter(Cards, this,this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_remote_server);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(msgRecycleAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    class FeatchDataThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                Edison edison = new Edison(108, 80, "memory");
                List<EdisonMemory> data = edison.getArray(EdisonMemory.class);

                for (EdisonMemory tar : data) {
                    Msg Card = new Msg(-1, posterName, tar.getMemoryText(), tar.getTag(), "drawable://" + R.drawable.greatwall, tar.getMusicHash());
                    Cards.add(Card);

                }
                edison.close();
                Message postmessage = new Message();
                postmessage.what=INITIAL_VIEW;
                mHandler.sendMessage(postmessage);
            } catch (EdisonError e) {
                System.out.println(e.getMessage());
            }

        }
    }

    private android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case INITIAL_VIEW:
                    fill_recyclerView();
                    break;
            }
        }
    };

}
