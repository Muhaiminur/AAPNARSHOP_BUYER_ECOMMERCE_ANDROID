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

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRepo {
    private final ApiInterface apiInterface;
    private final Utility utility;
    private final MutableLiveData<API_RESPONSE> loginMutableLiveData;
    private final MutableLiveData<Boolean> progressbarObservable;

    public LoginRepo(Application application) {
        loginMutableLiveData = new MutableLiveData<>();
        progressbarObservable= new MutableLiveData<>();
        apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
        utility = new Utility(application);
    }

    public void Login(SEND_REGISTRATION userLogin) {
        try {
            if (utility.isNetworkAvailable()) {
                utility.logger("Login" + userLogin.getVia());
                progressbarObservable.postValue(true);
                Call<API_RESPONSE> call = apiInterface.Aapnarshop_Get_Login(utility.getAuthorizationKey(), KeyWord.USERID, KeyWord.TOKEN,"EN", userLogin);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(@NotNull Call<API_RESPONSE> call, @NotNull Response<API_RESPONSE> response) {
                        try {
                            utility.logger("GET LOGIN" + response.toString());
                            progressbarObservable.postValue(false);
                            if (response.isSuccessful()) {
                                API_RESPONSE apiResponse = response.body();
                                Log.d("GET LOGIN", Objects.requireNonNull(apiResponse).getCode().toString());

                                if (apiResponse.getCode() == 200) {
                                    loginMutableLiveData.postValue(apiResponse);
                                   // utility.logger("GET LOGIN" + apiResponse.getData().getAsString());
                                }
                                else if(apiResponse.getCode() == 401)
                                {
                                    loginMutableLiveData.postValue(apiResponse);
                                }
                                else {
                                    //showConfirmation();
                                    loginMutableLiveData.postValue(apiResponse);
                                    utility.logger("GET LOGIN" + apiResponse.getData().getAsString());
                                }
                            }
                            else {
                                //showConfirmation();
                                loginMutableLiveData.postValue(response.body());
                                utility.logger("GET LOGIN failed");
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

    public  MutableLiveData<API_RESPONSE> getLoginMutableData() { return loginMutableLiveData; }
    public MutableLiveData<Boolean> getProgress() {
        return progressbarObservable;
    }
}
