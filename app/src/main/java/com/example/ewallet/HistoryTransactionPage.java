package com.example.ewallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.example.ewallet.adapter.HistoryAdapters;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.example.ewallet.Domain.HistoryDomain;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryTransactionPage extends AppCompatActivity {
    ArrayList<HistoryDomain> items = new ArrayList<>();
    private RecyclerView.Adapter adapterTransaction;
    ConstraintLayout btn_back_history;
    private  RecyclerView recyclerView;
    private androidx.appcompat.widget.SearchView mSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_transaction_page);
        mSearch=findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.viewListTransHistory);

        setVariable();

        getDtHistory();
//       initRecycleView();
        mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                findHistory();
                Toast.makeText(HistoryTransactionPage.this, mSearch.getQuery().toString(), Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


    }

    private void initRecycleView() {

        items.add(new HistoryDomain("","00:00-05/05/2024","Transfer money to Phat Tien","", BigDecimal.valueOf(10), R.drawable.money_bill_transfer_solid));
        items.add(new HistoryDomain("","00:00-05/05/2024","Transfer money to Phat Tien","",BigDecimal.valueOf(10),R.drawable.money_bill_transfer_solid));
        items.add(new HistoryDomain("","00:00-05/05/2024","Transfer money to Phat Tien","",BigDecimal.valueOf(10),R.drawable.btn_1));
        items.add(new HistoryDomain("","00:00-05/05/2024","Transfer money to Phat Tien","",BigDecimal.valueOf(10),R.drawable.btn_2));
        items.add(new HistoryDomain("","00:00-05/05/2024","Transfer money to Phat Tien","",BigDecimal.valueOf(10),R.drawable.btn_1));
        items.add(new HistoryDomain("","00:00-05/05/2024","Transfer money to Phat Tien","",BigDecimal.valueOf(10),R.drawable.btn_2));

        recyclerView = findViewById(R.id.viewListTransHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        adapterTransaction = new HistoryAdapters(items,HistoryTransactionPage.this);

            recyclerView.setAdapter(adapterTransaction);



    }

    private void setVariable() {
         btn_back_history = findViewById(R.id.btn_back_history);
        btn_back_history.setOnClickListener(v -> startActivity(new Intent(HistoryTransactionPage.this,MainActivity.class)));
    }

    private void getDtHistory(){
        ProgressDialog progressDialog = new ProgressDialog(HistoryTransactionPage.this);

        progressDialog.setMessage("waiting...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        ApiService apiService=ApiService.ApiUtils.getApiService(HistoryTransactionPage.this);
        apiService.getHistory().enqueue(new Callback<ArrayList<HistoryDomain>>() {
            @Override
            public void onResponse(Call<ArrayList<HistoryDomain>> call, Response<ArrayList<HistoryDomain>> response) {
                for(HistoryDomain item: response.body()){
                    items.add(new HistoryDomain(item.getType(),item.getTime(),item.getContent(),item.getReceiver(),item.getAmount(),0));
                }
                progressDialog.dismiss();
                Toast.makeText(HistoryTransactionPage.this, "Success"+ items.size(), Toast.LENGTH_SHORT).show();

                recyclerView.setLayoutManager(new LinearLayoutManager(HistoryTransactionPage.this,LinearLayoutManager.VERTICAL,false));
                adapterTransaction = new HistoryAdapters(items,HistoryTransactionPage.this);
                recyclerView.setAdapter(adapterTransaction);
            }

            @Override
            public void onFailure(Call<ArrayList<HistoryDomain>> call, Throwable t) {
                Toast.makeText(HistoryTransactionPage.this, "failure", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void findHistory(){
        ProgressDialog progressDialog = new ProgressDialog(HistoryTransactionPage.this);

        progressDialog.setMessage("waiting...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        items = new ArrayList<>();
        JsonObject obj =new JsonObject();
        obj.addProperty("content",  mSearch.getQuery().toString());
        ApiService _apiService=ApiService.ApiUtils.getApiService(HistoryTransactionPage.this);
        _apiService.findHistory(obj).enqueue(new Callback<ArrayList<HistoryDomain>>() {
            @Override
            public void onResponse(Call<ArrayList<HistoryDomain>> call, Response<ArrayList<HistoryDomain>> response) {
                for(HistoryDomain item: response.body()){
                    items.add(new HistoryDomain(item.getType(),item.getTime(),item.getContent(),item.getReceiver(),item.getAmount(),0));
                }
                progressDialog.dismiss();
                Toast.makeText(HistoryTransactionPage.this, "Done"+ items.size(), Toast.LENGTH_SHORT).show();
                recyclerView = findViewById(R.id.viewListTransHistory);
                recyclerView.setLayoutManager(new LinearLayoutManager(HistoryTransactionPage.this,LinearLayoutManager.VERTICAL,false));
                adapterTransaction = new HistoryAdapters(items,HistoryTransactionPage.this);
                recyclerView.setAdapter(adapterTransaction);
            }

            @Override
            public void onFailure(Call<ArrayList<HistoryDomain>> call, Throwable t) {
                Toast.makeText(HistoryTransactionPage.this, "Error", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}