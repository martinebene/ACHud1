package com.mebene.ACHud;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Fm_datos extends Fragment {

    private List<String> item_datos = null;
    private List<String> item_esquemas = null;
    ImageButton ibProcesar, ibOpen, ibDeleteDato;
    AcCore acCore;
    ListView listaArchivosDatos;
    ArrayAdapter<String> fileListAdapter;
    public final int ORDEN_ASCENDENTE = 1;
    public final int ORDEN_DESCENDENTE = -1;
    int order = ORDEN_ASCENDENTE;
    Spinner listaArchivosEsquemas;
    EditText et_delay;
    String archivoDatosSeleccionado, archivoEsquemaSeleccionado, rutaDatos;


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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here
        inflater.inflate(R.menu.menu_datos, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


//********************************************************************************************************************************
    @Override
    public void onResume() {
        super.onResume();

        //final int order=1;
        archivoDatosSeleccionado=null;
        item_datos = new ArrayList<String>();
        item_esquemas = new ArrayList<String>();

        rutaDatos = Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.app_name)+File.separator+ getResources().getString(R.string.s_datos_dir);
        TextView ruta = (TextView)  getView().findViewById(R.id.tV_ruta);
        ruta.setText("Ruta de datos: "+rutaDatos);

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
            if (file.isFile() && acCore.isXML(file.getName()))
                item_esquemas.add(file.getName());
        }

        //Localizamos y llenamos las listas
        listaArchivosDatos = (ListView)  getView().findViewById(R.id.lst_archivos_datos);
        fileListAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.item_de_lista, item_datos);
        sortList(order);
        listaArchivosDatos.setAdapter(fileListAdapter);
        listaArchivosDatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                archivoDatosSeleccionado = (String) listaArchivosDatos.getItemAtPosition(position);

            }});

        listaArchivosEsquemas = (Spinner)  getView().findViewById(R.id.lstEsquemas);
        //ArrayAdapter fileListEsq = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_item, item_esquemas);
        ArrayAdapter fileListEsq = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.item_de_lista, item_esquemas);
        fileListEsq.sort(new Comparator<String>() {
            @Override
            public int compare(String arg0, String arg1) {
                return arg0.compareTo(arg1);
            }
        });
        listaArchivosEsquemas.setAdapter(fileListEsq);
/*
        listaArchivosEsquemas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                archivoEsquemaSeleccionado = (String) listaArchivosEsquemas.getItemAtPosition(position);

            }});
*/


//***********************************************************************************************************************
        //Botones
        ibProcesar = (ImageButton) getView().findViewById(R.id.ibProcesar);
        ibProcesar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                archivoEsquemaSeleccionado = (String) listaArchivosEsquemas.getSelectedItem();
                int n = acCore.procesarDatos(archivoEsquemaSeleccionado, archivoDatosSeleccionado, et_delay.getText().toString());

                Log.e("tag444", "se procesaron: " + n + " lineas.");
            }
        });


        ibOpen = (ImageButton) getView().findViewById(R.id.ibOpen);
        ibOpen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                File file = new File(rutaDatos + File.separator +archivoDatosSeleccionado);

                if (file.exists()) {
                    Uri path = Uri.fromFile(file);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(path, "text/csv");
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
                    Toast.makeText(getActivity(),"Debe elegir un archivo de la lista", Toast.LENGTH_SHORT).show();
                }

                Log.i("tag4444", "Se selecciono para editar: " + archivoDatosSeleccionado);


            }
        });

        ibDeleteDato = (ImageButton) getView().findViewById(R.id.ibDeleteDato);
        ibDeleteDato.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //archivoEsquemaSeleccionado = (String) listaArchivosEsquemas.getSelectedItem();
                //int n = acCore.procesarDatos(archivoEsquemaSeleccionado, archivoDatosSeleccionado, et_delay.getText().toString());

                final File file = new File(rutaDatos + File.separator +archivoDatosSeleccionado);

                if (file.exists()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Esta seguro que desea proceder?");
                    builder.setMessage("Esta accion eliminara el archivo " + archivoDatosSeleccionado + " de forma permanente");
                    builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing but close the dialog
                            file.delete();
                            onResume();
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else {
                    Toast.makeText(getActivity(),"Debe elegir un archivo de la lista", Toast.LENGTH_SHORT).show();
                }
                Log.i("tag4444", "Se elimino: " + archivoDatosSeleccionado);
            }
        });

    }

//********************************************************************************************************************************
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("tag4444", "Se ingerso al menu con: " + item.getItemId());
        File file=null;
        switch (item.getItemId()) {
            case (R.id.refresh):
                onResume();
                Toast.makeText(getActivity(),"Vista refrescada", Toast.LENGTH_SHORT).show();
                return true;
            case (R.id.abrir_con_fbrowser):
                file = new File(rutaDatos);
                if (file.exists()) {
                    //Uri path = Uri.fromFile(file);
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setDataAndType(Uri.fromFile(file), "*/*");
                    //intent.setDataAndType(Uri.fromFile(file), "text/csv");
                    //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    try {
                        startActivity(intent);
                        //startActivity(Intent.createChooser(intent, "Open folder"));
                    }
                    catch (ActivityNotFoundException e) {
                        Toast.makeText(getActivity(), "No Application Available to View File: " + e, Toast.LENGTH_SHORT).show();
                    }
                }
                Log.i("tag4444", "Se selecciono: " + archivoDatosSeleccionado);
                return true;
            case (R.id.invertOrder):
                if(order == ORDEN_ASCENDENTE) {
                    order = ORDEN_DESCENDENTE;
                    sortList(order);
                }
                else{
                    order = ORDEN_ASCENDENTE;
                    sortList(order);
                }
                onResume();
                Toast.makeText(getActivity(), "Datos reordenados", Toast.LENGTH_SHORT).show();
                return true;
            case (R.id.infoFile):
                file = new File(rutaDatos + File.separator +archivoDatosSeleccionado);
                if (file.exists()) {
                    String lastLine="";
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(file));
                        String last = br.readLine();
                        while (last != null) {
                            lastLine = last;
                            last = br.readLine();
                        }
                    }catch (Exception e){ e.printStackTrace(); }
                    String[] arrayValores = lastLine.split(",");
                    if(arrayValores.length != MedicionDeEntorno.EDA.values().length){
                        Toast.makeText(getActivity(), "Archivo de datos no valido", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    int size = (int)(file.length()/1024);
                    DecimalFormat format = new DecimalFormat("###,###.##");
                    long lastmodified = file.lastModified();
                    Date dateModified = new Date();
                    dateModified.setTime(lastmodified);
                    SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd 'a las' HH:mm:ss");
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Detalles");
                    builder.setMessage(
                            "Nombre:\n"+archivoDatosSeleccionado+"\n\n"
                            +"Ruta:\n"+rutaDatos+"\n\n"
                            +"Tamaño:\n"+format.format(size)+"Kb\n\n"
                            +"Ultima Modificacion:\n"+dateFormater.format(dateModified)+"\n\n"
                            +"Tiempo de medicion:\n"
                                    +arrayValores[MedicionDeEntorno.EDA.CR_HH_MED.ordinal()]+":"
                                    +arrayValores[MedicionDeEntorno.EDA.CR_mm_MED.ordinal()]+":"
                                    +arrayValores[MedicionDeEntorno.EDA.CR_ss_MED.ordinal()]+","
                                    +arrayValores[MedicionDeEntorno.EDA.CR_SSS_MED.ordinal()]+"\n\n"
                            +"Cantidad de registros:\n"+arrayValores[MedicionDeEntorno.EDA.NRO_MED.ordinal()]
                    );
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else{
                    Toast.makeText(getActivity(),"Debe elegir un archivo de la lista", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//********************************************************************************************************************************
    public void sortList(int order) {
        if(order >= 0){
            fileListAdapter.sort(new Comparator<String>() {
                @Override
                public int compare(String arg0, String arg1) {
                    File file0 = new File(rutaDatos + File.separator +arg0);
                    File file1 = new File(rutaDatos + File.separator +arg1);
                    return String.valueOf(file0.lastModified()).compareTo(String.valueOf(file1.lastModified()));
                }
            });
            fileListAdapter.notifyDataSetChanged();
        }
        else{
            fileListAdapter.sort(new Comparator<String>() {
                @Override
                public int compare(String arg0, String arg1) {
                    File file0 = new File(rutaDatos + File.separator + arg0);
                    File file1 = new File(rutaDatos + File.separator + arg1);
                    return String.valueOf(file1.lastModified()).compareTo(String.valueOf(file0.lastModified()));
                }
            });
            fileListAdapter.notifyDataSetChanged();
        }
    }


//********************************************************************************************************************************
 /*   public void sortList(int order) {
        if(order >= 0){
            fileListAdapter.sort(new Comparator<String>() {
                @Override
                public int compare(String arg0, String arg1) {
                    String filenamePartsArray0[] = arg0.split("_");
                    String filenamePartsArray1[] = arg1.split("_");
                    if (filenamePartsArray0[4].compareTo(filenamePartsArray1[4]) != 0)
                        return filenamePartsArray0[4].compareTo(filenamePartsArray1[4]);
                    if (filenamePartsArray0[3].compareTo(filenamePartsArray1[3]) != 0)
                        return filenamePartsArray0[3].compareTo(filenamePartsArray1[3]);
                    if (filenamePartsArray0[2].compareTo(filenamePartsArray1[2]) != 0)
                        return filenamePartsArray0[2].compareTo(filenamePartsArray1[2]);
                    if (filenamePartsArray0[5].compareTo(filenamePartsArray1[5]) != 0)
                        return filenamePartsArray0[5].compareTo(filenamePartsArray1[5]);
                    if (filenamePartsArray0[6].compareTo(filenamePartsArray1[6]) != 0)
                        return filenamePartsArray0[6].compareTo(filenamePartsArray1[6]);
                    if (filenamePartsArray0[7].compareTo(filenamePartsArray1[7]) != 0)
                        return filenamePartsArray0[7].compareTo(filenamePartsArray1[7]);
                    return arg0.compareToIgnoreCase(arg1);
                }
            });
            fileListAdapter.notifyDataSetChanged();
        }
        else{
            fileListAdapter.sort(new Comparator<String>() {
                @Override
                public int compare(String arg0, String arg1) {
                    String filenamePartsArray0[] = arg1.split("_");
                    String filenamePartsArray1[] = arg0.split("_");
                    if (filenamePartsArray0[4].compareTo(filenamePartsArray1[4]) != 0)
                        return filenamePartsArray0[4].compareTo(filenamePartsArray1[4]);
                    if (filenamePartsArray0[3].compareTo(filenamePartsArray1[3]) != 0)
                        return filenamePartsArray0[3].compareTo(filenamePartsArray1[3]);
                    if (filenamePartsArray0[2].compareTo(filenamePartsArray1[2]) != 0)
                        return filenamePartsArray0[2].compareTo(filenamePartsArray1[2]);
                    if (filenamePartsArray0[5].compareTo(filenamePartsArray1[5]) != 0)
                        return filenamePartsArray0[5].compareTo(filenamePartsArray1[5]);
                    if (filenamePartsArray0[6].compareTo(filenamePartsArray1[6]) != 0)
                        return filenamePartsArray0[6].compareTo(filenamePartsArray1[6]);
                    if (filenamePartsArray0[7].compareTo(filenamePartsArray1[7]) != 0)
                        return filenamePartsArray0[7].compareTo(filenamePartsArray1[7]);
                    return arg0.compareToIgnoreCase(arg1);
                }
            });
            fileListAdapter.notifyDataSetChanged();
        }
    }
*/

    }