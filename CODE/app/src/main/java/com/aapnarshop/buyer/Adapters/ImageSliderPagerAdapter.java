package com.aapnarshop.buyer.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import androidx.viewpager.widget.PagerAdapter;

import com.aapnarshop.buyer.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class ImageSliderPagerAdapter extends PagerAdapter {
    private final Context context;
    private final ArrayList<String> image_arraylist;
    //Loads Image In the Slider Through Glide
    public ImageSliderPagerAdapter(Context context, ArrayList<String> image_arraylist) {
        this.context = context;
        this.image_arraylist = image_arraylist;
    }

    @SuppressLint("CheckResult")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.image_slider_layout_product_details, container, false);
        ImageView im_slider = view.findViewById(R.id.im_slider);

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(circularProgressDrawable);
        requestOptions.error(R.drawable.ic_image_default);
        requestOptions.skipMemoryCache(true);
        requestOptions.fitCenter();

        Glide.with(context)
                .load(image_arraylist.get(position))
                .centerCrop()
                .apply(requestOptions) // here you have all options you need
                .transition(DrawableTransitionOptions.withCrossFade()) // when image (url) will be loaded by glide then this face in animation help to replace url image in the place of placeHolder (default) image.
                .into(im_slider);


        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return image_arraylist.size();
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
