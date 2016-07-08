package com.sjtu.bwphoto.memory.Activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.app.ActionBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import com.sjtu.bwphoto.memory.Class.Msg;
import com.sjtu.bwphoto.memory.Class.MsgRecycleAdapter;
import com.sjtu.bwphoto.memory.Class.TabsPagerAdapter;
import com.sjtu.bwphoto.memory.R;

import java.util.List;

import androidviewhover.BlurLayout;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener{
    private String user_name;

    // Tab titles
    private String[] tabs = { "Recent", "Recommend", "Personal" };

    private ViewPager viewPager;
    private TabsPagerAdapter tabsPagerAdapter;
    private ActionBar actionBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_ACTION_BAR);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        Bundle bundle = this.getIntent().getExtras();  //接收数据
        user_name = bundle.getString("userName");

        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        if (actionBar==null) System.out.println("Create ActionBar Fail !!!");
        tabsPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(tabsPagerAdapter);
        if(actionBar != null) {
            actionBar.setHomeButtonEnabled(false);
            // Specify that tabs should be displayed in the action bar.
            actionBar.setNavigationMode(android.app.ActionBar.NAVIGATION_MODE_TABS);

            // Adding Tabs
            for (String tab : tabs) {
                actionBar.addTab(actionBar.newTab().setText(tab).setTabListener(this));
            }
        }

        /**
         * on swiping the viewpager make respective tab selected
         * */
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected

                if (actionBar != null) actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

    }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            // on tab selected
            // show respected fragment view
            viewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        }

}
