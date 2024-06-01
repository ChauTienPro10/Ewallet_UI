package com.example.ewallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.ewallet.adapter.BankAdapters;

import java.util.ArrayList;

import com.example.ewallet.Domain.BankDomain;

public class PaymentProcessing extends AppCompatActivity {
    // Declare UI elements
    private RecyclerView.Adapter adapterBank;
    private RecyclerView recyclerView;
    private ConstraintLayout btn_backProcessing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_processing);

        // Initialize UI elements
        btn_backProcessing=findViewById(R.id.btn_back_processing);

        // Set onClickListener for back button
        btn_backProcessing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to TransferMoney activity
                Intent intent = new Intent(PaymentProcessing.this,TranferMoney.class);
                startActivity(intent);
            }
        });

        // Initialize RecyclerView
        initRecycleview();
    }

    // Method to initialize RecyclerView
    private void initRecycleview() {
        // Create a list of BankDomain objects
        ArrayList<BankDomain> items = new ArrayList<>();
        items.add(new BankDomain("Bitcoin",100.00,"bitcoin"));
        items.add(new BankDomain("Ethereum",200.00,"ethereum"));
        items.add(new BankDomain("Trox",300.00,"trox"));

        // Find RecyclerView from layout
        recyclerView = findViewById(R.id.viewListBank);
        // Set RecyclerView layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        // Create and set adapter for RecyclerView
        adapterBank = new BankAdapters(items);
        recyclerView.setAdapter(adapterBank);
    }
}
