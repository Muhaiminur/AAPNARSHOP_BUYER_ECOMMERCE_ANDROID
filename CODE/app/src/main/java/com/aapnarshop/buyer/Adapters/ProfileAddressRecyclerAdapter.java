package com.aapnarshop.buyer.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.aapnarshop.buyer.Library.KeyWord;
import com.aapnarshop.buyer.Library.Utility;
import com.aapnarshop.buyer.Model.GET.BuyerAddress;
import com.aapnarshop.buyer.R;
import com.aapnarshop.buyer.VIEWMODEL.BuyerProfileViewModel;
import com.aapnarshop.buyer.fragment.AddAddressFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ProfileAddressRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Declare Variables
    private final Context ctx;
    private List<Integer> items = new ArrayList<>();
    private final List<BuyerAddress> buyerAddressList;
    private final int VIEW_TYPE_ADDRESS=2;
    private final BuyerProfileViewModel buyerProfileViewModel;
    private final Utility utility;

    public ProfileAddressRecyclerAdapter(Context context, BuyerProfileViewModel buyerProfileViewModel, List<BuyerAddress> buyerAddressList, Utility utility) {
        this.ctx = context;
        this.buyerProfileViewModel=buyerProfileViewModel;
        this.buyerAddressList = buyerAddressList;
        this.utility = utility;
    }

    //View Holder for loading Buyer Address List
    public static class OriginalViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        CardView cardView;
        FrameLayout addressFrame;
        ImageView selectedTickImg;
        TextView buyerReceiverName,buyerReceiverPhone,buyerReceiverAddress,addressFrameRemove,addressFrameEdit;

        OriginalViewHolder(View v) {
            super(v);
            cardView = v.findViewById(R.id.card_view_parent_layout);
            addressFrame=v.findViewById(R.id.address_frame);
            selectedTickImg=v.findViewById(R.id.address_frame_selected_tick);
            buyerReceiverName=v.findViewById(R.id.address_frame_receiver_name);
            buyerReceiverPhone=v.findViewById(R.id.address_frame_receiver_number);
            buyerReceiverAddress=v.findViewById(R.id.address_frame_receiver_address);
            addressFrameRemove=v.findViewById(R.id.address_frame_remove);
            addressFrameEdit=v.findViewById(R.id.address_frame_edit);
        }
    }

    //View Holder For Add Address Card in Bottom
    public static class LastItemViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        CardView cardView;

        LastItemViewHolder(View v) {
            super(v);
            cardView = v.findViewById(R.id.add_address_card_view_layout);
        }
    }

    @Override
    public int getItemViewType(int position) {
        //throws to last Item view change for adding address card
        int VIEW_TYPE_ADD_ADDRESS = 1;
        return (position == items.size()) ? VIEW_TYPE_ADD_ADDRESS : VIEW_TYPE_ADDRESS;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;

        if (viewType == VIEW_TYPE_ADDRESS) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_card_view, parent, false);
            vh = new ProfileAddressRecyclerAdapter.OriginalViewHolder(v);
        }
        else
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_address_card_view, parent, false);
            vh = new ProfileAddressRecyclerAdapter.LastItemViewHolder(v);
        }

        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ProfileAddressRecyclerAdapter.OriginalViewHolder) {
            ProfileAddressRecyclerAdapter.OriginalViewHolder vItem = (ProfileAddressRecyclerAdapter.OriginalViewHolder) holder;
            BuyerAddress buyerAddress=buyerAddressList.get(position);

            vItem.buyerReceiverName.setText(buyerAddress.getReceiverName());
            vItem.buyerReceiverPhone.setText(utility.Mobile_Number_Read(buyerAddress.getReceiverPhone()));
            vItem.buyerReceiverAddress.setText(buyerAddress.getAddress());

           if(buyerAddress.getPrimary().equals("yes"))
           {
               vItem.addressFrame.setBackgroundResource(R.drawable.address_textview_border);
               vItem.selectedTickImg.setVisibility(View.VISIBLE);
           }
           else
           {
               vItem.addressFrame.setBackgroundResource(R.drawable.address_textview_border_unselected);
               vItem.selectedTickImg.setVisibility(View.INVISIBLE);
           }

            vItem.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(ctx, "Selected As Primary Address", Toast.LENGTH_SHORT).show();
                    buyerProfileViewModel.setBuyerPrimaryAddress(buyerAddress);
                }
            });

           vItem.addressFrameRemove.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   showDialogYesNo(buyerAddress.getAddressId());
               }
           });

           vItem.addressFrameEdit.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   AppCompatActivity activity = (AppCompatActivity) view.getContext();

                   Bundle bundleEditAddressObj = new Bundle();
                   bundleEditAddressObj.putSerializable("editAddressObj", buyerAddress);

                   Fragment myFragment = new AddAddressFragment();
                   myFragment.setArguments(bundleEditAddressObj);
                   activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, myFragment).addToBackStack(null).commit();
               }
           });

        }
        else if(holder instanceof ProfileAddressRecyclerAdapter.LastItemViewHolder)
        {
            ProfileAddressRecyclerAdapter.LastItemViewHolder lastItem = (ProfileAddressRecyclerAdapter.LastItemViewHolder) holder;
            lastItem.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                    Fragment myFragment = new AddAddressFragment();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, myFragment).addToBackStack(null).commit();
                }
            });
        }

    }

    private void showDialogYesNo(String addressId){
        HashMap<String,Integer>screen=getScreenRes();
        int width=screen.get(KeyWord.SCREEN_WIDTH);
        int mywidth=(width/10)*7;
        final Dialog dialog=new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_yes_no);
        TextView tvMessage=dialog.findViewById(R.id.tv_message);
        Button btnYes=dialog.findViewById(R.id.btn_yes);
        Button btnNo=dialog.findViewById(R.id.btn_no);
        tvMessage.setText(R.string.delete_address_ask_text);
        LinearLayout ll=dialog.findViewById(R.id.dialog_layout_size);
        LinearLayout.LayoutParams params=(LinearLayout.LayoutParams)ll.getLayoutParams();
        params.height=LinearLayout.LayoutParams.WRAP_CONTENT;
        params.width=mywidth;
        ll.setLayoutParams(params);
        btnYes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                BuyerAddress receiverAddress = new BuyerAddress(addressId);
                buyerProfileViewModel.deleteReceiverAddress(receiverAddress);
                dialog.dismiss();
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }


    private HashMap<String, Integer> getScreenRes(){
        HashMap<String, Integer> map= new HashMap<>();
        DisplayMetrics metrics=new DisplayMetrics();
        ((Activity)ctx).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width=metrics.widthPixels;
        int height=metrics.heightPixels;
        map.put(KeyWord.SCREEN_WIDTH,width);
        map.put(KeyWord.SCREEN_HEIGHT,height);
        map.put(KeyWord.SCREEN_DENSITY,(int)metrics.density);
        return map;
    }

    @Override
    public int getItemCount() {
        Integer[] count = new Integer[buyerAddressList.size()];
        items = Arrays.asList(count);
        return items.size()+1;
    }
}
