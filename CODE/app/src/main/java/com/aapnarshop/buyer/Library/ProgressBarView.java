package com.aapnarshop.buyer.Library;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.aapnarshop.buyer.R;
import com.aapnarshop.buyer.databinding.DialogProgressbarBinding;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ProgressBarView extends DialogFragment {
    private DialogProgressbarBinding progressbarBinding;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //View rootView = inflater.inflate(R.layout.dialog_progressbar, container);
        if (progressbarBinding == null) {
            progressbarBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_progressbar, container, false);
        }

        return progressbarBinding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        try {
            super.onActivityCreated(savedInstanceState);
            Objects.requireNonNull(getDialog().getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            if (getDialog() != null) {
                getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);
            }
        } catch (Exception e) {
            Log.d("Error Line Number", Log.getStackTraceString(e));
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        progressbarBinding = null;
    }
}