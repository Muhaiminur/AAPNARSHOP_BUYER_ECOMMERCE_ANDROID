package com.aapnarshop.buyer.Adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aapnarshop.buyer.Model.DataModel;
import com.aapnarshop.buyer.Model.ProductModel;
import com.aapnarshop.buyer.Navigation.AddFragmentHandler;
import com.aapnarshop.buyer.OrderFragments.OrderDetailsFragment;
import com.aapnarshop.buyer.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AllOrderAdapter extends RecyclerView.Adapter<AllOrderAdapter.OrderOA> {
    private final List<ProductModel> productList;
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
    private final Context ctx;


    public AllOrderAdapter(Context ctx,List<ProductModel> productList) {
        this.productList = productList;
        this.ctx=ctx;
    }

    @NonNull
    @Override
    public AllOrderAdapter.OrderOA onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_order,parent,false);
        return new OrderOA(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllOrderAdapter.OrderOA holder, int position) {
        ProductModel productModel=productList.get(position);
        holder.orderNumber.setText(productModel.getOrderNumber());
        holder.orderSubTotal.setText(productModel.getOrderSubTotal());
        holder.orderDeliveryCharge.setText(productModel.getOrderDeliveryCharge());
        holder.orderDiscount.setText(productModel.getOrderDiscount());

        LayerDrawable orderRatingStars = (LayerDrawable) holder.orderRatingBar.getProgressDrawable();
        // Filled stars
        setRatingStarColor(orderRatingStars.getDrawable(2), ContextCompat.getColor(ctx, R.color.app_yellow));
        // Empty stars
        setRatingStarColor(orderRatingStars.getDrawable(0), ContextCompat.getColor(ctx, R.color.text_hint));

        boolean isExpandable=productList.get(position).isExpandable();
        holder.expandableLayout.setVisibility(View.GONE);
    //    holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);
        if(isExpandable) {
           /* holder.collapsedLayout.setBackgroundResource(R.color.white);
            holder.orderDetailsIcon.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_arrow_expand,0);*/
         /*   AppCompatActivity activity = (AppCompatActivity) ctx;
            FragmentManager fragmentManager = Objects.requireNonNull(activity).getSupportFragmentManager();*/
            AddFragmentHandler fragmentHandler = new AddFragmentHandler(((AppCompatActivity) ctx).getSupportFragmentManager());
            OrderDetailsFragment odf = new OrderDetailsFragment();
            fragmentHandler.add(odf.newInstance());
        }
        else {
            holder.collapsedLayout.setBackgroundResource(R.color.background_shape);
            holder.orderDetailsIcon.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_right_arrow,0);
        }

        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setItemAnimator(new DefaultItemAnimator());
        ArrayList<DataModel> data = new ArrayList<>();
        for (int i = 0; i < orderItemName.length; i++) {
            data.add(new DataModel(
                    orderItemName[i],
                    orderItemCount[i],
                    orderItemPrice[i]
            ));
        }

        RecyclerView.Adapter<CustomAdapter.MyViewHolder> adapter = new CustomAdapter(data);
        holder.recyclerView.setAdapter(adapter);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager((ctx)));
        // holder.recyclerView.addOnItemTouchListener(mScrollTouchListener);
    }

    RecyclerView.OnItemTouchListener mScrollTouchListener = new RecyclerView.OnItemTouchListener() {
        @Override
        public boolean onInterceptTouchEvent(@NotNull RecyclerView rv, MotionEvent e) {
            int action = e.getAction();
            if (action == MotionEvent.ACTION_MOVE) {
                rv.getParent().requestDisallowInterceptTouchEvent(true);
            }
            return false;
        }

        @Override
        public void onTouchEvent(@NotNull RecyclerView rv, @NotNull MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    };

    private void setRatingStarColor(Drawable drawable, @ColorInt int color)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            DrawableCompat.setTint(drawable, color);
        }
        else
        {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class OrderOA extends RecyclerView.ViewHolder
    {
        LinearLayout linearLayout,expandableLayout,collapsedLayout;
        TextView orderNumber,orderSubTotal,orderDeliveryCharge,orderDiscount,orderDetailsIcon;
        RecyclerView recyclerView;
        RatingBar orderRatingBar;

        OrderOA(@NonNull View itemView) {
            super(itemView);
            orderRatingBar=itemView.findViewById(R.id.OrderRating);
            orderNumber=itemView.findViewById(R.id.OrderNumber);
            orderSubTotal=itemView.findViewById(R.id.OrderSubTotal);
            orderDeliveryCharge=itemView.findViewById(R.id.OrderDeliveryCharge);
            orderDiscount=itemView.findViewById(R.id.OrderDiscount);
            orderDetailsIcon=itemView.findViewById(R.id.OrderDetails);
            linearLayout=itemView.findViewById(R.id.linear_layout_order);
            expandableLayout=itemView.findViewById(R.id.expandable_layout_order);
            collapsedLayout=itemView.findViewById(R.id.order_collapsed_layout);

            recyclerView = itemView.findViewById(R.id.order_inner_recycler_view);


            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProductModel productModel=productList.get(getAdapterPosition());
                    productModel.setExpandable(!productModel.isExpandable());
                   // collapsedLayout.setBackgroundResource(R.color.white);
                  //  orderDetailsIcon.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_arrow_expand,0);
                    notifyItemChanged(getAdapterPosition());
                }
            });

        }
    }
}
