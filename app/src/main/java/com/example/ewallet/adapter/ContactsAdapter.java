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
import com.example.ewallet.ContactsDomain;
import com.example.ewallet.R;

import java.util.ArrayList;


public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.Viewholder> {
    // ArrayList to hold contact items
    ArrayList<ContactsDomain> items;
    Context context;

    // Constructor to initialize the adapter with contact items
    public ContactsAdapter(ArrayList<ContactsDomain> items) {
        this.items = items;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ContactsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        context = parent.getContext();
        return new Viewholder(inflate);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull ContactsAdapter.Viewholder holder, int position) {
        // Get element from dataset at this position and replace the contents of the view with that element
        holder.name.setText(items.get(position).getName());

        // Load image using Glide library
        int drawableResourceId = holder.itemView.getResources().getIdentifier(items.get(position).getPicPath(), "drawable", holder.itemView.getContext().getPackageName());
        Glide.with(holder.itemView.getContext()).load(drawableResourceId).into(holder.pic);
    }

    // Return the size of dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }

    // Provide a reference to the views for each data item
    public class Viewholder extends RecyclerView.ViewHolder {
        // UI elements to be displayed in RecyclerView
        TextView name;
        ImageView pic;

        // Constructor to initialize UI elements
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.titleTxt);
            pic = itemView.findViewById(R.id.pic);
        }
    }
}
