package com.aapnarshop.buyer.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public abstract class CustomSpinnerAdapter<T> extends ArrayAdapter<T>{

    private final List<T> list;

    protected CustomSpinnerAdapter(Context context, int textViewResourceId, List<T> list) {
        super(context, textViewResourceId, list);
        this.list = list;
    }
    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView,parent);
    }

    @Nullable
    @Override
    public T getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position,convertView,parent);
    }

    public abstract View getCustomView(int position, View view, ViewGroup parent);

}
