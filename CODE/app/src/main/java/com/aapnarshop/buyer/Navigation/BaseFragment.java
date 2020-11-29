package com.aapnarshop.buyer.Navigation;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {

    private AddFragmentHandler fragmentHandler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        fragmentHandler = new AddFragmentHandler(getParentFragmentManager());
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getActivity()!=null){
            getActivity().setTitle(getTitle());
        }
    }

    protected abstract String getTitle();

    protected void add(BaseFragment fragment) {
        fragmentHandler.add(fragment);
    }
}
