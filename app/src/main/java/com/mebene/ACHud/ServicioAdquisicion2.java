package com.mebene.ACHud;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;


public class ServicioAdquisicion2 extends Service implements SensorEventListener, IBaseGpsListener {

    static final public String BROADCAST_MEDICION = "com.mebene.ACHud.BROADCAST_MEDICION";
    private MedicionDeEntorno medicion;
    private SensorManager sensorManager;
    AsyncMedicion asyncMedicion;
    public Context lcontext = this;
    SharedPreferences sharedPref;
    //private final IBinder mBinder = new LocalBinder();


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //**********************************************************************************************************************//
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public ServicioAdquisicion2() {
    }

    //**********************************************************************************************************************//
    @Override
    public void onCreate() {
        super.onCreate();


        asyncMedicion = new AsyncMedicion();


    }


    //**********************************************************************************************************************//
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        medicion = new MedicionDeEntorno(sharedPref);


        iniciarSensores();


        asyncMedicion.execute();
        //return super.onStartCommand(intent, flags, startId);
        Log.i("tag111", "Servicio adquisicion onStart");
        return START_NOT_STICKY;

    }


    //**********************************************************************************************************************//
    @Override
    public void onDestroy() {
        asyncMedicion.stop();
        Log.i("tag111", "Servicio adquisicion onDestroy");
        super.onDestroy();

    }


    //**********************************************************************************************************************//
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.i("tag111", "Servicio adquisicion onTaskRemoved");
        asyncMedicion.cancel(true);
        stopSelf();
    }

    //**********************************************************************************************************************//
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;

    }

    //**********************************************************************************************************************//
    public MedicionDeEntorno getMedicion() {
        return medicion;
    }

    //**********************************************************************************************************************//
    private void iniciarSensores() {


        medicion.cronometro.activo=true;

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        this.updateSpeed(null);
        medicion.velocidad.disponible = true; //meter en una condicion si gps disponible
        medicion.velocidad.activo = sharedPref.getBoolean("check_box_preference_velocidad",true);;


        List<Sensor> listSensors;

        listSensors = sensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);
        if(!listSensors.isEmpty()){
            Sensor magneticSensor = listSensors.get(0);
            sensorManager.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_UI);
            medicion.campoMagnetico.disponible = true;
            medicion.campoMagnetico.activo = sharedPref.getBoolean("check_box_preference_magnetico",true);
        }else{
            medicion.campoMagnetico.disponible = false;
        }

        listSensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if(!listSensors.isEmpty()){
            Sensor acelerometerSensor = listSensors.get(0);
            sensorManager.registerListener(this, acelerometerSensor, SensorManager.SENSOR_DELAY_UI);
            medicion.aceleracion.disponible = true;
            medicion.aceleracion.activo = sharedPref.getBoolean("check_box_preference_acelerometro",true);
        }else{
            medicion.aceleracion.disponible = false;
        }

        listSensors = sensorManager.getSensorList(Sensor.TYPE_GYROSCOPE);
        if(!listSensors.isEmpty()){
            Sensor gyroscopeSensor = listSensors.get(0);
            sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_UI);
            medicion.giro.disponible = true;
            medicion.giro.activo = sharedPref.getBoolean("check_box_preference_giro",true);
        }else{
            medicion.giro.disponible = false;
        }


    }

    //**********************************************************************************************************************//
    public static String listarSensores(Context c) {

        String salida="";
        SensorManager lsensorManager = (SensorManager)c.getSystemService(Context.SENSOR_SERVICE);;

        List<Sensor> listaSensores = lsensorManager.getSensorList(Sensor.TYPE_ALL);
        for(Sensor sensor: listaSensores) {
            salida = salida + sensor.getName() + "\n";
        }
        return salida;
    }

    //**********************************************************************************************************************//
    @Override
    public void onSensorChanged(SensorEvent evento) {

        //Log.i("tag", "entro a sensor changed");


        synchronized (this) {
            switch(evento.sensor.getType()) {
                case Sensor.TYPE_GYROSCOPE:
                    //Log.i("Sen", "Orientación "+i+": "+evento.values[i]);
                    medicion.giro.push(evento.values[0], evento.values[1], evento.values[2]);
                    break;
                case Sensor.TYPE_ACCELEROMETER:
                    //Log.i("Sen","Acelerómetro "+i+": "+evento.values[i]);
                    medicion.aceleracion.push(evento.values[0], evento.values[1], evento.values[2]);
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    //Log.i("Sen","Magnetismo "+i+": "+evento.values[i]);
                    medicion.campoMagnetico.push(evento.values[0], evento.values[1], evento.values[2]);
                    break;
                default:

            }
            //limpiarConsola();
            //agregarTextoAConsola(medicion.toString2());
        }



    }


    //**********************************************************************************************************************//
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null)
        {
            this.updateSpeed(location);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onGpsStatusChanged(int event) {

    }

    //**********************************************************************************************************************//
    public void updateSpeed(Location location)
    {
        double nCurrentSpeed = 0;

        if( location!=null )
        {
            nCurrentSpeed = location.getSpeed();
        }
        //String strUnits = "Km/h";
        medicion.velocidad.setVelocidad(nCurrentSpeed);

    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //**********************************************************************************************************************//
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
  /*  public class LocalBinder extends Binder {
        ServicioAdquisicion2 getService() {
            // Return this instance of LocalService so clients can call public methods
            return ServicioAdquisicion2.this;
        }
    }
*/


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //**********************************************************************************************************************//
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private class AsyncMedicion extends AsyncTask<Object, Object, Object> {

        public boolean running=false;

        OutputStreamWriter fout;
        String filename_out=null;
        @Override
        protected void onPreExecute() {

            Calendar c = Calendar.getInstance();
            filename_out = "ACHUD_Out_" + c.get(Calendar.DAY_OF_MONTH)+"_"+
                    c.get(Calendar.MONTH)+"_"+
                    c.get(Calendar.YEAR)+"_"+
                    c.get(Calendar.HOUR_OF_DAY)+"_"+
                    c.get(Calendar.MINUTE)+"_"+
                    c.get(Calendar.SECOND)+".csv";
            try {

                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                    File ruta_sd = Environment.getExternalStorageDirectory();
                    File ruta_app_dir = new File(ruta_sd.getAbsolutePath(), getResources().getString(R.string.app_name));
                    if (!ruta_app_dir.exists()) {
                        ruta_app_dir.mkdir();
                    }
                    File f = new File(ruta_app_dir.getAbsolutePath(),filename_out);
                    fout = new OutputStreamWriter(new FileOutputStream(f));

                } else{
                    this.cancel(true);
                    stopSelf();
                    Log.e("tag23", "no disponible alamacenamiento externo");
                }
            }
            catch (Exception ex){
                Log.e("Ficheros", "Error al escribir fichero a memoria interna");
            }
        }

        @Override
        protected Object doInBackground(Object... params) {
            String msg;

            if (isCancelled()){
                return null;
            }
            running = true;
            medicion.cronometro.iniciar();

            Log.i("tag111", "AsyncMedicion iniciando");
            while (running){
                try {
                        Thread.sleep(100);
                        publishProgress();
                } catch (Exception e) {
                    e.printStackTrace();
                    running=false;
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate (Object... params) {

            //Log.i("tag", "onProgressUpdate: publishing medicion" + medicion.toString3());
            medicion.cronometro.getTranscurrido();
            try {
                fout.write(medicion.toCVS());

            } catch (IOException e) {
                Log.e("Ficheros", "Error al escribir fichero a memoria interna linea");
                e.printStackTrace();
            }


            Intent intent = new Intent(BROADCAST_MEDICION);
            intent.putExtra("medicion", medicion.toString4());
            LocalBroadcastManager.getInstance(lcontext).sendBroadcast(intent);
           // Toast.makeText(getApplicationContext(), medicion.toString3(), Toast.LENGTH_LONG).show();
            //Log.i("tag1", "Info que traigo: \n" + local_gPStatus[0].toString());

        }

        @Override
        protected void onPostExecute(Object o) {
            Log.i("tag111", "AsyncMedicion onPostExecute");
            try {
                fout.flush();
                fout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            running=false;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.i("tag111", "AsyncMedicion onCancelled");
            running=false;
        }

        public boolean isRunning(){
            return running;
        }

        public void stop(){
            this.running=false;
        }

    }

}