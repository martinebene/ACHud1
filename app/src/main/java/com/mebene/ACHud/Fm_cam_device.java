package com.mebene.ACHud;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by miguelmorales on 17/4/15.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class Fm_cam_device extends Fragment implements SurfaceHolder.Callback  {

    AcCore acCore;
    TextView tV_Status;
    EditText eT_Consola;
    ImageButton ibRec, ibStop, ibSyncro, ibAyudaInterface;



    private MediaRecorder mediaRecorder = null;
    //private MediaPlayer mediaPlayer = null;
    private String fileNameVideo = null;
    private boolean recordingVideo = false;
    //Camera mCamera;


    public Fm_cam_device() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fm_cam_device, container, false);

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

        eT_Consola = (EditText) getView().findViewById(R.id.eT_Consola_cd);
        //eT_Consola.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        LocalBroadcastManager.getInstance(this.getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter(ServicioAdquisicion2.BROADCAST_MEDICION));

        ibRec = (ImageButton) getView().findViewById(R.id.ibRec_cd);
        ibStop = (ImageButton) getView().findViewById(R.id.ibStop_cd);
        ibSyncro = (ImageButton) getView().findViewById(R.id.ibSyncro_cd);
        ibAyudaInterface = (ImageButton) getView().findViewById(R.id.ibAyudaInterface_cd);

        final String rutaDeSalida = Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.app_name)+File.separator+getResources().getString(R.string.s_out_dir);

        //fileNameVideo = rutaDeSalida + filename_out;



       // SurfaceView surface = (SurfaceView)getView().findViewById(R.id.surfaceV_cd);
        SurfaceView surface = (SurfaceView)getView().findViewById(R.id.texture);
        SurfaceHolder holder = surface.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);



        if(acCore.isAdquisicionRunning()){
            ibRec.setClickable(false);
            ibRec.setBackgroundResource(R.drawable.ic_icono_bsckground_selected);
        }

        ibRec.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                acCore.iniciarAdquisicion();
                ibRec.setClickable(false);
                ibRec.setBackgroundResource(R.drawable.ic_icono_bsckground_selected);
                Toast toast = Toast.makeText(getActivity(), "RECORDING", Toast.LENGTH_LONG);
                View toastView = toast.getView(); //This'll return the default View of the Toast.
                TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                toastMessage.setCompoundDrawablePadding(16);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();


                prepareRecorder();
                SimpleDateFormat formateador = new SimpleDateFormat("dd_MM_yy_HH_mm_ss");
                String filenameVideo_out = "ACHUD_Out_" + formateador.format(new Date()) +".mp4";
                mediaRecorder.setOutputFile(rutaDeSalida +File.separator+ filenameVideo_out);
                try {
                    mediaRecorder.prepare();
                } catch (IllegalStateException e) {
                } catch (IOException e) {
                }

                mediaRecorder.start();
                recordingVideo = true;

            }
        });

        ibStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //bcConsolaActivo = false;
                acCore.detenerAdquisicion();
                ibRec.setClickable(true);
                ibRec.setBackgroundResource(R.drawable.ic_icono_bsckground_unselected);

                if (recordingVideo) {
                    recordingVideo = false;
                    mediaRecorder.stop();
                    mediaRecorder.reset();
                } /*else if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                }*/

            }
        });

        ibSyncro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                eT_Consola.setText(ServicioAdquisicion2.listarSensores(getActivity()));
            }
        });

        ibAyudaInterface.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                eT_Consola.setText("pedo");
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



    @Override
    public void surfaceCreated(SurfaceHolder holder) {
       // mCamera = Camera.open();
        //mCamera.unlock();

        if (mediaRecorder == null) {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setPreviewDisplay(holder.getSurface());
        }
/*
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDisplay(holder);
        }
*/
    }

    public void prepareRecorder(){
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //mCamera.stopPreview();
        //mCamera.release();

        mediaRecorder.release();
        //mediaPlayer.release();
    }

}
