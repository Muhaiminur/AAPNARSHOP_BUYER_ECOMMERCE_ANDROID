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
import com.aapnarshop.buyer.Model.GET.Area;
import com.aapnarshop.buyer.Model.GET.BuyerAddress;
import com.aapnarshop.buyer.Model.GET.City;
import com.aapnarshop.buyer.Model.GET.District;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAddressRepo {
    private final MutableLiveData<List<City>> cityMutableLiveData;
    private final MutableLiveData<List<Area>> areaMutableLiveData;
    private final MutableLiveData<API_RESPONSE> addAddressMutableLiveDataResponse;
    private final MutableLiveData<API_RESPONSE> updateAddressMutableLiveDataResponse;
    private final ApiInterface apiInterface;
    private final Utility utility;
    private final Gson gson;
    private List<City> cityList;
    private List<Area> areaList;

    public AddAddressRepo(Application application) {
        cityMutableLiveData = new MutableLiveData<>();
        areaMutableLiveData = new MutableLiveData<>();
        addAddressMutableLiveDataResponse = new MutableLiveData<>();
        updateAddressMutableLiveDataResponse = new MutableLiveData<>();
        apiInterface = ApiClient.getBaseClient().create(ApiInterface.class);
        utility = new Utility(application);
        gson = new Gson();
    }

    public LiveData<List<City>> getCityList(District district) {

        Call<API_RESPONSE> call = apiInterface.getCity(utility.getAuthorizationKey(), "2", KeyWord.TOKEN,"EN", district);
        call.enqueue(new Callback<API_RESPONSE>() {
            @Override
            public void onResponse(@NotNull Call<API_RESPONSE> call, @NotNull Response<API_RESPONSE> response) {
                try {
                    if (response.isSuccessful()) {
                        API_RESPONSE apiResponse = response.body();
                        if (apiResponse != null) {
                            if (apiResponse.getCode() == 200) {
                                Type listType = new TypeToken<List<City>>() {
                                }.getType();
                                cityList = gson.fromJson(apiResponse.getData(), listType);
                                cityMutableLiveData.postValue(cityList);
                                Log.d("AREA2", String.valueOf(cityList.size()));
                            } else {

                                cityMutableLiveData.postValue(cityList);
                            }
                        }

                    }
                } catch (Exception e) {
                    Log.d("Error Line Number", Log.getStackTraceString(e));
                }
            }

            @Override
            public void onFailure(@NotNull Call<API_RESPONSE> call, @NotNull Throwable t) {
                utility.logger("Error" + t.toString());
            }
        });


        return cityMutableLiveData;
    }

    public LiveData<List<Area>> getAreaList(City city) {

        Call<API_RESPONSE> call = apiInterface.getArea(utility.getAuthorizationKey(),  "2", KeyWord.TOKEN,"EN", city);
        call.enqueue(new Callback<API_RESPONSE>() {
            @Override
            public void onResponse(@NotNull Call<API_RESPONSE> call, @NotNull Response<API_RESPONSE> response) {
                try {
                    if (response.isSuccessful()) {
                        API_RESPONSE apiResponse = response.body();
                        if (apiResponse != null) {
                            if (apiResponse.getCode() == 200) {

                                Type listType = new TypeToken<List<Area>>() {
                                }.getType();
                                areaList = gson.fromJson(apiResponse.getData(), listType);
                                areaMutableLiveData.postValue(areaList);
                            } else {

                                areaMutableLiveData.postValue(areaList);
                            }
                        }

                    }
                } catch (Exception e) {
                    Log.d("Error Line Number", Log.getStackTraceString(e));
                }
            }

            @Override
            public void onFailure(@NotNull Call<API_RESPONSE> call, @NotNull Throwable t) {
                utility.logger("Error" + t.toString());
            }
        });


        return areaMutableLiveData;
    }

    public void addReceiverAddress(BuyerAddress addAddress) {

        try {
            if (utility.isNetworkAvailable()) {
                utility.logger("AddReceiverAddress" + addAddress.getReceiverName());
                Call<API_RESPONSE> call = apiInterface.addNewAddress(utility.getAuthorizationKey(), "2", KeyWord.TOKEN,"EN", addAddress);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(@NotNull Call<API_RESPONSE> call, @NotNull Response<API_RESPONSE> response) {
                        try {
                            utility.logger("AddReceiverAddress" + response.toString());
                            if (response.isSuccessful()) {
                                API_RESPONSE apiResponse = response.body();
                                if (apiResponse != null) {
                                    Log.d("AddAddressResponse", apiResponse.getCode().toString());
                                }

                                if (apiResponse != null) {
                                    if (apiResponse.getCode() == 200) {
                                        addAddressMutableLiveDataResponse.postValue(apiResponse);
                                        utility.logger("AddReceiverAddress" + apiResponse.getData().getAsString());
                                    }
                                    else {
                                        //showConfirmation();
                                        addAddressMutableLiveDataResponse.postValue(apiResponse);
                                        utility.logger("AddReceiverAddress Not 200" + apiResponse.getData().getAsString());
                                    }
                                }
                            }
                            else {
                                //showConfirmation();
                                utility.logger("AddReceiverAddress failed");
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

    public void updateReceiverAddress(BuyerAddress updateReceiverAddress) {

        try {
            if (utility.isNetworkAvailable()) {
                utility.logger("updateReceiverAddress" + updateReceiverAddress.getReceiverName());
                Call<API_RESPONSE> call = apiInterface.updateReceiverAddress(utility.getAuthorizationKey(), "2", KeyWord.TOKEN,"EN", updateReceiverAddress);
                call.enqueue(new Callback<API_RESPONSE>() {
                    @Override
                    public void onResponse(@NotNull Call<API_RESPONSE> call, @NotNull Response<API_RESPONSE> response) {
                        try {
                            utility.logger("updateReceiverAddress" + response.toString());
                            if (response.isSuccessful()) {
                                API_RESPONSE apiResponse = response.body();
                                if (apiResponse != null) {
                                    Log.d("updateAddressResponse", apiResponse.getCode().toString());
                                }

                                if (apiResponse != null) {
                                    if (apiResponse.getCode() == 200) {
                                        updateAddressMutableLiveDataResponse.postValue(apiResponse);
                                        utility.logger("updateReceiverAddress" + apiResponse.getData().getAsString());
                                    }
                                    else {
                                        //showConfirmation();
                                        updateAddressMutableLiveDataResponse.postValue(apiResponse);
                                        utility.logger("updateReceiverAddress Not 200" + apiResponse.getData().getAsString());
                                    }
                                }
                            }
                            else {
                                //showConfirmation();
                                utility.logger("updateReceiverAddress failed");
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

    public MutableLiveData<API_RESPONSE> getAddAddressMutableLiveDataResponse () { return addAddressMutableLiveDataResponse; }

    public MutableLiveData<API_RESPONSE> getUpdateAddressMutableLiveDataResponse () { return updateAddressMutableLiveDataResponse; }
}
