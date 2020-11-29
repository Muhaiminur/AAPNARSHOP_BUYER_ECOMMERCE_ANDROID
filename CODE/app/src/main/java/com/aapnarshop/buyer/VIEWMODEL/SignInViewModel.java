package com.aapnarshop.buyer.VIEWMODEL;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.aapnarshop.buyer.Http.API_RESPONSE;
import com.aapnarshop.buyer.Model.SEND.SEND_REGISTRATION;
import com.aapnarshop.buyer.Repository.LoginRepo;

public class SignInViewModel extends AndroidViewModel {
    private final LoginRepo loginRepo;
    private final MutableLiveData<Boolean> progressbarObservable;

    public SignInViewModel(@NonNull Application application) {
        super(application);
        loginRepo = new LoginRepo(application);
        progressbarObservable = loginRepo.getProgress();
    }

    public void getLogin(SEND_REGISTRATION userLogin){
        loginRepo.Login(userLogin);
    }

    public MutableLiveData<API_RESPONSE> getLoginMutableDataResponse() { return loginRepo.getLoginMutableData(); }
    public MutableLiveData<Boolean> getProgressbar() {
        return progressbarObservable;
    }

}
