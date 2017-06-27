package com.mebene.ACHud;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Martin on 03/04/2016.
 */
public class AcCore {

    Context context;
    public boolean adquiriendo;
    ServicioAdquisicion2 mService;

    public String string_prueba;
    private EsquemaHUD esquemaHud;

    //**********************************************************************************************************************//
    public AcCore(Context lcontext) {

        context = lcontext;
        adquiriendo = false;
        //ultimaMedicion = null;
        crearDirectorios();
    }

    //**********************************************************************************************************************//
    public void iniciarAdquisicion(){
        Log.i("tag111", "iniciarAdquisicion");
        try {
            if(!isAdquisicionRunning()) {
                context.startService(new Intent(context, ServicioAdquisicion2.class));
            } else{
                Toast toast = Toast.makeText(context, R.string.s_mensaje_servicio_en_ejecucion, Toast.LENGTH_LONG);
                toast.show(); }
        } catch (Exception e){
            Log.e("Error", "Error al iniciar servicio");
        }

    }
    //**********************************************************************************************************************//
    public void detenerAdquisicion(){
        Log.i("tag111", "detenerAdquisicion");
        context.stopService(new Intent(context, ServicioAdquisicion2.class));
    }

    //**********************************************************************************************************************//
    public boolean isAdquisicionRunning() {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager
                .getRunningServices(Integer.MAX_VALUE)) {
            if (ServicioAdquisicion2.class.getName().equals(
                    service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    //**********************************************************************************************************************//
    public void crearDirectorios(){
        Log.i("tag111", "crear directorios");
        String pathEsquemas;
        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                File ruta_sd = Environment.getExternalStorageDirectory();
                File ruta_app_dir = new File(ruta_sd.getAbsolutePath(), context.getString(R.string.app_name));
                if (!ruta_app_dir.exists()) {
                    ruta_app_dir.mkdir();
                }
                File f = new File(ruta_app_dir.getAbsolutePath(),context.getString(R.string.s_datos_dir));
                if (!f.exists()) {
                    f.mkdir();
                }
                f = new File(ruta_app_dir.getAbsolutePath(),context.getString(R.string.s_out_dir));
                if (!f.exists()) {
                    f.mkdir();
                }
                f = new File(ruta_app_dir.getAbsolutePath(),context.getString(R.string.s_esquemas_dir));
                if (!f.exists()) {
                    f.mkdir();
                }
                pathEsquemas = f.getAbsolutePath();
                AssetManager assetManager = context.getAssets();

                InputStream in = null;
                OutputStream out = null;
                OutputStream outBack = null;

                String [] fl = assetManager.list(context.getString(R.string.s_esquemas_assets_dir));

                for (int i = 0; i < fl.length; i++) {
                    try {
                        in = assetManager.open(context.getString(R.string.s_esquemas_assets_dir)+File.separator+fl[i]);
                        String newFileName = pathEsquemas +File.separator+ fl[i];
                        out = new FileOutputStream(newFileName);

                        byte[] buffer = new byte[1024];
                        int read;
                        while ((read = in.read(buffer)) != -1) {
                            out.write(buffer, 0, read);
                        }
                        if(isXML(fl[i])){
                            String newFileNameBack = pathEsquemas +File.separator+ fl[i]+".back";
                            outBack = new FileOutputStream(newFileNameBack);
                            while ((read = in.read(buffer)) != -1) {
                                outBack.write(buffer, 0, read);
                            }
                        }

                        in.close();
                        in = null;
                        out.flush();
                        outBack.flush();
                        out.close();
                        outBack.close();
                        out = null;
                        outBack = null;
                    } catch (Exception e) {
                        System.out.println("Exception in copyFile" + e);
                    }
                }
            } else{
                Log.e("tag23", "no disponible alamacenamiento externo");
            }
        }
        catch (Exception ex){
            Log.e("Ficheros", "Error al escribir fichero en memoria interna");
        }

    }


    //**********************************************************************************************************************//
    public int procesarDatos(String fn_esquema, String fn_datos, int delay, int irm_gui){

        int n=0;
        int irm=0;
        int nroLine=0;
        String readLine=null;

        Log.i("tag444", "procesar datos con: " + fn_esquema +" "+ fn_datos + " " + delay);

        File f_datos=null, f_esquema=null, f_salida_procesada=null;
        OutputStreamWriter fout;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            f_datos = new File(Environment.getExternalStorageDirectory() + File.separator + context.getResources().getString(R.string.app_name)+File.separator
                    +context.getResources().getString(R.string.s_datos_dir)+ File.separator + fn_datos);
            f_esquema = new File(Environment.getExternalStorageDirectory() + File.separator + context.getResources().getString(R.string.app_name)+File.separator
                    +context.getResources().getString(R.string.s_esquemas_dir)+ File.separator + fn_esquema);
        } else{
            Log.e("tag23", "no disponible alamacenamiento externo");
            return -1;
        }

        if(f_esquema.exists()){
            try{
                Log.i("tag444", "entre f esquemas");
                FileInputStream fstream = new FileInputStream(f_esquema);
                DataInputStream in = new DataInputStream(fstream);

                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser parser = factory.newSAXParser();
                EsquemaHandler handler = new EsquemaHandler();
                parser.parse(in, handler);
                esquemaHud = handler.getEsquema();

                in.close();

                Log.i("tag555 esquema Ext: ", esquemaHud.getExt());
                Log.i("tag555 esquema Header: ", esquemaHud.getHeader());
                Log.i("tag555 esquema intSub: ", esquemaHud.getIntro_sub() );
                Log.i("tag555 esquema medsub: ", esquemaHud.getMed_sub() );
                Log.i("tag555 esquema Delay:", ""+esquemaHud.getDelay() );

            }catch (Exception e){
                Log.e("Procesar", "Error al procesar esquema" + e);
                return -1;}
        }else{
            Toast.makeText(context, context.getResources().getString(R.string.s_elemento_no_seleccionado), Toast.LENGTH_LONG).show();
            return -1;
        }

        if(f_datos.exists()){
            try{
                Log.i("tag444", "entre f datos");

                SimpleDateFormat formateador = new SimpleDateFormat("dd_MM_yy_HH_mm_ss");
                String filename_out = "ACHUD_Out_" + formateador.format(new Date()) +"."+esquemaHud.getExt();

                f_salida_procesada = new File(Environment.getExternalStorageDirectory() + File.separator + context.getResources().getString(R.string.app_name)+File.separator
                        +context.getResources().getString(R.string.s_out_dir), filename_out);

                if (f_salida_procesada != null)
                    fout = new OutputStreamWriter(new FileOutputStream(f_salida_procesada));
                else {
                    Log.e("Procesar", "Error al abrir archivo de salida");
                    return -1;}

                FileInputStream fstream = new FileInputStream(f_datos);
                DataInputStream in = new DataInputStream(fstream);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));

                fout.write(esquemaHud.getHeader());

                Long tMedicionAnterior = 0L;

                String[] arrayValoresAnterior= null;

                Log.i("tag4444", "Esto devuelve:... "+esquemaHud.getIntervaloRef());

                if(irm_gui>esquemaHud.getIntervaloRef()){
                    irm = irm_gui;
                } else {
                    irm = (int) esquemaHud.getIntervaloRef();
                }

                nroLine=0;

                while ((readLine = br.readLine()) != null) {

                    String[] arrayValores = readLine.split(",");
                    //aca chequear si no es un achivo con una linea valida

                    String lineaSrt="";

                    if ((Long.valueOf(arrayValores[MedicionDeEntorno.EDA.T0_SSS_ABS.ordinal()]) - tMedicionAnterior) > irm) {
                        tMedicionAnterior = Long.valueOf(arrayValores[MedicionDeEntorno.EDA.T0_SSS_ABS.ordinal()]);

                        if (arrayValoresAnterior != null) {
                            nroLine++;

                            arrayValoresAnterior[MedicionDeEntorno.EDA.T1_HH_MED.ordinal()] = arrayValores[MedicionDeEntorno.EDA.T0_HH_MED.ordinal()];
                            arrayValoresAnterior[MedicionDeEntorno.EDA.T1_mm_MED.ordinal()] = arrayValores[MedicionDeEntorno.EDA.T0_mm_MED.ordinal()];
                            arrayValoresAnterior[MedicionDeEntorno.EDA.T1_ss_MED.ordinal()] = arrayValores[MedicionDeEntorno.EDA.T0_ss_MED.ordinal()];
                            arrayValoresAnterior[MedicionDeEntorno.EDA.T1_SSS_MED.ordinal()] = arrayValores[MedicionDeEntorno.EDA.T0_SSS_MED.ordinal()];

                            String[] arrayValoresConDelay = aplicarDelay(arrayValoresAnterior, delay + esquemaHud.getDelay());

                            if(Long.valueOf(arrayValores[MedicionDeEntorno.EDA.T0_SSS_ABS.ordinal()])<esquemaHud.getIntroTime())
                                lineaSrt = esquemaHud.getIntro_sub();
                            else
                                lineaSrt = esquemaHud.getMed_sub();

                            for (MedicionDeEntorno.EDA valor : MedicionDeEntorno.EDA.values()) {
                                lineaSrt = lineaSrt.replaceAll("\\{" + valor.toString() + "\\}", arrayValoresConDelay[valor.ordinal()]);
                            }
                            lineaSrt = lineaSrt.replaceAll("\\{" + "NRO_LINE" + "\\}", String.valueOf(nroLine));

                            fout.write(lineaSrt);
                            Log.i("tag444", lineaSrt);
                        }

                    arrayValoresAnterior = arrayValores;
                    }
                }

                fout.write(esquemaHud.getFooter());

                Log.i("tag444", "lineas al final del readline de datos: "+String.valueOf(n));
                in.close();
                fout.close();

            }catch (Exception e){
                Log.e("tag444", "Error al procesar: " + e);
                return -1;}

        } else{
            Toast.makeText(context, context.getResources().getString(R.string.s_elemento_no_seleccionado), Toast.LENGTH_LONG).show();
        }

        return nroLine;
    }


//**********************************************************************************************************************//
    private String[] aplicarDelay(String[] arrayValores, int delayString) {
        long delayLong;
        String hora="", minuto="", segundo="", milisegundo="";

        try {
            delayLong = delayString;
        }catch (NumberFormatException e){
            delayLong=0;
        }

        hora = String.valueOf(TimeUnit.MILLISECONDS.toHours(delayLong));
        minuto = String.valueOf(TimeUnit.MILLISECONDS.toMinutes(delayLong) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(delayLong)));
        segundo = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(delayLong) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(delayLong)));
        //milisegundo = String.valueOf((delayLong - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(delayLong)))/10);
        milisegundo = String.valueOf((delayLong - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(delayLong))));

        arrayValores[MedicionDeEntorno.EDA.T0_HH_MED.ordinal()] =   String.format(Locale.getDefault(),"%01d",
                Long.valueOf(arrayValores[MedicionDeEntorno.EDA.T0_HH_MED.ordinal()]) +
                Long.valueOf(hora) );
        arrayValores[MedicionDeEntorno.EDA.T0_mm_MED.ordinal()] =   String.format(Locale.getDefault(),"%02d",
                Long.valueOf(arrayValores[MedicionDeEntorno.EDA.T0_mm_MED.ordinal()]) +
                        Long.valueOf(minuto) );
        arrayValores[MedicionDeEntorno.EDA.T0_ss_MED.ordinal()] =   String.format(Locale.getDefault(),"%02d",
                Long.valueOf(arrayValores[MedicionDeEntorno.EDA.T0_ss_MED.ordinal()]) +
                        Long.valueOf(segundo) );
        arrayValores[MedicionDeEntorno.EDA.T0_SSS_MED.ordinal()] =   String.format(Locale.getDefault(),"%03d",
                Long.valueOf(arrayValores[MedicionDeEntorno.EDA.T0_SSS_MED.ordinal()]) +
                        Long.valueOf(milisegundo) );
        arrayValores[MedicionDeEntorno.EDA.CR_HH_MED.ordinal()] = arrayValores[MedicionDeEntorno.EDA.T0_HH_MED.ordinal()];
        arrayValores[MedicionDeEntorno.EDA.CR_mm_MED.ordinal()] = arrayValores[MedicionDeEntorno.EDA.T0_mm_MED.ordinal()];
        arrayValores[MedicionDeEntorno.EDA.CR_ss_MED.ordinal()] = arrayValores[MedicionDeEntorno.EDA.T0_ss_MED.ordinal()];
        arrayValores[MedicionDeEntorno.EDA.CR_SSS_MED.ordinal()] = arrayValores[MedicionDeEntorno.EDA.T0_SSS_MED.ordinal()];
        arrayValores[MedicionDeEntorno.EDA.T1_HH_MED.ordinal()] =   String.format(Locale.getDefault(),"%01d",
                Long.valueOf(arrayValores[MedicionDeEntorno.EDA.T1_HH_MED.ordinal()]) +
                        Long.valueOf(hora) );
        arrayValores[MedicionDeEntorno.EDA.T1_mm_MED.ordinal()] =   String.format(Locale.getDefault(),"%02d",
                Long.valueOf(arrayValores[MedicionDeEntorno.EDA.T1_mm_MED.ordinal()]) +
                        Long.valueOf(minuto) );
        arrayValores[MedicionDeEntorno.EDA.T1_ss_MED.ordinal()] =   String.format(Locale.getDefault(),"%02d",
                Long.valueOf(arrayValores[MedicionDeEntorno.EDA.T1_ss_MED.ordinal()]) +
                        Long.valueOf(segundo) );
        arrayValores[MedicionDeEntorno.EDA.T1_SSS_MED.ordinal()] =   String.format(Locale.getDefault(),"%03d",
                Long.valueOf(arrayValores[MedicionDeEntorno.EDA.T1_SSS_MED.ordinal()]) +
                        Long.valueOf(milisegundo) );

        return arrayValores;
    }


    //**********************************************************************************************************************//
    /** Defines callbacks for service binding, passed to bindService() */
/*    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            ServicioAdquisicion2.LocalBinder binder = (ServicioAdquisicion2.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
*/
    //**********************************************************************************************************************//
  /*  public MedicionDeEntorno getUltimaMedicion() {

        if (mBound) {
            // Call a method from the LocalService.
            // However, if this call were something that might hang, then this request should
            // occur in a separate thread to avoid slowing down the activity performance.
            ultimaMedicion = mService.getMedicion();
        }

        return ultimaMedicion;
    }
*/
//********************************************************************************************************************************
    public void copyFile(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }

//**********************************************************************************************************************//
    public boolean isXML(String filename) {
        String filenameArray[] = filename.split("\\.");
        String extension = filenameArray[filenameArray.length-1];
        if (extension.toLowerCase().compareTo("xml")==0)
            return true;
        else
            return false;
    }

    //**********************************************************************************************************************//
    public boolean isBack(String filename) {
        String filenameArray[] = filename.split("\\.");
        String extension = filenameArray[filenameArray.length-1];
        if (extension.toLowerCase().compareTo("back")==0)
            return true;
        else
            return false;
    }

    //**********************************************************************************************************************//
    public boolean isCsv(String filename) {
        String filenameArray[] = filename.split("\\.");
        String extension = filenameArray[filenameArray.length-1];
        if (extension.toLowerCase().compareTo("csv")==0)
            return true;
        else
            return false;
    }

//**********************************************************************************************************************//
}