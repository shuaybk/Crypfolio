package com.shuayb.capstone.android.crypfolio.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.shuayb.capstone.android.crypfolio.CustomAdapters.MarketRecyclerViewAdapter;
import com.shuayb.capstone.android.crypfolio.DatabaseUtils.AppDatabase;
import com.shuayb.capstone.android.crypfolio.DatabaseUtils.Crypto;
import com.shuayb.capstone.android.crypfolio.MainActivity;
import com.shuayb.capstone.android.crypfolio.R;
import com.shuayb.capstone.android.crypfolio.databinding.WatchlistFragmentBinding;

import java.util.ArrayList;
import java.util.List;

public class WatchlistFragment extends Fragment {
    private static final String TAG = "WatchlistFragment";

    private WatchlistFragmentBinding mBinding;
    private AppDatabase mDb;

    private ArrayList<Crypto> watchlistItems;

    public static final WatchlistFragment newInstance() {
        WatchlistFragment f = new WatchlistFragment();
        //Bundle bundle = new Bundle(1);
        //f.setArguments(bundle);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = WatchlistFragmentBinding.inflate(inflater, container, false);
        mDb = AppDatabase.getInstance(getContext());

        fetchWatchlistItems();

        return mBinding.getRoot();
    }


    private void fetchWatchlistItems() {
        LiveData<List<Crypto>> items = mDb.watchlistDao().loadAllWatchListItems();
        items.observe(this, new Observer<List<Crypto>>() {
            @Override
            public void onChanged(List<Crypto> cryptos) {
                watchlistItems = new ArrayList<Crypto>(cryptos);
                initRecyclerview();
            }
        });
    }

    private void initRecyclerview() {
        MarketRecyclerViewAdapter adapter = new MarketRecyclerViewAdapter(getContext(), watchlistItems, (MainActivity)getContext());
        mBinding.recyclerView.setAdapter(adapter);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }



    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
