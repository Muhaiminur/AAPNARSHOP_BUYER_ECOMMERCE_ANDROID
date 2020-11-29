package com.aapnarshop.buyer.VIEW.FRAGMENT;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.aapnarshop.buyer.Http.API_RESPONSE;
import com.aapnarshop.buyer.Library.Utility;
import com.aapnarshop.buyer.Model.GET.BuyerProfile;
import com.aapnarshop.buyer.Model.SEND.Send_Otp;
import com.aapnarshop.buyer.R;
import com.aapnarshop.buyer.VIEW.ACTIVITY.HomePage;
import com.aapnarshop.buyer.VIEW.ACTIVITY.LandingPage;
import com.aapnarshop.buyer.VIEWMODEL.SignUpViewModel;
import com.google.gson.Gson;
import com.poovam.pinedittextfield.CirclePinField;

import java.util.Objects;

public class OtpRegistrationFragment extends Fragment {
    private CirclePinField otp_input;
    private SignUpViewModel signUpViewModel;
    private String userName;
    private Utility utility;
    private Gson gson;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson=new Gson();
        signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);
        utility = new Utility(getActivity());
        signUpViewModel.getOtpRegistrationResponse().observe(this, new Observer<API_RESPONSE>() {
            @Override
            public void onChanged(API_RESPONSE api_response) {
                if (api_response.getCode() == 200) {
                    Log.d("ResponseOtpReg","SUccEss " + api_response.getMessage() );
                    utility.showDialog(getString(R.string.signup_successful_text));
                    BuyerProfile buyerProfile = gson.fromJson(api_response.getData(), BuyerProfile.class);
                    saveSharedPreference(buyerProfile);
                    requireActivity().finish();
                    Intent intent = new Intent(getActivity(), LandingPage.class);
                    startActivity(intent);
                } else if (api_response.getCode() == 401) {
                    Log.d("ResponseOtpReg","Expired " + api_response.getMessage() );
                    utility.showDialog(api_response.getMessage());
                }
                else{
                    utility.showDialog(api_response.getMessage());
                    Log.d("ResponseOtpReg","error " + api_response.getMessage()+" " +api_response.getCode()+ " "+ otp_input.getText() + userName );
                }
            }
        });

        signUpViewModel.getProgressbar().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(final Boolean progressObserve) {
                if (progressObserve) {
                    utility.showProgress(false, "LOADING, Please wait!!");
                } else {
                    utility.hideProgress();
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate( R.layout.fragment_otp, container, false);
        Log.d("ResponseOtpReg",Objects.requireNonNull(requireArguments().getString("user_mobile_or_email")));
        userName= requireArguments().getString("user_mobile_or_email");
        otp_input=view.findViewById(R.id.otp_input);
        Button otp_Submit = view.findViewById(R.id.otp_submit);

        otp_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OtpValidation();
            }
        });

        return view;
    }

    private void OtpValidation()
    {
        if(TextUtils.isEmpty(String.valueOf(otp_input.getText())))
        {
            otp_input.setError("Please, Enter your OTP pin.");
        }
        else {
            Send_Otp otpRegistration = new Send_Otp(userName,String.valueOf(otp_input.getText()));
            signUpViewModel.sendOtpRegistration(otpRegistration);
        }
    }

    private void saveSharedPreference(BuyerProfile buyerProfile) {
        Activity activity = getActivity();
        if(activity != null && isAdded())
        {
            SharedPreferences sharedPref = activity.getSharedPreferences("BuyerProfile", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            Gson gson = new Gson();
            String json = gson.toJson(buyerProfile);
            editor.putString("BuyerProfileObject", json);
            editor.apply();
        }
    }

}
