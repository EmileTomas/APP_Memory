package com.sjtu.bwphoto.memory.Class.Adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sjtu.bwphoto.memory.Class.RestUtil;
import com.sjtu.bwphoto.memory.Class.ServerUrl;
import com.sjtu.bwphoto.memory.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2016/8/5.
 */
public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.FriendCardHolder>{

    private final static ServerUrl url = new ServerUrl();
    private View rootView;
    private LayoutInflater inflater;
    private ArrayList<UserCard> friendList;

    public FriendListAdapter(View rootView,ArrayList<UserCard>friendList){
        this.rootView=rootView;
        this.friendList=friendList;
        this.inflater=LayoutInflater.from(rootView.getContext());
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }


    @Override
    public FriendCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View friendCard = inflater.inflate(R.layout.friend_list_card, null);
        final FriendCardHolder friendCardHolder = new FriendCardHolder(friendCard);

        friendCardHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final UserCard userCard = friendList.get(friendCardHolder.getAdapterPosition());
                //Haven't response to this request yet, then refuse

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //Delete the friend
                        }
                    }).start();

                friendList.remove(friendCardHolder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });

        return friendCardHolder;
    }


    @Override
    public void onBindViewHolder(final FriendCardHolder holder, final int position) {
        final UserCard userCard = friendList.get(position);

        holder.content.setText(userCard.getContent());
        holder.name.setText(userCard.getName());

        //Set profile
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.loading)
                .showImageOnFail(R.drawable.load_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .extraForDownloader(RestUtil.getAuth())
                .build();
        ImageLoader.getInstance().displayImage(userCard.getProfile(), holder.profile, options);

    }

    class FriendCardHolder extends RecyclerView.ViewHolder{
        public View view;
        public CircleImageView profile;
        public TextView name;
        public TextView content;
        public Button deleteButton;

        public FriendCardHolder(View friendCard){
            super(friendCard);
            this.view=friendCard;
            this.profile = (CircleImageView) view.findViewById(R.id.friend_list_profile);
            this.name=(TextView)view.findViewById(R.id.friend_list_name);
            this.content=(TextView)view.findViewById(R.id.friend_list_content);
            this.deleteButton=(Button)view.findViewById(R.id.friend_list_delete);
        }
    }
}
