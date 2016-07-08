package com.sjtu.bwphoto.memory.Class.Util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sjtu.bwphoto.memory.Fragement.PersonalFragment;
import com.sjtu.bwphoto.memory.Fragement.RecentFragment;
import com.sjtu.bwphoto.memory.Fragement.RecommendFragment;

/**
 * Created by ly on 7/6/2016.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /*
     * Fragment selection
     */
    @Override
    public Fragment getItem(int index) {

        switch(index){
            case 0:
                return new RecentFragment();
            case 1:
                return new RecommendFragment();
            case 2:
                return new PersonalFragment();
        }
        return null;
    }

    /*
     * Return the item count which is equal to the number of tabs
     */
    @Override
    public int getCount() {
        return 3;
    }
}
