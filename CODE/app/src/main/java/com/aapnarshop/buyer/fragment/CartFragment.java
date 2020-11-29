package com.aapnarshop.buyer.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aapnarshop.buyer.Adapters.CartProductsRecyclerAdapter;
import com.aapnarshop.buyer.Navigation.BaseFragment;
import com.aapnarshop.buyer.R;


public class CartFragment extends BaseFragment {
    private View view;
    private RadioButton radioButtonCOD, radioButtonOP;

    public CartFragment newInstance() {
        return new CartFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_cart, container, false);
        initView();
        setupToolbar();
        setupRecyclerView();


        return view;

    }
    private void initView()
    {
        radioButtonCOD = view.findViewById(R.id.radioButtonCashOnDelivery);
        radioButtonOP = view.findViewById(R.id.radioButtonOnlinePayment);
        radioButtonCOD.setOnClickListener(this::onRadioButtonClicked);
    }

    @SuppressLint("NonConstantResourceId")
    private void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButtonCashOnDelivery:
                if (checked){
                    radioButtonOP.setChecked(false);
                    radioButtonCOD.setChecked(true);
                }
                    break;
            case R.id.radioButtonOnlinePayment:
                if (checked){
                    radioButtonCOD.setChecked(false);
                    radioButtonOP.setChecked(true);
                }
                    break;
        }
    }

    //Added products to Cart Recyclerview
    private void setupRecyclerView()
    {
        RecyclerView recyclerView = view.findViewById(R.id.mRecyclerViewCartProducts);
        recyclerView.setHasFixedSize(true);
        CartProductsRecyclerAdapter adapter = new CartProductsRecyclerAdapter(getActivity(),new String[]{"Popular", "New", "Offer"});
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
    }

    private void setupToolbar()
    {
        Toolbar toolbarDashboard = requireActivity().findViewById(R.id.toolbarDashboard);
        Toolbar toolbar_store_homepage = requireActivity().findViewById(R.id.toolbarStoreHomepage);
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbarNotification);
        ((AppCompatActivity)requireActivity()).setSupportActionBar(toolbar);

        toolbarDashboard.setVisibility(View.GONE);
        toolbar.setVisibility(View.VISIBLE);
        toolbar_store_homepage.setVisibility(View.GONE);

        TextView textToolHeader = toolbar.findViewById(R.id.toolbarModifiedTitle);
        ImageView toolbarImgBack = toolbar.findViewById(R.id.back_arrow);
        ImageView toolbarImgDrawerLogo = toolbar.findViewById(R.id.drawer_logo_notification_toolbar);

        textToolHeader.setText(R.string.cart);
        toolbarImgBack.setVisibility(View.VISIBLE);
        toolbarImgDrawerLogo.setVisibility(View.GONE);
        toolbarImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getParentFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    Log.i("cart", "Back button popping backstack from cart");
                    fm.popBackStack();
                } else {
                    Log.i("cart", "nothing on backstack, calling super");
                }
            }
        });
    }


    @Override
    protected String getTitle() {
        return "Cart_Fragment";
    }
}
