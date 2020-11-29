package com.aapnarshop.buyer.VIEWMODEL;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.aapnarshop.buyer.Http.API_RESPONSE;
import com.aapnarshop.buyer.Model.GET.BuyerAddress;
import com.aapnarshop.buyer.Model.GET.BuyerProfile;
import com.aapnarshop.buyer.Model.SEND.Buyer_Primary_Address;
import com.aapnarshop.buyer.Model.SEND.Buyer_Update;
import com.aapnarshop.buyer.Repository.BuyerProfileRepo;

import java.util.List;

public class BuyerProfileViewModel extends AndroidViewModel {
    private final BuyerProfileRepo buyerProfileRepo ;

    public BuyerProfileViewModel(@NonNull Application application) {
        super(application);
        buyerProfileRepo=new BuyerProfileRepo(application);
    }
    public LiveData<BuyerProfile> getBuyerProfileLiveData(){

        return buyerProfileRepo.getBuyerProfileLiveData();
    }

    public LiveData<List<BuyerAddress>> getBuyerAddressList(){
        return buyerProfileRepo.getBuyerAddressListLiveData();
    }

    public void setBuyerPrimaryAddress(BuyerAddress buyerPrimaryAddress){
        buyerProfileRepo.setBuyerPrimaryAddress(buyerPrimaryAddress);
    }

    public void deleteReceiverAddress(BuyerAddress receiverAddress){
        buyerProfileRepo.deleteReceiverAddress(receiverAddress);
    }

    public void sendBuyerProfileUpdate(Buyer_Update buyer_update){
        buyerProfileRepo.sendBuyerProfileUpdate(buyer_update);
    }

    public MutableLiveData<API_RESPONSE> getDeleteReceiverAddressResponse() { return buyerProfileRepo.getDeleteReceiverAddressResponse(); }
    public MutableLiveData<API_RESPONSE> getBuyerPrimaryAddressResponseMutableLiveData() { return buyerProfileRepo.getBuyerPrimaryAddressResponseMutableLiveData(); }
    public MutableLiveData<API_RESPONSE> getBuyerProfileUpdateResponse() {return buyerProfileRepo.getBuyerProfileUpdateResponse();}
}
