package com.example.ewallet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ewallet.R;

import java.util.ArrayList;

import Domain.BankDomain;

public class BankAdapters extends RecyclerView.Adapter<BankAdapters.viewholder> {
    ArrayList<BankDomain> items;
    Context context;

    public BankAdapters(ArrayList<BankDomain> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public BankAdapters.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_bank,parent,false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull BankAdapters.viewholder holder, int position) {
        holder.nameTxt.setText(items.get(position).getName());
        holder.balanceTxt.setText(items.get(position).getBalance()+"$");

        int drawableResourceId=holder.itemView.getResources()
                .getIdentifier(items.get(position).getPicPath(),"drawable",holder.itemView.getContext().getPackageName());

        Glide.with(context)
                .load(drawableResourceId)
                .into(holder.pic_1);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        TextView nameTxt,balanceTxt;
        ImageView pic_1;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            nameTxt=itemView.findViewById(R.id.nameTxt);
            balanceTxt = itemView.findViewById(R.id.balanceTxt);
            pic_1=itemView.findViewById(R.id.pic_1);
        }
    }
}
