package com.aapnarshop.buyer.Model.GET;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class App_Config {

    @SerializedName("UPDATE_TYPE")
    @Expose
    private String uPDATETYPE;
    @SerializedName("VERSION_CODE")
    @Expose
    private String vERSIONCODE;
    @SerializedName("MESSAGE_BODY")
    @Expose
    private String mESSAGEBODY;
    @SerializedName("MESSAGE_SHOW")
    @Expose
    private String mESSAGESHOW;

    public String getUPDATETYPE() {
        return uPDATETYPE;
    }

    public void setUPDATETYPE(String uPDATETYPE) {
        this.uPDATETYPE = uPDATETYPE;
    }

    public String getVERSIONCODE() {
        return vERSIONCODE;
    }

    public void setVERSIONCODE(String vERSIONCODE) {
        this.vERSIONCODE = vERSIONCODE;
    }

    public String getMESSAGEBODY() {
        return mESSAGEBODY;
    }

    public void setMESSAGEBODY(String mESSAGEBODY) {
        this.mESSAGEBODY = mESSAGEBODY;
    }

    public String getMESSAGESHOW() {
        return mESSAGESHOW;
    }

    public void setMESSAGESHOW(String mESSAGESHOW) {
        this.mESSAGESHOW = mESSAGESHOW;
    }

}
