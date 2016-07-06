package com.mebene.ACHud;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

//import apigopro.core.GoProStatus;

public class ServicioAdquisicion extends Service implements SensorEventListener {

    private MedicionDeEntorno medicion;
    private SensorManager sensorManager;
    AsyncMedicion asyncMedicion;
    private final IBinder mBinder = new LocalBinder();


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //**********************************************************************************************************************//
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public ServicioAdquisicion() {
    }

    //**********************************************************************************************************************//
    @Override
    public void onCreate() {
        super.onCreate();


        asyncMedicion = new AsyncMedicion();
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> listaSensores = sensorManager.getSensorList(Sensor.TYPE_ALL);
        //medicion = new MedicionDeEntorno(listaSensores);

        medicion = new MedicionDeEntorno();
        iniciarSensores();

    }


    //**********************************************************************************************************************//
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        asyncMedicion.execute();
        //return super.onStartCommand(intent, flags, startId);
        Log.i("tag111", "Servicio adquisicion onStart");
        return START_STICKY;

    }


    //**********************************************************************************************************************//
    @Override
    public void onDestroy() {
        asyncMedicion.cancel(true);
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
        return mBinder;

    }

    //**********************************************************************************************************************//
    public MedicionDeEntorno getMedicion() {
        return medicion;
    }

    //**********************************************************************************************************************//
    private void iniciarSensores() {

        List<Sensor> listSensors;

        listSensors = sensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);
        if(listSensors.get(0)!= null){
            Sensor magneticSensor = listSensors.get(0);
            sensorManager.registerListener(this, magneticSensor, SensorManager.SENSOR_DELAY_UI);
            medicion.campoMagnetico.disponible = true;
            medicion.campoMagnetico.activo = true;
        }else{
            medicion.campoMagnetico.disponible = false;
        }

        listSensors = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if(listSensors.get(0)!= null){
            Sensor acelerometerSensor = listSensors.get(0);
            sensorManager.registerListener(this, acelerometerSensor, SensorManager.SENSOR_DELAY_UI);
            medicion.aceleracion.disponible = true;
            medicion.aceleracion.activo = true;
        }else{
            medicion.aceleracion.disponible = false;
        }

        listSensors = sensorManager.getSensorList(Sensor.TYPE_GYROSCOPE);
        if(listSensors.get(0)!= null){
            Sensor gyroscopeSensor = listSensors.get(0);
            sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_UI);
            medicion.giro.disponible = true;
            medicion.giro.activo = true;
        }else{
            medicion.giro.disponible = false;
        }


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


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //**********************************************************************************************************************//
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        ServicioAdquisicion getService() {
            // Return this instance of LocalService so clients can call public methods
            return ServicioAdquisicion.this;
        }
    }



    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //**********************************************************************************************************************//
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private class AsyncMedicion extends AsyncTask<Object, Object, Object> {

        public boolean running=false;
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Object doInBackground(Object... params) {
            String msg;
            running = true;
            //GoProHelper local_gp_helper = new GoProHelper("10.5.5.9", 80, "martin123456");
            //GoProHelper local_gp_helper = new GoProHelper();
            //GoProStatus local_gPStatus= new GoProStatus();

            Log.i("tag111", "AsyncMedicion iniciando");
            while (running){
                try {
                        Thread.sleep(4000);
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

            Log.i("tag", "onProgressUpdate: publishing medicion");

            Toast.makeText(getApplicationContext(), medicion.toString3(),
                    Toast.LENGTH_LONG).show();
            //Log.i("tag1", "Info que traigo: \n" + local_gPStatus[0].toString());

        }

        @Override
        protected void onPostExecute(Object o) {
            Log.i("tag111", "AsyncMedicion onPostExecute");
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

    }

}