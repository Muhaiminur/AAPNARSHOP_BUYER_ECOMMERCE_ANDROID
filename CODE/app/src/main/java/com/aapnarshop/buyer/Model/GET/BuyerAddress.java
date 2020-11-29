package com.aapnarshop.buyer.Model.GET;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BuyerAddress implements Serializable {

    @SerializedName("cityId")
    @Expose
    private String cityId;
    @SerializedName("areaId")
    @Expose
    private String areaId;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("receiverPhone")
    @Expose
    private String receiverPhone;
    @SerializedName("receiverName")
    @Expose
    private String receiverName;
    @SerializedName("buyerId")
    @Expose
    private String buyerId;
    @SerializedName("buyerName")
    @Expose
    private String buyerName;
    @SerializedName("primary")
    @Expose
    private String primary;
    @SerializedName("addressId")
    @Expose
    private String addressId;
    @SerializedName("cityTitle")
    @Expose
    private String cityTitle;
    @SerializedName("areaTitle")
    @Expose
    private String areaTitle;


    //For DeleteReceiverAddress & EditReceiverAddress
    public BuyerAddress(String addressId) {
        this.addressId = addressId;
    }

    //For AddReceiverAddress
    public BuyerAddress(String areaId, String address,String receiverName,String receiverPhone, String primary) {
        this.areaId = areaId;
        this.address = address;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.primary = primary;
    }

    //For UpdateReceiverAddress
    public BuyerAddress(String addressId,String areaId, String address,String receiverName,String receiverPhone, String primary) {
        this.addressId = addressId;
        this.areaId = areaId;
        this.address = address;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.primary = primary;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getCityTitle() {
        return cityTitle;
    }

    public void setCityTitle(String cityTitle) {
        this.cityTitle = cityTitle;
    }

    public String getAreaTitle() {
        return areaTitle;
    }

    public void setAreaTitle(String areaTitle) {
        this.areaTitle = areaTitle;
    }

    @Override
    public String toString() {
        return "BuyerAddress{" +
                "cityId='" + cityId + '\'' +
                ", areaId='" + areaId + '\'' +
                ", address='" + address + '\'' +
                ", receiverPhone='" + receiverPhone + '\'' +
                ", receiverName='" + receiverName + '\'' +
                ", buyerId='" + buyerId + '\'' +
                ", buyerName='" + buyerName + '\'' +
                ", primary='" + primary + '\'' +
                ", addressId='" + addressId + '\'' +
                '}';
    }
}
