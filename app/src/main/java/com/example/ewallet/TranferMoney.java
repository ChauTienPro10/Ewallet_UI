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
    private TextView mIdCard;
    private TextView mName;
    private RecyclerView.Adapter adapterContact;
    private RecyclerView recyclerViewConatact;
    private androidx.appcompat.widget.SearchView mSearch;

    private LinearLayout btnSendMoney;
    private ConstraintLayout btn_back_send_money;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tranfer_money);
        mSearch=findViewById(R.id.searchView);
        btn_back_send_money=findViewById(R.id.btn_back_send_money);
        btn_back_send_money.setOnClickListener(v -> startActivity(new Intent(TranferMoney.this, MainActivity.class)));
        mIdCard=findViewById(R.id.textView10);
        mName=findViewById(R.id.textView20);
        init();
        initRecyclerView();

        btnSendMoney=findViewById(R.id.buttonSendMoney);
        btnSendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TranferMoney.this, PaymentProcessing.class);
                startActivity(intent);
            }
        });

        mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                findMember();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


    }

    private void initRecyclerView() {
        ArrayList<ContactsDomain> items=new ArrayList<>();
        items.add(new ContactsDomain("David","user_1"));
        items.add(new ContactsDomain("Alice","user_2"));
        items.add(new ContactsDomain("Rose","user_3"));
        items.add(new ContactsDomain("Sara","user_4"));
        items.add(new ContactsDomain("David","user_5"));

        recyclerViewConatact=findViewById(R.id.viewList);
        recyclerViewConatact.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        adapterContact=new ContactsAdapter(items);
        recyclerViewConatact.setAdapter(adapterContact);
    }

    private void findMember(){
        ProgressDialog progressDialog = new ProgressDialog(TranferMoney.this);
        progressDialog.setMessage("waiting...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        JsonObject obj =new JsonObject();
        obj.addProperty("infor_data",mSearch.getQuery().toString());
        ApiService apiService =ApiService.ApiUtils.getApiService(TranferMoney.this);
        apiService.searhMember(obj).enqueue(new Callback<Member>() {
            @Override
            public void onResponse(Call<Member> call, Response<Member> response) {
                Member member=response.body();
                if(member!=null){
                    Toast.makeText(TranferMoney.this, mSearch.getQuery().toString(), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(TranferMoney.this, InforTranfer.class );
                    intent.putExtra("name", member.getFname()+" "+member.getLname());
                    intent.putExtra("phone",member.getPhone());
                    progressDialog.dismiss();
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Member> call, Throwable t) {
                progressDialog.dismiss();
            }
        });

    }
    private void init(){
        ProgressDialog progressDialog = new ProgressDialog(TranferMoney.this);
        progressDialog.setMessage("waiting...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        ApiService apiService=ApiService.ApiUtils.getApiService(TranferMoney.this);
        apiService.getCard().enqueue(new Callback<Card>() {
            @Override
            public void onResponse(Call<Card> call, Response<Card> response) {
                Card card=response.body();
                if(card!=null){
                    mIdCard.setText(addSpaceEveryFourCharacters(card.getCard_number()));
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    mName.setText(sharedPreferences.getString("fullName",""));
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Card> call, Throwable t) {

            }
        });
    }

    public  String addSpaceEveryFourCharacters(String input) {
        StringBuilder builder = new StringBuilder();
        int count = 0;

        for (int i = 0; i < input.length(); i++) {
            if (count == 4) {
                builder.append(' ');
                count = 0;
            }
            builder.append(input.charAt(i));
            count++;
        }

        return builder.toString();
    }

}