package com.aapnarshop.buyer.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.aapnarshop.buyer.Adapters.AllOrderViewPagerAdapter;
import com.aapnarshop.buyer.Navigation.BaseFragment;
import com.aapnarshop.buyer.OrderFragments.AllFragment;
import com.aapnarshop.buyer.OrderFragments.DateFragment;
import com.aapnarshop.buyer.OrderFragments.LastMonthFragment;
import com.aapnarshop.buyer.OrderFragments.LastWeekFragment;
import com.aapnarshop.buyer.OrderFragments.LastYearFragment;
import com.aapnarshop.buyer.R;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.Objects;

public class OrderFragment extends BaseFragment {


    private SimpleDateFormat monthFormat, dayFormat, dateFormat, yearFormat;
    private String month, year, day_date;

    public OrderFragment newInstance() {
        return new OrderFragment();
    }

    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected String getTitle() {
        return "Order_Fragment";
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        setupToolbar();


        try {
            ViewPager viewPager = view.findViewById(R.id.view_pager_order);
            TabLayout tabLayout = view.findViewById(R.id.tabs_order);


            AllOrderViewPagerAdapter adapter = new AllOrderViewPagerAdapter(this.getChildFragmentManager());
            adapter.addFragment(new AllFragment(), "ALL");
            adapter.addFragment(new LastWeekFragment(), "LAST WEEK");
            adapter.addFragment(new LastMonthFragment(), "LAST MONTH");
            adapter.addFragment(new LastYearFragment(), "LAST YEAR");
            adapter.addFragment(new DateFragment(), "DATE");

            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);

            /*monthFormat = new SimpleDateFormat("MMM");
            yearFormat = new SimpleDateFormat("yyyy");
            dayFormat = new SimpleDateFormat("EEEE");
            dateFormat = new SimpleDateFormat("dd");


            // get 7 days ago date
            getSevenDaysAgoDate();

            getTodaysDate();

            clickEvent();*/
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }

        return view;
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

        textToolHeader.setText(R.string.text_navbar_order);
        toolbarImgBack.setVisibility(View.VISIBLE);
        toolbarImgDrawerLogo.setVisibility(View.GONE);
        toolbarImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getParentFragmentManager();
                if (Objects.requireNonNull(fm).getBackStackEntryCount() > 0) {
                    Log.i("cart", "popping backstackORD");
                    fm.popBackStack();
                } else {
                    Log.i("cart", "nothing on backstack, calling super ORD");
                }
            }
        });
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem menu1 = menu.findItem(R.menu.cart_menu);
        menu1.setVisible(false);
    }

    /*private void getTodaysDate() {
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();

        month = monthFormat.format(today);
        year = yearFormat.format(today);
        binding.completeOrderDay2.setText(dayFormat.format(today));
        day_date = dateFormat.format(today);
        binding.completeOrderDate2.setText(day_date);
        switch (day_date) {
            case "01":
                binding.completeOrderMonthYear2.setText(String.format("ST %s %s", month, year));
                break;
            case "02":
                binding.completeOrderMonthYear2.setText(String.format("ND %s %s", month, year));
                break;
            case "03":
                binding.completeOrderMonthYear2.setText(String.format("RD %s %s", month, year));
                break;
            default:
                binding.completeOrderMonthYear2.setText(String.format("TH %s %s", month, year));
                break;
        }
    }

    private void getSevenDaysAgoDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, -6);
        Date sevenDaysBeforeDate = cal.getTime();

        month = monthFormat.format(sevenDaysBeforeDate);
        year = yearFormat.format(sevenDaysBeforeDate);
        binding.completeOrderDay.setText(dayFormat.format(sevenDaysBeforeDate));
        day_date = dateFormat.format(sevenDaysBeforeDate);
        binding.completeOrderDate.setText(day_date);
        switch (day_date) {
            case "01":
                binding.completeOrderMonthYear.setText(String.format("ST %s %s", month, year));
                break;
            case "02":
                binding.completeOrderMonthYear.setText(String.format("ND %s %s", month, year));
                break;
            case "03":
                binding.completeOrderMonthYear.setText(String.format("RD %s %s", month, year));
                break;
            default:
                binding.completeOrderMonthYear.setText(String.format("TH %s %s", month, year));
                break;
        }
    }

    private void clickEvent() {
        binding.completeOrderFromDate.setOnClickListener(v -> {

            openCalenderDialog(binding.completeOrderDay, binding.completeOrderDate, binding.completeOrderMonthYear);

        });

        binding.completeOrderFromDate2.setOnClickListener(v -> {

            openCalenderDialog(binding.completeOrderDay2, binding.completeOrderDate2, binding.completeOrderMonthYear2);

        });
    }*/

}
