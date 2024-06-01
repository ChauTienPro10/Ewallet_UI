package com.example.ewallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ewallet.adapter.ContactsAdapter;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import Entities.Card;
import Entities.Member;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TranferMoney extends AppCompatActivity {
    // Declare UI elements
    private TextView mIdCard;
    private TextView mName;
    private androidx.appcompat.widget.SearchView mSearch;
    private RecyclerView recyclerViewConatact;
    private RecyclerView.Adapter adapterContact;
    private LinearLayout btnSendMoney;
    private ConstraintLayout btn_back_send_money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tranfer_money);

        // Initialize UI elements
        mSearch=findViewById(R.id.searchView);
        btn_back_send_money=findViewById(R.id.btn_back_send_money);
        mIdCard=findViewById(R.id.textView10);
        mName=findViewById(R.id.textView20);

        // Initialize RecyclerView
        initRecyclerView();

        // Set click listener for back button
        btn_back_send_money.setOnClickListener(v -> startActivity(new Intent(TranferMoney.this, MainActivity.class)));

        // Fetch card details and set up UI
        init();

        // Set click listener for send money button
        btnSendMoney=findViewById(R.id.buttonSendMoney);
        btnSendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to payment processing page
                Intent intent = new Intent(TranferMoney.this, PaymentProcessing.class);
                startActivity(intent);
            }
        });

        // Set up search functionality
        mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform member search
                findMember();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    // Initialize RecyclerView with sample data
    private void initRecyclerView() {
        ArrayList<ContactsDomain> items=new ArrayList<>();
        // Add sample contacts
        items.add(new ContactsDomain("David","user_1"));
        items.add(new ContactsDomain("Alice","user_2"));
        items.add(new ContactsDomain("Rose","user_3"));
        items.add(new ContactsDomain("Sara","user_4"));
        items.add(new ContactsDomain("David","user_5"));

        // Set up RecyclerView
        recyclerViewConatact=findViewById(R.id.viewList);
        recyclerViewConatact.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        // Set adapter for RecyclerView
        adapterContact=new ContactsAdapter(items);
        recyclerViewConatact.setAdapter(adapterContact);
    }

    // Search for a member
    private void findMember(){
        // Show progress dialog
        ProgressDialog progressDialog = new ProgressDialog(TranferMoney.this);
        progressDialog.setMessage("waiting...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        // Prepare search query
        JsonObject obj =new JsonObject();
        obj.addProperty("infor_data",mSearch.getQuery().toString());

        // Initialize API service
        ApiService apiService =ApiService.ApiUtils.getApiService(TranferMoney.this);
        // Perform member search
        apiService.searhMember(obj).enqueue(new Callback<Member>() {
            @Override
            public void onResponse(Call<Member> call, Response<Member> response) {
                // Process response
                Member member=response.body();
                if(member!=null){
                    // Navigate to member information page if member is found
                    Intent intent=new Intent(TranferMoney.this, InforTranfer.class );
                    intent.putExtra("name", member.getFname()+" "+member.getLname());
                    intent.putExtra("phone",member.getPhone());
                    progressDialog.dismiss();
                    startActivity(intent);
                }
                else{
                    // Show message if member is not found
                    Toast.makeText(TranferMoney.this, "Member doesn't exist!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Member> call, Throwable t) {
                // Handle failure
                progressDialog.dismiss();
            }
        });
    }

    // Initialize card details
    private void init(){
        // Show progress dialog
        ProgressDialog progressDialog = new ProgressDialog(TranferMoney.this);
        progressDialog.setMessage("waiting...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        // Initialize API service
        ApiService apiService=ApiService.ApiUtils.getApiService(TranferMoney.this);
        // Fetch card details from API
        apiService.getCard().enqueue(new Callback<Card>() {
            @Override
            public void onResponse(Call<Card> call, Response<Card> response) {
                // Process response
                Card card=response.body();
                if(card!=null){
                    // Update UI with card details
                    mIdCard.setText(addSpaceEveryFourCharacters(card.getCard_number()));
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    mName.setText(sharedPreferences.getString("fullName",""));
                }
                // Dismiss progress dialog
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Card> call, Throwable t) {
                // Handle failure
            }
        });
    }

    // Utility method to add spaces every four characters to a string
    public  String addSpaceEveryFourCharacters(String input) {
        StringBuilder builder = new StringBuilder();
        int count = 0;

        for (int i = 0; i < input.length(); i++) {
            if (count == 4) {
                builder.append(' '); // Add space after every four characters
                count = 0;
            }
            builder.append(input.charAt(i));
            count++;
        }

        return builder.toString();
    }

}
