package com.example.ewallet.adapter;

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

import com.example.ewallet.Domain.BankDomain;


public class BankAdapters extends RecyclerView.Adapter<BankAdapters.viewholder> {
    // ArrayList to hold bank items
    ArrayList<BankDomain> items;
    Context context;

    // Constructor to initialize the adapter with bank items
    public BankAdapters(ArrayList<BankDomain> items) {
        this.items = items;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public BankAdapters.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_bank, parent, false);
        return new viewholder(inflate);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull BankAdapters.viewholder holder, int position) {
        // Get element from dataset at this position and replace the contents of the view with that element
        holder.nameTxt.setText(items.get(position).getName());
        holder.balanceTxt.setText(items.get(position).getBalance() + "$");

        // Load image using Glide library
        int drawableResourceId = holder.itemView.getResources()
                .getIdentifier(items.get(position).getPicPath(), "drawable", holder.itemView.getContext().getPackageName());
        Glide.with(context)
                .load(drawableResourceId)
                .into(holder.pic_1);
    }

    // Return the size of dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }

    // Provide a reference to the views for each data item
    public class viewholder extends RecyclerView.ViewHolder {
        // UI elements to be displayed in RecyclerView
        TextView nameTxt, balanceTxt;
        ImageView pic_1;

        // Constructor to initialize UI elements
        public viewholder(@NonNull View itemView) {
            super(itemView);
            nameTxt = itemView.findViewById(R.id.nameTxt);
            balanceTxt = itemView.findViewById(R.id.balanceTxt);
            pic_1 = itemView.findViewById(R.id.pic_1);
        }
    }
}
