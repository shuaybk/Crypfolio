package com.shuayb.capstone.android.crypfolio.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shuayb.capstone.android.crypfolio.AddPortfolioItemActivity;
import com.shuayb.capstone.android.crypfolio.DatabaseUtils.Crypto;
import com.shuayb.capstone.android.crypfolio.databinding.PortfolioFragmentBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class PortfolioFragment extends Fragment {
    private static final String TAG = "PortfolioFragment";

    private static final String KEY_BUNDLE_ARRAYLIST = "crypto_list";
    private static final int RC_SIGN_IN = 123;
    private static final int RC_ADD_PORTFOLIO_ITEM = 456;

    private PortfolioFragmentBinding mBinding;
    private FirebaseAuth authFb;
    private FirebaseUser userFb;
    private FirebaseFirestore dbf;
    private DocumentReference portfolioRef;
    private ArrayList<Crypto> cryptos;

    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build());

    public static final PortfolioFragment newInstance(ArrayList<Crypto> list) {
        PortfolioFragment f = new PortfolioFragment();
        Bundle bundle = new Bundle(1);
        bundle.putParcelableArrayList(KEY_BUNDLE_ARRAYLIST, list);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authFb = FirebaseAuth.getInstance();
        dbf = FirebaseFirestore.getInstance();
        cryptos = getArguments().getParcelableArrayList(KEY_BUNDLE_ARRAYLIST);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = PortfolioFragmentBinding.inflate(inflater, container, false);

        userFb = authFb.getCurrentUser();

        initViews();

        return mBinding.getRoot();
    }

    private void initViews() {

        mBinding.fabAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddPortfolioItemActivity.class);
                intent.putParcelableArrayListExtra(KEY_BUNDLE_ARRAYLIST, cryptos);
                startActivityForResult(intent, RC_ADD_PORTFOLIO_ITEM);
            }
        });

        if (userFb == null) {
            mBinding.signInPrompt.setVisibility(View.VISIBLE);
            mBinding.mainContentContainer.setVisibility(View.GONE);
        } else {
            portfolioRef = dbf.collection("users").document(userFb.getUid());
            mBinding.signInPrompt.setVisibility(View.GONE);
            mBinding.mainContentContainer.setVisibility(View.VISIBLE);
            mBinding.testText.setText("Signed is as " + userFb.getDisplayName());
            readTestData();
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
    }

    private void readTestData() {

        portfolioRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot result = task.getResult();
                            String resultString = task.getResult().getId() + " => " + task.getResult().getData();
                            mBinding.testText.setText(resultString);
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void addTestDataToDb() {
        Map<String, Object> portfolio = new HashMap<>();
        portfolio.put("XRP", 32000);
        portfolio.put("ETH", 14.98);
        portfolio.put("VET", 67000);

        portfolioRef.set(portfolio);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                //Successfully signed in
                userFb = authFb.getCurrentUser();
                mBinding.signInPrompt.setVisibility(View.GONE);
                mBinding.mainContentContainer.setVisibility(View.VISIBLE);
                mBinding.testText.setText("Signed is as " + userFb.getDisplayName());
            } else {
                Toast.makeText(getContext(), "Unable to sign in!", Toast.LENGTH_LONG).show();
                Log.w(TAG, "Authentication failed!!!");
            }
        } else if (requestCode == RC_ADD_PORTFOLIO_ITEM) {
            //How do we get here?  Figure it out
        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }


}
