package com.sjtu.bwphoto.memory.Fragement;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sjtu.bwphoto.memory.R;

/**
 * Created by ly on 7/7/2016.
 */
public class PersonalFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_personal, container, false);
        return rootView;
    }
}
