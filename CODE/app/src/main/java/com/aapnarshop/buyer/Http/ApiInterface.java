package com.aapnarshop.buyer.Http;

import com.aapnarshop.buyer.Model.GET.BuyerAddress;
import com.aapnarshop.buyer.Model.GET.City;
import com.aapnarshop.buyer.Model.GET.District;
import com.aapnarshop.buyer.Model.SEND.Buyer_Update;
import com.aapnarshop.buyer.Model.SEND.Change_Password;
import com.aapnarshop.buyer.Model.SEND.SEND_REGISTRATION;
import com.aapnarshop.buyer.Model.SEND.Send_Otp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterface {
    //1 PRE REGISTRATION
    @POST("buyer/pre/registration")
    Call<API_RESPONSE> Aapnarshop_Pre_Registration(@Header("Authorization") String apiKey, @Header("userId") String usersId, @Header("token") String token, @Header("Language") String Language, @Body SEND_REGISTRATION SEND_REGISTRATION);

    //2 OTP REGISTRATION
    @POST("buyer/registration")
    Call<API_RESPONSE> Aapnarshop_Otp_Registration(@Header("Authorization") String apiKey, @Header("userId") String usersId, @Header("token") String token,  @Header("Language") String Language,@Body Send_Otp Send_Otp);

    //3 APP CONFIG
    @POST("buyer/get/config")
    Call<API_RESPONSE> Buyer_Config(@Header("Authorization") String apiKey, @Header("userId") String usersId, @Header("token") String token, @Header("Language") String Language);

    //4 USER LOGIN
    @POST("buyer/login")
    Call<API_RESPONSE> Aapnarshop_Get_Login(@Header("Authorization") String apiKey, @Header("userId") String usersId, @Header("token") String token, @Header("Language") String Language, @Body SEND_REGISTRATION getLogin);

    //5 CHANGE PASSWORD
    @POST("buyer/profile/change/password")
    Call<API_RESPONSE> Change_Password(@Header("Authorization") String apiKey, @Header("userId") String usersId, @Header("token") String token, @Header("Language") String Language, @Body Change_Password change_password);

    //6 BUYER PROFILE
    @POST("buyer/profile")
    Call<API_RESPONSE> Get_Buyer_Profile(@Header("Authorization") String apiKey, @Header("userId") String usersId, @Header("token") String token, @Header("Language") String Language);

    //7 City List
    @POST("general/address/city/list")
    Call<API_RESPONSE> getCity(@Header("Authorization") String apiKey, @Header("userId") String usersId, @Header("token") String token, @Header("Language") String Language, @Body District district);

    //8 Area List
    @POST("general/address/area/list")
    Call<API_RESPONSE> getArea(@Header("Authorization") String apiKey, @Header("userId") String usersId, @Header("token") String token, @Header("Language") String Language, @Body City city);

    //9 Buyer Address List
    @POST("buyer/address/get/list")
    Call<API_RESPONSE> getBuyerAddress(@Header("Authorization") String apiKey, @Header("userId") String usersId, @Header("token") String token, @Header("Language") String Language);

    //10 Set Receiver Primary Address
    @POST("buyer/address/make/primary")
    Call<API_RESPONSE> setBuyerPrimaryAddress(@Header("Authorization") String apiKey, @Header("userId") String usersId, @Header("token") String token, @Header("Language") String Language, @Body BuyerAddress buyerPrimaryAddress);

    //11 Add New Receiver Address
    @POST("buyer/address/add")
    Call<API_RESPONSE> addNewAddress(@Header("Authorization") String apiKey, @Header("userId") String usersId, @Header("token") String token, @Header("Language") String Language, @Body BuyerAddress addReceiverAddress);

    //12 Delete Receiver Address
    @POST("buyer/address/delete")
    Call<API_RESPONSE> deleteReceiverAddress(@Header("Authorization") String apiKey, @Header("userId") String usersId, @Header("token") String token, @Header("Language") String Language, @Body BuyerAddress deleteReceiverAddress);

    //13 Update Receiver Address
    @POST("buyer/address/update")
    Call<API_RESPONSE> updateReceiverAddress(@Header("Authorization") String apiKey, @Header("userId") String usersId, @Header("token") String token, @Header("Language") String Language, @Body BuyerAddress updateReceiverAddress);

    //14 FORGOT PASSWORD
    @POST("buyer/retrieve/password/otp")
    Call<API_RESPONSE> Aapnarshop_Forgot_Password(@Header("Authorization") String apiKey, @Header("userId") String usersId, @Header("token") String token, @Header("Language") String Language, @Body SEND_REGISTRATION forgotPasswordInfo);

    //15 OTP REGISTRATION
    @POST("buyer/retrieve/password/set")
    Call<API_RESPONSE> Aapnarshop_Otp_Forgot_Password(@Header("Authorization") String apiKey, @Header("userId") String usersId, @Header("token") String token,  @Header("Language") String Language,@Body Send_Otp Send_Otp_Forgot_Pass);

    //16 UPDATE BUYER PROFILE
    @POST("buyer/profile/update")
    Call<API_RESPONSE> sendBuyerProfileUpdate(@Header("Authorization") String apiKey, @Header("userId") String usersId, @Header("token") String token,  @Header("Language") String Language,@Body Buyer_Update sendBuyerUpdate);

    //17 BUYER PIN REQUEST
    @POST("buyer/pin/request")
    Call<API_RESPONSE> Aapnarshop_Buyer_Pin_Request(@Header("Authorization") String apiKey, @Header("userId") String usersId, @Header("token") String token, @Header("Language") String Language, @Body SEND_REGISTRATION buyerPinRequest);

    //18 BUYER PIN VERIFICATION
    @POST("buyer/pin/verification")
    Call<API_RESPONSE> Aapnarshop_Buyer_Pin_Verification(@Header("Authorization") String apiKey, @Header("userId") String usersId, @Header("token") String token,  @Header("Language") String Language,@Body Send_Otp buyerPinVerification);

}
