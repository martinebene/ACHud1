package com.mebene.ACHud;

import android.content.SharedPreferences;
import android.util.Log;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Formatter;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by Martin on 01/07/2015.
 */

public class MedicionDeEntorno {

    int nroDeMedicion=0;
    Calendar fechaYhora;
    long cantMediciiones;
    Aceleracion aceleracion;
    Giro giro;
    CampoMagnetico campoMagnetico;
    Clima clima;
    Cronometro cronometro;
    Velocidad velocidad;
    SharedPreferences sharedPref;
    public static final int KMH=0, MPH=1, MS=2, MKM=3, MM=4;
    //Estructura de datos de archivo
    public enum EDA
    {
        T0_HH_MED,
        T0_mm_MED,
        T0_ss_MED,
        T0_SSS_MED,
        T1_HH_MED,
        T1_mm_MED,
        T1_ss_MED,
        T1_SSS_MED,
        NRO_MED,
        VEL,
        VEL_MAX,
        ACEL_X,
        ACEL_MA_AB_X,
        ACEL_MA_X,
        ACEL_MI_X,
        ACEL_Y,
        ACEL_MA_AB_Y,
        ACEL_MA_Y,
        ACEL_MI_Y,
        ACEL_Z,
        ACEL_MA_AB_Z,
        ACEL_MA_Z,
        ACEL_MI_Z
        }


    //**********************************************************************************************************************//
//    public MedicionDeEntorno(List<Sensor> listaSensores) {
        public MedicionDeEntorno(SharedPreferences l_sharedPref) {


/*
        for(Sensor sensor: listaSensores) {
            Log.i("tag", sensor.getName());
        }
*/
        sharedPref = l_sharedPref;
        fechaYhora = Calendar.getInstance();

        velocidad = new Velocidad(false, false, sharedPref.getString("list_preference_unidades", "Km/h"));
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
                ", velocidadMaxima=" + velocidad.velocidadMaxima +
                ", velocidadPromedio=" + velocidad.velocidadPromedio +
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
        if(cronometro.activo) salida = salida + cronometro + "\n\n";
        if(velocidad.activo) salida = salida + velocidad + "\n";

        return salida;
    }

    public String toStringDisplay() {
        String salida="";
        if(aceleracion.activo) salida = salida + aceleracion + "\n";
        if(giro.activo) salida = salida + giro + "\n";
        if(campoMagnetico.activo) salida = salida + campoMagnetico + "\n";
        if(cronometro.activo) salida = salida + cronometro.toString("HH:mm:ss") + "\n\n";
        if(velocidad.activo) salida = salida + velocidad + "\n";

        return salida;
    }

    public String toCVS() {
        String salida="";
        salida = salida + nroDeMedicion + ",";
        //if(cronometro.activo) salida = salida + cronometro + ",";
        if(velocidad.activo) salida = salida + velocidad.getVel() + ","; else salida = salida + "_,";
        if(velocidad.activo) salida = salida + velocidad.getVelMax() + ","; else salida = salida + "_,";
        if(aceleracion.activo) salida = salida + aceleracion.getX() + ","; else salida = salida + "_,";
        if(aceleracion.activo) salida = salida + aceleracion.getMaxAbX() + ","; else salida = salida + "_,";
        if(aceleracion.activo) salida = salida + aceleracion.getMaxX() + ","; else salida = salida + "_,";
        if(aceleracion.activo) salida = salida + aceleracion.getMinX()+ ","; else salida = salida + "_,";
        if(aceleracion.activo) salida = salida + aceleracion.getY() + ","; else salida = salida + "_,";
        if(aceleracion.activo) salida = salida + aceleracion.getMaxAbY() + ","; else salida = salida + "_,";
        if(aceleracion.activo) salida = salida + aceleracion.getMaxY() + ","; else salida = salida + "_,";
        if(aceleracion.activo) salida = salida + aceleracion.getMinY() + ","; else salida = salida + "_,";
        if(aceleracion.activo) salida = salida + aceleracion.getZ() + ","; else salida = salida + "_,";
        if(aceleracion.activo) salida = salida + aceleracion.getMaxAbZ() + ","; else salida = salida + "_,";
        if(aceleracion.activo) salida = salida + aceleracion.getMaxZ() + ","; else salida = salida + "_,";
        if(aceleracion.activo) salida = salida + aceleracion.getMinZ(); else salida = salida + "_,";
        //if(giro.activo) salida = salida + giro + "\n";
        //if(campoMagnetico.activo) salida = salida + campoMagnetico + "\n";
        salida=salida+"\n";
        return salida;
    }

    public String toCVS2() {
        String salida="";
        salida = salida + nroDeMedicion + ",";
        //if(cronometro.activo) salida = salida + cronometro + ",";
        if(velocidad.activo) salida = salida + velocidad + ","; else salida = salida + "_,";
        if(aceleracion.activo) salida = salida + aceleracion.getX() + ","; else salida = salida + "_,";
        if(aceleracion.activo) salida = salida + aceleracion.getMaxAbX() + ","; else salida = salida + "_,";
        if(aceleracion.activo) salida = salida + aceleracion.getMaxX() + ","; else salida = salida + "_,";
        if(aceleracion.activo) salida = salida + aceleracion.getMinX()+ ","; else salida = salida + "_,";
        if(aceleracion.activo) salida = salida + aceleracion.getY() + ","; else salida = salida + "_,";
        if(aceleracion.activo) salida = salida + aceleracion.getMaxAbY() + ","; else salida = salida + "_,";
        if(aceleracion.activo) salida = salida + aceleracion.getMaxY() + ","; else salida = salida + "_,";
        if(aceleracion.activo) salida = salida + aceleracion.getMinY() + ","; else salida = salida + "_,";
        if(aceleracion.activo) salida = salida + aceleracion.getZ() + ","; else salida = salida + "_,";
        if(aceleracion.activo) salida = salida + aceleracion.getMaxAbZ() + ","; else salida = salida + "_,";
        if(aceleracion.activo) salida = salida + aceleracion.getMaxZ() + ","; else salida = salida + "_,";
        if(aceleracion.activo) salida = salida + aceleracion.getMinZ(); else salida = salida + "_,";
        //if(giro.activo) salida = salida + giro + "\n";
        //if(campoMagnetico.activo) salida = salida + campoMagnetico + "\n";
        salida=salida+"\n";
        return salida;
    }
}


//**********************************************************************************************************************//
class Aceleracion {

    public long timestamp;
    public boolean activo, disponible;

    float x,y,z,ax,ay,az,maxX,maxY,maxZ,minX,minY,minZ;
    float [] gravity;
    final float alpha = 0.8f;
    final int delayMax = 30;
    int i;
    DecimalFormat df;

    Aceleracion(boolean ldisponible, boolean lactivo) {
        activo = lactivo;
        disponible = ldisponible;
        x=y=z=ax=ay=az=maxX=maxY=maxZ=minX=minY=minZ=0;
        i=0;
        gravity = new float[3];
        gravity[0] = 0f;
        gravity[1] = 0f;
        gravity[2] = 0f;

        df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);
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

    public String getX() {
        if (x>0) return "+"+df.format(x);
        else return df.format(x);
    }

    public String getY() {
        if (y>0) return "+"+df.format(y);
        else return df.format(y);
    }

    public String getZ() {
        if (z>0) return "+"+df.format(z);
        else return df.format(z);
    }

    public String getMaxX() {
        return "+"+df.format(maxX);
    }

    public String getMaxY() {
        return "+"+df.format(maxY);
    }

    public String getMaxZ() {
        return "+"+df.format(maxZ);
    }

    public String getMinX() {
        return df.format(minX);
    }

    public String getMinY() {
        return df.format(minY);
    }

    public String getMinZ() {
        return df.format(minZ);
    }

    public String getMaxAbX() {
        if(Math.abs(maxX)>Math.abs(minX))return  "+"+df.format(maxX);
        else return  df.format(minX);
    }

    public String getMaxAbY() {
        if(Math.abs(maxY)>Math.abs(minY))return  "+"+df.format(maxY);
        else return  df.format(minY);
    }

    public String getMaxAbZ() {
        if(Math.abs(maxZ)>Math.abs(minZ))return  "+"+df.format(maxZ);
        else return  df.format(minZ);
    }

    @Override
    public String toString() {
        return "Aceleracion:\n" +
                "x=" + String.format("%.2f", getX()) + ", Max: "+ getMaxAbX() +"\n"+
                "y=" + String.format("%.2f", getY()) + ", Max: "+ getMaxAbZ() +"\n"+
                "z=" + String.format("%.2f", getZ()) + ", Max: "+ getMaxAbZ() +"\n";
    }

}

//**********************************************************************************************************************//
class Giro {

    public long timestamp;
    public boolean activo, disponible;

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
    public boolean activo, disponible;

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
    public boolean activo, disponible;

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

    public boolean activo, disponible;
    public long tInicial, t0;

    Cronometro(boolean ldisponible, boolean lactivo) {
        activo = lactivo;
        disponible = ldisponible;
        tInicial = t0 =0;
    }

    void iniciar(){
        tInicial =System.currentTimeMillis();
    }

    long getT0() {
        t0 = System.currentTimeMillis()- tInicial;
        return t0;
    }


    @Override
    public String toString() {

       // Date date = new Date(this.getTranscurrido());
       //return date.toString();

        //SimpleDateFormat df= new SimpleDateFormat("hh:mm:ss");
        //String formatted = df.format(date );
        long millis = this.getT0();

/*
        long time = end - init;
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        final SimpleDateFormat sdfParser = new SimpleDateFormat("HH:mm:ss", new Locale("ES"));
        String sTime = sdfParser.format(cal.getTime());

        String formatted = String.format("%02d:%02d:%02d,%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), // The change is in this line
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)),
                (millis - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(millis)))/10

        );
        return formatted;*/
        return Long.toString(millis);

    }

    public String toString(String formato) {

        // Date date = new Date(this.getTranscurrido());
        //return date.toString();

        //SimpleDateFormat df= new SimpleDateFormat("hh:mm:ss");
        //String formatted = df.format(date );
        long millis = this.getT0();
        TimeZone tz = TimeZone.getDefault();

        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        final SimpleDateFormat sdfParser = new SimpleDateFormat(formato);
        sdfParser.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdfParser.format(cal.getTime());

    }
}


//**********************************************************************************************************************//
class Velocidad {

    public boolean activo, disponible;
    public double velocidad, velocidadMaxima, velocidadPromedio;
    int unidad;
    public String strUnidad;

    Velocidad(boolean ldisponible, boolean lactivo, String l_strUnidad) {
        activo = lactivo;
        disponible = ldisponible;
        velocidad = velocidadMaxima = velocidadPromedio = 0;
        //unidad=l_unidad;
        strUnidad=l_strUnidad;

        switch (strUnidad) {
            case "Km/h": unidad = MedicionDeEntorno.KMH; Log.e("Aviso2", "Entre en kmh: " + unidad + " y str: " + strUnidad);break;
            case "Millas/h": unidad = MedicionDeEntorno.MPH; Log.e("Aviso2", "Entre en mph: " + unidad + " y str: " + strUnidad);break;
            case "m/s": unidad = MedicionDeEntorno.MS; Log.e("Aviso2", "Entre en m/s: " + unidad + " y str: " + strUnidad);break;
            case "min/km": unidad = MedicionDeEntorno.MKM;Log.e("Aviso2", "Entre en min/km: " + unidad + " y str: " + strUnidad);break;
            case "min/milla": unidad = MedicionDeEntorno.MM;Log.e("Aviso2", "Entre en min/milla: " + unidad + " y str: " + strUnidad);break;
            default:
                unidad = MedicionDeEntorno.KMH;
                strUnidad="Km/h";
                Log.e("Aviso2", "Entre en default: " + unidad + " y str: " + strUnidad);break;
        }


        /*
        *         switch (unidad) {
            case MedicionDeEntorno.KMH: strUnidad = "Km/h"; break;
            case MedicionDeEntorno.MPH: strUnidad = "Millas/h"; break;
            case MedicionDeEntorno.MS: strUnidad = "m/s"; break;
            case MedicionDeEntorno.MKM: strUnidad = "min/km"; break;
            case MedicionDeEntorno.MM: strUnidad = "min/milla"; break;
        }
        * */
    }


    public void setVelocidad (double velMedida) {

        Log.e("Aviso2", "Entre en set vel con velmedida: " + velMedida );

        if (velMedida > velocidadMaxima)
            velocidadMaxima=velMedida;

            switch (unidad) {
                case MedicionDeEntorno.KMH:
                    velocidad = velMedida * 3.6; Log.e("Aviso2", "Entre en kmh: " + unidad + " y str: " + strUnidad);break;
                case MedicionDeEntorno.MPH:
                    velocidad = velMedida * 2.23694; Log.e("Aviso2", "Entre en mph: " + unidad + " y str: " + strUnidad);break;
                case MedicionDeEntorno.MS:
                    velocidad = velMedida; Log.e("Aviso2", "Entre en m/s: " + unidad + " y str: " + strUnidad);break;
                case MedicionDeEntorno.MKM:
                    velocidad = (3600/(velMedida * 3.6))/60; Log.e("Aviso2", "Entre en min/km: " + unidad + " y str: " + strUnidad);break;
                case MedicionDeEntorno.MM:
                    velocidad = (3600/(velMedida * 2.23694))/60; Log.e("Aviso2", "Entre en min/milla: " + unidad + " y str: " + strUnidad);break;
            }
    }

    public String getVel() {
        Formatter fmt = new Formatter(new StringBuilder());
        fmt.format("%4.1f", velocidad);
        String strCurrentSpeed = fmt.toString();
        strCurrentSpeed = strCurrentSpeed.replace(' ', '0');

        return strCurrentSpeed + strUnidad;
    }

    public String getVelMax() {
        Formatter fmt = new Formatter(new StringBuilder());
        fmt.format("%4.1f", velocidadMaxima);
        String strCurrentSpeed = fmt.toString();
        strCurrentSpeed = strCurrentSpeed.replace(' ', '0');

        return strCurrentSpeed + strUnidad;
    }

    @Override
    public String toString() {
        return this.getVel();
    }

}