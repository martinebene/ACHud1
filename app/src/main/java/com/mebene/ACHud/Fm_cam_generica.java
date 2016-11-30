package com.mebene.ACHud;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mebene.ACHud.MainActivity;

/**
 * Created by miguelmorales on 17/4/15.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Fm_cam_generica extends Fragment {

    AcCore acCore;
    TextView tV_Status;
    EditText eT_Consola;
    Button bt1, bt2, bt3;

    public Fm_cam_generica() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fm_cam_generica, container, false);

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

        eT_Consola = (EditText) getView().findViewById(R.id.eT_Consola);

        LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter(ServicioAdquisicion2.BROADCAST_MEDICION));

        bt1 = (Button) getView().findViewById(R.id.bt1);
        bt2 = (Button) getView().findViewById(R.id.bt2);
        bt3 = (Button) getView().findViewById(R.id.bt3);

        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                acCore.iniciarAdquisicion();
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                acCore.detenerAdquisicion();
                eT_Consola.setText("detenido");
            }
        });

        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                eT_Consola.setText(ServicioAdquisicion2.listarSensores(getActivity()));
                //eT_Consola.setText("caca");
            }
        });
    }



    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            eT_Consola.setText(intent.getStringExtra("medicion"));
        }
    };

    @Override
    public void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this.getActivity()).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }
}
