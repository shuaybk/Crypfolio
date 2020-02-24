package com.shuayb.capstone.android.crypfolio.Fragments;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.shuayb.capstone.android.crypfolio.AddPortfolioItemActivity;
import com.shuayb.capstone.android.crypfolio.CustomAdapters.PortfolioRecyclerViewAdapter;
import com.shuayb.capstone.android.crypfolio.DataUtils.RandomUtils;
import com.shuayb.capstone.android.crypfolio.DataViewModel;
import com.shuayb.capstone.android.crypfolio.DatabaseUtils.Crypto;
import com.shuayb.capstone.android.crypfolio.POJOs.PortfolioItem;
import com.shuayb.capstone.android.crypfolio.PortfolioWidgetProvider;
import com.shuayb.capstone.android.crypfolio.R;
import com.shuayb.capstone.android.crypfolio.databinding.PortfolioFragmentBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class PortfolioFragment extends Fragment
        implements PortfolioRecyclerViewAdapter.PortfolioItemClickListener,
                DeletePortfolioItemFragment.DeletePortfolioDialogListener {

    private static final String TAG = "PortfolioFragment";

    private static final String DELETE_DIALOG_FRAGMENT_TAG = "DeletePortfolioItemFragment";
    private static final String KEY_CRYPTO_ID = "key_crypto_id";
    private static final String KEY_AMOUNT = "key_amount";
    private static final String KEY_PURCHASE_PRICE = "key_purchase_price";
    private static final String KEY_BUNDLE_APPWIDGET_ID = "appwidget_id";
    private static final int RC_SIGN_IN = 123;
    private static final int RC_ADD_PORTFOLIO_ITEM = 456;
    private static final int DB_AMOUNT_INDEX = 0;
    private static final int DB_PRICE_INDEX = 1;

    private PortfolioFragmentBinding mBinding;
    private FirebaseAuth authFb;
    private FirebaseUser userFb;
    private FirebaseFirestore dbf;
    private DataViewModel mData;
    private DocumentReference portfolioRef;
    private ArrayList<PortfolioItem> portfolioItems;
    private ListenerRegistration portfolioListenerFb;
    private Context mContext;
    private DocumentSnapshot lastResult;
    MutableLiveData<ArrayList<Crypto>> cryptoLD;
    Observer<ArrayList<Crypto>> cryptoObserver;
    private int appWidgetId;

    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build());

    public static final PortfolioFragment newInstance(int widgetId) {
        PortfolioFragment f = new PortfolioFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_BUNDLE_APPWIDGET_ID, widgetId);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authFb = FirebaseAuth.getInstance();
        dbf = FirebaseFirestore.getInstance();
        mContext = getContext();
        appWidgetId = getArguments().getInt(KEY_BUNDLE_APPWIDGET_ID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = PortfolioFragmentBinding.inflate(inflater, container, false);
        showLoadingScreen();

        mData = ViewModelProviders.of(getActivity()).get(DataViewModel.class);

        initViews();

        return mBinding.getRoot();
    }


    private void initViews() {
        userFb = authFb.getCurrentUser();
        mBinding.backgroundCover.setVisibility(View.GONE);
        mBinding.backgroundCover.setAlpha(0.5f);

        if (userFb == null) {
            mBinding.signInPrompt.setVisibility(View.VISIBLE);
            mBinding.mainContentContainer.setVisibility(View.GONE);
            unregisterPortfolioListener();
            unregisterDataObservers();
            setHasOptionsMenu(false);
            portfolioRef = null;
            showMainScreen();
        } else {
            setHasOptionsMenu(true); //to display sign out button
            portfolioRef = dbf.collection("users").document(userFb.getUid());
            registerPortfolioListener();
            registerDataObservers();

            mBinding.signInPrompt.setVisibility(View.GONE);
            mBinding.mainContentContainer.setVisibility(View.VISIBLE);
            fetchPortfolioInfo();
        }

        mBinding.signInPrompt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .build(),
                        RC_SIGN_IN);
            }
        });
        mBinding.fabAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddPortfolioItemActivity.class);
                startActivityForResult(intent, RC_ADD_PORTFOLIO_ITEM);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.portfolio_menu, menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setViews() {
        double totalVal = 0;
        for (PortfolioItem item: portfolioItems) {
            totalVal+= item.getAmount()*item.getCurrentPrice();
        }

        if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
            updateAppWidget(totalVal);
        }

        mBinding.totalValue.setText("$" + RandomUtils.getFormattedCurrencyAmount(totalVal));

        if (mBinding.recyclerView.getAdapter() == null) {  //Initial setup
            PortfolioRecyclerViewAdapter adapter = new PortfolioRecyclerViewAdapter(getContext(), portfolioItems, this);
            mBinding.recyclerView.setAdapter(adapter);
            mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        } else {  //Just update it
            PortfolioRecyclerViewAdapter adapter = (PortfolioRecyclerViewAdapter)(mBinding.recyclerView.getAdapter());
            adapter.updatePortfolioItems(portfolioItems);
            adapter.notifyDataSetChanged();
        }
        showMainScreen();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_signOut) {
            if (userFb != null) { //sign out
                Toast.makeText(mContext, "Signing out...", Toast.LENGTH_LONG).show();
                AuthUI.getInstance().signOut(mContext)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                //TODO - Pause everything to allow sign out
                                initViews();
                            }
                        });
            }
            //Remove sign out button
            setHasOptionsMenu(false);
        }

        return super.onOptionsItemSelected(item);
    }

    //Helper method to process data on a DB change event
    private void onDbChangeEvent(DocumentSnapshot result) {
        lastResult = result;
        generatePortfolioItems(result.getData());
    }

    //First get the Portfolio info from Firebase here
    private void fetchPortfolioInfo() {
        portfolioRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> docData = task.getResult().getData();
                            if (docData == null) {
                                docData = new HashMap<>();
                            }
                            generatePortfolioItems(docData);
                        } else {
                            portfolioItems = new ArrayList<>(); //Empty list to not throw null errors
                            mBinding.signInPrompt.setVisibility(View.GONE);
                            mBinding.mainContentContainer.setVisibility(View.GONE);
                            mBinding.errorMessage.setVisibility(View.VISIBLE);
                            Toast.makeText(getContext(), "Error: Could not fetch Portfolio", Toast.LENGTH_LONG).show();
                            showMainScreen(); //This main screen includes the above error view
                        }
                    }
                });
    }

    //Generate the portfolio items from Firebase info
    //and fetch the remaining needed info from CoinGecko
    private void generatePortfolioItems(Map<String, Object> dataMap) {
        final HashMap<String, PortfolioItem> mapItems = new HashMap<>();
        String id;
        String name;
        String image;
        double amount;
        double avgPrice;
        double currentPrice;

        StringBuilder ids = new StringBuilder("");

        for (String key: dataMap.keySet()) {
            List<Double> list = (ArrayList<Double>)(dataMap.get(key));
            id = key;
            name = "";
            image = "";
            amount = list.get(DB_AMOUNT_INDEX);
            avgPrice = list.get(DB_PRICE_INDEX);
            currentPrice = -1;
            PortfolioItem item = new PortfolioItem(id, name, image, amount, avgPrice, currentPrice);
            mapItems.put(id, item);

            if (ids.length() == 0) {
                ids.append(id);
            } else {
                ids.append(",").append(id);
            }
        }

        //Look up the price info for our portfolio items (if any)
        if (ids.length() > 0) {

            mData.refreshPortfolioItems(mContext, ids.toString());
            final MutableLiveData<ArrayList<Crypto>> portfolioItemsMoreDetailsLD = mData.getPortfolioItemsMoreDetails();
            mData.clearPortfolioItemsMoreDetails();
            portfolioItemsMoreDetailsLD.observe(this, new Observer<ArrayList<Crypto>>() {
                        @Override
                        public void onChanged(ArrayList<Crypto> portfolioItemsMoreDetails) {
                            if (portfolioItemsMoreDetails.size() > 0) {
                                portfolioItemsMoreDetailsLD.removeObserver(this);
                                portfolioItems = new ArrayList<>();

                                //Combine details from Map and list above to generate full Portfolio Item list (using map because its O(n))
                                for (Crypto c : portfolioItemsMoreDetails) {
                                    PortfolioItem item = mapItems.get(c.getId());
                                    if (item != null) {
                                        item.setName(c.getName());
                                        item.setImage(c.getImage());
                                        item.setCurrentPrice(c.getCurrentPrice());
                                        portfolioItems.add(item);
                                    }
                                }

                                //Now we have all the info needed, we can finish setting up / updating the views
                                setViews();
                            }
                        }
                    });
        }
    }


    private void addPortfolioItemOnCloud(final String cryptoId, final double amount, final double purchasePrice) {
        portfolioRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot result = task.getResult();

                            Map<String, Object> data = task.getResult().getData();

                            if (data == null) {
                                data = new HashMap<>();
                            }

                            if (data.containsKey(cryptoId)) { //Update the existing portfolio item
                                List<Double> list = (List<Double>)(data.get(cryptoId));
                                double oldAmount = list.get(DB_AMOUNT_INDEX);
                                double totalAmount = amount + oldAmount;
                                double oldPrice = list.get(DB_PRICE_INDEX);
                                double avgPrice = (oldAmount/totalAmount)*oldPrice + (amount/totalAmount)*purchasePrice;
                                list.set(DB_AMOUNT_INDEX, totalAmount);
                                list.set(DB_PRICE_INDEX, avgPrice);
                                data.put(cryptoId, list);
                                portfolioRef.set(data);

                            } else { //Add a new portfolio item
                                List<Double> list = new ArrayList<Double>();
                                list.add(DB_AMOUNT_INDEX, amount);
                                list.add(DB_PRICE_INDEX, purchasePrice);
                                data.put(cryptoId, list);
                                portfolioRef.set(data);
                                Toast.makeText(getContext(), "Added to DB!!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Error adding Portfolio item", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void registerPortfolioListener() {
        if (portfolioRef != null && portfolioListenerFb == null) {
            portfolioListenerFb = portfolioRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Toast.makeText(getContext(), "Error on DB listener event", Toast.LENGTH_LONG);
                        Log.w(TAG, e.toString());
                        return;
                    }
                    if (documentSnapshot.exists()) {
                        onDbChangeEvent(documentSnapshot);
                    }
                }
            });
        }
    }

    private void unregisterPortfolioListener() {
        if (portfolioListenerFb != null) {
            portfolioListenerFb.remove();
            portfolioListenerFb = null;
        }
    }

    private void dismissDialog() {
        FragmentManager fm = getFragmentManager();
        DeletePortfolioItemFragment fragment = (DeletePortfolioItemFragment)(fm.findFragmentByTag(DELETE_DIALOG_FRAGMENT_TAG));
        fragment.dismiss();
    }

    //Observe the crypto updates.  Every time crypto updates, so should the portfolio
    //so refresh the portfolio on update
    private void registerDataObservers() {
        if (cryptoObserver == null) {
            cryptoLD = mData.getCryptos();
            cryptoObserver = new Observer<ArrayList<Crypto>>() {
                @Override
                public void onChanged(ArrayList<Crypto> portfolioItemsMoreDetails) {
                    fetchPortfolioInfo();
                }
            };
            cryptoLD.observe(this, cryptoObserver);
        }
    }

    private void unregisterDataObservers() {
        if (cryptoObserver != null && cryptoLD != null) {
            cryptoLD.removeObserver(cryptoObserver);
            cryptoObserver = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        unregisterDataObservers();
        unregisterPortfolioListener();

        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case RC_SIGN_IN:
                IdpResponse response = IdpResponse.fromResultIntent(data);

                if (resultCode == RESULT_OK) {
                    //Successfully signed in
                    userFb = authFb.getCurrentUser();
                    mBinding.signInPrompt.setVisibility(View.GONE);
                    mBinding.mainContentContainer.setVisibility(View.VISIBLE);
                    //refresh all views
                    initViews();
                } else {
                    Toast.makeText(getContext(), "Unable to sign in!", Toast.LENGTH_SHORT).show();
                    Log.w(TAG, "Authentication failed!!!");
                }
                break;

            case RC_ADD_PORTFOLIO_ITEM:
                if (resultCode == RESULT_OK) {
                    String cryptoId = data.getStringExtra(KEY_CRYPTO_ID);
                    double amount = data.getDoubleExtra(KEY_AMOUNT, 0);
                    double purchasePrice = data.getDoubleExtra(KEY_PURCHASE_PRICE, 0);

                    addPortfolioItemOnCloud(cryptoId, amount, purchasePrice);

                } else if (resultCode == RESULT_CANCELED) {
                    //Do nothing, this is fine
                } else {
                    Toast.makeText(getContext(), "Unexpected result!", Toast.LENGTH_SHORT).show();
                    Log.w(TAG, "Add Portfolio item activity returned an unexpected result.  requestCode = " + requestCode);
                }
                break;
        }
    }

    @Override
    public void onPortfolioItemClick(PortfolioItem portfolioItem) {
        //Do nothing
    }

    @Override
    public void onPortfolioItemLongClick(PortfolioItem portfolioItem) {
        DeletePortfolioItemFragment fragment = DeletePortfolioItemFragment.newInstance(portfolioItem);
        fragment.setTargetFragment(this, 1);

        mBinding.backgroundCover.setVisibility(View.VISIBLE);
        fragment.show(getFragmentManager(), DELETE_DIALOG_FRAGMENT_TAG);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    //From the Portfolio delete item dialog fragment
    @Override
    public void onDeleteClicked(final PortfolioItem item) {
        dismissDialog();
        Toast.makeText(mContext, "Deleting - one moment...", Toast.LENGTH_SHORT).show();

        portfolioRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> data = task.getResult().getData();
                            if (data != null) {
                                data.remove(item.getId());
                            }
                            portfolioRef.set(data);
                        }
                    }
                });
    }

    //From the Portfolio delete item dialog fragment
    @Override
    public void onCancelDeleteClicked() {
        dismissDialog();
    }

    @Override
    public void onDismissed() {
        mBinding.backgroundCover.setVisibility(View.GONE);
    }

    private void showLoadingScreen() {
        mBinding.mainContainer.setVisibility(View.GONE);
        mBinding.loadingContainer.setVisibility(View.VISIBLE);
    }

    private void showMainScreen() {
        mBinding.mainContainer.setVisibility(View.VISIBLE);
        mBinding.loadingContainer.setVisibility(View.GONE);
    }

    private void updateAppWidget(double totalValue) {
        PortfolioWidgetProvider.updateWidgetText(mContext, AppWidgetManager.getInstance(mContext), appWidgetId, totalValue);
    }
}
