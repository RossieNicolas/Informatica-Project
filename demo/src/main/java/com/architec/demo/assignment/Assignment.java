package com.architec.demo.assignment;

public class Assignment {
    private int ficheID;
    private String titel;
    private String soort;
    private String omschrijving;
    private int aantalUren;
    private int aantalStudenten;
    private String beginDatum;
    private String eindDatum;
    private String opdrachtgever;

    public Assignment() {
    }

    public Assignment(int ficheID, String titel, String soort, String omschrijving, int aantalUren, int aantalStudenten, String beginDatum, String eindDatum, String opdrachtgever) {
        this.ficheID = ficheID;
        this.titel = titel;
        this.soort = soort;
        this.omschrijving = omschrijving;
        this.aantalUren = aantalUren;
        this.aantalStudenten = aantalStudenten;
        this.beginDatum = beginDatum;
        this.eindDatum = eindDatum;
        this.opdrachtgever = opdrachtgever;
    }

    public int getFicheID() {
        return ficheID;
    }

    public String getTitel() {
        return titel;
    }

    public String getSoort() {
        return soort;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public int getAantalUren() {
        return aantalUren;
    }

    public int getAantalStudenten() {
        return aantalStudenten;
    }

    public String getBeginDatum() {
        return beginDatum;
    }

    public String getEindDatum() {
        return eindDatum;
    }

    public String getOpdrachtgever() {
        return opdrachtgever;
    }
}
