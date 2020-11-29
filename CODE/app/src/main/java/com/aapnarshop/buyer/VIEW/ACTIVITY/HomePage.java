package com.aapnarshop.buyer.VIEW.ACTIVITY;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.aapnarshop.buyer.Model.GET.BuyerProfile;
import com.aapnarshop.buyer.Navigation.AddFragmentHandler;
import com.aapnarshop.buyer.Navigation.BackButtonSupportFragment;
import com.aapnarshop.buyer.Navigation.BaseFragment;
import com.aapnarshop.buyer.R;
import com.aapnarshop.buyer.fragment.DashboardFragment;
import com.aapnarshop.buyer.fragment.NotificationFragment;
import com.aapnarshop.buyer.fragment.OrderFragment;
import com.aapnarshop.buyer.fragment.ProfileFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView navigationView;
    private DrawerLayout drawerLayout;
    ImageView drawerLogo,drawerLogoNotification,drawerLogoStoreHomepage;
    Toolbar myToolbar;

    private FragmentManager fragmentManager;
    private AddFragmentHandler fragmentHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_main);
        try {
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLogo=findViewById(R.id.drawer_logo);
        drawerLogoNotification=findViewById(R.id.drawer_logo_notification_toolbar);
        drawerLogoStoreHomepage=findViewById(R.id.drawer_logo_store_homepage);

        myToolbar=findViewById(R.id.toolbarDashboard);

        fragmentManager = getSupportFragmentManager();
        fragmentHandler = new AddFragmentHandler(fragmentManager);

        setSupportActionBar (myToolbar);

        navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }

        showDashboard();

        //Selecting navigation drawer items based on fragment stacks
        this.getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                        Fragment current = getCurrentFragment();
                        if (current instanceof DashboardFragment) {
                            navigationView.setCheckedItem(R.id.nav_home);
                        }
                        else if(current instanceof OrderFragment)
                        {
                            navigationView.setCheckedItem(R.id.nav_order);
                        }
                        else if(current instanceof NotificationFragment)
                        {
                            navigationView.setCheckedItem(R.id.nav_notification);
                        }
                        else if(current instanceof ProfileFragment)
                        {
                            navigationView.setCheckedItem(R.id.nav_profile);
                        }
                    }
                });
        // LANGUAGE EN TO BN SWITCH OPTION
        Menu menu = navigationView.getMenu();
        SwitchCompat actionViewSwitch =  menu.findItem(R.id.nav_language).getActionView().findViewById(R.id.navbar_language_switch);
        TextView languageText =  menu.findItem(R.id.nav_language).getActionView().findViewById(R.id.language_option);
        actionViewSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(actionViewSwitch.isChecked()) {
                    languageText.setText(getString(R.string.bangla_format));
                }else{
                    languageText.setText(getString(R.string.english_format));
                }
            }
        });
        //NAVIGATION HEADER CONTROLS
        View navigationHeader= navigationView.getHeaderView(0);
        TextView navbarHeaderProfileName = navigationHeader.findViewById(R.id.header_profile_name);
        TextView navbarHeaderProfileNumber = navigationHeader.findViewById(R.id.header_profile_phone_number);
        AppCompatImageButton navbarBackBtn = navigationHeader.findViewById(R.id.navbar_back_button);
        navbarBackBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        });

        //SET NAME AND PHONE/EMAIL TO NAVIGATION DRAWER
        SharedPreferences sharedPref = getSharedPreferences("BuyerProfile", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPref.getString("BuyerProfileObject","");
        BuyerProfile buyerProfile = gson.fromJson(json, BuyerProfile.class);
        if (buyerProfile !=null){
            navbarHeaderProfileName.setText(buyerProfile.getFullName());
            if(!TextUtils.isEmpty(buyerProfile.getPhone())){
                navbarHeaderProfileNumber.setText(buyerProfile.getPhone());
            }else{
                navbarHeaderProfileNumber.setText(buyerProfile.getEmail());
            }
        }

        drawerLogo.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {

                if (!drawerLayout.isDrawerOpen(Gravity.START)) drawerLayout.openDrawer(Gravity.START);
                else drawerLayout.closeDrawer(Gravity.END);
            }
        });

        drawerLogoNotification.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {

                if (!drawerLayout.isDrawerOpen(Gravity.START)) drawerLayout.openDrawer(Gravity.START);
                else drawerLayout.closeDrawer(Gravity.END);
            }
        });

        drawerLogoStoreHomepage.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {

                if (!drawerLayout.isDrawerOpen(Gravity.START)) drawerLayout.openDrawer(Gravity.START);
                else drawerLayout.closeDrawer(Gravity.END);
            }
        });

    }
    public Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
    }

    private void showDashboard()
    {
        DashboardFragment df = new DashboardFragment();
        add(df.newInstance());
    }
    private void showNotification()
    {
        NotificationFragment nf = new NotificationFragment();
        add(nf.newInstance());
    }
    private void showOrder()
    {
        OrderFragment of = new OrderFragment();
        add(of.newInstance());
    }
    private void showProfile()
    {
        ProfileFragment pf = new ProfileFragment();
        add(pf.newInstance());
    }
    /*private void showSettings() {
        SettingsFragment sf = new SettingsFragment();
        add(sf.newInstance());
    }*/
    private void logout() {
        SharedPreferences sharedPref = getSharedPreferences("BuyerProfile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("BuyerProfileObject");
        editor.apply();
        this.finish();

        Intent intentToLogout = new Intent(this, LoginPage.class);
        startActivity(intentToLogout);
        this.overridePendingTransition(0, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NotNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NotNull View drawerView) {
               // syncDrawerToggleState();
            }

            @Override
            public void onDrawerClosed(@NotNull View drawerView) {
                //syncDrawerToggleState();
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        //fragmentManager.removeOnBackStackChangedListener(backStackListener);
        fragmentManager = null;
        super.onDestroy();
    }

    protected void add(BaseFragment fragment) {
        fragmentHandler.add(fragment);
    }

    @Override
    public void onBackPressed() {
        if (sendBackPressToDrawer()) {
            //the drawer consumed the backpress
            return;
        }
        if (sendBackPressToFragmentOnTop()) {
            // fragment on top consumed the back press
            return;
        }
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments == 1) {
            finish();
        } else if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }

    }

    private boolean sendBackPressToDrawer() {
        DrawerLayout drawer = drawerLayout;
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }

    private boolean sendBackPressToFragmentOnTop() {
        BaseFragment fragmentOnTop = fragmentHandler.getCurrentFragment();
        if (fragmentOnTop == null) {
            return false;
        }
        if (!(fragmentOnTop instanceof BackButtonSupportFragment)) {
            return false;
        }
        return ((BackButtonSupportFragment) fragmentOnTop).onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        Log.d( "Main", "onCreateOptionsMenu" );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item ) {
        Log.d( "Main", "onOptionsItemSelected" );

        return super.onOptionsItemSelected( item );
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        menuItem.setChecked(true);

        drawerLayout.closeDrawers();

        int id = menuItem.getItemId();

        switch (id) {

            case R.id.nav_home:
                showDashboard();
                break;

            case R.id.nav_order:
               showOrder();
                break;

            case R.id.nav_notification:
               showNotification();
                break;

            case R.id.nav_profile:
                showProfile();
                break;

          /*  case R.id.nav_settings:
                showSettings();
                break;
*/
            case R.id.nav_logout:
                logout();
                break;

        }
        return true;

    }

}
