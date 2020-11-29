package com.aapnarshop.buyer.VIEWMODEL;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.aapnarshop.buyer.Http.API_RESPONSE;
import com.aapnarshop.buyer.Model.GET.Area;
import com.aapnarshop.buyer.Model.GET.BuyerAddress;
import com.aapnarshop.buyer.Model.GET.City;
import com.aapnarshop.buyer.Model.GET.District;
import com.aapnarshop.buyer.Repository.AddAddressRepo;

import java.util.List;

public class AddAddressViewModel extends AndroidViewModel {
    private final AddAddressRepo addAddressRepo;

    public AddAddressViewModel(@NonNull Application application) {
        super(application);
        addAddressRepo = new AddAddressRepo(application);
    }
    public LiveData<List<City>> getCityList(District district){
        return addAddressRepo.getCityList(district);
    }
    public LiveData<List<Area>> getAreaList(City city){
        return addAddressRepo.getAreaList(city);
    }

    public void addReceiverAddress(BuyerAddress addReceiverAddress){
        addAddressRepo.addReceiverAddress(addReceiverAddress);
    }

    public void updateReceiverAddress(BuyerAddress updateReceiverAddress){
        addAddressRepo.updateReceiverAddress(updateReceiverAddress);
    }

    public MutableLiveData<API_RESPONSE> getAddAddressResponse() {
        return addAddressRepo.getAddAddressMutableLiveDataResponse();
    }

    public MutableLiveData<API_RESPONSE> getUpdateAddressResponse() {
        return addAddressRepo.getUpdateAddressMutableLiveDataResponse();
    }

}
