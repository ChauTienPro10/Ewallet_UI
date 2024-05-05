package com.example.ewallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;


import com.example.ewallet.adapter.HistoryAdapters;

import java.util.ArrayList;

import Domain.HistoryDomain;

public class HistoryTransactionPage extends AppCompatActivity {
    private RecyclerView.Adapter adapterTransaction;
    ConstraintLayout btn_back_history;
    private  RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_transaction_page);


            setVariable();
            initRecycleView();


    }

    private void initRecycleView() {
        ArrayList<HistoryDomain> items = new ArrayList<>();
        items.add(new HistoryDomain("","00:00-05/05/2024","Transfer money to Phat Tien","",1000.00,"bitcoin"));
        items.add(new HistoryDomain("","00:00-05/05/2024","Transfer money to Phat Tien","",1000.00,"bitcoin"));
        items.add(new HistoryDomain("","00:00-05/05/2024","Transfer money to Phat Tien","",1000.00,"bitcoin"));
        items.add(new HistoryDomain("","00:00-05/05/2024","Transfer money to Phat Tien","",1000.00,"bitcoin"));
        items.add(new HistoryDomain("","00:00-05/05/2024","Transfer money to Phat Tien","",1000.00,"bitcoin"));

        recyclerView = findViewById(R.id.viewListTransHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        adapterTransaction = new HistoryAdapters(items);

            recyclerView.setAdapter(adapterTransaction);



    }

    private void setVariable() {
         btn_back_history = findViewById(R.id.btn_back_history);
        btn_back_history.setOnClickListener(v -> startActivity(new Intent(HistoryTransactionPage.this,MainActivity.class)));
    }
}