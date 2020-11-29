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
import com.aapnarshop.buyer.Model.SEND.Change_Password;
import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsRepo {
    private final MutableLiveData<API_RESPONSE> changePasswordResponseMutableLiveData;
    private final Utility utility;

    private final ApiInterface apiInterface;

    public SettingsRepo(Application application) {
        changePasswordResponseMutableLiveData = new MutableLiveData<>();
        apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
        utility = new Utility(application);
    }

    public void changePassword(Change_Password change_password) {
        try {
            if (utility.isNetworkAvailable()) {
                utility.logger("Change Password" + change_password.getNewPassword());
                Call<API_RESPONSE> call = apiInterface.Change_Password(utility.getAuthorizationKey(), "2", KeyWord.TOKEN, "EN", change_password);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(@NotNull Call<API_RESPONSE> call, @NotNull Response<API_RESPONSE> response) {
                        try {
                            utility.logger("Change Password" + response.toString());
                            if (response.isSuccessful()) {
                                API_RESPONSE apiResponse = response.body();

                                if (apiResponse != null) {
                                    if (apiResponse.getCode() == 200 || apiResponse.getCode() == 401) {
                                        changePasswordResponseMutableLiveData.postValue(apiResponse);
                                        utility.logger("Change Password" + apiResponse.getData().getAsString());
                                    } else {
                                        utility.logger("Change Password" + apiResponse.getData().getAsString());
                                    }
                                }
                            } else {
                                //showConfirmation();
                                utility.logger("Change Password failed.. Response not successful");
                            }
                        } catch (Exception e) {
                            Log.d("Error Line Number", Log.getStackTraceString(e));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<API_RESPONSE> call, @NotNull Throwable t) {
                        utility.logger(t.toString());
                        utility.showToast("Try Again");
                    }
                });
            }
        } catch (Exception ex) {
            Log.d("Error Line Number", Log.getStackTraceString(ex));
        }
    }


    public LiveData<API_RESPONSE> getChangePasswordResponse() {

        return changePasswordResponseMutableLiveData;
    }

}
