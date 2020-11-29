package com.aapnarshop.buyer.Repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.aapnarshop.buyer.Http.API_RESPONSE;
import com.aapnarshop.buyer.Http.ApiClient;
import com.aapnarshop.buyer.Http.ApiInterface;
import com.aapnarshop.buyer.Library.KeyWord;
import com.aapnarshop.buyer.Library.Utility;
import com.aapnarshop.buyer.Model.SEND.SEND_REGISTRATION;
import com.aapnarshop.buyer.Model.SEND.Send_Otp;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PreRegistrationRepo {
    private final ApiInterface apiInterface;
    private final Utility utility;
    private final MutableLiveData<API_RESPONSE> preRegistrationMutableLiveData;
    private final MutableLiveData<API_RESPONSE> otpRegistrationMutableLiveData;
    private final MutableLiveData<Boolean> progressbarObservable;


    public PreRegistrationRepo(Application application) {
        preRegistrationMutableLiveData = new MutableLiveData<>();
        otpRegistrationMutableLiveData = new MutableLiveData<>();
        progressbarObservable= new MutableLiveData<>();
        apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
        utility = new Utility(application);
    }

    public void sendPreRegistration(SEND_REGISTRATION preRegistration) {

        try {
            if (utility.isNetworkAvailable()) {
                utility.logger("PreRegistration" + preRegistration.getVia());
                progressbarObservable.postValue(true);
                Call<API_RESPONSE> call = apiInterface.Aapnarshop_Buyer_Pin_Request(utility.getAuthorizationKey(), KeyWord.USERID, KeyWord.TOKEN,"EN", preRegistration);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(@NotNull Call<API_RESPONSE> call, @NotNull Response<API_RESPONSE> response) {
                        try {
                            utility.logger("GET PRE-REGISTRATION" + response.toString());
                            progressbarObservable.postValue(false);
                            if (response.isSuccessful()) {
                                API_RESPONSE apiResponse = response.body();
                                Log.d("ApiResponsePreReg", Objects.requireNonNull(apiResponse).getCode().toString());

                                if (apiResponse.getCode() == 200) {
                                    preRegistrationMutableLiveData.postValue(apiResponse);
                                    utility.logger("GET PRE-REGISTRATION" + apiResponse.getData().getAsString());
                                }
                                else {
                                    //showConfirmation();
                                    preRegistrationMutableLiveData.postValue(apiResponse);
                                    utility.logger("GET PRE-REGISTRATION Not 200" + apiResponse.getData().getAsString());
                                }
                            }
                            else {
                                //showConfirmation();
                                preRegistrationMutableLiveData.postValue(response.body());
                                utility.logger("GET PRE-REGISTRATION failed");
                            }
                        } catch (Exception e) {
                            Log.d("Error Line Number", Log.getStackTraceString(e));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<API_RESPONSE> call, @NotNull Throwable t) {
                        progressbarObservable.postValue(false);
                        utility.logger(t.toString());
                        utility.showToast("Try Again");
                    }
                });
            } else {
                utility.showDialog("No Internet Connection.");
            }
        } catch (Exception e) {
            progressbarObservable.postValue(false);
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }

    }

    public void sendOtpRegistration(Send_Otp otpRegistration) {

        try {
        if (utility.isNetworkAvailable()) {
            //utility.showProgress(false, "Please Wait!!");
            utility.logger("OtpRegistration" + otpRegistration.getVia());
            progressbarObservable.postValue(true);
            Call<API_RESPONSE> call = apiInterface.Aapnarshop_Buyer_Pin_Verification(utility.getAuthorizationKey(), KeyWord.USERID, KeyWord.TOKEN,"EN", otpRegistration);
            call.enqueue(new Callback<API_RESPONSE>() {
                @Override
                public void onResponse(@NotNull Call<API_RESPONSE> call, @NotNull Response<API_RESPONSE> response) {
                    try {
                        utility.logger("GET OTP-REGISTRATION" + response.toString());
                        progressbarObservable.postValue(false);
                        //  utility.hideProgress();
                        if (response.isSuccessful()) {
                            API_RESPONSE apiResponse = response.body();
                            Log.d("ApiResponseOtp", Objects.requireNonNull(apiResponse).getCode().toString());
                            otpRegistrationMutableLiveData.postValue(apiResponse);
                            utility.logger("SEND OTP-REGISTRATION" + apiResponse.getData().getAsString());
                        } else{
                            otpRegistrationMutableLiveData.postValue(response.body());
                            utility.logger("SEND OTP-REGISTRATION failed");
                        }
                    } catch (Exception e) {
                        Log.d("Error Line Number", Log.getStackTraceString(e));
                    }
                }

                @Override
                public void onFailure(@NotNull Call<API_RESPONSE> call, @NotNull Throwable t) {
                    progressbarObservable.postValue(false);
                    utility.logger(t.toString());
                    utility.showToast("Try Again");
                    //  utility.hideProgress();
                }
            });
        }
        else
        {
            utility.showDialog("No Internet Connection.");
        }
        } catch (Exception e) {
            progressbarObservable.postValue(false);
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    public MutableLiveData<API_RESPONSE> getPreRegistrationMutableLiveDataResponse () { return preRegistrationMutableLiveData; }

    public MutableLiveData<API_RESPONSE> getOtpRegistrationMutableLiveDataResponse () { return otpRegistrationMutableLiveData; }

    public MutableLiveData<Boolean> getProgress() {
        return progressbarObservable;
    }

}
