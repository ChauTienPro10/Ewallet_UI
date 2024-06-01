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
    ArrayList<HistoryDomain> items;
    Context context;

    public HistoryAdapters(ArrayList<HistoryDomain> items,  Context context) {
        this.context=context;
        this.items = items;
    }

    @NonNull
    @Override
    public HistoryAdapters.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_history_transaction,parent,false);


        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapters.viewholder holder, int position) {

        BigDecimal amount = items.get(position).getAmount();
        String amountString = amount.toString();
        holder.contentTxt.setText(items.get(position).getContent());
        holder.timeTxt.setText("Date"+items.get(position).getTime());
        holder.amountTxt.setText(amountString+" vnd");
        holder.pic_2.setImageResource(items.get(position).getPicPath());


//        int drawableResourceId = holder.itemView.getResources()
//                .getIdentifier(items.get(position).getPicPath(),"drawable",holder.itemView.getContext().getPackageName());
//
//        Glide.with(context)
//                .load(drawableResourceId)
//                .into(holder.pic_2);

        holder.reCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    HistoryDomain selectedItem = items.get(position);
                    if (selectedItem != null) {
                        Intent intent = new Intent(context, HistoryTransactionDetail.class);
                        intent.putExtra("Image", selectedItem.getPicPath());
                        intent.putExtra("Content", selectedItem.getContent());
                        intent.putExtra("Time", selectedItem.getTime());
                        intent.putExtra("Amount", (selectedItem.getAmount()).toString()+"$");
                        context.startActivity(intent);
                    } else {
                        Log.d("HistoryAdapters", "Selected item is null at position: " + position);
                    }
                } else {
                    Log.d("HistoryAdapters", "Invalid position");
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        TextView contentTxt,timeTxt,amountTxt;
        ImageView pic_2;
        ConstraintLayout reCard;
        public viewholder(@NonNull View itemView) {
            super(itemView);

            contentTxt=itemView.findViewById(R.id.contentTxt);
            timeTxt=itemView.findViewById(R.id.timeTxt);
            amountTxt=itemView.findViewById(R.id.amountTxt);
            pic_2=itemView.findViewById(R.id.pic_2);
            reCard=itemView.findViewById(R.id.reCard);
        }
    }
}
