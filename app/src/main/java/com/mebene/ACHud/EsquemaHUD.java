package com.mebene.ACHud;

/**
 * Created by Martin on 30/05/2017.
 */

public class EsquemaHUD {

    String ext="", header="", intro_sub="", med_sub="";
    int delay;

    public EsquemaHUD() {
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getIntro_sub() {
        return intro_sub;
    }

    public void setIntro_sub(String intro_sub) {
        this.intro_sub = intro_sub;
    }

    public String getMed_sub() {
        return med_sub;
    }

    public void setMed_sub(String med_sub) {
        this.med_sub = med_sub;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
