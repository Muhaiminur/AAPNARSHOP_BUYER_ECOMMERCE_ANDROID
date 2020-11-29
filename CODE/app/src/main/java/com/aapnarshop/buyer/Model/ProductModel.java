package com.aapnarshop.buyer.Model;

public class ProductModel {
    private String orderNumber;
    private String orderSubTotal;
    private String orderDeliveryCharge;
    private String orderDiscount;
    private boolean expandable;

    public ProductModel(String orderNumber, String orderSubTotal, String orderDeliveryCharge, String orderDiscount) {
        this.orderNumber = orderNumber;
        this.orderSubTotal = orderSubTotal;
        this.orderDeliveryCharge = orderDeliveryCharge;
        this.orderDiscount = orderDiscount;
        this.expandable=false;
    }

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderSubTotal() {
        return orderSubTotal;
    }

    public void setOrderSubTotal(String orderSubTotal) {
        this.orderSubTotal = orderSubTotal;
    }

    public String getOrderDeliveryCharge() {
        return orderDeliveryCharge;
    }

    public void setOrderDeliveryCharge(String orderDeliveryCharge) {
        this.orderDeliveryCharge = orderDeliveryCharge;
    }

    public String getOrderDiscount() {
        return orderDiscount;
    }

    public void setOrderDiscount(String orderDiscount) {
        this.orderDiscount = orderDiscount;
    }

    @Override
    public String toString() {
        return "ProductModel{" +
                "orderNumber='" + orderNumber + '\'' +
                ", orderSubTotal='" + orderSubTotal + '\'' +
                ", orderDeliveryCharge='" + orderDeliveryCharge + '\'' +
                ", orderDiscount='" + orderDiscount + '\'' +
                '}';
    }
}
