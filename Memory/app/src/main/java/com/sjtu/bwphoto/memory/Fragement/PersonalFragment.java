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
import com.sjtu.bwphoto.memory.Class.Adapter.Msg;
import com.sjtu.bwphoto.memory.Class.Resource.Memory;
import com.sjtu.bwphoto.memory.Class.Resource.Resource;
import com.sjtu.bwphoto.memory.Class.Resource.ResourceList;
import com.sjtu.bwphoto.memory.Class.RestUtil;
import com.sjtu.bwphoto.memory.Class.ServerUrl;
import com.sjtu.bwphoto.memory.Class.Adapter.MsgRecycleAdapter;
import com.sjtu.bwphoto.memory.R;

import java.util.ArrayList;
import java.util.List;

import androidviewhover.BlurLayout;

/**
 * Created by ly on 7/7/2016.
 */
public class PersonalFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private final int INITIAL_VIEW = 0;
    private final int LOAD_MORE_DATA = 1;
    private final int NOTIFY_CARDS_CHANGE = 2;

    private final int RecentPage = 0;
    private final int PersonalPage = 1;
    private final int RecommendPage = 2;
    private final static ServerUrl url = new ServerUrl();
    ;

    private View rootView;
    private View mainActivityrootVeiw;
    private RecyclerView recyclerView;
    private MsgRecycleAdapter msgRecycleAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Msg> Cards;

    private MainActivity mainActivity;

    private String userAccount;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;

    private int firstVisibleItem;
    private int lastVisibleItem;
    private boolean isCardEmpty = true;
    private boolean fetchDataSuccess = false;
    private boolean initializeViewSuccess = false;
    private boolean freushFlag = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_personal, container, false);
        mainActivity = (MainActivity) getActivity();
        mainActivityrootVeiw = mainActivity.getMainActivityRootView();
        BlurLayout.setGlobalDefaultDuration(400);

        databaseHelper=new DatabaseHelper(getContext(),"AppDatabase.db",null,1);
        DatabaseManager.initializeInstance(databaseHelper);


        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_Refresh_personal);
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
            freushFlag = true;
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


            //FreshData from server
            freushFlag = true;
            fetchDataNew();
        }
    }

    class RefreshDataThread extends Thread {
        @Override
        public void run() {
            super.run();
            fetchDataNew();
        }
    }

    class StoreDataThread extends Thread{
        @Override
        public void run() {
            super.run();
            sqLiteDatabase=DatabaseManager.getInstance().openDatabase();
            //DeletePreviousData
            sqLiteDatabase.delete("PersonalPage","account=?",new String[]{userAccount});
            //store Cards via Qtbase
            ContentValues values=new ContentValues();
            for(int i=0;i<Cards.size();++i)
            {
                Msg card=Cards.get(i);
                values.put("rankNum", i);
                values.put("resourceId",card.getResourceId());
                values.put("account",userAccount);
                values.put("posterAccount",card.getPosterAccount());
                values.put("tag",card.getTag());
                values.put("memoryText",card.getContent());
                values.put("imageURL",card.getImageUrl());
                values.put("musicHash",card.getMusicHash());

                sqLiteDatabase.insert("PersonalPage",null,values);
                values.clear();
            }
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    private void restoreData() {
        sqLiteDatabase=DatabaseManager.getInstance().openDatabase();
        Cards = new ArrayList<Msg>();
        Cursor cursor=sqLiteDatabase.query("PersonalPage",null,"account=?",new String[]{userAccount},null,null,null);
        if(cursor.moveToFirst()){
            do{
                int rankNum=cursor.getInt(cursor.getColumnIndex("rankNum"));
                int resourceId=cursor.getInt(cursor.getColumnIndex("resourceId"));
                String posterAccount=cursor.getString(cursor.getColumnIndex("posterAccount"));
                String tag=cursor.getString(cursor.getColumnIndex("tag"));
                String memoryText=cursor.getString(cursor.getColumnIndex("memoryText"));
                String imageURL=cursor.getString(cursor.getColumnIndex("imageURL"));
                String musicHash=cursor.getString(cursor.getColumnIndex("musicHash"));
                Msg Card=new Msg(resourceId,posterAccount,memoryText,tag,imageURL,musicHash);
                Cards.add(Card);
            }while(cursor.moveToNext());
        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();
    }


    private void fetchDataNew(){
        //fetchData success part
        //Msg Card1 = new Msg("John","This is a Story about the future", "Paris", "drawable://" + R.drawable.paris,"c23d025ee9ece593abd96d7b97db97b4");
        Cards.clear();

        ResourceList resources;
        resources = RestUtil.getForObject(url.url + "/resources/self", ResourceList.class);
        if (resources != null) {
            Memory memory;
            Msg card;
            String imageId;

            for (int i = 0; i < resources.size(); ++i) {
                Resource resource=resources.get(i);
                memory = RestUtil.getForObject(url.url + "/resources/" + resource.getId() + "/words", Memory.class);
                imageId = url.url + "/resources/" + resource.getId() + "/image";

                card = new Msg(resource.getId(),resource.getName(), memory.getContent(), Integer.toString(memory.getTimestamp()), imageId, "c23d025ee9ece593abd96d7b97db97b4");
                Cards.add(0, card);
                System.out.println(url.url + "/resources/" + resources.get(i).getId() + "/words");
                System.out.println(imageId);
            }


            fetchDataSuccess = true;
            isCardEmpty = false;
            freushFlag = false;
            if (!initializeViewSuccess) {
                initializeViewSuccess = true;
                Message msg = new Message();
                msg.what = INITIAL_VIEW;
                mHandler.sendMessage(msg);
            } else {
                Message msg = new Message();
                msg.what = NOTIFY_CARDS_CHANGE;
                mHandler.sendMessage(msg);
            }
        } else {
            freushFlag = false;
            //Notify no new Data;

        }

    }
    //This function will be called only when Cards is not empty
    private void intialView(){
        final LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext());
        msgRecycleAdapter = new MsgRecycleAdapter(Cards, rootView.getContext(), mainActivity, PersonalPage);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_personal);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(msgRecycleAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!freushFlag && newState == RecyclerView.SCROLL_STATE_IDLE && (lastVisibleItem + 1 == msgRecycleAdapter.getItemCount()) && (firstVisibleItem != 0)) {
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
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

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
                        Msg msg3 = new Msg(-1,"Tomas","This is a Story about the future", "GreatWall", "drawable://" + R.drawable.greatwall,"c23d025ee9ece593abd96d7b97db97b4");
                        Cards.add(msg3);
                        swipeRefreshLayout.setRefreshing(false);
                        msgRecycleAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }

    };
    private String getUserAccount(){
        return mainActivity.getUserAccount();
    }
}
