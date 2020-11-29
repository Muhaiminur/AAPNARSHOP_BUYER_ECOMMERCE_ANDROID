package com.aapnarshop.buyer.VIEW.FRAGMENT;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.aapnarshop.buyer.Http.API_RESPONSE;
import com.aapnarshop.buyer.Library.Utility;
import com.aapnarshop.buyer.Model.GET.BuyerProfile;
import com.aapnarshop.buyer.Model.SEND.SEND_REGISTRATION;
import com.aapnarshop.buyer.R;
import com.aapnarshop.buyer.VIEW.ACTIVITY.HomePage;
import com.aapnarshop.buyer.VIEWMODEL.SignInViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

public class SignInFragment extends Fragment {
    private TextInputEditText signInPassword,signInUsername;
    private Utility utility;
    private SignInViewModel signInViewModel;
    private Gson gson;

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson=new Gson();
        signInViewModel=new ViewModelProvider(this).get(SignInViewModel.class);
        utility = new Utility(getActivity());

        signInViewModel.getProgressbar().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(final Boolean progressObserve) {
                if (progressObserve) {
                    utility.showProgress(false, "Signing In, Please wait!!");
                } else {
                    utility.hideProgress();
                }
            }
        });

        signInViewModel.getLoginMutableDataResponse().observe(this, new Observer<API_RESPONSE>() {
            @Override
            public void onChanged(API_RESPONSE api_response) {
                if (api_response.getCode() == 200) {
                    BuyerProfile buyerProfile = gson.fromJson(api_response.getData(), BuyerProfile.class);
                    Log.d("BuyerProfile","SUccEssful " + buyerProfile.getEmail());
                    saveSharedPreference(buyerProfile);
                    Intent intent = new Intent(getActivity(), HomePage.class);
                    startActivity(intent);
                    requireActivity().finish();
                }
                else if(api_response.getCode() == 401)
                {
                    utility.showDialog(api_response.getData().getAsString());
                }
                else if (api_response.getCode() == 409) {
                    Log.d("ResponseLogin","Failed " + api_response.getMessage() );
                    utility.showDialog(api_response.getData().getAsString());
                }
                else{
                    utility.showDialog(api_response.getData().getAsString());
                    Log.d("ResponseLogin","Failed " + api_response.getMessage() +api_response.getCode() );
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_sign_in, container, false);

        Button signInBtn = view.findViewById(R.id.signin_button);
        TextView forgotPasswordText = view.findViewById(R.id.forgot_password);
        TextView signUpText = view.findViewById(R.id.text_create_account);
        TextView privacy_text = view.findViewById(R.id.text_privacy_policy_signin);
        TextView termsofservice_text = view.findViewById(R.id.text_terms_of_service_signin);

        privacy_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCustomTab(getString(R.string.privacy_url));
            }
        });
        termsofservice_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCustomTab(getString(R.string.termsofservice_url));
            }
        });

        signInUsername = view.findViewById(R.id.login_phone_email_input_text);
        signInPassword = view.findViewById(R.id.login_password_input);


        forgotPasswordText.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_signInFragment_to_forgotPassFragment));

        try {
            requireActivity().getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
            );

            signInBtn.setOnClickListener(v -> {
                utility.hideKeyboard(view);
                signInValidation();
            });
            signUpText.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_signInFragment_to_signUpFragment));

         } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }

        return view;
    }
    private void openCustomTab(String uriString)
    {
        try {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(ContextCompat.getColor(requireContext(), R.color.app_yellow));
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(requireContext(), Uri.parse(uriString));
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
            try {
                Uri uri = Uri.parse(uriString); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                requireContext().startActivity(intent);
            } catch (Exception e2) {
                Log.d("Error Line Number", Log.getStackTraceString(e2));
                utility.showDialog(requireContext().getResources().getString(R.string.link_fail_text));
            }
        }
    }

    private void signInValidation()
    {
        String signInInput = signInUsername.getEditableText().toString();
        String signInPass = signInPassword.getEditableText().toString();

        if(!TextUtils.isEmpty(signInInput) && !TextUtils.isEmpty(signInPass)) {
            if (signInInput.contains("@")) {
                if (!utility.isEmailValid(signInInput)) {
                    signInUsername.requestFocus();
                    signInUsername.setError(getString(R.string.username_error_email));
                } else if (TextUtils.isEmpty(signInPass)) {
                    signInPassword.requestFocus();
                    signInPassword.setError(getString(R.string.password_error));
                } else {
                    //signIn;
                    SEND_REGISTRATION userLogin = new SEND_REGISTRATION(signInUsername.getEditableText().toString(), signInPassword.getEditableText().toString());
                    signInViewModel.getLogin(userLogin);
                }
            } else {
                if (!utility.validateMsisdn(signInInput)) {
                    signInUsername.requestFocus();
                    signInUsername.setError(getString(R.string.username_error_phone));
                } else if (TextUtils.isEmpty(signInPass)) {
                    signInPassword.requestFocus();
                    signInPassword.setError(getString(R.string.password_error));
                } else {
                    //signIn;
                    SEND_REGISTRATION userLogin = new SEND_REGISTRATION(signInUsername.getEditableText().toString(), signInPassword.getEditableText().toString());
                    signInViewModel.getLogin(userLogin);
                }
            }
        }
        else
        {
            signInUsername.requestFocus();
            signInUsername.setError(getString(R.string.username_error));
            signInPassword.requestFocus();
            signInPassword.setError(getString(R.string.password_error));
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
