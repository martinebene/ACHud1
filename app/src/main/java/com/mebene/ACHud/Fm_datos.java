package com.mebene.ACHud;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Fm_datos extends Fragment {

    private List<String> item_datos = null;
    private List<String> item_esquemas = null;
    ImageButton ibProcesar;
    AcCore acCore;
    ListView listaArchivosDatos;
    Spinner listaArchivosEsquemas;
    EditText et_delay;
    String archivoDatosSeleccionado, archivoEsquemaSeleccionado;


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


        item_datos = new ArrayList<String>();
        item_esquemas = new ArrayList<String>();

        TextView ruta = (TextView)  getView().findViewById(R.id.tV_ruta);
        ruta.setText("Ruta de datos: "+Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.app_name)+File.separator+ getResources().getString(R.string.s_datos_dir));

        et_delay = (EditText) getView().findViewById(R.id.et_delay);

        Log.e("tag33", "ruta: " + Environment.getExternalStorageDirectory());
        Log.e("tag34", "ruta: " + File.separator);
        Log.e("tag35", "ruta: " + getResources().getString(R.string.app_name));
        Log.e("tag36", "ruta: " + Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.app_name));

        File f = new File(Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.app_name)+File.separator+ getResources().getString(R.string.s_datos_dir));
        File[] files = f.listFiles();

        for (int i = 0; i < files.length; i++){
        File file = files[i];
        if (file.isFile())
            item_datos.add(file.getName());
        }

        f = new File(Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.app_name)+File.separator+ getResources().getString(R.string.s_esquemas_dir));
        files = f.listFiles();

        for (int i = 0; i < files.length; i++){
            File file = files[i];
            if (file.isFile())
                item_esquemas.add(file.getName());
        }

        //Localizamos y llenamos las listas
        listaArchivosDatos = (ListView)  getView().findViewById(R.id.lst_archivos_datos);
        ArrayAdapter<String> fileList = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.item_de_lista, item_datos);
        listaArchivosDatos.setAdapter(fileList);
        listaArchivosDatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                archivoDatosSeleccionado = (String) listaArchivosDatos.getItemAtPosition(position);

            }});

        listaArchivosEsquemas = (Spinner)  getView().findViewById(R.id.lstEsquemas);
        ArrayAdapter fileListEsq = new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, item_esquemas);
        listaArchivosEsquemas.setAdapter(fileListEsq);
/*
        listaArchivosEsquemas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                archivoEsquemaSeleccionado = (String) listaArchivosEsquemas.getItemAtPosition(position);

            }});
*/


        //Botones
        ibProcesar = (ImageButton) getView().findViewById(R.id.ibProcesar);

        ibProcesar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                archivoEsquemaSeleccionado = (String) listaArchivosEsquemas.getSelectedItem();
                int n = acCore.procesarDatos(archivoEsquemaSeleccionado, archivoDatosSeleccionado, et_delay.getText().toString());

                Log.e("tag444", "se procesaron: " + n + " lineas.");
            }
        });






    }

}