package com.mebene.ACHud;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mebene.ACHud.MainActivity;

/**
 * Created by miguelmorales on 17/4/15.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Fm_cam_generica extends Fragment {

AcCore acCore;
    TextView tV_status;

    public Fm_cam_generica() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fm_cam_generica, container, false);

        //tV_status = (TextView)getView().findViewById(R.id.textView_caca);

        //tV_status.setText(acCore.string_prueba);
        //tV_status.setText("holis");


        return rootView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        MainActivity ma = (MainActivity) activity;
        acCore = ma.acCore;
    }


    @Override
    public void onResume() {
        super.onResume();

        tV_status = (TextView)getView().findViewById(R.id.textView_caca);

        tV_status.setText(acCore.string_prueba);


    }
}
