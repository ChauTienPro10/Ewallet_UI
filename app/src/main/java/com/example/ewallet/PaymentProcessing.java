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

import Domain.BankDomain;

public class PaymentProcessing extends AppCompatActivity {
    private RecyclerView.Adapter adapterBank;
    private RecyclerView recyclerView;
    private ConstraintLayout btn_backProcessing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_processing);

        btn_backProcessing=findViewById(R.id.btn_back_processing);
        btn_backProcessing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentProcessing.this,TranferMoney.class);
                startActivity(intent);
            }
        });

        initRecycleview();
    }

    private void initRecycleview() {
        ArrayList<BankDomain> items = new ArrayList<>();

        items.add(new BankDomain("Bitcoin",100.00,"bitcoin"));
        items.add(new BankDomain("Ethereum",200.00,"ethereum"));
        items.add(new BankDomain("Trox",300.00,"trox"));

        recyclerView = findViewById(R.id.viewListBank);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        adapterBank = new BankAdapters(items);
        recyclerView.setAdapter(adapterBank);

    }
}