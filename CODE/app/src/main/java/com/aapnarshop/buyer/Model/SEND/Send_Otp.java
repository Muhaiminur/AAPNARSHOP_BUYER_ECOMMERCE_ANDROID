package com.aapnarshop.buyer.Model.SEND;

public class Send_Otp {
    private String via;

    private String otp;

    public Send_Otp(String via, String otp) {
        this.via = via;
        this.otp = otp;
    }

    public String getVia() {
        return this.via;
    }

    public void setVia(String value) {
        this.via = value;
    }

    public String getOtp() {
        return this.otp;
    }

    public void setOtp(String value) {
        this.otp = value;
    }

    @Override
    public String toString() {
        return "Send_Otp{" +
                "via='" + via + '\'' +
                ", otp='" + otp + '\'' +
                '}';
    }
}
