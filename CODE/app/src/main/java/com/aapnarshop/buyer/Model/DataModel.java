package com.aapnarshop.buyer.Model;

public class DataModel {
    private String orderItem;
    private String orderItemCount;
    private String orderItemPrice;

    public DataModel(String orderItem, String orderItemCount, String orderItemPrice) {
        this.orderItem = orderItem;
        this.orderItemCount = orderItemCount;
        this.orderItemPrice = orderItemPrice;
    }

    public String getOrderItem() {
        return orderItem;
    }

    public String getOrderItemCount() {
        return orderItemCount;
    }

    public String getOrderItemPrice() {
        return orderItemPrice;
    }
}
