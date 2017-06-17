package com.mebene.ACHud;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Fm_huds extends Fragment {


    private List<String> item_esquemas = null;
    ImageButton ibEditar;
    AcCore acCore;
    ListView listaArchivosEsquemas;
    String rutaHuds;
    String archivoEsquemaSeleccionado;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fm_huds, container, false);

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


        item_esquemas = new ArrayList<String>();

        rutaHuds = Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.app_name)+File.separator+ getResources().getString(R.string.s_esquemas_dir);
        TextView ruta = (TextView)  getView().findViewById(R.id.tV_ruta_huds);
        ruta.setText("Ruta de HUD's: "+ rutaHuds);


        Log.e("tag33", "ruta: " + Environment.getExternalStorageDirectory());
        Log.e("tag34", "ruta: " + File.separator);
        Log.e("tag35", "ruta: " + getResources().getString(R.string.app_name));
        Log.e("tag36", "ruta: " + Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.app_name));

        File f = new File(Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.app_name)+File.separator+ getResources().getString(R.string.s_esquemas_dir));
        File[] files = f.listFiles();

        for (int i = 0; i < files.length; i++){
        File file = files[i];
        if (file.isFile() && isXML(file))
            item_esquemas.add(file.getName());
        Log.i("tag4444", "archivo: " + file.getName());
        }


        //Localizamos y llenamos las listas
        listaArchivosEsquemas = (ListView)  getView().findViewById(R.id.lst_archivos_esquemas);
        ArrayAdapter<String> fileList = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.item_de_lista, item_esquemas);
        listaArchivosEsquemas.setAdapter(fileList);

        listaArchivosEsquemas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                archivoEsquemaSeleccionado = (String) listaArchivosEsquemas.getItemAtPosition(position);

            }});




/*
        listaArchivosEsquemas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                archivoEsquemaSeleccionado = (String) listaArchivosEsquemas.getItemAtPosition(position);

            }});
*/


        //Botones

        ibEditar = (ImageButton) getView().findViewById(R.id.ibEditHud);

        ibEditar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //archivoEsquemaSeleccionado = (String) listaArchivosEsquemas.getSelectedItem();
                //int n = acCore.procesarDatos(archivoEsquemaSeleccionado, archivoDatosSeleccionado, et_delay.getText().toString());

                File file = new File(rutaHuds + File.separator +archivoEsquemaSeleccionado);

                if (file.exists()) {
                    Uri path = Uri.fromFile(file);
                    Intent intent = new Intent(Intent.ACTION_EDIT);
                    intent.setDataAndType(path, "text/plain");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    try {
                        startActivity(intent);
                    }

                    catch (ActivityNotFoundException e) {
                        Toast.makeText(getActivity(),
                                "No Application Available to View File: " + e,
                                Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    Toast.makeText(getActivity(),
                            "Debe elegir un archivo de la lista",
                            Toast.LENGTH_SHORT).show();
                }

                Log.i("tag4444", "Se selecciono: " + archivoEsquemaSeleccionado);
            }
        });

    }

    private boolean isXML(File file) {

        String filename = file.getName();
        String filenameArray[] = filename.split("\\.");
        String extension = filenameArray[filenameArray.length-1];
        if (extension.toLowerCase().compareTo("xml")==0)
            return true;
        else
            return false;
    }

}