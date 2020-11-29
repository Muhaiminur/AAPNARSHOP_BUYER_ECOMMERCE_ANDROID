package com.aapnarshop.buyer.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.aapnarshop.buyer.Adapters.CustomSpinnerAdapter;
import com.aapnarshop.buyer.Http.API_RESPONSE;
import com.aapnarshop.buyer.Library.Utility;
import com.aapnarshop.buyer.Model.GET.Area;
import com.aapnarshop.buyer.Model.GET.BuyerAddress;
import com.aapnarshop.buyer.Model.GET.City;
import com.aapnarshop.buyer.Model.GET.District;
import com.aapnarshop.buyer.Navigation.AddFragmentHandler;
import com.aapnarshop.buyer.Navigation.BaseFragment;
import com.aapnarshop.buyer.R;
import com.aapnarshop.buyer.VIEWMODEL.AddAddressViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class AddAddressFragment extends BaseFragment {
    private View view;
    private final List<City> cityList = new ArrayList<>();
    private final List<Area> areaList = new ArrayList<>();
    private Utility utility;
    private String areaId;
    private Spinner areaSpin,citySpin;
    private EditText addReceiverName,addReceiverNumber,addReceiverAddress;
    private TextView addReceiverAreaError;
    private SwitchCompat primaryAddressSwitch;
    private AddAddressViewModel addAddressViewModel;
    private String primaryAddressSwitchValue="no";
    private Button addAddressSubmitBtn;
    private BuyerAddress editAddressObj;


    public static AddAddressFragment newInstance() {
        return new AddAddressFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        addAddressViewModel = new ViewModelProvider(this).get(AddAddressViewModel.class);
        utility = new Utility(getActivity());

        addAddressViewModel.getAddAddressResponse().observe(this, new Observer<API_RESPONSE>() {
            @Override
            public void onChanged(API_RESPONSE api_response) {
                if (api_response.getCode() == 200) {
                    Log.d("Primary1","Yo");
                    utility.showDialog(getString(R.string.address_add_successful_text));
                    getParentFragmentManager().popBackStack();
                } else {
                    utility.showDialog(api_response.getData().getAsString());
                }
            }
        });

        addAddressViewModel.getUpdateAddressResponse().observe(this, new Observer<API_RESPONSE>() {
            @Override
            public void onChanged(API_RESPONSE api_response) {
                if (api_response.getCode() == 200) {
                    utility.showDialog(getString(R.string.address_update_successful_text));
                    getParentFragmentManager().popBackStack();
                } else {
                    utility.showDialog(api_response.getData().getAsString());
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.add_address, container, false);

        initView();
        setupToolbar();

        primaryAddressSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    primaryAddressSwitchValue = "yes";
                } else {
                    primaryAddressSwitchValue = "no";
                }
            }
        });

        Bundle bundle = getArguments();

        if(bundle != null){
            editAddressObj= (BuyerAddress) bundle.getSerializable("editAddressObj");
        }

        if(editAddressObj == null) {
            //ADD ADDRESS
            setUpAddAddressPage();
        }
        else {
            //UPDATE ADDRESS
            getEditAddressInfo(editAddressObj);
        }

        return view;

    }

    @Override
    protected String getTitle() {
        return "Add_Address_Fragment";
    }

    private void initView()
    {
        addReceiverName = view.findViewById(R.id.add_address_receiver_name);
        addReceiverNumber = view.findViewById(R.id.add_address_receiver_mobile);
        areaSpin = view.findViewById(R.id.add_address_area_selection);
        citySpin = view.findViewById(R.id.add_address_city_selection);
        addReceiverAddress = view.findViewById(R.id.add_address);
        primaryAddressSwitch = view.findViewById(R.id.add_address_primary_address_switch);
        addReceiverAreaError = view.findViewById(R.id.add_address_area_error);
        addAddressSubmitBtn = view.findViewById(R.id.add_address_save_button);
    }

    //Opens Add address Page
    private void setUpAddAddressPage()
    {
        Activity activity = getActivity();
        if (activity != null && isAdded()) {
            if (utility.isNetworkAvailable()) {
                District Dhaka = new District("2", "Dhaka");
                observeCityData(Dhaka);
                cityList.add(new City("City"));
                initCityAdapter(cityList);
                areaList.add(new Area("Area"));
                initAreaAdapter(areaList);

                setUpCitySpinner();
                setUpAreaSpinner();
                addAddressSubmitBtn.setText(R.string.profile_save_button);
                addAddressSubmitBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ObserveAddAddress();
                    }
                });
            }
            else{
                utility.showDialog(getResources().getString(R.string.no_internet_string));
            }
        }
    }

    //Getting City List
    private void observeCityData(District district) {

        addAddressViewModel.getCityList(district).observe(getViewLifecycleOwner(), cities -> {
            if(getViewLifecycleOwner().getLifecycle().getCurrentState()== Lifecycle.State.RESUMED) {
                cityList.clear();
                cityList.add(new City("City"));
                cityList.addAll(cities);
                Log.d("AREA2","method "+cityList.size());
                initCityAdapter(cityList);
                if(editAddressObj!=null){
                    citySpin.setSelection(Integer.parseInt(editAddressObj.getCityId()), false);
                }

            }

        });
    }

    //Getting Area List
    private void observeAreaData(City city) {

         addAddressViewModel.getAreaList(city).observe(getViewLifecycleOwner(), areas -> {
            if(getViewLifecycleOwner().getLifecycle().getCurrentState()== Lifecycle.State.RESUMED) {
                areaList.clear();
                areaList.add(new Area("Area"));
                areaList.addAll(areas);
                 initAreaAdapter(areaList);
                List<String> AreaListString = new ArrayList<String>();
                for (Area member : areaList){
                    AreaListString.add(member.getAreaName());
                }
                if(editAddressObj!=null){
                   // areaSpin.setSelection(Integer.parseInt(editAddressObj.getAreaId()), false);
                    areaSpin.setSelection(AreaListString.indexOf(editAddressObj.getAreaTitle()));
                }
            }

        });
    }

    //Observe Area on basis of city
    private void setUpCitySpinner()
    {
        citySpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    areaList.clear();
                    areaList.add(new Area("Area"));
                    initAreaAdapter(areaList);
                } else {
                    String cityId = cityList.get(position).getCityId();
                    City city = new City();
                    city.setCityId(cityId);
                    observeAreaData(city);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    // Observe selected area
    private void setUpAreaSpinner()
    {
        areaSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    areaId = "";
                } else {
                    addReceiverAreaError.setVisibility(View.GONE);
                    areaId = areaList.get(position).getAreaId();
                    Log.d("Area",areaId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setupToolbar()
    {
        Toolbar toolbarDashboard = requireActivity().findViewById(R.id.toolbarDashboard);
        Toolbar toolbar_store_homepage= requireActivity().findViewById(R.id.toolbarStoreHomepage);
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbarNotification);
        ((AppCompatActivity)requireActivity()).setSupportActionBar(toolbar);

        toolbarDashboard.setVisibility(View.GONE);
        toolbar.setVisibility(View.VISIBLE);
        toolbar_store_homepage.setVisibility(View.GONE);

        TextView textToolHeader = toolbar.findViewById(R.id.toolbarModifiedTitle);
        ImageView toolbarImgBack = toolbar.findViewById(R.id.back_arrow);
        ImageView toolbarImgDrawerLogo = toolbar.findViewById(R.id.drawer_logo_notification_toolbar);

        textToolHeader.setText(R.string.address_text);
        toolbarImgBack.setVisibility(View.VISIBLE);
        toolbarImgDrawerLogo.setVisibility(View.GONE);
        toolbarImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getParentFragmentManager();
                if (Objects.requireNonNull(fm).getBackStackEntryCount() > 0) {
                    Log.i("AddAddress", "popping backstack");
                    fm.popBackStack();
                } else {
                    Log.i("AddAddress", "nothing on backstack, calling super");
                }
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d("AddAddress", "fragmentcreate1");
        menu.clear();
        inflater.inflate(R.menu.cart_menu, menu);
        MenuItem awesomeMenuItem = menu.findItem(R.id.action_addcart);
        View awesomeActionView = awesomeMenuItem.getActionView();
        awesomeActionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("AddAddress", "fragmentcreate2tr");
                onOptionsItemSelected(awesomeMenuItem);

                Log.d("AddAddress", "fragmentcreate21");
            }
        });
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d("AddAddress","yes");
        if (item.getItemId() == R.id.action_addcart) {
            AddFragmentHandler fragmentHandler = new AddFragmentHandler(getParentFragmentManager());
            CartFragment cf = new CartFragment();
            fragmentHandler.add(cf.newInstance());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Adapter for area
    private void initAreaAdapter(List<Area> areaList) {
        try {
            areaSpin.setAdapter(new CustomSpinnerAdapter<Area>(requireActivity(), android.R.layout.simple_spinner_item, areaList) {
                @Override
                public View getCustomView(int position, View view, ViewGroup parent) {

                    Area model = getItem(position);
                    View spinnerRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_spinner_layout, parent, false);

                    TextView label = spinnerRow.findViewById(R.id.address_spinner);

                    if (model != null) {
                        label.setText(model.getAreaName());
                    }
                    if (position == 0) {
                        label.setTextColor(requireActivity().getResources().getColor(R.color.black));
                    }

                    return spinnerRow;
                }
            });
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    //Adapter for city
    private void initCityAdapter(List<City> cityList) {
        try {
            citySpin.setAdapter(new CustomSpinnerAdapter<City>(getActivity(), android.R.layout.simple_spinner_item, cityList) {
                @Override
                public View getCustomView(int position, View view, ViewGroup parent) {
                    Log.d("AREA2","adapt"+ cityList.size() +" pos "+position);
                    City model = getItem(position);
                    View spinnerRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_spinner_layout, parent, false);

                    TextView label = spinnerRow.findViewById(R.id.address_spinner);

                    if (model != null) {
                        label.setText(model.getCityName());
                    }
                    if (position == 0) {
                        label.setTextColor(requireActivity().getResources().getColor(R.color.black));
                    }

                    return spinnerRow;
                }
            });
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    private void ObserveAddAddress()
    {
        if(TextUtils.isEmpty(addReceiverName.getText().toString()) && TextUtils.isEmpty(addReceiverNumber.getText().toString()) && TextUtils.isEmpty(addReceiverAddress.getText().toString()) && TextUtils.isEmpty(areaId))
        {
            addReceiverName.setError("Enter Receiver Name");
            addReceiverNumber.setError("Enter Receiver Number");
            addReceiverAddress.setError("Enter Receiver Address");
            addReceiverAreaError.setVisibility(View.VISIBLE);
            addReceiverAreaError.setText(R.string.add_address_select_area_text);
        }
        else if(TextUtils.isEmpty(addReceiverName.getText().toString()))
        {
            addReceiverName.setError("Enter Receiver Name");
        }
        else if(TextUtils.isEmpty(addReceiverNumber.getText().toString()))
        {
            addReceiverNumber.setError("Enter Receiver Number");
        }
        else if(TextUtils.isEmpty(addReceiverAddress.getText().toString()))
        {
            addReceiverAddress.setError("Enter Receiver Address");
        }
        else if(TextUtils.isEmpty(areaId))
        {
            addReceiverAreaError.setVisibility(View.VISIBLE);
            addReceiverAreaError.setText(R.string.add_address_select_area_text);
        }
        else if(!utility.validateMsisdn(addReceiverNumber.getText().toString()))
        {
            addReceiverNumber.setError("Enter a valid Phone Number");
        }
        else
        {
            BuyerAddress addNewReceiverAddress = new BuyerAddress(areaId,addReceiverAddress.getText().toString(),addReceiverName.getText().toString(),addReceiverNumber.getText().toString(),primaryAddressSwitchValue);
           /* sendNewReceiverAddress(addNewReceiverAddress);*/
            addAddressViewModel.addReceiverAddress(addNewReceiverAddress);
        }
    }

    private void ObserveEditAddress()
    {
        if(TextUtils.isEmpty(addReceiverName.getText().toString()) && TextUtils.isEmpty(addReceiverNumber.getText().toString()) && TextUtils.isEmpty(addReceiverAddress.getText().toString()) && TextUtils.isEmpty(areaId))
        {
            addReceiverName.setError("Enter Receiver Name");
            addReceiverNumber.setError("Enter Receiver Number");
            addReceiverAddress.setError("Enter Receiver Address");
            addReceiverAreaError.setVisibility(View.VISIBLE);
            addReceiverAreaError.setText(R.string.add_address_select_area_text);
        }
        else if(TextUtils.isEmpty(addReceiverName.getText().toString()))
        {
            addReceiverName.setError("Enter Receiver Name");
        }
        else if(TextUtils.isEmpty(addReceiverNumber.getText().toString()))
        {
            addReceiverNumber.setError("Enter Receiver Number");
        }
        else if(TextUtils.isEmpty(addReceiverAddress.getText().toString()))
        {
            addReceiverAddress.setError("Enter Receiver Address");
        }
        else if(TextUtils.isEmpty(areaId))
        {
            addReceiverAreaError.setVisibility(View.VISIBLE);
            addReceiverAreaError.setText(R.string.add_address_select_area_text);
        }
        else if(!utility.validateMsisdn(addReceiverNumber.getText().toString()))
        {
            addReceiverNumber.setError("Enter a valid Phone Number");
        }
        else
        {
            BuyerAddress updateReceiverAddress = new BuyerAddress(editAddressObj.getAddressId(),areaId,addReceiverAddress.getText().toString(),addReceiverName.getText().toString(),addReceiverNumber.getText().toString(),primaryAddressSwitchValue);
            /*sendUpdatedReceiverAddress(updateReceiverAddress);*/
            addAddressViewModel.updateReceiverAddress(updateReceiverAddress);
        }
    }
// For Add
    /*private void sendNewReceiverAddress(BuyerAddress addNewReceiverAddress)
    {
        addAddressViewModel.addReceiverAddress(addNewReceiverAddress).observe(getViewLifecycleOwner(),api_response -> {
            if(getViewLifecycleOwner().getLifecycle().getCurrentState()== Lifecycle.State.RESUMED) {
            if (api_response.getCode() == 200) {
                Log.d("Primary1","Yo");
                utility.showDialog(getString(R.string.address_add_successful_text));
                getParentFragmentManager().popBackStack();
            } else {
                utility.showDialog(api_response.getData().getAsString());
            }
            }
        });
    }*/
// For Update
   /* private void sendUpdatedReceiverAddress(BuyerAddress updateReceiverAddress)
    {
        addAddressViewModel.updateReceiverAddress(updateReceiverAddress).observe(getViewLifecycleOwner(),api_response ->
        {
            if(getViewLifecycleOwner().getLifecycle().getCurrentState()== Lifecycle.State.RESUMED) {
                if (api_response.getCode() == 200) {
                    utility.showDialog(getString(R.string.address_update_successful_text));
                    getParentFragmentManager().popBackStack();
                } else {
                    utility.showDialog(api_response.getData().getAsString());
                }
            }
        });
    }*/
// Get Buyer INFO for update view
    private void getEditAddressInfo(BuyerAddress editAddressObj)
    {
        Activity activity = getActivity();
        if (activity != null && isAdded()) {
            if (utility.isNetworkAvailable()) {
                District Dhaka = new District("2", "Dhaka");
                observeCityData(Dhaka);
                Log.d("AREA2","upper "+cityList.size());

                City city = new City();
                city.setCityId(editAddressObj.getCityId());
                observeAreaData(city);

                addReceiverName.setText(editAddressObj.getReceiverName());
                addReceiverNumber.setText(utility.Mobile_Number_Read(editAddressObj.getReceiverPhone()));
                addReceiverAddress.setText(editAddressObj.getAddress());
                primaryAddressSwitch.setChecked(editAddressObj.getPrimary().equals("yes"));
                Log.d("AREA2","Lower "+cityList.size() + " area " + areaList.size());

                setUpCitySpinner();
                setUpAreaSpinner();

                addAddressSubmitBtn.setText(R.string.profile_apply_button);
                addAddressSubmitBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ObserveEditAddress();
                    }
                });

                }
            else{
                utility.showDialog(getResources().getString(R.string.no_internet_string));
            }
            }
    }

}
