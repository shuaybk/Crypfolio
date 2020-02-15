package com.shuayb.capstone.android.crypfolio.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.shuayb.capstone.android.crypfolio.DatabaseUtils.AppDatabase;
import com.shuayb.capstone.android.crypfolio.DatabaseUtils.Crypto;
import com.shuayb.capstone.android.crypfolio.R;
import com.shuayb.capstone.android.crypfolio.databinding.DetailsFragmentBinding;

public class DetailsFragment extends Fragment {

    private AppDatabase mDb;

    DetailsFragmentBinding mBinding;
    Crypto crypto;
    boolean isFavourite = false;
    Menu menu;

    public DetailsFragment(Crypto crypto) {
        this.crypto = crypto;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DetailsFragmentBinding.inflate(inflater, container, false);
        mDb = AppDatabase.getInstance(getContext().getApplicationContext());

        initViews();
        setHasOptionsMenu(true);

        return mBinding.getRoot();
    }


    //Helper method to initialize the views with crypto information
    private void initViews() {
        mBinding.symbolText.setText(crypto.getSymbol());
        mBinding.nameText.setText(crypto.getName());
        mBinding.marketcapText.setText("Market Cap: " + crypto.getFormattedMarketcapFull());
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.details_menu, menu);
        this.menu = menu;
        setIfFavourite();

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_watchlist) {
            toggleFavourite();
        }

        return super.onOptionsItemSelected(item);
    }

    private void toggleFavourite() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                if (isFavourite) {
                    isFavourite = false;
                    mDb.watchlistDao().deleteWatchlistItem(crypto);
                } else {
                    isFavourite = true;
                    mDb.watchlistDao().insertWatchlistItem(crypto);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                MenuItem favButton = menu.findItem(R.id.action_watchlist);

                if (isFavourite) {
                    favButton.setIcon(android.R.drawable.btn_star_big_on);
                } else {
                    favButton.setIcon(android.R.drawable.btn_star_big_off);
                }

                super.onPostExecute(aVoid);
            }
        }.execute();
    }

    //Set the value of the isFavourite boolean by checking if its a favourite
    private void setIfFavourite() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                if (mDb.watchlistDao().getWatchlistItemById(crypto.getId()) != null) {
                    isFavourite = true;
                } else {
                    isFavourite = false;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                MenuItem favButton = menu.findItem(R.id.action_watchlist);

                if (isFavourite) {
                    favButton.setIcon(android.R.drawable.btn_star_big_on);
                } else {
                    favButton.setIcon(android.R.drawable.btn_star_big_off);
                }

                super.onPostExecute(aVoid);
            }
        }.execute();
    }
}
