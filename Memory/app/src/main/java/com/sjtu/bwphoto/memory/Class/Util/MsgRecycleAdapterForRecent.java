package com.sjtu.bwphoto.memory.Class.Util;

import android.app.Service;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Message;
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
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sjtu.bwphoto.memory.Activities.MainActivity;
import com.sjtu.bwphoto.memory.Class.Msg;
import com.sjtu.bwphoto.memory.Class.RestUtil;
import com.sjtu.bwphoto.memory.Class.ServerUrl;
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
    private final int RecentPage = 0;
    private final int PersonalPage = 1;
    private final int RecommendPage = 2;
    private final int VISIBLE = 0;
    private final int APPLY_SUCCESS=1;
    private final int APPLY_FAIL=2;
    private final int SET_SONG_TITLE=3;



    private static MediaPlayer mediaplayer = new MediaPlayer();
    private final static ServerUrl url = new ServerUrl();
    private List<Msg> Cards;
    private LayoutInflater inflater;
    private View mainActivityView;
    private int pageNumber;
    Song song = new Song();

    private FloatingActionsMenu FAB;


    public MsgRecycleAdapterForRecent(List<Msg> Cards, View rootView, View mainActivityView, int pageNumber) {
        this.Cards = Cards;
        this.mainActivityView = mainActivityView;
        inflater = LayoutInflater.from(rootView.getContext());
        FAB = (FloatingActionsMenu) this.mainActivityView.findViewById(R.id.menuFAB);
        this.pageNumber = pageNumber;
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
    public void onBindViewHolder(final CardViewHolder holder, final int position) {
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
            mSampleLayout.addChildAppearAnimator(hover, R.id.content, Techniques.BounceIn);
            mSampleLayout.addChildAppearAnimator(hover, R.id.comment, Techniques.FadeIn);
            mSampleLayout.addChildAppearAnimator(hover, R.id.music, Techniques.FadeIn);
            mSampleLayout.addChildAppearAnimator(hover, R.id.recorder, Techniques.FadeIn);
            mSampleLayout.addChildAppearAnimator(hover, R.id.detail, Techniques.FadeIn);
            mSampleLayout.addChildAppearAnimator(hover, R.id.hover_view, Techniques.FadeIn);

            mSampleLayout.addChildDisappearAnimator(hover, R.id.content, Techniques.FadeOutUp);
            mSampleLayout.addChildDisappearAnimator(hover, R.id.comment, Techniques.FadeOut);
            mSampleLayout.addChildDisappearAnimator(hover, R.id.music, Techniques.FadeOut);
            mSampleLayout.addChildDisappearAnimator(hover, R.id.recorder, Techniques.FadeOut);
            mSampleLayout.addChildDisappearAnimator(hover, R.id.detail, Techniques.FadeOut);
            mSampleLayout.addChildDisappearAnimator(hover, R.id.hover_view, Techniques.FadeOut);

            mSampleLayout.addAppearListener(new BlurLayout.AppearListener() {
                @Override
                public void onStart() {
                    if (msg.getMusicHash() != "") {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String httpUrl = "http://apis.baidu.com/geekery/music/playinfo";
                                String httpArg = "hash=" + msg.getMusicHash();
                                String jsonResult = request(httpUrl, httpArg);
                                try {
                                    ObjectMapper objectMapper = new ObjectMapper();
                                    song = objectMapper.readValue(jsonResult, Song.class);

                                    //Show song Title
                                    Bundle bundle = new Bundle();
                                    bundle.putString("SongTitle", song.getData().getFileName());
                                    Message postmessage = new Message();
                                    postmessage.setData(bundle);
                                    postmessage.obj = holder.songTitle;
                                    postmessage.what = SET_SONG_TITLE;
                                    mHandler.sendMessage(postmessage);
                                    System.out.println(song.getData().getFileName());

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                }

                @Override
                public void onEnd() {
                    //If the hover is disappeared, stop playing music.
                    mediaplayer.reset();
                }
            });


            //setting the add_friend button
            if (pageNumber == RecentPage) {
                ImageView add_friend = (ImageView) hover.findViewById(R.id.add_friend);
                mSampleLayout.addChildAppearAnimator(hover, R.id.add_friend, Techniques.FadeIn);
                mSampleLayout.addChildDisappearAnimator(hover, R.id.add_friend, Techniques.FadeOut);
                add_friend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                String result = RestUtil.postForObject(url.url + "/friends/apply/" + msg.getPosterAccount(), null, String.class);
                                Message postmessage = new Message();
                                if (result.contains("success")) {
                                    postmessage.what = APPLY_SUCCESS;
                                    mHandler.sendMessage(postmessage);

                                }
                                else{
                                    postmessage.what = APPLY_SUCCESS;
                                    mHandler.sendMessage(postmessage);
                                }
                            }
                        }).start();
                    }
                });
            } else {
                ImageView add_friend = (ImageView) hover.findViewById(R.id.add_friend);
                add_friend.setVisibility(View.GONE);
            }
            //setting the comment button
            final InputMethodManager imm;
            final EditText commentBox = (EditText) mainActivityView.findViewById(R.id.commentBox);
            final LinearLayout commentView = (LinearLayout) mainActivityView.findViewById(R.id.commentView);
            final Button commentSendButton = (Button) mainActivityView.findViewById(R.id.sendButton);
            imm = (InputMethodManager) commentBox.getContext().getSystemService(Service.INPUT_METHOD_SERVICE);
            hover.findViewById(R.id.comment).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    commentView.setVisibility(View.VISIBLE);
                    commentBox.requestFocus();
                    FAB.setVisibility(View.INVISIBLE);
                    imm.showSoftInput(commentBox, 0);
                }
            });


            //Send Button
            commentSendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String text = commentBox.getText().toString();
                    commentBox.setText("");
                    commentView.setVisibility(View.GONE);
                    imm.hideSoftInputFromWindow(commentBox.getWindowToken(), 0);

                    Message postmessage = new Message();
                    postmessage.what = VISIBLE;
                    mHandler.sendMessageDelayed(postmessage, 250);

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
                            try {
                                //Play music
                                mediaplayer.reset();
                                mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                mediaplayer.setDataSource(song.getData().getUrl());
                                mediaplayer.prepare();
                                mediaplayer.start();
                            } catch (Exception e) {
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
        TextView songTitle;
        BlurLayout mSampleLayout;

        public CardViewHolder(View view, View hover) {
            super(view);
            this.hover = hover;
            mSampleLayout = (BlurLayout) view.findViewById(R.id.blur_layout);
            imageView = (ImageView) view.findViewById(R.id.msg_photo);
            textView = (TextView) view.findViewById(R.id.msg_position);
            content = (TextView) hover.findViewById(R.id.content);
            songTitle=(TextView) hover.findViewById(R.id.song_title);
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
            connection.setRequestProperty("apikey", "fad22d042fecdcc0be3244f57faa757f");
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


    private android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case VISIBLE:
                    FAB.setVisibility(View.VISIBLE);
                    break;
                case APPLY_SUCCESS:
                    Toast.makeText(MainActivity.mainActivityContext, "请求发送成功", Toast.LENGTH_SHORT).show();
                    break;
                case APPLY_FAIL:
                    Toast.makeText(MainActivity.mainActivityContext, "请求发送失败", Toast.LENGTH_SHORT).show();
                    break;
                case SET_SONG_TITLE:
                    TextView songTitle=(TextView)msg.obj;
                    String title=msg.getData().get("SongTitle").toString();
                    songTitle.setText(title);
                    break;
            }
        }

    };

}
