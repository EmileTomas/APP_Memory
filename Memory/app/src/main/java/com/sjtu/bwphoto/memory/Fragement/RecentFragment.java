package com.sjtu.bwphoto.memory.Fragement;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sjtu.bwphoto.memory.Class.Msg;
import com.sjtu.bwphoto.memory.Class.Util.MsgRecycleAdapter;
import com.sjtu.bwphoto.memory.R;

import java.util.ArrayList;
import java.util.List;

import androidviewhover.BlurLayout;

/**
 * Created by ly on 7/7/2016.
 */
public class RecentFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private View rootView;
    private RecyclerView recyclerView;
    private MsgRecycleAdapter msgRecycleAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Msg> Cards;

    private int lastVisibleItem;
    private boolean isCardEmpty=true;
    private boolean freushFlag = false;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_recent, container, false);
        BlurLayout.setGlobalDefaultDuration(800);


        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_Refresh);
        swipeRefreshLayout.setColorSchemeResources(
                R.color.GoogleBlue,
                R.color.GoogleGreen,
                R.color.GoogleRed,
                R.color.GoogleYellow
        );
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));

        //restore data from last time
        restoreData();

        //if the database has data, load them and inital view
        isCardEmpty=Cards.isEmpty();
        if(!isCardEmpty) intialView();

        //Ask the newest data from server
        //if iscardEmpty is true, set as false and initial View

        return rootView;
    }

    @Override
    public void onRefresh() {
        if (!isCardEmpty) {
            swipeRefreshLayout.setRefreshing(true);
            Message msg = new Message();
            msg.what = 1;
            mHandler.sendMessageDelayed(msg, 2000);
            //申请数据
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //store Cards via Qtbase
    }

    private boolean restoreData() {
        Cards = new ArrayList<Msg>();
        Msg msg = new Msg("This is a Story about the future", "Paris", "drawable://" + R.drawable.paris);
        Cards.add(msg);
        Msg msg2 = new Msg("一个人的旅行，一个人的远方。在悉尼这座城市，享受恬静的海风，任时间流过。", "Sydeney", "drawable://" + R.drawable.sydeney);
        Cards.add(msg2);
        Msg msg3 = new Msg("This is a Story about the future", "GreatWall", "http://static.asiawebdirect.com/m/phuket/portals/www-singapore-com/homepage/attractions/all-attractions/pagePropertiesImage/singapore1.jpg");
        Cards.add(msg3);
        Msg msg4 = new Msg("This is a Story about the future", "Tokyo", "http://www.arrivalguides.com/s3/ag-images-eu/16/d8465238ff0e0298991405b8597d8da6.jpg");
        Cards.add(msg4);
        return true;
    }

    private android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(!isCardEmpty) {
                switch (msg.what) {
                    case 0:
                        // bottom
                        freushFlag = false;
                        Msg msg3 = new Msg("This is a Story about the future", "GreatWall", "drawable://" + R.drawable.greatwall);
                        Cards.add(msg3);
                        swipeRefreshLayout.setRefreshing(false);
                        msgRecycleAdapter.notifyDataSetChanged();
                        break;
                    case 1:
                        //上拉
                        Msg msg2 = new Msg("一个人的旅行，一个人的远方。在悉尼这座城市，享受恬静的海风，任时间流过。", "Sydeney", "drawable://" + R.drawable.sydeney);
                        Cards.add(0, msg2);
                        swipeRefreshLayout.setRefreshing(false);
                        msgRecycleAdapter.notifyDataSetChanged();
                        break;
                }
            }
        }

    };


    //This function should be called only when Cards is not empty
    private void intialView(){
        final LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext());
        msgRecycleAdapter = new MsgRecycleAdapter(rootView.getContext(), Cards);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(msgRecycleAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!freushFlag && newState == RecyclerView.SCROLL_STATE_IDLE && (lastVisibleItem + 1 == msgRecycleAdapter.getItemCount())) {
                    swipeRefreshLayout.setRefreshing(true);
                    freushFlag = true;
                    Message msg = new Message();
                    msg.what = 0;
                    mHandler.sendMessageDelayed(msg, 2000);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });
    }
}
