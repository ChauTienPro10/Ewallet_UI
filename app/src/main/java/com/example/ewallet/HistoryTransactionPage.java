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
    // Declare UI components
    private ArrayList<HistoryDomain> items = new ArrayList<>(); // List to store transaction history items
    private RecyclerView.Adapter adapterTransaction;
    private ConstraintLayout btn_back_history;
    private RecyclerView recyclerView;
    private SearchView mSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_transaction_page);

        // Initialize UI components
        mSearch = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.viewListTransHistory);

        // Set up back button and its listener
        setVariable();

        // Fetch transaction history data
        getDtHistory();

        // Set up SearchView query listener
        mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                findHistory(); // Perform search when query is submitted
                Toast.makeText(HistoryTransactionPage.this, mSearch.getQuery().toString(), Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false; // No action needed on text change
            }
        });
    }

    // Method to initialize RecyclerView with sample data
    private void initRecycleView() {
        // Add sample transaction history items
        items.add(new HistoryDomain("", "00:00-05/05/2024", "Transfer money to Phat Tien", "", BigDecimal.valueOf(10), R.drawable.money_bill_transfer_solid));
        items.add(new HistoryDomain("", "00:00-05/05/2024", "Transfer money to Phat Tien", "", BigDecimal.valueOf(10), R.drawable.money_bill_transfer_solid));
        items.add(new HistoryDomain("", "00:00-05/05/2024", "Transfer money to Phat Tien", "", BigDecimal.valueOf(10), R.drawable.btn_1));
        items.add(new HistoryDomain("", "00:00-05/05/2024", "Transfer money to Phat Tien", "", BigDecimal.valueOf(10), R.drawable.btn_2));
        items.add(new HistoryDomain("", "00:00-05/05/2024", "Transfer money to Phat Tien", "", BigDecimal.valueOf(10), R.drawable.btn_1));
        items.add(new HistoryDomain("", "00:00-05/05/2024", "Transfer money to Phat Tien", "", BigDecimal.valueOf(10), R.drawable.btn_2));

        // Set up RecyclerView with LinearLayoutManager and adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapterTransaction = new HistoryAdapters(items, HistoryTransactionPage.this);
        recyclerView.setAdapter(adapterTransaction);
    }

    // Method to set up back button functionality
    private void setVariable() {
        btn_back_history = findViewById(R.id.btn_back_history);
        btn_back_history.setOnClickListener(v -> startActivity(new Intent(HistoryTransactionPage.this, MainActivity.class)));
    }

    // Method to fetch transaction history from the server
    private void getDtHistory() {
        // Show a progress dialog while fetching data
        ProgressDialog progressDialog = new ProgressDialog(HistoryTransactionPage.this);
        progressDialog.setMessage("waiting...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        // Get the API service instance
        ApiService apiService = ApiService.ApiUtils.getApiService(HistoryTransactionPage.this);

        // Make an asynchronous call to fetch transaction history
        apiService.getHistory().enqueue(new Callback<ArrayList<HistoryDomain>>() {
            @Override
            public void onResponse(Call<ArrayList<HistoryDomain>> call, Response<ArrayList<HistoryDomain>> response) {
                for (HistoryDomain item : response.body()) {
                    items.add(new HistoryDomain(item.getType(), item.getTime(), item.getContent(), item.getReceiver(), item.getAmount(), 0));
                }
                progressDialog.dismiss(); // Dismiss the progress dialog
                Toast.makeText(HistoryTransactionPage.this, "Success" + items.size(), Toast.LENGTH_SHORT).show();

                // Set up RecyclerView with fetched data
                recyclerView.setLayoutManager(new LinearLayoutManager(HistoryTransactionPage.this, LinearLayoutManager.VERTICAL, false));
                adapterTransaction = new HistoryAdapters(items, HistoryTransactionPage.this);
                recyclerView.setAdapter(adapterTransaction);
            }

            @Override
            public void onFailure(Call<ArrayList<HistoryDomain>> call, Throwable t) {
                Toast.makeText(HistoryTransactionPage.this, "failure", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss(); // Dismiss the progress dialog
            }
        });
    }

    // Method to search transaction history based on query
    private void findHistory() {
        // Show a progress dialog while searching
        ProgressDialog progressDialog = new ProgressDialog(HistoryTransactionPage.this);
        progressDialog.setMessage("waiting...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        // Clear the current items list
        items = new ArrayList<>();

        // Create a JSON object with the search query
        JsonObject obj = new JsonObject();
        obj.addProperty("content", mSearch.getQuery().toString());

        // Get the API service instance
        ApiService _apiService = ApiService.ApiUtils.getApiService(HistoryTransactionPage.this);

        // Make an asynchronous call to search transaction history
        _apiService.findHistory(obj).enqueue(new Callback<ArrayList<HistoryDomain>>() {
            @Override
            public void onResponse(Call<ArrayList<HistoryDomain>> call, Response<ArrayList<HistoryDomain>> response) {
                for (HistoryDomain item : response.body()) {
                    items.add(new HistoryDomain(item.getType(), item.getTime(), item.getContent(), item.getReceiver(), item.getAmount(), 0));
                }
                progressDialog.dismiss(); // Dismiss the progress dialog
                Toast.makeText(HistoryTransactionPage.this, "Done" + items.size(), Toast.LENGTH_SHORT).show();

                // Set up RecyclerView with searched data
                recyclerView.setLayoutManager(new LinearLayoutManager(HistoryTransactionPage.this, LinearLayoutManager.VERTICAL, false));
                adapterTransaction = new HistoryAdapters(items, HistoryTransactionPage.this);
                recyclerView.setAdapter(adapterTransaction);
            }

            @Override
            public void onFailure(Call<ArrayList<HistoryDomain>> call, Throwable t) {
                Toast.makeText(HistoryTransactionPage.this, "Error", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss(); // Dismiss the progress dialog
            }
        });
    }
}
