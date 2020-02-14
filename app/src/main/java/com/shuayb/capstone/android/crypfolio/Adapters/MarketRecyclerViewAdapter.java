package com.shuayb.capstone.android.crypfolio.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.shuayb.capstone.android.crypfolio.POJOs.Crypto;
import com.shuayb.capstone.android.crypfolio.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MarketRecyclerViewAdapter
        extends RecyclerView.Adapter<MarketRecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<Crypto> cryptos;
    private Context mContext;

    public MarketRecyclerViewAdapter(Context mContext, ArrayList<Crypto> cryptos) {
        this.mContext = mContext;
        this.cryptos = cryptos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.crypto_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Picasso.get().load(cryptos.get(position).getImage()).into(holder.logoImage);
        holder.tvName.setText(cryptos.get(position).getName());
        holder.tvMarketcap.setText("$" + cryptos.get(position).getMarketCap());
        holder.tvPrice.setText("$" + cryptos.get(position).getCurrentPrice());

        holder.parentLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Clicked on item " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cryptos.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView logoImage;
        TextView tvName;
        TextView tvMarketcap;
        TextView tvPrice;
        ConstraintLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            logoImage = itemView.findViewById(R.id.logo_image);
            tvName = itemView.findViewById(R.id.tv_name);
            tvMarketcap = itemView.findViewById(R.id.tv_marketcap);
            tvPrice = itemView.findViewById(R.id.tv_price);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
