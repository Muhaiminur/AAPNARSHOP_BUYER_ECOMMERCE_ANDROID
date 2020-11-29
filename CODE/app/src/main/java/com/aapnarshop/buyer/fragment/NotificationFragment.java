package com.aapnarshop.buyer.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aapnarshop.buyer.Adapters.NotificationAdapter;
import com.aapnarshop.buyer.Model.ProductModel;
import com.aapnarshop.buyer.Navigation.AddFragmentHandler;
import com.aapnarshop.buyer.Navigation.BaseFragment;
import com.aapnarshop.buyer.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class NotificationFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private List<ProductModel> productList;

    public NotificationFragment newInstance() {
        return new NotificationFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    @Override
    protected String getTitle() {
        return "Notification_Fragment";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        setupToolbar();
        recyclerView=view.findViewById(R.id.notification_recycler_view);
        initData();
        setRecyclerView();

        return view;

    }

    private void setupToolbar()
    {
        Toolbar toolbarDashboard =  requireActivity().findViewById(R.id.toolbarDashboard);
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbarNotification);
        Toolbar toolbar_store_homepage = requireActivity().findViewById(R.id.toolbarStoreHomepage);
        ((AppCompatActivity)requireActivity()).setSupportActionBar(toolbar);

        toolbarDashboard.setVisibility(View.GONE);
        toolbar.setVisibility(View.VISIBLE);
        toolbar_store_homepage.setVisibility(View.GONE);
        TextView textToolHeader = toolbar.findViewById(R.id.toolbarModifiedTitle);
        ImageView toolbarImgBack = toolbar.findViewById(R.id.back_arrow);
        ImageView toolbarImgDrawerLogo = toolbar.findViewById(R.id.drawer_logo_notification_toolbar);
        textToolHeader.setText(R.string.text_navbar_notification);
        toolbarImgBack.setVisibility(View.GONE);
        toolbarImgDrawerLogo.setVisibility(View.VISIBLE);
    }
    //Notification Recycler data
    private void setRecyclerView()
    {
        NotificationAdapter notificationAdapter= new NotificationAdapter(productList);
        recyclerView.setAdapter(notificationAdapter);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d("cart", "fragmentcreate1");
        menu.clear();
        inflater.inflate(R.menu.cart_menu, menu);
        MenuItem awesomeMenuItem = menu.findItem(R.id.action_addcart);
        View awesomeActionView = awesomeMenuItem.getActionView();
        awesomeActionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("cart", "fragmentcreate2tr");
                onOptionsItemSelected(awesomeMenuItem);

                Log.d("cart", "fragmentcreate21");
            }
        });
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d("onOptionsItemSelected","yes");
        if (item.getItemId() == R.id.action_addcart) {
            AddFragmentHandler fragmentHandler = new AddFragmentHandler(getParentFragmentManager());
            CartFragment cf = new CartFragment();
            fragmentHandler.add(cf.newInstance());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
