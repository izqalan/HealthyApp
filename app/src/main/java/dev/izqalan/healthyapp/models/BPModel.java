package dev.izqalan.healthyapp.models;

public class BPModel {
    String id, dia, sys, bp;

    public BPModel( String dia, String sys, String bp) {

        this.dia = dia;
        this.sys = sys;
        this.bp = bp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getSys() {
        return sys;
    }

    public void setSys(String sys) {
        this.sys = sys;
    }

    public String getBp() {
        return bp;
    }

    public void setBp(String bp) {
        this.bp = bp;
    }

}
