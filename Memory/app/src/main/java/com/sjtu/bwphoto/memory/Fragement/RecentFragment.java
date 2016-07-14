package com.sjtu.bwphoto.memory.Fragement;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sjtu.bwphoto.memory.Activities.MainActivity;
import com.sjtu.bwphoto.memory.Class.DatabaseHelper;
import com.sjtu.bwphoto.memory.Class.Msg;
import com.sjtu.bwphoto.memory.Class.Util.MsgRecycleAdapter;
import com.sjtu.bwphoto.memory.R;

import java.util.ArrayList;
import java.util.List;

import androidviewhover.BlurLayout;

/**
 * Created by ly on 7/7/2016.
 * Place need to change:
 * Function
 *      fectchData() should be modified as web access Code
 *
 * class
 *      RefreshDataThread
 *          mHandler.sendMessageDelayed(msg,1000); remove 1000
 *
 * load_more_data in the handler should create a thread for it;
 *
 * Ondestory should also create a thread for it ;
 */
public class RecentFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private final int INITIAL_VIEW=0;
    private final int REFRESH_DATA=1;
    private final int LOAD_MORE_DATA=2;
    private final int NOTIFY_CARDS_CHANGE=3;

    private View rootView;
    private RecyclerView recyclerView;
    private MsgRecycleAdapter msgRecycleAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Msg> Cards;

    private String userAccount;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    private int lastVisibleItem;
    private boolean isCardEmpty=true;
    private boolean freushFlag = false;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_recent, container, false);
        BlurLayout.setGlobalDefaultDuration(800);

        databaseHelper=new DatabaseHelper(getContext(),"AppDatabase.db",null,1);

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

        //restore data from last time and refresh data
        userAccount=getUserAccount();
        new RestoreDataThread().start();

        return rootView;
    }

    @Override
    public void onRefresh() {
        if (!isCardEmpty) {
            swipeRefreshLayout.setRefreshing(true);
            new RefreshDataThread().start();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        sqLiteDatabase=databaseHelper.getWritableDatabase();
        //DeletePreviousData
        sqLiteDatabase.delete("Page1",null,null);
        //store Cards via Qtbase
        ContentValues values=new ContentValues();
        for(int i=0;i<Cards.size();++i)
        {
            values.put("account",userAccount);
            values.put("rankNum",i);
            values.put("location",Cards.get(i).getMap_position());
            values.put("memoryText",Cards.get(i).getContent());
            values.put("imageURL",Cards.get(i).getImageUrl());

            sqLiteDatabase.insert("Page1",null,values);
            values.clear();
        }
        sqLiteDatabase.close();
    }


    class  RestoreDataThread extends Thread {
        @Override
        public void run() {
            super.run();

            //RestoreData from db
            restoreData();
            isCardEmpty = Cards.isEmpty();   //if the database has data, load them and inital view
            if (!isCardEmpty) {
                Message msg = new Message();
                msg.what = INITIAL_VIEW;
                mHandler.sendMessage(msg);
            }

            //FreshData from server
            fetchData();
            if (isCardEmpty) {
                Message msg = new Message();
                isCardEmpty = false;
                msg.what = INITIAL_VIEW;
                mHandler.sendMessage(msg);
            }

            //Notify the data has been updated, notice here may be a bug if view was not initial before the msg was send
            Message msg = new Message();
            msg.what = NOTIFY_CARDS_CHANGE;
            mHandler.sendMessage(msg);
        }
    }

    private void restoreData() {
        sqLiteDatabase=databaseHelper.getWritableDatabase();
        Cards = new ArrayList<Msg>();
        Cursor cursor=sqLiteDatabase.query("Page1",null,"account=?",new String[]{userAccount},null,null,null);
        if(cursor.moveToFirst()){
            do{
                int rankNum=cursor.getInt(cursor.getColumnIndex("rankNum"));
                Log.d("Restore from database", "restoreData: "+rankNum);
            }while(cursor.moveToNext());
        }
        cursor.close();
    }

    private void fetchData(){
        Msg msg4 = new Msg("This is a Story about the future", "Tokyo", "http://www.arrivalguides.com/s3/ag-images-eu/16/d8465238ff0e0298991405b8597d8da6.jpg");
        Cards.add(0,msg4);
        Msg msg3 = new Msg("This is a Story about the future", "GreatWall", "http://static.asiawebdirect.com/m/phuket/portals/www-singapore-com/homepage/attractions/all-attractions/pagePropertiesImage/singapore1.jpg");
        Cards.add(0,msg3);
        Msg msg2 = new Msg("一个人的旅行，一个人的远方。在悉尼这座城市，享受恬静的海风，任时间流过。", "Sydeney", "drawable://" + R.drawable.sydeney);
        Cards.add(0,msg2);
        Msg msg = new Msg("This is a Story about the future", "Paris", "drawable://" + R.drawable.paris);
        Cards.add(0,msg);
    }

    //This function will be called only when Cards is not empty
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
                    msg.what = LOAD_MORE_DATA;
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

    class  RefreshDataThread extends Thread {
        @Override
        public void run() {
            super.run();

            //FreshData from server
            fetchData();

            //Notify the data has been updated
            Message msg = new Message();
            msg.what = NOTIFY_CARDS_CHANGE;
            mHandler.sendMessageDelayed(msg,2000);
        }
    }

    private android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case INITIAL_VIEW:
                    intialView();
                    break;

                case NOTIFY_CARDS_CHANGE:
                    swipeRefreshLayout.setRefreshing(false);
                    msgRecycleAdapter.notifyDataSetChanged();
                    break;

                case LOAD_MORE_DATA  :
                    if(!isCardEmpty) {
                        // bottom
                        freushFlag = false;
                        Msg msg3 = new Msg("This is a Story about the future", "GreatWall", "drawable://" + R.drawable.greatwall);
                        Cards.add(msg3);
                        swipeRefreshLayout.setRefreshing(false);
                        msgRecycleAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }

    };
    public String getUserAccount(){
        MainActivity activity = (MainActivity) getActivity();
        return activity.getUserAccount();
    }
}

