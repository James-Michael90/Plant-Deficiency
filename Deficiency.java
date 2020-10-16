package com.visione.amndd.data;

public class Deficiency {
    private String id;
    private String date;
    private String deficiency;
    private String solution;
    private String diagnosed;
    private byte[] image;


    public Deficiency() {
    }

    public Deficiency(String id, String date, String deficiency, String solution, String diagnosed, byte[] image) {
        this.id = id;
        this.date = date;
        this.deficiency = deficiency;
        this.solution = solution;
        this.diagnosed = diagnosed;
        this.image = image;
    }

    public Deficiency(String date, String deficiency, String solution, String diagnosed, byte[] image) {
        this.date = date;
        this.deficiency = deficiency;
        this.solution = solution;
        this.diagnosed = diagnosed;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDeficiency() {
        return deficiency;
    }

    public void setDeficiency(String deficiency) {
        this.deficiency = deficiency;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getDiagnosed() {
        return diagnosed;
    }

    public void setDiagnosed(String diagnosed) {
        this.diagnosed = diagnosed;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
