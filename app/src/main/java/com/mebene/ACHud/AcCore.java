package com.mebene.ACHud;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Martin on 03/04/2016.
 */
public class AcCore {

    Context context;
    public boolean adquiriendo;
    ServicioAdquisicion2 mService;
    MedicionDeEntorno ultimaMedicion;

    public String string_prueba;

    public AcCore(Context lcontext) {

        context = lcontext;
        adquiriendo = false;
        ultimaMedicion = null;
        crearDirectorios();
    }

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

    public void detenerAdquisicion(){
        Log.i("tag111", "detenerAdquisicion");
        context.stopService(new Intent(context, ServicioAdquisicion2.class));
    }


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
                        in.close();
                        in = null;
                        out.flush();
                        out.close();
                        out = null;
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
}
