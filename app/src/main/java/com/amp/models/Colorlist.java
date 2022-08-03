package com.amp.models;

public class Colorlist {
    int colorid ;
    String Colorname ;


    public Colorlist(int colorid, String colorname) {
        this.colorid = colorid;
        Colorname = colorname;
    }

    public int getColorid() {
        return colorid;
    }

    public void setColorid(int colorid) {
        this.colorid = colorid;
    }

    public String getColorname() {
        return Colorname;
    }

    public void setColorname(String colorname) {
        Colorname = colorname;
    }
}
