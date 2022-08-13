package com.amp.models;

public class Skulist {
    int SKUID ;
    int SKUCuttingID ;
    int SizeID ;
    String SizeName ;
    int ProcessID  ;
    int Qty ;

    public Skulist(int SKUID, int SKUCuttingID, int sizeID, String sizeName, int processID, int qty) {
        this.SKUID = SKUID;
        this.SKUCuttingID = SKUCuttingID;
        SizeID = sizeID;
        SizeName = sizeName;
        ProcessID = processID;
        Qty = qty;
    }

    public int getSKUID() {
        return SKUID;
    }

    public void setSKUID(int SKUID) {
        this.SKUID = SKUID;
    }

    public int getSKUCuttingID() {
        return SKUCuttingID;
    }

    public void setSKUCuttingID(int SKUCuttingID) {
        this.SKUCuttingID = SKUCuttingID;
    }

    public int getSizeID() {
        return SizeID;
    }

    public void setSizeID(int sizeID) {
        SizeID = sizeID;
    }

    public String getSizeName() {
        return SizeName;
    }

    public void setSizeName(String sizeName) {
        SizeName = sizeName;
    }

    public int getProcessID() {
        return ProcessID;
    }

    public void setProcessID(int processID) {
        ProcessID = processID;
    }

    public int getQty() {
        return Qty;
    }

    public void setQty(int qty) {
        Qty = qty;
    }
}
