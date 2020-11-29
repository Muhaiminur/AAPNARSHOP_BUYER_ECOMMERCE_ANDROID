package com.aapnarshop.buyer.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.aapnarshop.buyer.Navigation.AddFragmentHandler;
import com.aapnarshop.buyer.R;
import com.aapnarshop.buyer.fragment.StoreHomepageFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import org.jetbrains.annotations.NotNull;

public class DashboardRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Declare Variables
    private final Context context;
    private final String[] imagesAdapter;


    public DashboardRecyclerViewAdapter(Context context,  String[] images) {
        this.context = context;
        this.imagesAdapter = images;

    }

    public static class OriginalViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        ImageView thumb;
        LinearLayout lyt_parent;
        CardView cardView;

        OriginalViewHolder(View v) {
            super(v);
            thumb = v.findViewById(R.id.thumb);
            lyt_parent = v.findViewById(R.id.lyt_parent);
            cardView=v.findViewById(R.id.cardviewDashboard2);
        }
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_dashboard_items, parent, false);
        viewHolder = new com.aapnarshop.buyer.Adapters.DashboardRecyclerViewAdapter.OriginalViewHolder(view);

        return viewHolder;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("CheckResult")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof com.aapnarshop.buyer.Adapters.DashboardRecyclerViewAdapter.OriginalViewHolder) {
            com.aapnarshop.buyer.Adapters.DashboardRecyclerViewAdapter.OriginalViewHolder vItem = (com.aapnarshop.buyer.Adapters.DashboardRecyclerViewAdapter.OriginalViewHolder) holder;

            CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
            circularProgressDrawable.setStrokeWidth(5f);
            circularProgressDrawable.setCenterRadius(30f);
            circularProgressDrawable.start();

            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(circularProgressDrawable);
            requestOptions.error(R.drawable.ic_image_default);
            requestOptions.skipMemoryCache(true);
            requestOptions.fitCenter();

            Glide.with(context).load(imagesAdapter[position]).centerCrop().apply(requestOptions).timeout(6000) // here you have all options you need
                    .transition(DrawableTransitionOptions.withCrossFade()).into(vItem.thumb);


            vItem.thumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddFragmentHandler fragmentHandler = new AddFragmentHandler(((AppCompatActivity) context).getSupportFragmentManager());
                    StoreHomepageFragment shf = new StoreHomepageFragment();
                    fragmentHandler.add(shf.newInstance());
                }
            });

            //  Passing OnItemClick listner to Main Activity to handle clicks there
            vItem.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddFragmentHandler fragmentHandler = new AddFragmentHandler( ((AppCompatActivity) context).getSupportFragmentManager());
                    StoreHomepageFragment shf = new StoreHomepageFragment();
                    fragmentHandler.add(shf.newInstance());
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        //Integer[] count = new Integer[imagesAdapter.length];
       // List<Integer> items = Arrays.asList(count);
        return imagesAdapter.length;
    }

}