package com.aapnarshop.buyer.OrderFragments;

import android.app.AlertDialog;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aapnarshop.buyer.Adapters.AllOrderAdapter;
import com.aapnarshop.buyer.Model.ProductModel;
import com.aapnarshop.buyer.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LastMonthFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView;
    private List<ProductModel> productList;
    private String mParam1;
    private String mParam2;

    public LastMonthFragment() {
        // Required empty public constructor
    }

    public static LastMonthFragment newInstance(String param1, String param2) {
        LastMonthFragment fragment = new LastMonthFragment();
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

    @Override
    public void onDestroyView() {
        recyclerView.setAdapter(null);
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_order_tab, container, false);
        // Inflate the layout for this fragment
        /*TextView write_review = view.findViewById(R.id.write_review_product);
        write_review.setOnClickListener(ReviewDialog);*/
        recyclerView=view.findViewById(R.id.order_recycler_view);
        initData();
        setRecyclerView();

        return view;
    }
    private void setRecyclerView() {
        AllOrderAdapter allOrderAdapter= new AllOrderAdapter(getActivity(),productList);
        recyclerView.setAdapter(allOrderAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
    }

    private void initData() {
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

    private View.OnClickListener ReviewDialog = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            ViewGroup viewGroup = view.findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.rating_product, viewGroup, false);
            builder.setView(dialogView);
            RatingBar ratingBarPQR = dialogView.findViewById(R.id.product_quality_rating);
            RatingBar ratingBarSDS = dialogView.findViewById(R.id.delivery_service_rating);
            RatingBar ratingBarPOB = dialogView.findViewById(R.id.owner_behavior_rating);
            RatingBar ratingBarPP = dialogView.findViewById(R.id.product_packaging_rating);

            /*   // Half filled stars
              setRatingStarColor(stars.getDrawable(1), ContextCompat.getColor(getContext(), R.color.background));*/

            LayerDrawable starPQR = (LayerDrawable) ratingBarPQR.getProgressDrawable();
            LayerDrawable starSDS = (LayerDrawable) ratingBarSDS.getProgressDrawable();
            LayerDrawable starPOB = (LayerDrawable) ratingBarPOB.getProgressDrawable();
            LayerDrawable starPP = (LayerDrawable) ratingBarPP.getProgressDrawable();

            // Filled stars
            setRatingStarColor(starPQR.getDrawable(2), ContextCompat.getColor(requireContext(), R.color.app_yellow));
            // Empty stars
            setRatingStarColor(starPQR.getDrawable(0), ContextCompat.getColor(requireContext(), R.color.text_hint));

            // Filled stars
            setRatingStarColor(starSDS.getDrawable(2), ContextCompat.getColor(requireContext(), R.color.app_yellow));
            // Empty stars
            setRatingStarColor(starSDS.getDrawable(0), ContextCompat.getColor(requireContext(), R.color.text_hint));

            // Filled stars
            setRatingStarColor(starPOB.getDrawable(2), ContextCompat.getColor(requireContext(), R.color.app_yellow));
            // Empty stars
            setRatingStarColor(starPOB.getDrawable(0), ContextCompat.getColor(requireContext(), R.color.text_hint));

            // Filled stars
            setRatingStarColor(starPP.getDrawable(2), ContextCompat.getColor(requireContext(), R.color.app_yellow));
            // Empty stars
            setRatingStarColor(starPP.getDrawable(0), ContextCompat.getColor(requireContext(), R.color.text_hint));

            AlertDialog alertDialog = builder.create();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int displayWidth = displayMetrics.widthPixels;
            int displayHeight = displayMetrics.heightPixels;
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(Objects.requireNonNull(alertDialog.getWindow()).getAttributes());
            int dialogWindowWidth = (int) (displayWidth * 0.1f);
            int dialogWindowHeight = (int) (displayHeight * 0.1f);
            layoutParams.width = dialogWindowWidth;
            layoutParams.height = dialogWindowHeight;
            alertDialog.getWindow().setAttributes(layoutParams);
            alertDialog.show();

            ImageView closeBtn = dialogView.findViewById(R.id.product_rating_close_btn);
            closeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.cancel();
                }
            });
        }
    };

    private void setRatingStarColor(Drawable drawable, @ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            DrawableCompat.setTint(drawable, color);
        } else {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
    }

}

