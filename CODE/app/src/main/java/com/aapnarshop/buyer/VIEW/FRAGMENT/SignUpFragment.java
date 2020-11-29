package com.aapnarshop.buyer.VIEW.FRAGMENT;

import android.content.Intent;
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
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.aapnarshop.buyer.Http.API_RESPONSE;
import com.aapnarshop.buyer.Library.Utility;
import com.aapnarshop.buyer.Model.SEND.SEND_REGISTRATION;
import com.aapnarshop.buyer.R;
import com.aapnarshop.buyer.VIEWMODEL.SignUpViewModel;
import com.google.android.material.textfield.TextInputEditText;

public class SignUpFragment extends Fragment {
    private View view;
    private TextInputEditText signUpUsername;
    private SignUpViewModel signUpViewModel;
    private Utility utility;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);
        utility = new Utility(getActivity());
        signUpViewModel.getPreRegistrationResponse().observe(this, new Observer<API_RESPONSE>() {
            @Override
            public void onChanged(API_RESPONSE api_response) {
                if (api_response.getCode() == 200) {
                    Log.d("ResponsePreReg","SUccEssful " + api_response.getMessage() );
                    utility.showDialog(api_response.getData().getAsString());
                    Bundle bundle = new Bundle();
                    bundle.putString("user_mobile_or_email", signUpUsername.getEditableText().toString());
                    Navigation.findNavController(view).navigate(R.id.otpFragment, bundle);
                } else if (api_response.getCode() == 409) {
                    Log.d("ResponsePreReg","Failed " + api_response.getMessage() );
                    utility.showDialog(api_response.getData().getAsString());
                }
                else{
                    Log.d("ResponsePreReg","Failed " + api_response.getMessage() +api_response.getCode() );
                    utility.showDialog(getString(R.string.failed_otp_text));
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
        view= inflater.inflate( R.layout.fragment_sign_up, container, false);
        Button signUpBtn = view.findViewById(R.id.signup_button);
        AppCompatImageButton signUpBackBtn=view.findViewById(R.id.signup_back_button);
        signUpUsername = view.findViewById(R.id.signup_phone_email_input_text);
        TextView privacy_text = view.findViewById(R.id.text_privacy_policy_signup);
        TextView termsofservice_text = view.findViewById(R.id.text_terms_of_service_signup);

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

        try {
            requireActivity().getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
            );

            signUpBackBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_signUpFragment_to_signInFragment));

            signUpBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    utility.hideKeyboard(view);
                    SignUpValidation();
                }
            });

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

    private void SignUpValidation()
    {
        if(signUpUsername.getEditableText().toString().contains("@"))
        {
            if(!utility.isEmailValid(signUpUsername.getEditableText().toString()))
            {
                signUpUsername.requestFocus();
                signUpUsername.setError(getString(R.string.username_error_email));
            }
            else
            {
                SEND_REGISTRATION preRegistration = new SEND_REGISTRATION(signUpUsername.getEditableText().toString());
                signUpViewModel.sendPreRegistration(preRegistration);
            }
        }
        else if (TextUtils.isEmpty(signUpUsername.getEditableText().toString()))
        {
            signUpUsername.requestFocus();
            signUpUsername.setError(getString(R.string.mandatory_error));
        }
        else
        {
            if (!utility.validateMsisdn(signUpUsername.getEditableText().toString())) {
                signUpUsername.requestFocus();
                signUpUsername.setError(getString(R.string.username_error_phone));
            }
            else
            {
                SEND_REGISTRATION preRegistration = new SEND_REGISTRATION(signUpUsername.getEditableText().toString());
                //sendPreRegistration(preRegistration);
                signUpViewModel.sendPreRegistration(preRegistration);
            }
        }

    }

/*    private void sendPreRegistration(SEND_REGISTRATION preRegistration)
    {
        signUpViewModel.sendPreRegistration(preRegistration).observe(SignUpFragment.this, api_response -> {

            if (api_response.getCode() == 200) {
                Log.d("ResponsePreReg","SUccEss " + api_response.getMessage() );
                Bundle bundle = new Bundle();
                bundle.putString("user_mobile_or_email", signUpUsername.getEditableText().toString());
                Navigation.findNavController(view).navigate(R.id.otpFragment, bundle);
            } else if (api_response.getCode() == 409) {
                Log.d("ResponsePreReg","FailEss " + api_response.getMessage() );
                utility.showDialog(api_response.getData().getAsString());
            }
            else{
                Log.d("ResponsePreReg","SERVess " + api_response.getMessage() +api_response.getCode() );
                utility.showDialog("Failed to send OTP, Please, try again");
            }
        });
    }*/

}
