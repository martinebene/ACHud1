package com.mebene.ACHud;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by Martin on 30/05/2017.
 */

public class EsquemaHandler extends DefaultHandler {

    private EsquemaHUD esuquemaHUD;
    private StringBuilder sbTexto;
    private String etiquetaActual;

    public EsquemaHUD getEsquema(){
        return esuquemaHUD;
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {

        super.characters(ch, start, length);

        if (etiquetaActual != null)
            sbTexto.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String name)
            throws SAXException {

        super.endElement(uri, localName, name);

        if (etiquetaActual != null) {

            String ext="", header="", intro_sub="", med_sub="";
            int delay;

            if (localName.equals("Extension")) {
                esuquemaHUD.setExt(sbTexto.toString());
            } else if (localName.equals("Delay")) {
                esuquemaHUD.setDelay(Integer.valueOf(sbTexto.toString()));
            } else if (localName.equals("IntervaloRef")) {
     //           Log.i("tag4444", "IntRef: " + sbTexto.toString());
       //         Log.i("tag4444", "IntRef: " + Long.valueOf(sbTexto.toString()));
                esuquemaHUD.setIntervaloRef(Long.valueOf(sbTexto.toString()));
            } else if (localName.equals("Header")) {
                esuquemaHUD.setHeader(sbTexto.toString());
            } else if (localName.equals("IntroSub")) {
                esuquemaHUD.setIntro_sub(sbTexto.toString());
            } else if (localName.equals("MedSub")) {
                esuquemaHUD.setMed_sub(sbTexto.toString());
            } else if (localName.equals("Footer")) {
                esuquemaHUD.setFooter(sbTexto.toString());
            }
            sbTexto.setLength(0);
        }
    }

    @Override
    public void startDocument() throws SAXException {

        super.startDocument();

        esuquemaHUD = new EsquemaHUD();
        sbTexto = new StringBuilder();
        sbTexto.setLength(0);
        etiquetaActual = "";
    }

    @Override
    public void startElement(String uri, String localName,
                             String name, Attributes attributes) throws SAXException {

        super.startElement(uri, localName, name, attributes);

        if (localName.equals("IntroSub") && attributes.getLength()==1 && attributes.getQName(0).equals("Time"))
            esuquemaHUD.setIntroTime(Long.valueOf(attributes.getValue(0)));

        etiquetaActual = localName;
        sbTexto.setLength(0);
    }
}