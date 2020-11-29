package com.aapnarshop.buyer.VIEWMODEL;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.aapnarshop.buyer.Http.API_RESPONSE;
import com.aapnarshop.buyer.Model.SEND.SEND_REGISTRATION;
import com.aapnarshop.buyer.Model.SEND.Send_Otp;
import com.aapnarshop.buyer.Repository.PreRegistrationRepo;

public class SignUpViewModel extends AndroidViewModel {
    private final PreRegistrationRepo preRegistrationRepo;
    private final MutableLiveData<Boolean> progressbarObservable;

    public SignUpViewModel(@NonNull Application application) {
        super(application);
        preRegistrationRepo = new PreRegistrationRepo(application);
        progressbarObservable = preRegistrationRepo.getProgress();
    }

    public void sendPreRegistration(SEND_REGISTRATION preRegistration){
        preRegistrationRepo.sendPreRegistration(preRegistration);
    }

    public void sendOtpRegistration(Send_Otp otpRegistration){
        preRegistrationRepo.sendOtpRegistration(otpRegistration);
    }

    public MutableLiveData<Boolean> getProgressbar() {
        return progressbarObservable;
    }

    public MutableLiveData<API_RESPONSE> getPreRegistrationResponse() {
        return preRegistrationRepo.getPreRegistrationMutableLiveDataResponse();
    }

    public MutableLiveData<API_RESPONSE> getOtpRegistrationResponse() {
        return preRegistrationRepo.getOtpRegistrationMutableLiveDataResponse();
    }

}
