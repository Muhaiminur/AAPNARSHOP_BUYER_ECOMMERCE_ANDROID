package com.aapnarshop.buyer.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.aapnarshop.buyer.Adapters.ImageSliderPagerAdapter;
import com.aapnarshop.buyer.Adapters.ProductDetailsRecyclerAdapter;
import com.aapnarshop.buyer.Navigation.AddFragmentHandler;
import com.aapnarshop.buyer.Navigation.BaseFragment;
import com.aapnarshop.buyer.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ProductDetailsFragment extends BaseFragment {
    private  View view;
    private ViewPager vp_slider;
    private LinearLayout ll_dots;
    private ArrayList<String> slider_image_list;
    private int page_position = 0;
    private EditText countPlusMinus;
    private int count=0;

    public ProductDetailsFragment newInstance() {
        return new ProductDetailsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.product_details, container, false);
        setupToolbar();
        TextView plusBtn = view.findViewById(R.id.product_details_plus_button);
        TextView minusBtn = view.findViewById(R.id.product_details_minus_button);
        countPlusMinus=view.findViewById(R.id.product_details_plus_minus_count);
        // method for initialisation of Image Slider
        initImageSlider();

        // method for adding indicators
        addBottomDots(0);

        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                countPlusMinus.setText(String.valueOf(count));
            }
        });
        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count>0) {
                    count--;
                }
                countPlusMinus.setText(String.valueOf(count));
            }
        });

        final Handler handler = new Handler();

        // Handler for auto changing image in image slider
        final Runnable update = new Runnable() {
            public void run() {
                if (page_position == slider_image_list.size()) {
                    page_position = 0;
                } else {
                    page_position = page_position + 1;
                }
                vp_slider.setCurrentItem(page_position, true);
            }
        };

        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 50, 3000);

        setupRecyclerView();

        return view;

    }


    private void initImageSlider() {

        vp_slider = view.findViewById(R.id.vp_slider);
        ll_dots =  view.findViewById(R.id.ll_dots);

        slider_image_list = new ArrayList<>();

        slider_image_list.add("https://chahida.com.bd/wp-content/uploads/2020/04/fresh-chinigura-rice-1-kg_byl9l7.png");
        slider_image_list.add("https://chahida.com.bd/wp-content/uploads/2020/04/fresh-chinigura-rice-1-kg_byl9l7.png");
        slider_image_list.add("https://chahida.com.bd/wp-content/uploads/2020/04/fresh-chinigura-rice-1-kg_byl9l7.png");
        slider_image_list.add("https://chahida.com.bd/wp-content/uploads/2020/04/fresh-chinigura-rice-1-kg_byl9l7.png");

        ImageSliderPagerAdapter sliderPagerAdapter = new ImageSliderPagerAdapter(getActivity(), slider_image_list);
        vp_slider.setAdapter(sliderPagerAdapter);

        vp_slider.addOnPageChangeListener (new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //Image Slider DOTS
    private void addBottomDots(int currentPage) {
        TextView[] dots = new TextView[slider_image_list.size()];

        ll_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(view.getContext());
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.parseColor("#c5c5c5"));
            ll_dots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#000000"));
    }

    private void setupRecyclerView()
    {
        RecyclerView recyclerView = view.findViewById(R.id.mRecyclerViewProductDetails);
        recyclerView.setHasFixedSize(true);
        ProductDetailsRecyclerAdapter adapter = new ProductDetailsRecyclerAdapter(getActivity(),new String[]{"Popular", "New", "Offer", "Special","Popular", "New", "Offer", "Special"});
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
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

        textToolHeader.setText(R.string.details_text);
        toolbarImgBack.setVisibility(View.VISIBLE);
        toolbarImgDrawerLogo.setVisibility(View.GONE);
        toolbarImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getParentFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    Log.i("cart", "popping backstackProDE");
                    fm.popBackStack();
                } else {
                    Log.i("cart", "nothing on backstack, calling super ProDE");
                }
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
    }

    @Override
    protected String getTitle() {
        return "Product_Details";
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d("product details", "fragmentcreate1");
        menu.clear();
        inflater.inflate(R.menu.cart_menu, menu);
        MenuItem awesomeMenuItem = menu.findItem(R.id.action_addcart);
        View awesomeActionView = awesomeMenuItem.getActionView();
        awesomeActionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("product details", "fragmentcreate2tr");
                onOptionsItemSelected(awesomeMenuItem);

                Log.d("product details", "fragmentcreate21");
            }
        });
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d("onOptionsItemSelected ","yes");
        if (item.getItemId() == R.id.action_addcart) {
            AddFragmentHandler fragmentHandler = new AddFragmentHandler(getParentFragmentManager());
            CartFragment cf = new CartFragment();
            fragmentHandler.add(cf.newInstance());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
