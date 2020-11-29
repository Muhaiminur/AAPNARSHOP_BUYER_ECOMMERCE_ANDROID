package com.aapnarshop.buyer.Model.SEND;

public class SEND_REGISTRATION {
    private String via;

    private String password;

    public SEND_REGISTRATION(String via, String password) {
        this.via = via;
        this.password = password;
    }
    public SEND_REGISTRATION(String via) {
        this.via = via;
    }

    public String getVia() {
        return this.via;
    }

    public void setVia(String value) {
        this.via = value;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String value) {
        this.password = value;
    }

    @Override
    public String toString() {
        return "SEND_REGISTRATION{" +
                "via='" + via + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
