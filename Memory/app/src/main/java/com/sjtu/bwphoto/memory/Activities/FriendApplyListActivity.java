package com.sjtu.bwphoto.memory.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sjtu.bwphoto.memory.Class.Resource.FriendRequest;
import com.sjtu.bwphoto.memory.Class.Resource.FriendRequestList;
import com.sjtu.bwphoto.memory.Class.RestUtil;
import com.sjtu.bwphoto.memory.Class.ServerUrl;
import com.sjtu.bwphoto.memory.Class.Util.FriendRequestCard;
import com.sjtu.bwphoto.memory.Class.Util.Util_Cropper.FriendRequestAdapter;
import com.sjtu.bwphoto.memory.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/30.
 */
public class FriendApplyListActivity extends AppCompatActivity {
    private static final ServerUrl url = new ServerUrl();
    private FriendRequestList friendRequestList;
    private FriendRequestAdapter adapter;
    private ArrayList<FriendRequestCard> friendRequestCards;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.friend_apply);

        //Get data from Server
        friendRequestList = RestUtil.getForObject(url.url + "/friends/make", FriendRequestList.class);

        if (friendRequestList != null) {
            FriendRequestCard friendRequestCard;
            for (int i = 0; i < friendRequestList.size(); ++i) {
                String name = friendRequestList.get(i).getApplyer();
                //Here should changed to corresponding API
                String content = "Hope be Friend with you.";
                String imageURL = RestUtil.getForObject(url.url + "/identity/profile/" + name, String.class);
                friendRequestCard = new FriendRequestCard(name, content, imageURL);
                friendRequestCards.add(friendRequestCard);
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
