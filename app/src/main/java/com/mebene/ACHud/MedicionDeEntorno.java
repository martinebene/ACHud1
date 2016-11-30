package com.mebene.ACHud;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Martin on 01/07/2015.
 */

public class MedicionDeEntorno {

    Calendar fechaYhora;
    float velocidad, velocidadMaxima, velocidadPromedio;
    long cantMediciiones;
    Aceleracion aceleracion;
    Giro giro;
    CampoMagnetico campoMagnetico;
    Clima clima;
    Cronometro cronometro;


    //**********************************************************************************************************************//
//    public MedicionDeEntorno(List<Sensor> listaSensores) {
        public MedicionDeEntorno() {


/*
        for(Sensor sensor: listaSensores) {
            Log.i("tag", sensor.getName());
        }
*/
        fechaYhora = Calendar.getInstance();
        velocidad=velocidadMaxima=velocidadPromedio=0;
        cantMediciiones=0;
        aceleracion= new Aceleracion(false, false);
        giro=new Giro(false, false);
        campoMagnetico=new CampoMagnetico(false, false);
        clima=new Clima(false, false);
        cronometro=new Cronometro(false, false);
    }

    @Override
    public String toString() {
        return "medicionDeEntorno{" +
                "fechaYhora=" + fechaYhora +
                ", velocidad=" + velocidad +
                ", velocidadMaxima=" + velocidadMaxima +
                ", velocidadPromedio=" + velocidadPromedio +
                ", cantMediciiones=" + cantMediciiones +
                ", acelecarion=" + aceleracion +
                ", giro=" + giro +
                ", campoMagnetico=" + campoMagnetico +
                ", clima=" + clima +
                ", cronometro=" + cronometro +
                '}';
    }

    public String toString2() {
        return aceleracion + "\n"+
               giro + "\n"+
               campoMagnetico +"\n"+
                cronometro;
    }

    public String toString3() {
        return aceleracion.toString();
    }

    public String toString4() {
        String salida="";
        if(aceleracion.activo) salida = salida + aceleracion + "\n";
        if(giro.activo) salida = salida + giro + "\n";
        if(campoMagnetico.activo) salida = salida + campoMagnetico + "\n";
        if(cronometro.activo) salida = salida + cronometro + "\n";

        return salida;
    }
}


//**********************************************************************************************************************//
class Aceleracion {

    public long timestamp;
    boolean activo, disponible;

    float x,y,z,ax,ay,az,maxX,maxY,maxZ,minX,minY,minZ;
    float [] gravity;
    final float alpha = 0.8f;
    final int delayMax = 30;
    int i;

    Aceleracion(boolean ldisponible, boolean lactivo) {
        activo = lactivo;
        disponible = ldisponible;
        x=y=z=ax=ay=az=maxX=maxY=maxZ=minX=minY=minZ=0;
        i=0;
        gravity = new float[3];
        gravity[0] = 0f;
        gravity[1] = 0f;
        gravity[2] = 0f;
    }

    public void push(float lx, float ly, float lz){
        ax=x;
        ay=y;
        az=z;

        gravity[0] = alpha * gravity[0] + (1 - alpha) * lx;
        gravity[1] = alpha * gravity[1] + (1 - alpha) * ly;
        gravity[2] = alpha * gravity[2] + (1 - alpha) * lz;

        x = lx - gravity[0];
        y = ly - gravity[1];
        z = lz - gravity[2];

        if(i>delayMax) {
            if (x > maxX) maxX = x;
            if (y > maxY) maxY = y;
            if (z > maxZ) maxZ = z;
            if (x < minX) minX = x;
            if (y < minY) minY = y;
            if (z < minZ) minZ = z;
        }
        i++;
    }

    @Override
    public String toString() {
        return "Aceleracion:\n" +
                "x=" + String.format("%.2f", x) + ", "+ String.format("%.2f", maxX) + ", "+ String.format("%.2f", minX) +"\n"+
                "y=" + String.format("%.2f", y) + ", "+ String.format("%.2f", maxY) +", "+ String.format("%.2f", minY) +"\n"+
                "z=" + String.format("%.2f", z) + ", ="+ String.format("%.2f", maxZ) + ", "+ String.format("%.2f", minZ) +"\n";
    }
}

//**********************************************************************************************************************//
class Giro {

    public long timestamp;
    boolean activo, disponible;

    float x,y,z,ax,ay,az,maxX,maxY,maxZ,minX,minY,minZ;

    Giro(boolean ldisponible, boolean lactivo) {
        activo = lactivo;
        disponible = ldisponible;
        x=y=z=ax=ay=az=maxX=maxY=maxZ=minX=minY=minZ=0;
    }

    public void push(float lx, float ly, float lz){
        ax=x;
        ay=y;
        az=z;
        x=lx;
        y=ly;
        z=lz;
        if(x>maxX) maxX=x;
        if(y>maxY) maxY=y;
        if(z>maxZ) maxZ=z;
        if(x<minX) minX=x;
        if(y<minY) minY=y;
        if(z<minZ) minZ=z;
    }

    @Override
    public String toString() {
        return "Giro:\n" +
                "x=" + String.format("%.2f", x) + ", "+ String.format("%.2f", maxX) + ", "+ String.format("%.2f", minX) +"\n"+
                "y=" + String.format("%.2f", y) + ", "+ String.format("%.2f", maxY) +", "+ String.format("%.2f", minY) +"\n"+
                "z=" + String.format("%.2f", z) + ", ="+ String.format("%.2f", maxZ) + ", "+ String.format("%.2f", minZ) +"\n";
    }
}

//**********************************************************************************************************************//
class CampoMagnetico {

    public long timestamp;
    boolean activo, disponible;

    float x,y,z,ax,ay,az,maxX,maxY,maxZ,minX,minY,minZ;

    CampoMagnetico(boolean ldisponible, boolean lactivo) {
        activo = lactivo;
        disponible = ldisponible;
        x=y=z=ax=ay=az=maxX=maxY=maxZ=minX=minY=minZ=0;
    }

    public void push(float lx, float ly, float lz){
        ax=x;
        ay=y;
        az=z;
        x=lx;
        y=ly;
        z=lz;
        if(x>maxX) maxX=x;
        if(y>maxY) maxY=y;
        if(z>maxZ) maxZ=z;
        if(x<minX) minX=x;
        if(y<minY) minY=y;
        if(z<minZ) minZ=z;
    }

    @Override
    public String toString() {
        return "CampoMag:\n" +
                "x=" + String.format("%.2f", x) + ", "+ String.format("%.2f", maxX) + ", "+ String.format("%.2f", minX) +"\n"+
                "y=" + String.format("%.2f", y) + ", "+ String.format("%.2f", maxY) +", "+ String.format("%.2f", minY) +"\n"+
                "z=" + String.format("%.2f", z) + ", ="+ String.format("%.2f", maxZ) + ", "+ String.format("%.2f", minZ) +"\n";
    }
}

//**********************************************************************************************************************//
class Clima {

    public long timestamp;
    boolean activo, disponible;

    String temp, presion, humedad, intensidadViento, direViento, locacion;

    Clima(boolean ldisponible, boolean lactivo) {
        activo = lactivo;
        disponible = ldisponible;
        temp = presion = humedad = intensidadViento = direViento = locacion = "";
    }


    @Override
    public String toString() {
        return "Clima{" +
                "temp='" + temp + '\'' +
                ", presion='" + presion + '\'' +
                ", humedad='" + humedad + '\'' +
                ", intensidadViento='" + intensidadViento + '\'' +
                ", direViento='" + direViento + '\'' +
                ", locacion='" + locacion + '\'' +
                '}';
    }
}

//**********************************************************************************************************************//
class Cronometro {

    boolean activo, disponible;
    public long t0, transcurrido;


    Cronometro(boolean ldisponible, boolean lactivo) {
        activo = lactivo;
        disponible = ldisponible;
        t0=transcurrido=0;
    }

    void iniciar(){
        t0=System.currentTimeMillis();
    }

    long getTranscurrido() {
        transcurrido = System.currentTimeMillis()-t0;
        return transcurrido;
    }

    @Override
    public String toString() {

       // Date date = new Date(this.getTranscurrido());
       //return date.toString();

        //SimpleDateFormat df= new SimpleDateFormat("hh:mm:ss");
        //String formatted = df.format(date );
        long millis = this.getTranscurrido();
        String formatted = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), // The change is in this line
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        return "Cronometro: " + formatted;
    }


}