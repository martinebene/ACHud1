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
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mebene.ACHud.MainActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Vector;

/**
 * Created by miguelmorales on 17/4/15.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Fm_cam_generica extends Fragment {

    AcCore acCore;
    TextView tV_Status;
    EditText eT_Consola;
    boolean bcConsolaActivo;
    ImageButton ibRec, ibStop, ibSyncro, ibAyudaInterface;

    public Fm_cam_generica() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fm_cam_generica, container, false);

        return rootView;
    }

    @Override
    public void onAttach(Context cont) {
        super.onAttach(cont );
        MainActivity ma = (MainActivity) getActivity();
        acCore = ma.acCore;
        Log.e("tag3434", "entre en onAtach");
    }

    /*
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        MainActivity ma = (MainActivity) activity;
        acCore = ma.acCore;

    }
*/

    @Override
    public void onResume() {
        super.onResume();

        eT_Consola = (EditText) getView().findViewById(R.id.eT_Consola);
        //eT_Consola.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter(ServicioAdquisicion2.BROADCAST_MEDICION));

        ibRec = (ImageButton) getView().findViewById(R.id.ibRec);
        ibStop = (ImageButton) getView().findViewById(R.id.ibStop);
        ibSyncro = (ImageButton) getView().findViewById(R.id.ibSyncro);
        ibAyudaInterface = (ImageButton) getView().findViewById(R.id.ibAyudaInterface);

        if(acCore.isAdquisicionRunning()){
            ibRec.setClickable(false);
            ibRec.setBackgroundResource(R.drawable.ic_icono_bsckground_selected);
        }

        ibRec.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                acCore.iniciarAdquisicion();
                ibRec.setClickable(false);
                ibRec.setBackgroundResource(R.drawable.ic_icono_bsckground_selected);


                Toast toast = Toast.makeText(getActivity(), "SYNC", Toast.LENGTH_LONG);
                View toastView = toast.getView(); //This'll return the default View of the Toast.

        /* And now you can get the TextView of the default View of the Toast. */
                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                //toastMessage.setTextSize(R.integer.texto_gigante_altura);
                toastMessage.setTextSize(75);
                toastMessage.setBackgroundColor(getResources().getColor(R.color.rojo));
                //toastMessage.setHighlightColor(getResources().getColor(R.color.rojo));
                //toastMessage.setTextColor(R.color.ro);
                //toastMessage.setGravity(Gravity.CENTER,0,0);
                toastMessage.setCompoundDrawablePadding(16);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();

                // eT_Consola.setTextSize(R.dimen.texto_gigante);
                //eT_Consola.setGravity(Gravity.CENTER);
           //     eT_Consola.setText("SYNC");
           /*     try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
               // eT_Consola.setTextSize(R.dimen.abc_text_size_small_material);
               // eT_Consola.setGravity(Gravity.LEFT);
                //bcConsolaActivo = true;
            }
        });

        ibStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //bcConsolaActivo = false;
                acCore.detenerAdquisicion();
                ibRec.setClickable(true);
                ibRec.setBackgroundResource(R.drawable.ic_icono_bsckground_unselected);
                //eT_Consola.setText("detenido");
            }
        });

        ibSyncro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                eT_Consola.setText(ServicioAdquisicion2.listarSensores(getActivity()));
                //eT_Consola.setText("caca");
            }
        });

        ibAyudaInterface.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*Vector<String> result = new Vector<String>();
                try {
                    FileInputStream f = getActivity().openFileInput("prueba_int.txt");
                    BufferedReader entrada = new BufferedReader(
                            new InputStreamReader(f));
                    int n = 0;
                    String linea;
                    do {
                        linea = entrada.readLine();
                        if (linea != null) {
                            result.add(linea);
                            n++;
                        }
                    } while (linea != null);
                    f.close();
                } catch (Exception e) {
                    Log.e("Asteroides", e.getMessage(), e);
                }*/
                eT_Consola.setText(/*result.toString()*/"pedo");
            }
        });
    }



    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //if(bcConsolaActivo)
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
