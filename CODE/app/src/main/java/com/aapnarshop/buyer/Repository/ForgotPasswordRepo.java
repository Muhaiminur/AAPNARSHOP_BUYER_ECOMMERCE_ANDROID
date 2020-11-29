package com.aapnarshop.buyer.Repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.aapnarshop.buyer.Http.API_RESPONSE;
import com.aapnarshop.buyer.Http.ApiClient;
import com.aapnarshop.buyer.Http.ApiInterface;
import com.aapnarshop.buyer.Library.KeyWord;
import com.aapnarshop.buyer.Library.Utility;
import com.aapnarshop.buyer.Model.SEND.SEND_REGISTRATION;
import com.aapnarshop.buyer.Model.SEND.Send_Otp;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordRepo {
    private final ApiInterface apiInterface;
    private final Utility utility;
    private final MutableLiveData<API_RESPONSE> forgotPasswordMutableLiveData;
    private final MutableLiveData<API_RESPONSE> otpForgotPasswordMutableLiveData;
    private final MutableLiveData<Boolean> progressbarObservable;


    public ForgotPasswordRepo(Application application) {
        forgotPasswordMutableLiveData = new MutableLiveData<>();
        otpForgotPasswordMutableLiveData = new MutableLiveData<>();
        progressbarObservable= new MutableLiveData<>();
        apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
        utility = new Utility(application);
    }

    public void sendForgotPasswordInfo(SEND_REGISTRATION forgotPasswordInfo) {

        try {
            if (utility.isNetworkAvailable()) {
                utility.logger("ForgotPassword" + forgotPasswordInfo.getVia());
                progressbarObservable.postValue(true);
                Call<API_RESPONSE> call = apiInterface.Aapnarshop_Buyer_Pin_Request(utility.getAuthorizationKey(), KeyWord.USERID, KeyWord.TOKEN,"EN", forgotPasswordInfo);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(@NotNull Call<API_RESPONSE> call, @NotNull Response<API_RESPONSE> response) {
                        try {
                            utility.logger("GET ForgotPassword" + response.toString());
                            progressbarObservable.postValue(false);
                            if (response.isSuccessful()) {
                                API_RESPONSE apiResponse = response.body();
                                Log.d("ResponseForgotPassword", apiResponse.getCode().toString());
                                forgotPasswordMutableLiveData.postValue(apiResponse);
                                utility.logger("GET ForgotPassword" + apiResponse.getData().getAsString());
                            }
                            else {
                                //showConfirmation();
                                forgotPasswordMutableLiveData.postValue(response.body());
                                utility.logger("GET ForgotPassword failed");
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

    public void sendOtpForgotPassword(Send_Otp otpForgotPassword) {
        try {
        if (utility.isNetworkAvailable()) {
            //utility.showProgress(false, "Please Wait!!");
            utility.logger("Registration" + otpForgotPassword.getVia());
            progressbarObservable.postValue(true);
            Call<API_RESPONSE> call = apiInterface.Aapnarshop_Buyer_Pin_Verification(utility.getAuthorizationKey(), KeyWord.USERID, KeyWord.TOKEN,"EN", otpForgotPassword);
            call.enqueue(new Callback<API_RESPONSE>() {
                @Override
                public void onResponse(@NotNull Call<API_RESPONSE> call, @NotNull Response<API_RESPONSE> response) {
                    try {
                        //  utility.hideProgress();
                        utility.logger("GET ForgotPasswordOtp" + response.toString());
                        progressbarObservable.postValue(false);
                        if (response.isSuccessful()) {
                            API_RESPONSE apiResponse = response.body();
                            Log.d("ApiResponseOtpFP",apiResponse.getCode().toString());
                            otpForgotPasswordMutableLiveData.postValue(apiResponse);
                        }else{
                            otpForgotPasswordMutableLiveData.postValue(response.body());
                            utility.logger("GET ForgotPasswordOtp failed");
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

    public MutableLiveData<API_RESPONSE> getForgotPassLiveDataResponse () { return forgotPasswordMutableLiveData; }

    public MutableLiveData<API_RESPONSE> getForgotPassOtpLiveDataResponse () { return otpForgotPasswordMutableLiveData; }

    public MutableLiveData<Boolean> getProgress() {
        return progressbarObservable;
    }

}
