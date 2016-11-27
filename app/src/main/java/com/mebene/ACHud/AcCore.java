package com.mebene.ACHud;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * Created by Martin on 03/04/2016.
 */
public class AcCore {

    Context context;
    public boolean adquiriendo;
    ServicioAdquisicion2 mService;
    MedicionDeEntorno ultimaMedicion;
    boolean mBound = false;
    public String string_prueba;

    public AcCore(Context lcontext) {

        context = lcontext;
        adquiriendo = false;
        ultimaMedicion = null;
        mBound = false;
        // Bind to LocalService
        //Intent intentServicioAdquisicion = new Intent(context, ServicioAdquisicion2.class);
        //context.bindService(intentServicioAdquisicion, mConnection, Context.BIND_AUTO_CREATE);

        lcontext.startService(new Intent(lcontext, ServicioAdquisicion2.class));

        string_prueba = "harcodeado en acCore";

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
