package com.mebene.ACHud;

/**
 * Created by Martin on 30/05/2017.
 */

public class EsquemaHUD {

    String ext="", header="", intro_sub="", med_sub="", footer="";
    int delay=0;
    long intervaloRef = 0;
    long introTime = 0;

    public EsquemaHUD() {
    }

    public long getIntroTime() {
        return introTime;
    }

    public void setIntroTime(long introTime) {
        this.introTime = introTime;
    }

    public long getIntervaloRef() {
        return intervaloRef;
    }

    public void setIntervaloRef(long intervaloRef) {
        this.intervaloRef = intervaloRef;
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

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getIntro_sub() {
        return intro_sub+"\n";
    }

    public void setIntro_sub(String intro_sub) {
        this.intro_sub = intro_sub;
    }

    public String getMed_sub() {
        return med_sub+"\n";
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
