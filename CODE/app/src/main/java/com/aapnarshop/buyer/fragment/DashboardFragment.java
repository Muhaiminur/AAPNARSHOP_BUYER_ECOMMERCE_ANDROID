package com.aapnarshop.buyer.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aapnarshop.buyer.Adapters.CustomSpinnerAdapter;
import com.aapnarshop.buyer.Adapters.DashboardRecyclerViewAdapter;
import com.aapnarshop.buyer.Model.GET.Area;
import com.aapnarshop.buyer.Model.GET.City;
import com.aapnarshop.buyer.Model.GET.District;
import com.aapnarshop.buyer.Navigation.BackButtonSupportFragment;
import com.aapnarshop.buyer.Navigation.BaseFragment;
import com.aapnarshop.buyer.R;
import com.aapnarshop.buyer.VIEWMODEL.AddAddressViewModel;

import java.util.ArrayList;
import java.util.List;


public class DashboardFragment extends BaseFragment implements BackButtonSupportFragment {
    private View view;

    private PopupWindow search_popupWindow;
    private boolean search_filter_toggle=false;

    private static final String[] paths = {"City", "item 2", "item 3"};
    private static final String[] pathArea = {"Area", "item 2", "item 3"};

    private static final String[] paths2 = {"All", "Grocery Shop", "Pharmacy","Restaurant","Open"};

    private final String[] images = {"https://images.unsplash.com/photo-1545269041-30215d6eef33?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1951&q=80","https://images.unsplash.com/photo-1545269041-30215d6eef33?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1951&q=80","https://images.unsplash.com/photo-1545269041-30215d6eef33?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1951&q=80","https://images.unsplash.com/photo-1545269041-30215d6eef33?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1951&q=80" };

    public  DashboardFragment newInstance() {
        return new DashboardFragment();
    }
    private boolean consumingBackPress = true;
    private Toast toast;
    private final List<City> cityList = new ArrayList<>();
    private final List<Area> areaList = new ArrayList<>();
    private String areaId;
    private Spinner areaSpin,citySpin;
    private AddAddressViewModel addAddressViewModel;
    private AppCompatSpinner search_filter_spinner;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        if(getActivity()!=null)
        {
            addAddressViewModel = new ViewModelProvider(getActivity()).get(AddAddressViewModel.class);
        }
        areaSpin = view.findViewById(R.id.spinnerArea);
        citySpin = view.findViewById(R.id.spinnerCity);
        ImageView areaSpinImg = view.findViewById(R.id.spinnerAreaImg);
        ImageView citySpinImg = view.findViewById(R.id.spinnerCityImg);
        District Dhaka = new District("2", "Dhaka");
        observeCityData(Dhaka);
        cityList.add(new City("City"));
        initCityAdapter(cityList);
        areaList.add(new Area("Area"));
        initAreaAdapter(areaList);

        setUpCitySpinner();
        setUpAreaSpinner();

        setupToolbar();
        setupSpinnerLeftRight();
        setupRecyclerView();
        setupSearchFilterSpinner();

        areaSpinImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                areaSpin.performClick();
            }
        });
        citySpinImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                citySpin.performClick();
            }
        });

        return view;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
                Log.d("AREA2","methodAREA "+areaList.size());
                initAreaAdapter(areaList);
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
                    areaId = areaList.get(position).getAreaId();
                    Log.d("Area",areaId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    //Adapter for area
    private void initAreaAdapter(List<Area> areaList) {
        try {
            areaSpin.setAdapter(new CustomSpinnerAdapter<Area>(requireActivity(), R.layout.spinner_item_layout_dashboard, areaList) {
                @Override
                public View getCustomView(int position, View view, ViewGroup parent) {

                    Area model = getItem(position);
                    View spinnerRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_spinner_item_layout, parent, false);

                    CheckedTextView label = spinnerRow.findViewById(R.id.textSpinnerDashboard);

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
            citySpin.setAdapter(new CustomSpinnerAdapter<City>(getActivity(), R.layout.spinner_item_layout_dashboard, cityList) {
                @Override
                public View getCustomView(int position, View view, ViewGroup parent) {
                    Log.d("AREA2","adapt"+ cityList.size() +" pos "+position);
                    City model = getItem(position);
                    View spinnerRow = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_spinner_item_layout, parent, false);

                    CheckedTextView label = spinnerRow.findViewById(R.id.textSpinnerDashboard);
                    label.setBackgroundResource(R.drawable.dashboard_spinner_background);

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

    //Search filter spinner for Nearby shops pop-up window
    private void setupSearchFilterSpinner()
    {
        try {
        ViewGroup root = (ViewGroup) requireActivity().getWindow().getDecorView().getRootView();
        TextView search_popup_img = view.findViewById(R.id.search_popup_image);
        LinearLayout nearby_shop_layout = view.findViewById(R.id.nearby_shop_layout);
        //search_popup_img.setInputType(InputType.TYPE_NULL);

        LayoutInflater layoutInflater = (LayoutInflater) requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View customView = layoutInflater.inflate(R.layout.search_filter_popup_window, null);

        AppCompatTextView ratingBtn,offerBtn,applyBtn;
        ratingBtn = customView.findViewById(R.id.search_filter_rating);
        offerBtn = customView.findViewById(R.id.search_filter_offer);
        applyBtn = customView.findViewById(R.id.search_filter_apply);

        ratingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               ratingBtn.setBackgroundResource(R.drawable.rounded_corner);
                offerBtn.setBackgroundResource(R.drawable.rounded_corners_rating_button);
            }
        });
        offerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                offerBtn.setBackgroundResource(R.drawable.rounded_corner);
                ratingBtn.setBackgroundResource(R.drawable.rounded_corners_rating_button);
            }
        });
        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(search_popupWindow!=null) {
                    search_popupWindow.dismiss();
                    search_filter_toggle=false;
                }
            }
        });

            search_popupWindow = new PopupWindow(customView, LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
            search_popupWindow.setOutsideTouchable(true);
            search_popupWindow.setFocusable(true);
            search_popupWindow.setBackgroundDrawable(new BitmapDrawable(requireContext().getResources()));
            //search_popup_img.setFocusable(false);

            nearby_shop_layout.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
                @Override
                public void onClick(View v) {
                    if (!search_filter_toggle) {
                        search_filter_toggle = true;
                        //display the popup window
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                Log.d("DFPopup", "On");
                                if (!requireActivity().isFinishing()) {
                                    search_popupWindow.showAsDropDown(search_popup_img, -5, -20);
                                    //Category Search Filter Spinner inside popup window
                                    search_filter_spinner = customView.findViewById(R.id.search_filter_spinner);
                                    ArrayAdapter<String> adapter2 = new ArrayAdapter<>(requireActivity(),
                                            R.layout.spinner_item_layout_search_filter, paths2);

                                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    search_filter_spinner.setAdapter(adapter2);
                                }
                            }
                        }, 200);
                        applyDim(root, 0.3f);

                    } else {
                        Log.d("DFPopup", "Off");
                        //close the popup window
                        if (search_popupWindow != null && search_popupWindow.isShowing()) {
                            search_popupWindow.dismiss();
                            clearDim(root);
                        }
                        search_filter_toggle = false;
                    }
                }
            });

            search_popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
                @Override
                public void onDismiss() {
                    clearDim(root);
                    search_filter_toggle = false;
                }
            });
        }catch (Exception bte) {
            Log.e("Memory exceptions"," "+ bte);
            AppCompatSpinner spinner = new AppCompatSpinner(requireContext(),null,R.id.search_filter_spinner,Spinner.MODE_DIALOG);
            ArrayAdapter<String> adapter2 = new ArrayAdapter<>(requireActivity(),
                    R.layout.spinner_item_layout_search_filter, paths2);

            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            search_filter_spinner.setAdapter(adapter2);
        }

    }
    //Applies dim on the background when search filter shows
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void applyDim(@NonNull ViewGroup parent, float dimAmount){
        Drawable dim = new ColorDrawable(Color.BLACK);
        dim.setBounds(0, 0, parent.getWidth(), parent.getHeight());
        dim.setAlpha((int) (255 * dimAmount));

        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.add(dim);
    }
    //Clears background dim
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void clearDim(@NonNull ViewGroup parent) {
        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.clear();
    }
    //Showing dashboard items
    private void setupRecyclerView()
    {
        RecyclerView recyclerView = view.findViewById(R.id.mRecyclerViewDashboard);
        // Add LayoutManager for Recycler View
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        // Add Some behavioral properties to RecyclerView (Optional)
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        // Create Adapter for RecyclerView
        DashboardRecyclerViewAdapter rvAdapter = new DashboardRecyclerViewAdapter(getActivity(), images);
        recyclerView.setAdapter(rvAdapter);
    }

    //City & Area spinner for dashboard
    private void setupSpinnerLeftRight()
    {
        /*Spinner spinnerLeft =  view.findViewById(R.id.spinnerCity);
        Spinner spinnerRight = view.findViewById(R.id.spinnerArea);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireActivity(),
                R.layout.spinner_item_layout_dashboard, paths);
        ArrayAdapter<String> adapterArea = new ArrayAdapter<>(requireActivity(),
                R.layout.spinner_item_layout_dashboard, pathArea);

        adapter.setDropDownViewResource(R.layout.dashboard_spinner_item_layout);
        adapterArea.setDropDownViewResource(R.layout.dashboard_spinner_item_layout);
        spinnerLeft.setAdapter(adapter);
        spinnerRight.setAdapter(adapterArea);*/
    }

    private void setupToolbar()
    {
        Toolbar toolbarDashboard =  requireActivity().findViewById(R.id.toolbarDashboard);
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbarNotification);
        Toolbar toolbar_store_homepage = requireActivity().findViewById(R.id.toolbarStoreHomepage);
        ((AppCompatActivity)requireActivity()).setSupportActionBar(toolbarDashboard);

        toolbar.setVisibility(View.GONE);
        toolbar_store_homepage.setVisibility(View.GONE);
        toolbarDashboard.setVisibility(View.VISIBLE);

        TextView textToolHeader = toolbarDashboard.findViewById(R.id.toolbarTitle);
        ImageView toolbarImgBack = toolbarDashboard.findViewById(R.id.back_arrow_dashboard);
        ImageView toolbarImgDrawerLogo = toolbarDashboard.findViewById(R.id.drawer_logo);

        textToolHeader.setText(R.string.text_navbar_home);
        toolbarImgBack.setVisibility(View.GONE);
        toolbarImgDrawerLogo.setVisibility(View.VISIBLE);
    }

    //BackPressedOnDashboard
    @Override
    public boolean onBackPressed() {
        //return true when handled by Fragment and when false? By activity
        if (consumingBackPress) {
            toast = Toast.makeText(getActivity(), "Press back once more to quit the application", Toast.LENGTH_LONG);
            toast.show();
            consumingBackPress = false;
            return true; //consumed
        }
        toast.cancel();
        /*FragmentManager manager = getParentFragmentManager();
        while (manager.getBackStackEntryCount() > 0){
            if(manager.getBackStackEntryCount()==1) {
                if(isAdded() && getActivity()!=null) {
                    getActivity().finish();
                }
            }else{
                manager.popBackStackImmediate();
            }
        }*/

        return false; //delegated
    }

    @Override
    protected String getTitle() {
        return "Dashboard_Fragment";
    }
}
