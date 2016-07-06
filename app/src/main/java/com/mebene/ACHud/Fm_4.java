package com.mebene.ACHud;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by miguelmorales on 17/5/15.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Fm_4 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fm_4, container, false);

        return rootView;
    }
}
