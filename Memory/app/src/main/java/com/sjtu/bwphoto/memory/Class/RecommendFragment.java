package com.sjtu.bwphoto.memory.Class;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sjtu.bwphoto.memory.R;

/**
 * Created by ly on 7/7/2016.
 */
public class RecommendFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recommend, container, false);
        return rootView;
    }
}
