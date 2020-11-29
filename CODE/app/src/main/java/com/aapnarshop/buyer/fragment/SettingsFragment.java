package com.aapnarshop.buyer.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.aapnarshop.buyer.Library.Utility;
import com.aapnarshop.buyer.Model.SEND.Change_Password;
import com.aapnarshop.buyer.Navigation.BaseFragment;
import com.aapnarshop.buyer.R;
import com.aapnarshop.buyer.VIEWMODEL.SettingsViewModel;

public class SettingsFragment extends BaseFragment {
    private View view;
    private SettingsViewModel settingsViewModel;
    private Utility utility;
    private TextView settingsCurrentPassValue,settingsNewPassValue,settingsReTypeNewPassValue,settings_save;

    public SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        initView();
        setupToolbar();

        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        utility = new Utility(getActivity());

        observeChangePassword();
        settings_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePasswordValidation();
            }
        });

        return view;

    }
    private void initView() {
        settings_save  = view.findViewById(R.id.settings_save_btn);
        settingsCurrentPassValue = view.findViewById(R.id.settings_current_password_value);
        settingsNewPassValue = view.findViewById(R.id.settings_new_password_value);
        settingsReTypeNewPassValue = view.findViewById(R.id.settings_retype_new_password_value);
    }
    //Observing Api call for password change
    private void observeChangePassword()
    {
        settingsViewModel.getChangePasswordResponse().observe(requireActivity(), apiResponse -> {
            if (apiResponse.getCode() == 200) {
                utility.showDialog(apiResponse.getData().getAsString());
            } else if (apiResponse.getCode() == 401) {
                utility.showDialog(apiResponse.getData().getAsString());
            } else {
                utility.showDialog(apiResponse.getData().getAsString());
            }
            clearTextViewFields();
        });

    }

    private void clearTextViewFields()
    {
        settingsCurrentPassValue.setText("");
        settingsNewPassValue.setText("");
        settingsReTypeNewPassValue.setText("");
    }

    private void ChangePasswordValidation()
    {
        if (TextUtils.isEmpty(settingsCurrentPassValue.getText().toString())) {
            settingsCurrentPassValue.requestFocus();
            settingsCurrentPassValue.setError(getString(R.string.validation_current_password_error_text));
        } else if (TextUtils.isEmpty(settingsNewPassValue.getText().toString())) {
            settingsNewPassValue.requestFocus();
            settingsNewPassValue.setError(getString(R.string.validation_new_password_error_text));
        } else if (TextUtils.isEmpty(settingsReTypeNewPassValue.getText().toString())) {
            settingsReTypeNewPassValue.requestFocus();
            settingsReTypeNewPassValue.setError(getString(R.string.validation_renew_password_error_text));
        }
        else if (!TextUtils.isEmpty(settingsNewPassValue.getText().toString()) && !TextUtils.isEmpty(settingsReTypeNewPassValue.getText().toString())) {
            if (settingsNewPassValue.getText().toString().equals(settingsReTypeNewPassValue.getText().toString())) {
                Change_Password change_password = new Change_Password(settingsCurrentPassValue.getText().toString(), settingsReTypeNewPassValue.getText().toString());
                settingsViewModel.changePassword(change_password);
            }
            else
            {
                TextView settingsPasswordMatchText = view.findViewById(R.id.settings_password_match_text);
                settingsPasswordMatchText.setVisibility(View.VISIBLE);
                settingsPasswordMatchText.setText(R.string.password_not_matched_text);
            }
        }
    }

    private void setupToolbar()
    {
        Toolbar toolbarDashboard =  requireActivity().findViewById(R.id.toolbarDashboard);
        Toolbar toolbar_store_homepage = requireActivity().findViewById(R.id.toolbarStoreHomepage);
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbarNotification);
        ((AppCompatActivity)requireActivity()).setSupportActionBar(toolbar);

        toolbarDashboard.setVisibility(View.GONE);
        toolbar.setVisibility(View.VISIBLE);
        toolbar_store_homepage.setVisibility(View.GONE);
        TextView textToolHeader = toolbar.findViewById(R.id.toolbarModifiedTitle);
        ImageView toolbarImgBack = toolbar.findViewById(R.id.back_arrow);
        ImageView toolbarImgDrawerLogo = toolbar.findViewById(R.id.drawer_logo_notification_toolbar);

        textToolHeader.setText(R.string.text_settings);
        toolbarImgBack.setVisibility(View.VISIBLE);
        toolbarImgDrawerLogo.setVisibility(View.GONE);
        toolbarImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getParentFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    Log.i("settings", "popping backstack Settings");
                    fm.popBackStack();
                } else {
                    Log.i("settings", "nothing on backstack, calling super from settings");
                }
            }
        });
    }

    @Override
    protected String getTitle() {
        return "Settings_Fragment";
    }
}
