package com.aapnarshop.buyer.VIEWMODEL;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.aapnarshop.buyer.Model.GET.App_Config;
import com.aapnarshop.buyer.Repository.LandingPageRepo;

public class LandingPageViewModel extends AndroidViewModel {
    private final LandingPageRepo landingPageRepo ;

    public LandingPageViewModel(@NonNull Application application) {
        super(application);
        landingPageRepo=new LandingPageRepo(application);
    }
    public LiveData<App_Config> getConfigLiveData(){

        return landingPageRepo.getConfigLiveData();
    }

    public LiveData<Boolean> getProgressBar(){
        return landingPageRepo.getProgress();
    }

}
