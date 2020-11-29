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
import com.aapnarshop.buyer.Model.GET.ApiData;
import com.aapnarshop.buyer.Model.GET.App_Config;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LandingPageRepo {
    private final MutableLiveData<App_Config> configMutableLiveData;
    private final MutableLiveData<Boolean> progressbarMutableLiveData;
    private final ApiInterface apiInterface;
    private final Utility utility;
    private final Gson gson;
    private App_Config appConfig;
    private ApiData apiData;

    public LandingPageRepo(Application application) {
        configMutableLiveData = new MutableLiveData<>();
        progressbarMutableLiveData = new MutableLiveData<>();
        apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
        utility = new Utility(application);
        gson = new Gson();
    }

    public LiveData<App_Config> getConfigLiveData() {
        try {
            if (utility.isNetworkAvailable()) {
                progressbarMutableLiveData.postValue(true);
                Call<API_RESPONSE> call = apiInterface.Buyer_Config(utility.getApiKey(), utility.getUSER_ID(), KeyWord.TOKEN, "EN");
                utility.logger(call.request().headers().size() + "");
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(@NotNull Call<API_RESPONSE> call, @NotNull Response<API_RESPONSE> response) {
                        try {
                            assert response.body() != null;
                            Log.d("GET CONFIG", response.body().toString());
                            progressbarMutableLiveData.postValue(false);
                            if (response.isSuccessful()) {
                                API_RESPONSE api_response = response.body();
                                apiData = gson.fromJson(api_response.getData().toString(), ApiData.class);
                                appConfig = apiData.getAppConfig();
                                configMutableLiveData.postValue(appConfig);
                                Log.d("GET CONFIG", appConfig.getMESSAGEBODY());
                                utility.logger("Get Config" + appConfig.toString());

                            } else {
                                utility.logger("Config failed");
                            }
                        } catch (Exception e) {
                            utility.logger(e.toString());
                            Log.d("Error Line Number", Log.getStackTraceString(e));
                        }

                    }
                    @Override
                    public void onFailure(@NotNull Call<API_RESPONSE> call, @NotNull Throwable t) {
                        utility.logger(t.toString());
                        progressbarMutableLiveData.postValue(false);
                        Log.d("Error", t.toString());
                    }
                });
            } else {
                utility.showDialog("No Internet Connection.");
            }
        } catch (Exception ex) {
            progressbarMutableLiveData.postValue(false);
            Log.d("Error Line Number", Log.getStackTraceString(ex));
        }

        return configMutableLiveData;
    }


    public MutableLiveData<Boolean> getProgress() {
        return progressbarMutableLiveData;
    }


}
