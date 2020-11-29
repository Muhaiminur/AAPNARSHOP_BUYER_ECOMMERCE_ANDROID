package com.aapnarshop.buyer.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.aapnarshop.buyer.Navigation.AddFragmentHandler;
import com.aapnarshop.buyer.Navigation.BaseFragment;
import com.aapnarshop.buyer.R;
import com.aapnarshop.buyer.TabsRecyclerAdapter.TabLayoutAdapter;
import com.aapnarshop.buyer.TabLayout_Fragment.NewFragment;
import com.aapnarshop.buyer.TabLayout_Fragment.OfferFragment;
import com.aapnarshop.buyer.TabLayout_Fragment.PopularFragment;
import com.aapnarshop.buyer.TabLayout_Fragment.SpecialFragment;
import com.google.android.material.tabs.TabLayout;


public class StoreHomepageFragment extends BaseFragment {
    private View view;

    public StoreHomepageFragment newInstance() {
        return new StoreHomepageFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_store_homepage, container, false);
        setupToolbar();
        setupTabLayout();
        return view;

    }

    private void setupTabLayout()
    {
        ViewPager viewPager = view.findViewById(R.id.viewPager);
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        TabLayoutAdapter adapter = new TabLayoutAdapter(this.getChildFragmentManager());
        adapter.addFragment(new PopularFragment(), "Popular");
        adapter.addFragment(new NewFragment(), "New");
        adapter.addFragment(new OfferFragment(), "Offer");
        adapter.addFragment(new SpecialFragment(), "Eid Special");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        int betweenSpace = 5;

        // Tabs spacing and margin design between tabs
        ViewGroup slidingTabStrip = (ViewGroup) tabLayout.getChildAt(0);
        for (int i=0; i<slidingTabStrip.getChildCount()-1; i++) {
            View v1 = slidingTabStrip.getChildAt(i);
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v1.getLayoutParams();
            params.rightMargin = betweenSpace;
        }
    }

    private void setupToolbar()
    {
        Toolbar toolbarDashboard =  requireActivity().findViewById(R.id.toolbarDashboard);
        Toolbar toolbar = requireActivity().findViewById(R.id.toolbarNotification);
        Toolbar toolbar_store_homepage=requireActivity().findViewById(R.id.toolbarStoreHomepage);
        ((AppCompatActivity)requireActivity()).setSupportActionBar(toolbar_store_homepage);

        toolbarDashboard.setVisibility(View.GONE);
        toolbar.setVisibility(View.GONE);
        toolbar_store_homepage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d("cart", "fragmentcreate1SHF");
        menu.clear();
        inflater.inflate(R.menu.cart_menu, menu);
        MenuItem awesomeMenuItem = menu.findItem(R.id.action_addcart);
        View awesomeActionView = awesomeMenuItem.getActionView();
        awesomeActionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(awesomeMenuItem);
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    @Override
    protected String getTitle() {
        return "StoreHomepageFragment";
    }

}
