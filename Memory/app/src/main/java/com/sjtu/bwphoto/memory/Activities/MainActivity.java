package com.sjtu.bwphoto.memory.Activities;

import android.os.Bundle;
import android.renderscript.RenderScript;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TableLayout;

import com.sjtu.bwphoto.memory.Class.Util.TabsPagerAdapter;
import com.sjtu.bwphoto.memory.Fragement.PersonalFragment;
import com.sjtu.bwphoto.memory.Fragement.RecentFragment;
import com.sjtu.bwphoto.memory.Fragement.RecommendFragment;
import com.sjtu.bwphoto.memory.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private String user_name;

    // Tab titles
    private PersonalFragment personalFragment;
    private RecentFragment recentFragment;
    private RecommendFragment recommendFragment;
    private ArrayList<Fragment> list_fragment;
    private ArrayList<String> list_tab;

    //widgets
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabsPagerAdapter tabsPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Receive Data from last activity
        Bundle bundle = this.getIntent().getExtras();
        user_name = bundle.getString("userName");

        initial_widget();
    }

    private void initial_widget() {
        tabLayout=(TabLayout) findViewById(R.id.tablayout);
        viewPager =(ViewPager) findViewById(R.id.pager);

        //initial fragments and organizes by list
        personalFragment=new PersonalFragment();
        recentFragment= new RecentFragment();
        recommendFragment = new RecommendFragment();
        list_fragment = new ArrayList<>();
        list_fragment.add(recentFragment);
        list_fragment.add(personalFragment);
        list_fragment.add(recommendFragment);

        //intial tab titles
        list_tab =new ArrayList<>();
        list_tab.add("Recent");
        list_tab.add("Personal");
        list_tab.add("Recommend");

        //set tabs
        tabLayout.addTab(tabLayout.newTab().setText(list_tab.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(list_tab.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(list_tab.get(2)));

        //set Adapter for ViewPager
        tabsPagerAdapter = new TabsPagerAdapter(this.getSupportFragmentManager(),list_fragment,list_tab);
        viewPager.setAdapter(tabsPagerAdapter);
        //TabLayout加载viewpager
        tabLayout.setupWithViewPager(viewPager);

    }
}