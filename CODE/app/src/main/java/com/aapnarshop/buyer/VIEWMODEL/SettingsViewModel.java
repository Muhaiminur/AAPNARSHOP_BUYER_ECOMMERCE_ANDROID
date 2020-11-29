package com.aapnarshop.buyer.VIEWMODEL;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.aapnarshop.buyer.Http.API_RESPONSE;
import com.aapnarshop.buyer.Model.SEND.Change_Password;
import com.aapnarshop.buyer.Repository.SettingsRepo;

public class SettingsViewModel extends AndroidViewModel {
    private final SettingsRepo settingsRepo;

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        settingsRepo = new SettingsRepo(application);
    }

    public void changePassword(Change_Password change_password){
        settingsRepo.changePassword(change_password);
    }

    public LiveData<API_RESPONSE> getChangePasswordResponse(){

        return settingsRepo.getChangePasswordResponse();
    }

}
