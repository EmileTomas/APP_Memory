package com.sjtu.bwphoto.memory.Fragement;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sjtu.bwphoto.memory.Activities.MainActivity;
import com.sjtu.bwphoto.memory.Class.Datebase.DatabaseHelper;
import com.sjtu.bwphoto.memory.Class.Datebase.DatabaseManager;
import com.sjtu.bwphoto.memory.Class.Msg;
import com.sjtu.bwphoto.memory.Class.Util.MsgRecycleAdapter;
import com.sjtu.bwphoto.memory.R;

import java.util.ArrayList;
import java.util.List;

import androidviewhover.BlurLayout;

/**
 * Created by ly on 7/7/2016.
 */
public class PersonalFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private final int INITIAL_VIEW=0;
    private final int LOAD_MORE_DATA=1;
    private final int NOTIFY_CARDS_CHANGE=2;

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
    private boolean fetchDataSuccess = false;
    private boolean initializeViewSuccess=false;
    private boolean freushFlag=false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_recent, container, false);
        BlurLayout.setGlobalDefaultDuration(800);

        databaseHelper=new DatabaseHelper(getContext(),"AppDatabase.db",null,1);
        DatabaseManager.initializeInstance(databaseHelper);


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
        if(!freushFlag) {
            swipeRefreshLayout.setRefreshing(true);
            new RefreshDataThread().start();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        new StoreDataThread().start();
    }

    class RestoreDataThread extends Thread {
        @Override
        public void run() {
            super.run();

            //RestoreData from db
            restoreData();
            isCardEmpty = Cards.isEmpty();   //if the database has data, load them and inital view
            if (!isCardEmpty) {
                initializeViewSuccess=true;
                Message msg = new Message();
                msg.what = INITIAL_VIEW;
                mHandler.sendMessage(msg);
            }

            try {
                Thread.currentThread().sleep(2000);//阻断2秒 模仿访问服务器
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //FreshData from server
            fetchDataFirstTime();
        }
    }

    class RefreshDataThread extends Thread {
        @Override
        public void run() {
            super.run();


            if(!fetchDataSuccess) fetchDataFirstTime();  //if fetch data failure last time, freshData from server
            else fetchDataNew();                         //else fetch data new from sever

        }
    }

    class StoreDataThread extends Thread{
        @Override
        public void run() {
            super.run();
            sqLiteDatabase=DatabaseManager.getInstance().openDatabase();
            //DeletePreviousData
            sqLiteDatabase.delete("Page2","account=?",new String[]{userAccount});
            //store Cards via Qtbase
            ContentValues values=new ContentValues();
            for(int i=0;i<Cards.size();++i)
            {
                values.put("account",userAccount);
                values.put("rankNum",i);
                values.put("location",Cards.get(i).getMap_position());
                values.put("memoryText",Cards.get(i).getContent());
                values.put("imageURL",Cards.get(i).getImageUrl());

                sqLiteDatabase.insert("Page2",null,values);
                values.clear();
            }
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    private void restoreData() {
        sqLiteDatabase=DatabaseManager.getInstance().openDatabase();
        Cards = new ArrayList<Msg>();
        Cursor cursor=sqLiteDatabase.query("Page2",null,"account=?",new String[]{userAccount},null,null,null);
        if(cursor.moveToFirst()){
            do{
                int rankNum=cursor.getInt(cursor.getColumnIndex("rankNum"));
                String location=cursor.getString(cursor.getColumnIndex("location"));
                String memoryText=cursor.getString(cursor.getColumnIndex("memoryText"));
                String imageURL=cursor.getString(cursor.getColumnIndex("imageURL"));
                Msg Card=new Msg(memoryText,location,imageURL);
                Cards.add(Card);
            }while(cursor.moveToNext());
        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
    }

    private void fetchDataFirstTime(){
        //if(ConnectServer) return false; //conenct Severfaiure
        //else:
        //  Clear Cards
        //  Receive data and store it to Cards
        //  return true;
        Cards.clear();
        Msg Card4 = new Msg("This is a Story about the future", "Tokyo", "http://www.arrivalguides.com/s3/ag-images-eu/16/d8465238ff0e0298991405b8597d8da6.jpg");
        Cards.add(0,Card4);
        Msg Card3 = new Msg("This is a Story about the future", "GreatWall", "http://static.asiawebdirect.com/m/phuket/portals/www-singapore-com/homepage/attractions/all-attractions/pagePropertiesImage/singapore1.jpg");
        Cards.add(0,Card3);
        Msg Card2 = new Msg("一个人的旅行，一个人的远方。在悉尼这座城市，享受恬静的海风，任时间流过。", "Sydeney", "drawable://" + R.drawable.sydeney);
        Cards.add(0,Card2);
        Msg Card1 = new Msg("This is a Story about the future", "Paris", "drawable://" + R.drawable.paris);
        Cards.add(0,Card1);
        fetchDataSuccess=true;
        isCardEmpty=false;

        if(!initializeViewSuccess){
            initializeViewSuccess=true;
            Message msg = new Message();
            msg.what = INITIAL_VIEW;
            mHandler.sendMessage(msg);
        }
        else{
            Message msg = new Message();
            msg.what = NOTIFY_CARDS_CHANGE;
            mHandler.sendMessage(msg);
        }
    }

    private void fetchDataNew(){
        //fetchData success part
        Msg Card1 = new Msg("This is a Story about the future", "Paris", "drawable://" + R.drawable.paris);
        Cards.add(0,Card1);


        //Notify the data has been updated
        freushFlag=false;
        Message msg = new Message();
        msg.what = NOTIFY_CARDS_CHANGE;
        mHandler.sendMessageDelayed(msg,3000);

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
    private String getUserAccount(){
        MainActivity activity = (MainActivity) getActivity();
        return activity.getUserAccount();
    }
}
