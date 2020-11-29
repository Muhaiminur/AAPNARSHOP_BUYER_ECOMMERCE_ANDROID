package com.aapnarshop.buyer.OrderFragments;

import android.app.AlertDialog;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aapnarshop.buyer.Adapters.CustomAdapter;
import com.aapnarshop.buyer.Model.DataModel;
import com.aapnarshop.buyer.Navigation.BaseFragment;
import com.aapnarshop.buyer.R;

import java.util.ArrayList;
import java.util.Objects;

public class OrderDetailsFragment extends BaseFragment {
    private View view;
    //list of android version names
    private final String[] orderItemName = {"Cupcake", "Donut", "Eclair", "Froyo",
            "Gingerbread", "Honeycomb", "Ice Cream Sandwich 260gm Sandwich 260gm Sandwich 260gmSandwich 260gm",
            "JellyBean", "Kitkat", "Lollipop", "Delivery Charge"};
    //list of android version
    private final String[] orderItemCount = {"x5", "x6", "x1",
            "x3", "x7", "x6", "x4", "x1",
            "x4", "x1","x2"};
    //list of android version
    private final String[] orderItemPrice = {"1.53", "1.62", "2.02",
            "222", "237", "3032", "4040", "41",
            "443", "5.0","25"};

    public OrderDetailsFragment() {
        // Required empty public constructor
    }

    public OrderDetailsFragment newInstance() {
        return new OrderDetailsFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_order_expanded_view, container, false);
        setupToolbar();
        setupRecyclerView();
        TextView productReview = view.findViewById(R.id.ProductReview_expanded);
        productReview.setOnClickListener(productReviewListener);

        return view;
    }

    private final View.OnClickListener productReviewListener = new View.OnClickListener() {
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

    private void setupRecyclerView()
    {
        RecyclerView recyclerView = view.findViewById(R.id.order_inner_recycler_view_expanded);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        ArrayList<DataModel> data = new ArrayList<>();
        for (int i = 0; i < orderItemName.length; i++) {
            data.add(new DataModel(
                    orderItemName[i],
                    orderItemCount[i],
                    orderItemPrice[i]
            ));
        }

        RecyclerView.Adapter adapter = new CustomAdapter(data);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager((getContext())));
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

        textToolHeader.setText(R.string.order_details);
        toolbarImgBack.setVisibility(View.VISIBLE);
        toolbarImgDrawerLogo.setVisibility(View.GONE);
        toolbarImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getParentFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    Log.i("orderdetails", "popping backstack Order Details");
                    fm.popBackStack();
                } else {
                    Log.i("orderdetails", "nothing on backstack, calling super from Order Details");
                }
            }
        });
    }

    @Override
    protected String getTitle() {
        return "Order_Details_Fragment";
    }
}

