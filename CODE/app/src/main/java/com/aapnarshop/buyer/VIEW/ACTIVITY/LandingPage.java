package com.aapnarshop.buyer.VIEW.ACTIVITY;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.aapnarshop.buyer.Library.Utility;
import com.aapnarshop.buyer.Model.GET.BuyerProfile;
import com.aapnarshop.buyer.R;
import com.aapnarshop.buyer.VIEWMODEL.LandingPageViewModel;
import com.google.gson.Gson;


public class LandingPage extends AppCompatActivity {
    LandingPageViewModel landingPageViewModel;
    Utility utility = new Utility(this);
    private ProgressBar configProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        landingPageViewModel = new ViewModelProvider(this).get(LandingPageViewModel.class);
        configProgressBar = findViewById(R.id.landing_activity_progressbar);
        callApiConfig();
    }

    private void callApiConfig()
    {
        try {
            if (utility.isNetworkAvailable()) {

                landingPageViewModel.getProgressBar().observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean progressObserve) {
                        if (progressObserve) {
                            configProgressBar.setVisibility(View.VISIBLE);
                        }
                    }
                });

                landingPageViewModel.getConfigLiveData().observe(this, getConfig -> {
                    if (getConfig != null) {
                       getProfileObjectSharedPreference();
                    }
                    else {
                        configProgressBar.setVisibility(View.INVISIBLE);
                        utility.showDialog(getString(R.string.try_again_text));
                    }

                });
            } else {
                utility.showDialog(getResources().getString(R.string.no_internet_string));
            }
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    private void getProfileObjectSharedPreference() {
        SharedPreferences sharedPref = getSharedPreferences("BuyerProfile", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPref.getString("BuyerProfileObject","");
        BuyerProfile buyerProfile = gson.fromJson(json, BuyerProfile.class);
        if (buyerProfile !=null){
            startActivity(new Intent(LandingPage.this,HomePage.class));
            finish();
        }else {
            startActivity(new Intent(LandingPage.this, LoginPage.class));
            finish();
        }
    }


}