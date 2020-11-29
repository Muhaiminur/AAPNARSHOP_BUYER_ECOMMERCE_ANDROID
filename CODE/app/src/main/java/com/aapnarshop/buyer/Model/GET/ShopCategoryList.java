package com.aapnarshop.buyer.Model.GET;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopCategoryList {
    @SerializedName("shopCategoryId")
    @Expose
    private String shopCategoryId;
    @SerializedName("shopCategoryTitle")
    @Expose
    private String shopCategoryTitle;

    public String getShopCategoryId() {
        return shopCategoryId;
    }

    public void setShopCategoryId(String shopCategoryId) {
        this.shopCategoryId = shopCategoryId;
    }

    public String getShopCategoryTitle() {
        return shopCategoryTitle;
    }

    public void setShopCategoryTitle(String shopCategoryTitle) {
        this.shopCategoryTitle = shopCategoryTitle;
    }
}
