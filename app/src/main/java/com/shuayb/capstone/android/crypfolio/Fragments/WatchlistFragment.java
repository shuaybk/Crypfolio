package com.shuayb.capstone.android.crypfolio.Fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.shuayb.capstone.android.crypfolio.CustomAdapters.MarketRecyclerViewAdapter;
import com.shuayb.capstone.android.crypfolio.DataViewModel;
import com.shuayb.capstone.android.crypfolio.DatabaseUtils.AppDatabase;
import com.shuayb.capstone.android.crypfolio.DatabaseUtils.Crypto;
import com.shuayb.capstone.android.crypfolio.MainActivity;
import com.shuayb.capstone.android.crypfolio.databinding.WatchlistFragmentBinding;

import java.util.ArrayList;
import java.util.List;

public class WatchlistFragment extends Fragment {
    private static final String TAG = "WatchlistFragment";

    private WatchlistFragmentBinding mBinding;
    private AppDatabase mDb;
    private DataViewModel mData;
    private Observer<ArrayList<Crypto>> cryptoObserver;
    private MutableLiveData<ArrayList<Crypto>> cryptosLD;
    private Observer<List<Crypto>> watchlistItemsObserver;

    private LiveData<List<Crypto>> watchlistItemsLD;
    private Context mContext;

    private ArrayList<Crypto> watchlistItems;


    public static final WatchlistFragment newInstance() {
        WatchlistFragment f = new WatchlistFragment();
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = WatchlistFragmentBinding.inflate(inflater, container, false);
        showLoadingScreen();
        mDb = AppDatabase.getInstance(mContext);
        mData = ViewModelProviders.of(getActivity()).get(DataViewModel.class);
        watchlistItems = null;

        setDataObservers();

        return mBinding.getRoot();
    }

    //Helper method to set the LiveData observers
    private void setDataObservers() {

        //This observer watches the Room DB for changes to the Watchlist
        watchlistItemsObserver = new Observer<List<Crypto>>() {
            @Override
            public void onChanged(List<Crypto> cryptos) {
                watchlistItems = new ArrayList<Crypto>(cryptos);
                System.out.println(TAG + " setDataObservers WatchlistItems is: ");
                for (Crypto c: cryptos) {
                    System.out.println(c.getId());
                }
                setRecyclerview();
                showMainScreen();
            }
        };
        watchlistItemsLD = mDb.watchlistDao().loadAllWatchListItems();
        watchlistItemsLD.observe(this, watchlistItemsObserver);



        cryptoObserver = new Observer<ArrayList<Crypto>>() {
            @Override
            public void onChanged(ArrayList<Crypto> cryptos) {
                System.out.println(TAG + " setDataObservers cryptosLD is " + cryptos.size() + " long");
                if (watchlistItems != null) {
                    updateWatchlistItemData(cryptos, watchlistItems);
                }
            }
        };
        //This observer watches the DataViewModel for changes to crypto prices
        cryptosLD = mData.getCryptos();
        cryptosLD.observe(this, cryptoObserver);
    }

    @Override
    public void onDestroyView() {
        if (cryptoObserver != null) {
            cryptosLD.removeObserver(cryptoObserver);
            cryptoObserver = null;
        }
        if (watchlistItemsObserver != null) {
            watchlistItemsLD.removeObserver(watchlistItemsObserver);
            watchlistItemsObserver = null;
        }

        super.onDestroyView();
    }

    //Just need to update it in the Room DB, not update the UI
    //We already have a LiveData object watching the Room DB
    //which will update the UI automatically
    private void updateWatchlistItemData(ArrayList<Crypto> cryptos, final List<Crypto> itemsToUpdate) {

        //We won't keep watchlist items that are no longer part of the top 100 cryptosLD
        final List<Crypto> newWatchlist = new ArrayList<Crypto>();

        for (Crypto c: itemsToUpdate) {
            Crypto match = null;
            for (Crypto temp: cryptos) {
                if (c.getId().equals(temp.getId())) {
                    match = temp;
                    break;
                }
            }
            if (match != null) {
                newWatchlist.add(match);
            }
        }

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                mDb.watchlistDao().deleteAllWatchlistItems();
                mDb.watchlistDao().insertWatchlistItemsAsList(newWatchlist);
                return null;
            }
        }.execute();
    }

    private void setRecyclerview() {
        if (mBinding.recyclerView.getAdapter() == null) {
            MarketRecyclerViewAdapter adapter = new MarketRecyclerViewAdapter(mContext, watchlistItems, (MainActivity) mContext);
            mBinding.recyclerView.setAdapter(adapter);
            mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        } else {
            MarketRecyclerViewAdapter adapter = (MarketRecyclerViewAdapter)(mBinding.recyclerView.getAdapter());
            adapter.updateCryptos(watchlistItems);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    private void showLoadingScreen() {
        mBinding.mainContainer.setVisibility(View.GONE);
        mBinding.loadingContainer.setVisibility(View.VISIBLE);
    }

    private void showMainScreen() {
        mBinding.mainContainer.setVisibility(View.VISIBLE);
        mBinding.loadingContainer.setVisibility(View.GONE);
    }
}
