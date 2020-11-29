package com.aapnarshop.buyer.TabLayout_Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aapnarshop.buyer.Navigation.BaseFragment;
import com.aapnarshop.buyer.TabsRecyclerAdapter.PopularTabRecyclerAdapter;
import com.aapnarshop.buyer.R;

public class PopularFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.tablayout_popular_fragment, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.store_homepage_populartab_recycler_view);
        recyclerView.setHasFixedSize(true);
        PopularTabRecyclerAdapter adapter = new PopularTabRecyclerAdapter(getActivity(),new String[]{"Popular", "New", "Offer", "Special","Popular", "New", "Offer", "Special"});
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        return view;
    }

    @Override
    protected String getTitle() {
        return "Popular_Fragment";
    }
}
