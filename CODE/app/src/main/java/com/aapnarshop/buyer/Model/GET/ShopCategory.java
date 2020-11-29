package com.aapnarshop.buyer.Model.GET;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShopCategory {
    @SerializedName("shopCategoryList")
    @Expose
    private List<ShopCategoryList> shopCategoryList = null;

    public List<ShopCategoryList> getShopCategoryList() {
        return shopCategoryList;
    }

    public void setShopCategoryList(List<ShopCategoryList> shopCategoryList) {
        this.shopCategoryList = shopCategoryList;
    }

}
