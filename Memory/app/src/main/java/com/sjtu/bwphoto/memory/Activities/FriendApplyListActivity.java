package com.sjtu.bwphoto.memory.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sjtu.bwphoto.memory.Class.Resource.FriendRequestList;
import com.sjtu.bwphoto.memory.Class.RestUtil;
import com.sjtu.bwphoto.memory.Class.ServerUrl;
import com.sjtu.bwphoto.memory.Class.Adapter.FriendRequestCard;
import com.sjtu.bwphoto.memory.Class.Adapter.FriendRequestAdapter;
import com.sjtu.bwphoto.memory.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/30.
 */
public class FriendApplyListActivity extends AppCompatActivity {
    private static final ServerUrl url = new ServerUrl();
    private FriendRequestList friendRequestList;
    private FriendRequestAdapter adapter;
    private ArrayList<FriendRequestCard> friendRequestCards = new ArrayList<FriendRequestCard>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_apply);

        //Get data from Server
        friendRequestList = RestUtil.getForObject(url.url + "/friends/make", FriendRequestList.class);
        String temp = RestUtil.getForObject(url.url + "/friends/make", String.class);
        System.out.println(temp);
        if (friendRequestList != null) {
            System.out.println("Get Data");
            FriendRequestCard friendRequestCard;
            if (friendRequestList.size() != 0) {
                for (int i = 0; i < friendRequestList.size(); ++i) {
                    String name = friendRequestList.get(i).getApplyer();
                    //Here should changed to corresponding API
                    String content = "Hope be Friend with you.";
                    String imageURL = "https://case.edu/medicine/admissions/media/school-of-medicine/admissions/classprofile.png";
                    friendRequestCard = new FriendRequestCard(name, content, imageURL);
                    friendRequestCards.add(friendRequestCard);
                }
            } else {
                System.out.println("Get Data Failed");
                TextView info = (TextView) findViewById(R.id.friend_apply_info);
                info.setVisibility(View.VISIBLE);
            }
        }

        View rootView = findViewById(R.id.friend_apply_rootview);
        adapter = new FriendRequestAdapter(rootView, friendRequestCards);

        //Set Adapter for data
        final LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.friend_apply_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }
}
