package com.sjtu.bwphoto.memory.Class.Util;

import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sjtu.bwphoto.memory.Class.Msg;
import com.sjtu.bwphoto.memory.Class.Util.Util_Cropper.Handle;
import com.sjtu.bwphoto.memory.R;

import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import androidviewhover.BlurLayout;

/**
 * Created by Administrator on 2016/7/4.
 */
public class MsgRecycleAdapter extends RecyclerView.Adapter<MsgRecycleAdapter.CardViewHolder> {

    private List<Msg> Cards;
    private Context mContext;
    private LayoutInflater inflater;
    private View rootView;

    public MsgRecycleAdapter(Context context, List<Msg> Cards, View rootView) {
        this.mContext = context;
        this.Cards = Cards;
        this.rootView = rootView;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return Cards.size();
    }

    @Override
    public void onViewDetachedFromWindow(CardViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        BlurLayout.setGlobalDefaultDuration(1);
        holder.mSampleLayout.dismissHover();
    }


    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        Msg msg = Cards.get(position);
        BlurLayout mSampleLayout = holder.mSampleLayout;
        View hover = holder.hover;


        //Set card
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.loading)
                .showImageOnFail(R.drawable.load_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage(msg.getImageUrl(), holder.imageView, options);
        holder.textView.setText(msg.getMap_position());
        holder.content.setText(msg.getContent());

        BlurLayout.setGlobalDefaultDuration(1);
        holder.mSampleLayout.dismissHover();

        //Set Hover
        BlurLayout.setGlobalDefaultDuration(400);
        if (!holder.set_flag) {
            holder.set_flag = true;
            mSampleLayout.setHoverView(hover);
            mSampleLayout.addChildAppearAnimator(hover, R.id.cat, Techniques.SlideInRight);
            mSampleLayout.addChildAppearAnimator(hover, R.id.mail, Techniques.SlideInRight);

            mSampleLayout.addChildDisappearAnimator(hover, R.id.cat, Techniques.SlideOutRight);
            mSampleLayout.addChildDisappearAnimator(hover, R.id.mail, Techniques.SlideOutRight);

            mSampleLayout.addChildAppearAnimator(hover, R.id.content, Techniques.BounceIn);
            mSampleLayout.addChildDisappearAnimator(hover, R.id.content, Techniques.FadeOutUp);

            final InputMethodManager imm;
            final EditText editText = (EditText) rootView.findViewById(R.id.commentBox);
            final LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.commentView);
            imm = (InputMethodManager) editText.getContext().getSystemService(Service.INPUT_METHOD_SERVICE);
            hover.findViewById(R.id.cat).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    linearLayout.setVisibility(View.VISIBLE);
                    editText.requestFocus();
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            });
        }
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.card_item, null);
        View hover = inflater.inflate(R.layout.hover_sample4, null);
        CardViewHolder holder = new CardViewHolder(view, hover);
        return holder;
    }

    class CardViewHolder extends RecyclerView.ViewHolder {
        boolean set_flag = false;
        ImageView imageView;
        TextView textView;
        TextView content;
        View hover;
        BlurLayout mSampleLayout;

        public CardViewHolder(View view, View hover) {
            super(view);
            this.hover = hover;
            mSampleLayout = (BlurLayout) view.findViewById(R.id.blur_layout);
            imageView = (ImageView) view.findViewById(R.id.msg_photo);
            textView = (TextView) view.findViewById(R.id.msg_position);
            content = (TextView) hover.findViewById(R.id.content);
        }
    }


}
