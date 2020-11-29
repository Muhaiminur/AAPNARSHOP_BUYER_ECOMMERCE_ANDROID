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
import com.aapnarshop.buyer.Model.GET.BuyerAddress;
import com.aapnarshop.buyer.Model.GET.BuyerProfile;
import com.aapnarshop.buyer.Model.SEND.Buyer_Update;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyerProfileRepo {
    private final MutableLiveData<BuyerProfile> buyerProfileResponseMutableLiveData;
    private final MutableLiveData<List<BuyerAddress>> buyerAddressResponseMutableLiveData;
    private final MutableLiveData<API_RESPONSE> buyerPrimaryAddressResponseMutableLiveData;
    private final MutableLiveData<API_RESPONSE> deleteReceiverAddressResponseMutableLiveData;
    private final MutableLiveData<API_RESPONSE> buyerProfileUpdateResponseMutableLiveData;
    private final ApiInterface apiInterface;
    private final Utility utility;
    private final Gson gson;
    private BuyerProfile buyerProfile;
    private List<BuyerAddress> addressList;


    public BuyerProfileRepo(Application application) {
        buyerProfileResponseMutableLiveData = new MutableLiveData<>();
        buyerAddressResponseMutableLiveData = new MutableLiveData<>();
        buyerPrimaryAddressResponseMutableLiveData = new MutableLiveData<>();
        deleteReceiverAddressResponseMutableLiveData = new MutableLiveData<>();
        buyerProfileUpdateResponseMutableLiveData = new MutableLiveData<>();
        apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
        utility = new Utility(application);
        gson = new Gson();
    }

    public LiveData<BuyerProfile> getBuyerProfileLiveData() {
        try {
            if (utility.isNetworkAvailable()) {
                Call<API_RESPONSE> call = apiInterface.Get_Buyer_Profile(utility.getApiKey(), "2", KeyWord.TOKEN, "EN");
                utility.logger(call.request().headers().size() + "");
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(@NotNull Call<API_RESPONSE> call, @NotNull Response<API_RESPONSE> response) {
                        try {
                            assert response.body() != null;
                            Log.d("GET BUYER PROFILE", response.body().toString());
                            if (response.isSuccessful()) {
                                API_RESPONSE api_response = response.body();

                                if (api_response.getCode() == 200) {
                                    buyerProfile = gson.fromJson(api_response.getData().toString(), BuyerProfile.class);
                                    buyerProfileResponseMutableLiveData.postValue(buyerProfile);
                                    Log.d("GET BUYER PROFILE", buyerProfile.getEmail());
                                    utility.logger("Get BUYER PROFILE" + buyerProfile.toString());

                                } else {
                                    //buyerProfileResponseMutableLiveData.postValue(buyerProfile);
                                    utility.logger("Get BUYER PROFILE" + buyerProfile.toString());
                                }
                            } else {
                                utility.logger("BUYER PROFILE failed");
                            }
                        } catch (Exception e) {
                            utility.logger(e.toString());
                            Log.d("Error Line Number", Log.getStackTraceString(e));
                        }

                    }

                    @Override
                    public void onFailure(@NotNull Call<API_RESPONSE> call, @NotNull Throwable t) {
                        utility.logger(t.toString());
                        Log.d("Error", t.toString());
                    }
                });
            }
            else
            {
                utility.showDialog("No Internet Connection.");
            }
        }
        catch (Exception e)
        {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }

        return buyerProfileResponseMutableLiveData;
    }

    public LiveData<List<BuyerAddress>> getBuyerAddressListLiveData(){
        try {
            if (utility.isNetworkAvailable()) {
                Call<API_RESPONSE> call = apiInterface.getBuyerAddress(utility.getApiKey(), "2", KeyWord.TOKEN, "EN");
                utility.logger(call.request().headers().size() + "");
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(@NotNull Call<API_RESPONSE> call, @NotNull Response<API_RESPONSE> response) {
                        try {
                            assert response.body() != null;
                            API_RESPONSE api_response = response.body();
                            Log.d("GET BUYER ADDRESS", response.body().toString());
                            if (response.isSuccessful()) {
                                Type listType = new TypeToken<List<BuyerAddress>>() {
                                }.getType();
                                addressList = gson.fromJson(api_response.getData(), listType);
                                buyerAddressResponseMutableLiveData.postValue(addressList);
                                Log.d("GET BUYER ADDRESS", addressList.toString());
                                utility.logger("Get BUYER ADDRESS" + addressList.toString());
                            } else {
                                utility.logger("BUYER ADDRESS failed");
                            }
                        } catch (Exception e) {
                            utility.logger(e.toString());
                            Log.d("Error Line Number", Log.getStackTraceString(e));
                        }

                    }

                    @Override
                    public void onFailure(@NotNull Call<API_RESPONSE> call, @NotNull Throwable t) {
                        utility.logger(t.toString());
                        Log.d("Error", t.toString());
                    }
                });
            } else {
                utility.showDialog("No Internet Connection.");
            }
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }

        return buyerAddressResponseMutableLiveData;
    }

    public void setBuyerPrimaryAddress(BuyerAddress buyerPrimaryAddress) {
        try {
            if (utility.isNetworkAvailable()) {
                utility.logger("Buyer Primary address" + buyerPrimaryAddress.getAddressId());
                Call<API_RESPONSE> call = apiInterface.setBuyerPrimaryAddress(utility.getAuthorizationKey(), "2", KeyWord.TOKEN,"EN", buyerPrimaryAddress);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(@NotNull Call<API_RESPONSE> call, @NotNull Response<API_RESPONSE> response) {
                        try {
                            API_RESPONSE apiResponse = response.body();
                            utility.logger("Buyer Primary address" + response.toString());
                            if (response.isSuccessful()) {
                                Log.d("Primary successful", Objects.requireNonNull(apiResponse).getCode().toString());
                                buyerPrimaryAddressResponseMutableLiveData.postValue(apiResponse);

                            }
                            else {
                                //showConfirmation();
                                utility.logger("Buyer Primary address failed");
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
            } else {
                utility.showDialog("No Internet Connection.");
            }
        } catch (Exception e) {
          Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    public void deleteReceiverAddress(BuyerAddress receiverAddress) {
        try {
            if (utility.isNetworkAvailable()) {
                utility.logger("receiverAddress" + receiverAddress.getAddressId());
                Call<API_RESPONSE> call = apiInterface.deleteReceiverAddress(utility.getAuthorizationKey(), "2", KeyWord.TOKEN,"EN", receiverAddress);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(@NotNull Call<API_RESPONSE> call, @NotNull Response<API_RESPONSE> response) {
                        try {
                            utility.logger("Delete receiverAddress" + response.toString());
                            if (response.isSuccessful()) {
                                 API_RESPONSE apiResponse = response.body();
                                 Log.d("Delete receiverAddress", Objects.requireNonNull(apiResponse).getCode().toString());
                                 deleteReceiverAddressResponseMutableLiveData.postValue(apiResponse);
                                 utility.logger("Delete receiverAddress" + apiResponse.getData().getAsString());
                            }
                            else {
                                //showConfirmation();
                                utility.showDialog("Please, Try Again.");
                                utility.logger("Delete receiverAddress failed");
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
            } else {
                utility.showDialog("No Internet Connection.");
            }
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    public void sendBuyerProfileUpdate(Buyer_Update buyer_update) {
        try {
            if (utility.isNetworkAvailable()) {
                utility.logger("Update Buyer Profile" + buyer_update.toString());
                Call<API_RESPONSE> call = apiInterface.sendBuyerProfileUpdate(utility.getAuthorizationKey(), "2", KeyWord.TOKEN,"EN", buyer_update);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(@NotNull Call<API_RESPONSE> call, @NotNull Response<API_RESPONSE> response) {
                        try {
                            API_RESPONSE apiResponse = response.body();
                            utility.logger("Update Buyer Profile" + response.toString());
                            if (response.isSuccessful()) {
                                Log.d("Update Buyer Profile", Objects.requireNonNull(apiResponse).getCode().toString());
                                buyerProfileUpdateResponseMutableLiveData.postValue(apiResponse);

                            }
                            else {
                                //showConfirmation();
                                buyerProfileUpdateResponseMutableLiveData.postValue(apiResponse);
                                utility.logger("Update Buyer Profile failed");
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
            } else {
                utility.showDialog("No Internet Connection.");
            }
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    public  MutableLiveData<API_RESPONSE> getDeleteReceiverAddressResponse() { return deleteReceiverAddressResponseMutableLiveData; }
    public  MutableLiveData<API_RESPONSE> getBuyerPrimaryAddressResponseMutableLiveData() { return buyerPrimaryAddressResponseMutableLiveData; }
    public  MutableLiveData<API_RESPONSE> getBuyerProfileUpdateResponse(){return buyerProfileUpdateResponseMutableLiveData;}

}
