package com.aapnarshop.buyer.Model.GET;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class District {
    @SerializedName("districtId")
    @Expose
    private String districtId;
    @SerializedName("district")
    @Expose
    private String districtName;

    public District() {
    }

    public District(String districtName) {
        this.districtName = districtName;
    }

    public District(String districtId, String districtName) {
        this.districtId = districtId;
        this.districtName = districtName;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        District district = (District) o;

        if (!districtId.equals(district.districtId)) return false;
        return districtName.equals(district.districtName);
    }

    @Override
    public int hashCode() {
        int result = districtId.hashCode();
        result = 31 * result + districtName.hashCode();
        return result;
    }

}
