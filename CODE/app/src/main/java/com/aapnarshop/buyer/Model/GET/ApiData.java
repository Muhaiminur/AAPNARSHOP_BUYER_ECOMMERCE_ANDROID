package com.aapnarshop.buyer.Model.GET;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiData {
    @SerializedName("appConfig")
    @Expose
    private App_Config appConfig;
    @SerializedName("shopCategory")
    @Expose
    private ShopCategory shopCategory;

    public App_Config getAppConfig() {
        return appConfig;
    }

    public void setAppConfig(App_Config appConfig) {
        this.appConfig = appConfig;
    }

    public ShopCategory getShopCategory() {
        return shopCategory;
    }

    public void setShopCategory(ShopCategory shopCategory) {
        this.shopCategory = shopCategory;
    }

}
