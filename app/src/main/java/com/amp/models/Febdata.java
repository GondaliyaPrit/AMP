package com.amp.models;

public class Febdata {

    int InFabricID, VenderID, Color, BillNo, Status;
    String imagePath, Quantity, TakaBalesNo, VenderName, VenderType, ColorName;


    public Febdata(int inFabricID, int venderID, int color, int billNo, int status, String imagePath, String quantity, String takaBalesNo, String venderName, String venderType, String colorName) {
        this.InFabricID = inFabricID;
        this.VenderID = venderID;
        this.Color = color;
        this.BillNo = billNo;
        this.Status = status;
        this.imagePath = imagePath;
        this.Quantity = quantity;
        this.TakaBalesNo = takaBalesNo;
        this.VenderName = venderName;
        this.VenderType = venderType;
        this.ColorName = colorName;
    }

    public int getInFabricID() {
        return InFabricID;
    }

    public void setInFabricID(int inFabricID) {
        InFabricID = inFabricID;
    }

    public int getVenderID() {
        return VenderID;
    }

    public void setVenderID(int venderID) {
        VenderID = venderID;
    }

    public int getColor() {
        return Color;
    }

    public void setColor(int color) {
        Color = color;
    }

    public int getBillNo() {
        return BillNo;
    }

    public void setBillNo(int billNo) {
        BillNo = billNo;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getTakaBalesNo() {
        return TakaBalesNo;
    }

    public void setTakaBalesNo(String takaBalesNo) {
        TakaBalesNo = takaBalesNo;
    }

    public String getVenderName() {
        return VenderName;
    }

    public void setVenderName(String venderName) {
        VenderName = venderName;
    }

    public String getVenderType() {
        return VenderType;
    }

    public void setVenderType(String venderType) {
        VenderType = venderType;
    }

    public String getColorName() {
        return ColorName;
    }

    public void setColorName(String colorName) {
        ColorName = colorName;
    }
}
