package com.sjtu.bwphoto.memory.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sjtu.bwphoto.memory.Class.Adapter.FriendRequestAdapter;
import com.sjtu.bwphoto.memory.Class.Adapter.UserCard;
import com.sjtu.bwphoto.memory.Class.Resource.FriendRequestList;
import com.sjtu.bwphoto.memory.Class.Resource.RecommendFriendList;
import com.sjtu.bwphoto.memory.Class.RestUtil;
import com.sjtu.bwphoto.memory.Class.ServerUrl;
import com.sjtu.bwphoto.memory.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/30.
 */
public class RecommendFriendListActivity extends AppCompatActivity {
    private static final ServerUrl url = new ServerUrl();
    private RecommendFriendList RecommendFriendList=new RecommendFriendList();
    private FriendRequestAdapter adapter;
    private ArrayList<UserCard> userCards = new ArrayList<UserCard>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommend_friend_apply);

        //Get data from Server
        RecommendFriendList = RestUtil.getForObject(url.url + "/recommend/user", RecommendFriendList.class);
        String temp = RestUtil.getForObject(url.url + "/recommend/user", String.class);
        System.out.println(temp);
        if (RecommendFriendList!=null) {
            System.out.println("Get Data");
            UserCard userCard;
            if (RecommendFriendList.size() != 0) {
                for (int i = 0; i < RecommendFriendList.size(); ++i) {
                    String name = RecommendFriendList.get(i);
                    //Here should changed to corresponding API
                    String content = "Hope be Friend with you.";
                    String imageURL = url.url+"/identity/profile/"+name;
                    userCard = new UserCard(name, content, imageURL);
                    userCards.add(userCard);
                }
            }
        }
        else {
            System.out.println("Get Data Failed");
            TextView info = (TextView) findViewById(R.id.recommend_friend_apply_info);
            info.setVisibility(View.VISIBLE);
        }

        View rootView = findViewById(R.id.recommend_friend_apply_rootview);
        adapter = new FriendRequestAdapter(rootView, userCards);

        //Set Adapter for data
        final LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recommend_friend_apply_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }
}
