package com.aapnarshop.buyer.OrderFragments;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aapnarshop.buyer.Adapters.AllOrderAdapter;
import com.aapnarshop.buyer.Library.CalendarView;
import com.aapnarshop.buyer.Model.ProductModel;
import com.aapnarshop.buyer.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;


public class DateFragment extends Fragment {
    private TextView fromDateOrderDay, fromDateOrderDate, fromDateOrderMonthYear, toDateOrderDay, toDateOrderDate, toDateOrderMonthYear;
    private SimpleDateFormat monthFormat, dayFormat, yearFormat, dayOfWeekFormat;

    private String month, year, day_date, day_of_week;
    private RecyclerView recyclerView;
    private List<ProductModel> productList;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public DateFragment() {
        // Required empty public constructor
    }

    public static DateFragment newInstance(String param1, String param2) {
        DateFragment fragment = new DateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_date_order_tab, container, false);

        LinearLayout cofd = view.findViewById(R.id.complete_order_from_date);
        LinearLayout cofd2 = view.findViewById(R.id.complete_order_from_date2);

        //FROM DATE
        fromDateOrderDay = view.findViewById(R.id.complete_order_day);
        fromDateOrderDate = view.findViewById(R.id.complete_order_date);
        fromDateOrderMonthYear = view.findViewById(R.id.complete_order_month_year);

        //TO DATE
        toDateOrderDay = view.findViewById(R.id.complete_order_day2);
        toDateOrderDate = view.findViewById(R.id.complete_order_date2);
        toDateOrderMonthYear = view.findViewById(R.id.complete_order_month_year2);

        dayFormat = new SimpleDateFormat("dd");
        //dateFormat=new SimpleDateFormat("d/MMMM/YYYY");
        yearFormat = new SimpleDateFormat("yyyy");
        monthFormat = new SimpleDateFormat("MMMM");
        dayOfWeekFormat = new SimpleDateFormat("EEEE");

        //OrderFromDate
        cofd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCalenderDialog(fromDateOrderDay, fromDateOrderDate, fromDateOrderMonthYear);
            }
        });

        //OrderToDate
        cofd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCalenderDialog(toDateOrderDay, toDateOrderDate, toDateOrderMonthYear);
            }
        });

        recyclerView=view.findViewById(R.id.order_recycler_view_date);
        initData();
        setRecyclerView();

        return view;
    }

    @Override
    public void onDestroyView() {
        recyclerView.setAdapter(null);
        super.onDestroyView();
    }

    private void openCalenderDialog(TextView completeOrderDay, TextView completeOrderDate, TextView completeOrderMonthYear) {
        final Dialog dialog = new Dialog(requireActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.calendar_view);


        HashSet<Date> events = new HashSet<>();
        events.add(new Date());
        CalendarView cv = dialog.findViewById(R.id.calendar_view1);
        ImageView imageView = dialog.findViewById(R.id.control_calender_cancel);

        cv.updateCalendar(events);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        // assign event handler

        cv.setEventHandler(date -> {
            // show returned day

            day_of_week = dayOfWeekFormat.format(date);
            completeOrderDay.setText(day_of_week);
            day_date = dayFormat.format(date);
            completeOrderDate.setText(day_date);
            month = monthFormat.format(date);
            year = yearFormat.format(date);
            Log.d("DATE2", day_of_week + "  " + day_date + "   " + month + "   " + year);
            switch (day_date) {
                case "01":
                    completeOrderMonthYear.setText(String.format("ST %s %s", month, year));
                    break;
                case "02":
                    completeOrderMonthYear.setText(String.format("ND %s %s", month, year));
                    break;
                case "03":
                    completeOrderMonthYear.setText(String.format("RD %s %s", month, year));
                    break;
                default:
                    completeOrderMonthYear.setText(String.format("TH %s %s", month, year));
                    break;
            }

            dialog.dismiss();
        });

        dialog.show();
    }

    private void setRecyclerView()
    {
        AllOrderAdapter allOrderAdapter= new AllOrderAdapter(getActivity(),productList);
        recyclerView.setAdapter(allOrderAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
    }

    private void initData()
    {
        productList=new ArrayList<>();
        productList.add(new ProductModel("145968556", "400","30","20"));
        productList.add(new ProductModel("145968556", "400","30","20"));
        productList.add(new ProductModel("145968556", "400","30","20"));
        productList.add(new ProductModel("145968556", "400","30","20"));
        productList.add(new ProductModel("145968556", "400","30","20"));
        productList.add(new ProductModel("145968556", "400","30","20"));
        productList.add(new ProductModel("145968556", "400","30","20"));
        productList.add(new ProductModel("145968556", "400","30","20"));
        productList.add(new ProductModel("145968556", "400","30","20"));
        productList.add(new ProductModel("145968556", "400","30","20"));
        productList.add(new ProductModel("145968556", "400","30","20"));
    }


}

