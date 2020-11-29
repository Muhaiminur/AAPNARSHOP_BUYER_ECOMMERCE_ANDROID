package com.aapnarshop.buyer.VIEWMODEL;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.aapnarshop.buyer.Http.API_RESPONSE;
import com.aapnarshop.buyer.Model.SEND.SEND_REGISTRATION;
import com.aapnarshop.buyer.Model.SEND.Send_Otp;
import com.aapnarshop.buyer.Repository.ForgotPasswordRepo;

public class ForgotPassViewModel extends AndroidViewModel {
    private final ForgotPasswordRepo forgotPasswordRepo;
    private final MutableLiveData<Boolean> progressbarObservable;

    public ForgotPassViewModel(@NonNull Application application) {
        super(application);
        forgotPasswordRepo = new ForgotPasswordRepo(application);
        progressbarObservable = forgotPasswordRepo.getProgress();
    }

    public void sendForgotPasswordInfo(SEND_REGISTRATION forgotPasswordInfo){
        forgotPasswordRepo.sendForgotPasswordInfo(forgotPasswordInfo);
    }

    public void sendOtpForgotPassword(Send_Otp otpForgotPassword){
        forgotPasswordRepo.sendOtpForgotPassword(otpForgotPassword);
    }

    public MutableLiveData<Boolean> getProgressbar() {
        return progressbarObservable;
    }

    public MutableLiveData<API_RESPONSE> getForgotPassResponse() {
        return forgotPasswordRepo.getForgotPassLiveDataResponse();
    }

    public MutableLiveData<API_RESPONSE> getForgotPassOtpResponse() {
        return forgotPasswordRepo.getForgotPassOtpLiveDataResponse();
    }

}
