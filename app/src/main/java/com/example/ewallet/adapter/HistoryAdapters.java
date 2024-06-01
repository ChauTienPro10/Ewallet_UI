package com.example.ewallet.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ewallet.HistoryTransactionDetail;
import com.example.ewallet.R;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.example.ewallet.Domain.HistoryDomain;


public class HistoryAdapters extends RecyclerView.Adapter<HistoryAdapters.viewholder> {
    // ArrayList to hold transaction history items
    ArrayList<HistoryDomain> items;
    Context context;

    // Constructor to initialize the adapter with transaction history items and context
    public HistoryAdapters(ArrayList<HistoryDomain> items, Context context) {
        this.context = context;
        this.items = items;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public HistoryAdapters.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_history_transaction, parent, false);
        return new viewholder(inflate);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull HistoryAdapters.viewholder holder, int position) {
        // Get element from dataset at this position and replace the contents of the view with that element
        BigDecimal amount = items.get(position).getAmount();
        String amountString = amount.toString();
        holder.contentTxt.setText(items.get(position).getContent());
        holder.timeTxt.setText("Date" + items.get(position).getTime());
        holder.amountTxt.setText("-" + amountString + "$");
        holder.pic_2.setImageResource(items.get(position).getPicPath());

        // Set click listener on the layout
        holder.reCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    // Retrieve selected item
                    HistoryDomain selectedItem = items.get(position);
                    if (selectedItem != null) {
                        // Start HistoryTransactionDetail activity and pass data
                        Intent intent = new Intent(context, HistoryTransactionDetail.class);
                        intent.putExtra("Image", selectedItem.getPicPath());
                        intent.putExtra("Content", selectedItem.getContent());
                        intent.putExtra("Time", selectedItem.getTime());
                        intent.putExtra("Amount", (selectedItem.getAmount()).toString() + "$");
                        context.startActivity(intent);
                    } else {
                        // Log if selected item is null
                        Log.d("HistoryAdapters", "Selected item is null at position: " + position);
                    }
                } else {
                    // Log if position is invalid
                    Log.d("HistoryAdapters", "Invalid position");
                }
            }
        });
    }

    // Return the size of dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }

    // Provide a reference to the views for each data item
    public class viewholder extends RecyclerView.ViewHolder {
        // UI elements to be displayed in RecyclerView
        TextView contentTxt, timeTxt, amountTxt;
        ImageView pic_2;
        ConstraintLayout reCard;

        // Constructor to initialize UI elements
        public viewholder(@NonNull View itemView) {
            super(itemView);
            contentTxt = itemView.findViewById(R.id.contentTxt);
            timeTxt = itemView.findViewById(R.id.timeTxt);
            amountTxt = itemView.findViewById(R.id.amountTxt);
            pic_2 = itemView.findViewById(R.id.pic_2);
            reCard = itemView.findViewById(R.id.reCard);
        }
    }
}
