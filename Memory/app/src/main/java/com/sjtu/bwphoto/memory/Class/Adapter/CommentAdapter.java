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
import com.sjtu.bwphoto.memory.Class.Resource.MarkReceive;
import com.sjtu.bwphoto.memory.Class.Resource.MarkReceiveList;
import com.sjtu.bwphoto.memory.Class.RestUtil;
import com.sjtu.bwphoto.memory.Class.ServerUrl;
import com.sjtu.bwphoto.memory.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2016/8/4.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentCardHolder>{
    private static final ServerUrl Url=new ServerUrl();

    private View rootView;
    private MarkReceiveList markReceiveArrayList;
    private LayoutInflater inflater;

    public CommentAdapter(View rootView, MarkReceiveList markReceiveArrayList) {
        this.rootView = rootView;
        this.markReceiveArrayList = markReceiveArrayList;
        this.inflater = LayoutInflater.from(rootView.getContext());
    }


    @Override
    public int getItemCount() {
        return markReceiveArrayList.size();
    }

    @Override
    public CommentCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View commentCard = inflater.inflate(R.layout.comment_card, null);
        final CommentCardHolder commentCardHolder = new CommentCardHolder(commentCard);

        return commentCardHolder;
    }


    @Override
    public void onBindViewHolder(final CommentCardHolder holder, final int position) {
        MarkReceive markReceive=markReceiveArrayList.get(position);

        holder.name.setText(markReceive.getThisUser());
        holder.content.setText(markReceive.getContent());
        holder.time.setText(markReceive.getTime().toString());

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.loading)
                .showImageOnFail(R.drawable.load_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .extraForDownloader(RestUtil.getAuth())
                .build();

        ImageLoader.getInstance().displayImage(Url.url+"/identity/profile/"+markReceive.getThisUser(), holder.profile, options);

    }

    class CommentCardHolder extends RecyclerView.ViewHolder {
        public View view;
        public CircleImageView profile;
        public TextView name;
        public TextView content;
        public TextView time;

        public CommentCardHolder(View commentCardView) {
            super(commentCardView);
            this.view = commentCardView;
            this.profile = (CircleImageView) view.findViewById(R.id.comment_view_comment_profile);
            this.name = (TextView) view.findViewById(R.id.comment_view_comment_name);
            this.content = (TextView) view.findViewById(R.id.comment_view_comment_content);
            this.time=(TextView) view.findViewById(R.id.comment_view_comment_time);
        }
    }
}
