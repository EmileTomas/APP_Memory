package com.sjtu.bwphoto.memory.Class.Util;

import android.app.Service;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sjtu.bwphoto.memory.Class.Msg;
import com.sjtu.bwphoto.memory.Class.RestUtil;
import com.sjtu.bwphoto.memory.Fragement.RecentFragment;
import com.sjtu.bwphoto.memory.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import androidviewhover.BlurLayout;

/**
 * Created by Administrator on 2016/7/4.
 */
public class MsgRecycleAdapterForRecent extends RecyclerView.Adapter<MsgRecycleAdapterForRecent.CardViewHolder> {

    private static MediaPlayer mediaplayer=new MediaPlayer();
    private List<Msg> Cards;
    private RecentFragment mContext;
    private LayoutInflater inflater;
    private View rootView;
    private final int GONE = 1;
    private final int VISIBLE = 2;

    public MsgRecycleAdapterForRecent(RecentFragment mContext, List<Msg> Cards, View rootView) {
        this.mContext = mContext;
        this.Cards = Cards;
        this.rootView = rootView;
        inflater = LayoutInflater.from(rootView.getContext());
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
        final Msg msg = Cards.get(position);
        BlurLayout mSampleLayout = holder.mSampleLayout;
        View hover = holder.hover;


        //Set card
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.loading)
                .showImageOnFail(R.drawable.load_error)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .extraForDownloader(RestUtil.getAuth())
                .build();
        ImageLoader.getInstance().displayImage(msg.getImageUrl(), holder.imageView, options);
        holder.textView.setText(msg.getTag());
        holder.content.setText(msg.getContent());

        BlurLayout.setGlobalDefaultDuration(1);
        holder.mSampleLayout.dismissHover();

        //Set Hover
        BlurLayout.setGlobalDefaultDuration(400);
        if (!holder.set_flag) {
            holder.set_flag = true;
            mSampleLayout.setHoverView(hover);
            mSampleLayout.addChildAppearAnimator(hover, R.id.comment, Techniques.SlideInRight);
            mSampleLayout.addChildAppearAnimator(hover, R.id.add_friend, Techniques.SlideInRight);
            mSampleLayout.addChildAppearAnimator(hover, R.id.music, Techniques.SlideInRight);
            mSampleLayout.addChildAppearAnimator(hover, R.id.recorder, Techniques.SlideInRight);


            mSampleLayout.addChildDisappearAnimator(hover, R.id.comment, Techniques.SlideOutRight);
            mSampleLayout.addChildDisappearAnimator(hover, R.id.add_friend, Techniques.SlideOutRight);
            mSampleLayout.addChildDisappearAnimator(hover, R.id.music, Techniques.SlideOutRight);
            mSampleLayout.addChildDisappearAnimator(hover, R.id.recorder, Techniques.SlideOutRight);

            mSampleLayout.addChildAppearAnimator(hover, R.id.content, Techniques.BounceIn);
            mSampleLayout.addChildDisappearAnimator(hover, R.id.content, Techniques.FadeOutUp);


            //Comment
            final InputMethodManager imm;
            final EditText commentBox = (EditText) rootView.findViewById(R.id.commentBox);
            final LinearLayout commentView = (LinearLayout) rootView.findViewById(R.id.commentView);
            final Button commentSendButton=(Button)rootView.findViewById(R.id.sendButton);
            imm = (InputMethodManager) commentBox.getContext().getSystemService(Service.INPUT_METHOD_SERVICE);
            //Comment Button
            hover.findViewById(R.id.comment).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    commentView.setVisibility(View.VISIBLE);
                    commentBox.requestFocus();
                    mContext.setFABState(GONE);
                    imm.showSoftInput(commentBox, 0);
                }
            });
            //Send Button
            commentSendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String text=commentBox.getText().toString();
                    commentBox.setText("");
                    commentView.setVisibility(View.GONE);
                    imm.hideSoftInputFromWindow(commentBox.getWindowToken(), 0);
                    mContext.setFABState(VISIBLE);
                    //Send Message here

                }
            });



            //Music Button
            hover.findViewById(R.id.music).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            String httpUrl = "http://apis.baidu.com/geekery/music/playinfo";
                            String httpArg = "hash="+msg.getMusicHash();
                            String jsonResult = request(httpUrl, httpArg);

                            try{
                                Song song=new Song();
                                ObjectMapper objectMapper=new ObjectMapper();
                                song=objectMapper.readValue(jsonResult,Song.class);
                                System.out.println(song.getData().getFileName());
                                mediaplayer.reset();
                                mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                mediaplayer.setDataSource(song.getData().getUrl());
                                mediaplayer.prepare();
                                mediaplayer.start();
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            });
        }
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.card_item, null);
        View hover = inflater.inflate(R.layout.card_hover, null);
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

    //request for JSON feedback of Music
    public static String request(String httpUrl, String httpArg) {
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?" + httpArg;

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            // 填入apikey到HTTP header
            connection.setRequestProperty("apikey",  "fad22d042fecdcc0be3244f57faa757f");
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
