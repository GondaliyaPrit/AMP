package com.amp.models;

public class Vendorlist {
    int vendorid ;
    String vendorname ;
    String vendortype;


    public Vendorlist(int vendorid, String vendorname, String vendortype) {
        this.vendorid = vendorid;
        this.vendorname = vendorname;
        this.vendortype = vendortype;
    }

    public int getVendorid() {
        return vendorid;
    }

    public void setVendorid(int vendorid) {
        this.vendorid = vendorid;
    }

    public String getVendorname() {
        return vendorname;
    }

    public void setVendorname(String vendorname) {
        this.vendorname = vendorname;
    }

    public String getVendortype() {
        return vendortype;
    }

    public void setVendortype(String vendortype) {
        this.vendortype = vendortype;
    }
}
