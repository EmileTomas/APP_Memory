package com.sjtu.bwphoto.memory.Class.Util.Util_Cropper;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sjtu.bwphoto.memory.Class.Msg;
import com.sjtu.bwphoto.memory.Class.Resource.FriendRequest;
import com.sjtu.bwphoto.memory.Class.Resource.FriendRequestList;
import com.sjtu.bwphoto.memory.Class.RestUtil;
import com.sjtu.bwphoto.memory.Class.ServerUrl;
import com.sjtu.bwphoto.memory.Class.User;
import com.sjtu.bwphoto.memory.Class.Util.FriendRequestCard;
import com.sjtu.bwphoto.memory.R;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.zip.Inflater;

import androidviewhover.BlurLayout;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2016/7/30.
 */
public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.RequestCardHolder> {
    private final static ServerUrl url = new ServerUrl();
    private View rootView;
    private LayoutInflater inflater;
    private ArrayList<FriendRequestCard> friendRequestList;

    public FriendRequestAdapter(View rootView, ArrayList<FriendRequestCard> friendRequestList) {
        this.rootView = rootView;
        this.inflater = LayoutInflater.from(rootView.getContext());
        this.friendRequestList = friendRequestList;
    }

    @Override
    public int getItemCount() {
        return friendRequestList.size();
    }

    @Override
    public RequestCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View friendApplyCard = inflater.inflate(R.layout.friend_apply_card, null);
        RequestCardHolder requestCardHolder = new RequestCardHolder(friendApplyCard);
        return requestCardHolder;
    }

    class RequestCardHolder extends RecyclerView.ViewHolder {
        public View rootView;
        public CircleImageView profile;
        public TextView name;
        public TextView content;
        public TextView result;
        public Button acceptButton;
        public Button deleteButton;

        public RequestCardHolder(View friendApplyCard) {
            super(friendApplyCard);
            this.rootView = friendApplyCard;
            this.profile = (CircleImageView) rootView.findViewById(R.id.friend_apply_profile);
            this.name = (TextView) rootView.findViewById(R.id.friend_apply_name);
            this.content = (TextView) rootView.findViewById(R.id.friend_apply_content);
            this.result = (TextView) rootView.findViewById(R.id.friend_apply_result);
            this.acceptButton = (Button) rootView.findViewById(R.id.friend_apply_accept);
            this.deleteButton = (Button) rootView.findViewById(R.id.friend_apply_delete);
        }
    }

    @Override
    public void onBindViewHolder(final RequestCardHolder holder, final int position) {
        final FriendRequestCard friendRequestCard = friendRequestList.get(position);

        holder.content.setText(friendRequestCard.getContent());
        holder.name.setText(friendRequestCard.getName());

        //Set profile
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.loading)
                .showImageOnFail(R.drawable.load_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .extraForDownloader(RestUtil.getAuth())
                .build();
        ImageLoader.getInstance().displayImage(friendRequestCard.getProfile(), holder.profile, options);

        holder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String result = RestUtil.postForObject(url.url + "/friends/make/" + friendRequestCard.getName(), null, String.class);        //Send Accept here
                    }
                }).start();
                holder.acceptButton.setVisibility(View.GONE);
                holder.result.setVisibility(View.VISIBLE);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Haven't response to this request yet, then refuse
                if (holder.acceptButton.getVisibility() == View.VISIBLE) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //Refuse the request
                        }
                    }).start();
                }


                friendRequestList.remove(position);
                notifyDataSetChanged();
            }
        });
    }
}

