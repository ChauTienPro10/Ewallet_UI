package com.example.ewallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class TranferMoney extends AppCompatActivity {
    private RecyclerView.Adapter adapterContact;
    private RecyclerView recyclerViewConatact;

    private LinearLayout btnSendMoney;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tranfer_money);

        initRecyclerView();

        btnSendMoney.findViewById(R.id.buttonSendMoney);
        btnSendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TranferMoney.this, PaymentProcessing.class);
                startActivity(intent);
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

}