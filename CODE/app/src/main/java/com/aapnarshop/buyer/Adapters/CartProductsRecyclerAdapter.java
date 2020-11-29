package com.aapnarshop.buyer.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.aapnarshop.buyer.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import org.jetbrains.annotations.NotNull;

public class CartProductsRecyclerAdapter extends RecyclerView.Adapter< CartProductsRecyclerAdapter.MyViewHolder> {

    private final String[] mDataset;
    private final Context context;
    private int count=1;

    static class MyViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        TextView add_to_cart,plusBtn,minusBtn;
        EditText countPlusMinus;
        LinearLayout plus_minus_layout;
        ImageView productImage;

        MyViewHolder(View v) {
            super(v);
            mCardView =v.findViewById(R.id.items_store_homepage_cardview);
            plus_minus_layout=v.findViewById(R.id.store_homepage_plus_minus);
            add_to_cart=v.findViewById(R.id.store_homepage_add_to_cart);
            productImage=v.findViewById(R.id.store_homepage_product_image);
            plusBtn=v.findViewById(R.id.store_homepage_plus_button);
            minusBtn=v.findViewById(R.id.store_homepage_minus_button);
            countPlusMinus=v.findViewById(R.id.store_homepage_plus_minus_count);
        }
    }

    public CartProductsRecyclerAdapter(Context context,String[] myDataset) {
        this.context=context;
        mDataset = myDataset;
    }

    @NotNull
    @Override
    public  CartProductsRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_store_homepage_tablayout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new  MyViewHolder(v);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NotNull CartProductsRecyclerAdapter.MyViewHolder holder, final int position) {
        holder.add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.add_to_cart.setVisibility(View.GONE);
                holder.plus_minus_layout.setVisibility(View.VISIBLE);
                holder.plusBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        count++;
                        holder.countPlusMinus.setText(String.valueOf(count));
                    }
                });
                holder.minusBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(count>0){
                            count--;
                            holder.countPlusMinus.setText(String.valueOf(count));
                        }
                        if(count==0) {
                            holder.plus_minus_layout.setVisibility(View.GONE);
                            holder.add_to_cart.setVisibility(View.VISIBLE);
                        }
                    }
                });

            }
        });
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentValue = mDataset[position];
                Log.d("CardView", "CardView Clicked: " + currentValue);
            }
        });

        //Circular loader for image loading through glide
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
                .load("https://chahida.com.bd/wp-content/uploads/2020/04/fresh-chinigura-rice-1-kg_byl9l7.png")
                .centerCrop()
                .apply(requestOptions) // here you have all options you need
                .transition(DrawableTransitionOptions.withCrossFade()) // when image (url) will be loaded by glide then this face in animation help to replace url image in the place of placeHolder (default) image.
                .into(holder.productImage);

    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

}