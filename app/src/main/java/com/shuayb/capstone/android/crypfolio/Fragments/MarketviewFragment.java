package com.shuayb.capstone.android.crypfolio.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.shuayb.capstone.android.crypfolio.CustomAdapters.MarketRecyclerViewAdapter;
import com.shuayb.capstone.android.crypfolio.MainActivity;
import com.shuayb.capstone.android.crypfolio.DatabaseUtils.Crypto;
import com.shuayb.capstone.android.crypfolio.databinding.MarketviewFragmentBinding;

import java.util.ArrayList;

public class MarketviewFragment extends Fragment {
    private static final String TAG = "MarketviewFragment";
    private static final String KEY_BUNDLE_ARRAYLIST = "crypto_list";

    private MarketviewFragmentBinding mBinding;

    ArrayList<Crypto> cryptos;



    public static final MarketviewFragment newInstance(ArrayList<Crypto> cryptos) {
        MarketviewFragment f = new MarketviewFragment();
        Bundle bundle = new Bundle(1);
        bundle.putParcelableArrayList(KEY_BUNDLE_ARRAYLIST, cryptos);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cryptos = getArguments().getParcelableArrayList(KEY_BUNDLE_ARRAYLIST);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = MarketviewFragmentBinding.inflate(inflater, container, false);

        initRecyclerView(mBinding.getRoot());

        return mBinding.getRoot();
    }

    private void initRecyclerView(View view) {
        MarketRecyclerViewAdapter adapter = new MarketRecyclerViewAdapter(getContext(), cryptos, (MainActivity)getContext());
        mBinding.recyclerView.setAdapter(adapter);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
