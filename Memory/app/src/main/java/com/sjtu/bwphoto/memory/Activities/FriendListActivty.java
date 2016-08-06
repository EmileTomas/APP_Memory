package com.sjtu.bwphoto.memory.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sjtu.bwphoto.memory.Class.Adapter.FriendListAdapter;
import com.sjtu.bwphoto.memory.Class.Adapter.FriendRequestAdapter;
import com.sjtu.bwphoto.memory.Class.Adapter.UserCard;
import com.sjtu.bwphoto.memory.Class.Resource.FriendList;
import com.sjtu.bwphoto.memory.Class.RestUtil;
import com.sjtu.bwphoto.memory.Class.ServerUrl;
import com.sjtu.bwphoto.memory.Class.User;
import com.sjtu.bwphoto.memory.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/5.
 */
public class FriendListActivty extends AppCompatActivity{
    private final static ServerUrl url=new ServerUrl();
    private FriendList friends;
    private ArrayList<UserCard> userCards=new ArrayList<UserCard>();
    private FriendListAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_list);

        //Get Data
        friends= RestUtil.getForObject(url.url+"/friends",FriendList.class);
        if(friends.size()!=0){
            UserCard userCard;
            for(int i =0;i<friends.size();++i){
                String name=friends.get(i).getFrName();

                User friend=RestUtil.getForObject(url.url+"/identity/detail/"+name,User.class);
                String content=friend.getContent();
                String profileURL=url.url+"/identity/profile/"+name;

                userCard=new UserCard(name,content,profileURL);
                userCards.add(userCard);
            }
        }
        else{
            TextView info=(TextView)findViewById(R.id.friend_list_info);
            info.setVisibility(View.VISIBLE);
        }


        View rootView = findViewById(R.id.friend_list_rootview);
        adapter = new FriendListAdapter(rootView, userCards);

        //Set Adapter for data
        final LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.friend_list_recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

}
