package com.example.ewallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.ParseException;

public class InforTranfer extends AppCompatActivity {
    private TextView mName;
    private TextView mPhone;
    final DecimalFormat decimalFormat = new DecimalFormat("#,###");
    private TextView mAmount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor_tranfer);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String phone=intent.getStringExtra("phone");
        mName=findViewById(R.id.textView29);
        mPhone=findViewById(R.id.textView43);
        mName.setText(name);
        mPhone.setText(phone);
        mAmount=findViewById(R.id.editTextText);
        mAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mAmount.removeTextChangedListener(this);

                try {
                    String originalString = s.toString().replaceAll(",", "");

                    double value = decimalFormat.parse(originalString).doubleValue();

                    String formattedString = decimalFormat.format(value).replaceAll("\\.",",");
                    mAmount.setText(formattedString+" vnd");

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                mAmount.addTextChangedListener(this);
            }
        });
    }
}