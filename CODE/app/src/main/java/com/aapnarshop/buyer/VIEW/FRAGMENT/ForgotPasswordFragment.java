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
import android.widget.EditText;
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
import com.aapnarshop.buyer.VIEWMODEL.ForgotPassViewModel;

public class ForgotPasswordFragment extends Fragment {

    private View view;
    private EditText forgotPassEmailPhoneEditText;
    private ForgotPassViewModel forgotPassViewModel;
    private Utility utility;

    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utility = new Utility(getActivity());
        forgotPassViewModel = new ViewModelProvider(this).get(ForgotPassViewModel.class);
        forgotPassViewModel.getForgotPassResponse().observe(this, new Observer<API_RESPONSE>() {
            @Override
            public void onChanged(API_RESPONSE api_response) {
                if (api_response.getCode() == 200) {
                    Log.d("ResponseForgotPass", "SUccEss " + api_response.getMessage());
                    utility.showDialog(api_response.getData().getAsString());
                    Bundle bundle = new Bundle();
                    bundle.putString("user_mobile_or_email_ForgotPass", forgotPassEmailPhoneEditText.getText().toString());
                    Navigation.findNavController(view).navigate(R.id.forgotPassOtpFragment, bundle);
                } else if (api_response.getCode() == 401) {
                    Log.d("ResponseForgotPass", "FailEss " + api_response.getMessage());
                    utility.showDialog(api_response.getData().getAsString());
                } else {
                    Log.d("ResponseForgotPass", "error " + api_response.getMessage() + api_response.getCode());
                    utility.showDialog(getString(R.string.failed_otp_text));
                }
            }
        });

        forgotPassViewModel.getProgressbar().observe(this, new Observer<Boolean>() {
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
        view= inflater.inflate( R.layout.fragment_forgot_password, container, false);
        Button submitBtn = view.findViewById(R.id.forgot_pass_submit_button);
        AppCompatImageButton forgotPassBackBtn=view.findViewById(R.id.forgot_password_back_button);
        forgotPassEmailPhoneEditText = view.findViewById(R.id.forgot_pass_edit_text_phone_number);
        TextView privacy_text = view.findViewById(R.id.text_privacy_policy_forgotPass);
        TextView termsofservice_text = view.findViewById(R.id.text_terms_of_service_forgotPass);

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

            forgotPassBackBtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_forgotPassFragment_to_signInFragment));

            submitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ForgotPasswordValidation();
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

    private void ForgotPasswordValidation()
    {
        if(forgotPassEmailPhoneEditText.getText().toString().contains("@"))
        {
            if(!utility.isEmailValid(forgotPassEmailPhoneEditText.getText().toString())) {
                forgotPassEmailPhoneEditText.requestFocus();
                forgotPassEmailPhoneEditText.setError(getString(R.string.validation_email_text));
            }
            else
            {
                SEND_REGISTRATION forgotPasswordInfo = new SEND_REGISTRATION(forgotPassEmailPhoneEditText.getText().toString());
                forgotPassViewModel.sendForgotPasswordInfo(forgotPasswordInfo);
            }
        }
        else if (TextUtils.isEmpty(forgotPassEmailPhoneEditText.getText().toString()))
        {
            forgotPassEmailPhoneEditText.requestFocus();
            forgotPassEmailPhoneEditText.setError(getString(R.string.validation_phoneandemail_text));
        }
        else
        {
            if (!utility.validateMsisdn(forgotPassEmailPhoneEditText.getText().toString())) {
                forgotPassEmailPhoneEditText.requestFocus();
                forgotPassEmailPhoneEditText.setError(getString(R.string.validation_phone_text));
            }
            else
            {
                SEND_REGISTRATION forgotPasswordInfo = new SEND_REGISTRATION(forgotPassEmailPhoneEditText.getText().toString());
                forgotPassViewModel.sendForgotPasswordInfo(forgotPasswordInfo);
            }
        }

    }

}
