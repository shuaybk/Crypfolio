package com.shuayb.capstone.android.crypfolio.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.shuayb.capstone.android.crypfolio.CustomAdapters.MarketRecyclerViewAdapter;
import com.shuayb.capstone.android.crypfolio.DatabaseUtils.AppDatabase;
import com.shuayb.capstone.android.crypfolio.DatabaseUtils.Crypto;
import com.shuayb.capstone.android.crypfolio.MainActivity;
import com.shuayb.capstone.android.crypfolio.R;
import com.shuayb.capstone.android.crypfolio.databinding.WatchlistFragmentBinding;

import java.util.ArrayList;

public class WatchlistFragment extends Fragment {
    private static final String TAG = "WatchlistFragment";

    private WatchlistFragmentBinding mBinding;
    private AppDatabase mDb;

    private ArrayList<Crypto> watchlistItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = WatchlistFragmentBinding.inflate(inflater, container, false);
        mDb = AppDatabase.getInstance(getContext().getApplicationContext());

        fetchWatchlistItems();


        return mBinding.getRoot();
    }

    private void fetchWatchlistItems() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                watchlistItems = new ArrayList<Crypto>(mDb.watchlistDao().loadAllWatchListItems());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                initRecyclerview();
            }
        }.execute();
    }

    private void initRecyclerview() {
        MarketRecyclerViewAdapter adapter = new MarketRecyclerViewAdapter(getContext(), watchlistItems, (MainActivity)getContext());
        mBinding.recyclerView.setAdapter(adapter);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
