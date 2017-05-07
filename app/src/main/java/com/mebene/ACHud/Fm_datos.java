package com.mebene.ACHud;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Fm_datos extends Fragment {

    private List<String> item = null;
    AcCore acCore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fm_datos, container, false);

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


        item = new ArrayList<String>();

        TextView ruta = (TextView)  getView().findViewById(R.id.tV_ruta);
        ruta.setText("Ruta de datos: "+Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.app_name)+File.separator+ getResources().getString(R.string.s_datos_dir));

        Log.e("tag33", "ruta: " + Environment.getExternalStorageDirectory());
        Log.e("tag34", "ruta: " + File.separator);
        Log.e("tag35", "ruta: " + getResources().getString(R.string.app_name));
        Log.e("tag36", "ruta: " + Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.app_name));

        File f = new File(Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.app_name)+File.separator+ getResources().getString(R.string.s_datos_dir));
        File[] files = f.listFiles();

        for (int i = 0; i < files.length; i++){
        File file = files[i];
        if (file.isFile())
            item.add(file.getName());
        }

        //Localizamos y llenamos la lista
        ListView lstOpciones = (ListView)  getView().findViewById(R.id.lst_archivos_datos);
        ArrayAdapter<String> fileList = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.item_de_lista, item);
        lstOpciones.setAdapter(fileList);

        // Accion para realizar al pulsar sobre la lista
        lstOpciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> a, View v, int position,   long id) {

        //item.get(position)


        }
        });
    }

}