package com.aapnarshop.buyer.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aapnarshop.buyer.Adapters.ProfileAddressRecyclerAdapter;
import com.aapnarshop.buyer.Http.API_RESPONSE;
import com.aapnarshop.buyer.Library.KeyWord;
import com.aapnarshop.buyer.Library.Utility;
import com.aapnarshop.buyer.Model.GET.BuyerAddress;
import com.aapnarshop.buyer.Model.GET.BuyerProfile;
import com.aapnarshop.buyer.Model.SEND.Buyer_Update;
import com.aapnarshop.buyer.Navigation.AddFragmentHandler;
import com.aapnarshop.buyer.Navigation.BaseFragment;
import com.aapnarshop.buyer.R;
import com.aapnarshop.buyer.VIEW.ACTIVITY.IMAGE_CROP;
import com.aapnarshop.buyer.VIEWMODEL.BuyerProfileViewModel;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends BaseFragment {
    private View view;
    private BuyerProfileViewModel buyerProfileViewModel;
    private Utility utility;
    private EditText profileMobile,profileName,profileEmail;
    private Context context;
    private ProfileAddressRecyclerAdapter profileAddressRecyclerAdapter;
    private RecyclerView recyclerView;
    private ImageView profileImageEdit;
    private Button profileUpdateBtn,profileSettingsBtn;
    private String buyerUniqueId;

    public ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true); //For Showing Menu items
        buyerProfileViewModel = new ViewModelProvider(this).get(BuyerProfileViewModel.class);
        utility = new Utility(getActivity());
        buyerProfileViewModel.getBuyerProfileUpdateResponse().observe(this, new Observer<API_RESPONSE>() {
            @Override
            public void onChanged(API_RESPONSE api_response) {
                if (api_response.getCode() == 200) {
                    Log.d("profile", "buyer profile updated");
                    utility.showDialog(getString(R.string.profile_update_confirm_text));
                    buyerProfileViewModel.getBuyerProfileLiveData();
                }
            }
        });
    }

    @Override
    protected String getTitle() {
        return "Profile_Fragment";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile, container, false);
        initView();
        setupToolbar();

        try {
            requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

            initRecyclerView();
            callBuyerProfile();

            profileImageEdit.setOnClickListener(profileImageClickListener);
            profileUpdateBtn.setOnClickListener(profileUpdateClickListener);
            profileSettingsBtn.setOnClickListener(profileSettingsClickListener);
        } catch (Exception e){
            e.printStackTrace();
        }

        return view;
    }

    private void initView()
    {
        context=getActivity();
        profileName = view.findViewById(R.id.profile_name);
        profileMobile = view.findViewById(R.id.profile_mobile);
        profileEmail = view.findViewById(R.id.profile_email);
        profileImageEdit = view.findViewById(R.id.profile_image_edit);
        profileUpdateBtn = view.findViewById(R.id.profile_update_button);
        profileSettingsBtn = view.findViewById(R.id.profile_settings_button);
    }

    private void initRecyclerView()
    {
        recyclerView = view.findViewById(R.id.profile_recycler_view);
        // Add GridLayout for 2 items in a row Recycler View
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        // Add Some behavioral properties to RecyclerView (Optional)
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
    }
    //Move to Settings Fragment
    private View.OnClickListener profileSettingsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          /*  AppCompatActivity activity = (AppCompatActivity) getContext();
           // FragmentManager fragmentManager = Objects.requireNonNull(activity).getSupportFragmentManager();*/
            AddFragmentHandler fragmentHandler = new AddFragmentHandler(getParentFragmentManager());
            SettingsFragment sf = new SettingsFragment();
            fragmentHandler.add(sf.newInstance());
        }
    };

    //Update Button of profile
    private View.OnClickListener profileUpdateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            profileUpdateDialog();
        }
    };
    //Add image button of profile
    private View.OnClickListener profileImageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.profile_image_edit) {
                try {
                    ImagePicker.with(ProfileFragment.this)                         //  Initialize ImagePicker with activity or fragment context
                            .setToolbarColor("#fec107")         //  Toolbar color
                            .setStatusBarColor("#000000")       //  StatusBar color (works with SDK >= 21  )
                            .setToolbarTextColor("#000000")     //  Toolbar text color (Title and Done button)
                            .setToolbarIconColor("#000000")     //  Toolbar icon color (Back and Camera button)
                            .setProgressBarColor("#ffb6c1")     //  ProgressBar color
                            .setBackgroundColor("#212121")      //  Background color
                            .setCameraOnly(false)               //  Camera mode
                            .setMultipleMode(false)              //  Select multiple images or single image
                            .setFolderMode(false)                //  Folder mode
                            .setShowCamera(true)                //  Show camera button
                            .setFolderTitle(getResources().getString(R.string.app_name))           //  Folder title (works with FolderMode = true)
                            .setImageTitle(getResources().getString(R.string.add_image_string))         //  Image title (works with FolderMode = false)
                            .setDoneTitle("Done")               //  Done button title
                            .setLimitMessage("You have reached selection limit")    // Selection limit message
                            .setMaxSize(1)                     //  Max images can be selected
                            .setAlwaysShowDoneButton(true)      //  Set always show done button in multiple mode
                            .setRequestCode(Config.RC_PICK_IMAGES)                //  Set request code, default Config.RC_PICK_IMAGES
                            .start();
                } catch (Exception e) {
                    Log.d("Error Line Number", Log.getStackTraceString(e));
                }
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            // do your logic here...
            /*for (Image image : images) {
                utility.logger(image.getName());

            }*/
            if (images != null) {
                if (images.get(0) != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setClass(context, IMAGE_CROP.class);
                    intent.putExtra("image", Uri.fromFile(new File(images.get(0).getPath())).toString());
                    startActivityForResult(intent, KeyWord.CROP_PERMISSION);

                    Log.d("profile",images.get(0).getPath());
                    //Send_User_Image(userProfile.getUserId(), new File(images.get(0).getPath()));
                } else {
                    utility.showDialog(context.getResources().getString(R.string.remove_text));
                }
            }
            utility.logger("Image Work");
        } else if (requestCode == KeyWord.CROP_PERMISSION && resultCode == RESULT_OK) {
            try {
                Uri cropUri = Uri.parse(data.getStringExtra("data"));
                Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(cropUri));
                File file = new File(new File(context.getCacheDir().getAbsolutePath()), "temp.jpg");
                if (file.exists())
                    file.delete();
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception ex) {
                    utility.logger(ex.toString());
                }
                if (file.exists()) {
                    Log.d("profile","Idhar");
                    Log.d("profile",file.getName());
                 //   Send_User_Image(userProfile.getUserId(), file);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            utility.logger("Image CROP");
        }

        super.onActivityResult(requestCode, resultCode, data);  // You MUST have this line to be here
        // so ImagePicker can work with fragment mode
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

        textToolHeader.setText(R.string.text_navbar_profile);
        toolbarImgBack.setVisibility(View.GONE);
        toolbarImgDrawerLogo.setVisibility(View.VISIBLE);
    }

    private void profileUpdateDialog()
    {
        try {
            Display display = requireActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            HashMap<String, Integer> screen = utility.getScreenRes();
            int width = screen.get(KeyWord.SCREEN_WIDTH);
            int mywidth = (width / 10) * 7;
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setContentView(R.layout.dialog_profile_update);
            TextInputEditText update_email = dialog.findViewById(R.id.buyer_update_email);
            TextInputEditText update_name = dialog.findViewById(R.id.buyer_update_name);
            TextInputEditText update_phone = dialog.findViewById(R.id.buyer_update_phone);

            Button update_yes = dialog.findViewById(R.id.btn_update_yes);
            Button update_no = dialog.findViewById(R.id.btn_update_no);

            LinearLayout ll = dialog.findViewById(R.id.dialog_profile_update_size);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ll.getLayoutParams();
            params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            params.width = mywidth;
            ll.setLayoutParams(params);

            update_name.setText(profileName.getEditableText());
            update_email.setText(profileEmail.getEditableText());
            update_phone.setText(profileMobile.getEditableText());

            update_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                            if(utility.validateMsisdn(update_phone.getEditableText().toString())) {
                                if(utility.isEmailValid(update_email.getEditableText().toString())) {
                                    dialog.dismiss();
                                    Buyer_Update buyerUpdateInfo = new Buyer_Update(update_name.getEditableText().toString(),update_email.getEditableText().toString(),update_phone.getEditableText().toString());
                                    Log.d("profile",buyerUpdateInfo.toString());
                                    buyerProfileViewModel.sendBuyerProfileUpdate(buyerUpdateInfo);
                                    Activity activity = getActivity();
                                    if(activity != null && isAdded()) {
                                        NavigationView navigationView = activity.findViewById(R.id.nav_view);
                                        View navigationHeader = navigationView.getHeaderView(0);
                                        //Navigation Bar Header Buyer Info Updated
                                        TextView navbarHeaderProfileName = navigationHeader.findViewById(R.id.header_profile_name);
                                        TextView navbarHeaderProfileNumber = navigationHeader.findViewById(R.id.header_profile_phone_number);
                                        navbarHeaderProfileName.setText(buyerUpdateInfo.getFullName());
                                        if (!TextUtils.isEmpty(buyerUpdateInfo.getPhone())) {
                                            navbarHeaderProfileNumber.setText(buyerUpdateInfo.getPhone());
                                        } else {
                                            navbarHeaderProfileNumber.setText(buyerUpdateInfo.getEmail());
                                        }
                                        //Shared Preference Data Updated For Buyer Profile
                                        BuyerProfile buyerUpdateSharedPref = new BuyerProfile(buyerUniqueId,buyerUpdateInfo.getFullName(),buyerUpdateInfo.getEmail(),buyerUpdateInfo.getPhone(),"");
                                        saveSharedPreference(buyerUpdateSharedPref);
                                    }
                                }else{
                                    update_email.setError(getString(R.string.validation_email_text));
                                }
                            }else{
                                update_phone.setError(getString(R.string.validation_phone_text));
                            }
                }
            });

            update_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.setCancelable(true);
            dialog.show();
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }
    //Menu click detect
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.cart_menu, menu);
        MenuItem awesomeMenuItem = menu.findItem(R.id.action_addcart);
        View awesomeActionView = awesomeMenuItem.getActionView();
        awesomeActionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("profile", "Cart menu clicked from profile");
                onOptionsItemSelected(awesomeMenuItem);
            }
        });
        super.onCreateOptionsMenu(menu, inflater);

    }
    //Menu click action
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_addcart) {
            AddFragmentHandler fragmentHandler = new AddFragmentHandler(getParentFragmentManager());
            CartFragment cf = new CartFragment();
            fragmentHandler.add(cf.newInstance());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void callBuyerProfile()
    {
        try {
            Activity activity = getActivity();
            if (activity != null && isAdded()) {
                if (utility.isNetworkAvailable()) {
                    //Gets Buyer Profile Data
                    buyerProfileViewModel.getBuyerProfileLiveData().observe(getActivity(), new Observer<BuyerProfile>() {
                        @Override
                        public void onChanged(BuyerProfile buyerProfile) {
                            Log.d("profile", "Buyers profile data received");
                            profileName.setText(buyerProfile.getFullName());
                            profileMobile.setText(buyerProfile.getPhone());
                            profileEmail.setText(buyerProfile.getEmail());
                            buyerUniqueId = buyerProfile.getId();
                            // Glide.with(requireActivity()).load("https://cdn.maikoapp.com/3d4b/4quqa/150.jpg").apply(RequestOptions.circleCropTransform()).into(profilePic);
                        }
                    });
                    //Gets Buyer All Address List
                    buyerProfileViewModel.getBuyerAddressList().observe(getActivity(), new Observer<List<BuyerAddress>>() {
                        @Override
                        public void onChanged(List<BuyerAddress> buyerAddresses) {
                            Log.d("profile", "Buyers address list received");
                            profileAddressRecyclerAdapter = new ProfileAddressRecyclerAdapter(getActivity(), buyerProfileViewModel, buyerAddresses,utility);
                            recyclerView.setAdapter(profileAddressRecyclerAdapter);
                        }
                    });
                    //Observes Buyer Primary Address Change
                    buyerProfileViewModel.getBuyerPrimaryAddressResponseMutableLiveData().observe(getActivity(), new Observer<API_RESPONSE>() {
                        @Override
                        public void onChanged(API_RESPONSE api_response) {
                            if (api_response.getCode() == 200) {
                                Log.d("profile", "primary address updated");
                                buyerProfileViewModel.getBuyerAddressList();
                            }
                            else {
                                utility.showDialog(getString(R.string.operation_failed_text));
                            }
                        }
                    });
                    //Deletes Buyer Address
                    buyerProfileViewModel.getDeleteReceiverAddressResponse().observe(getActivity(), new Observer<API_RESPONSE>() {
                        @Override
                        public void onChanged(API_RESPONSE api_response) {
                            if (api_response.getCode() == 200) {
                                Log.d("profile", "address deleted");
                                buyerProfileViewModel.getBuyerAddressList();
                            }
                            else {
                                utility.showDialog(getString(R.string.operation_failed_text));
                            }
                        }
                    });

                } else {
                    utility.showDialog(getResources().getString(R.string.no_internet_string));
                }
            }
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
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
