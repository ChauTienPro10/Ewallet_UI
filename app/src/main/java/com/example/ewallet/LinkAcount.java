package com.example.ewallet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

public class LinkAcount extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_acount);
        EditText editText = findViewById(R.id.edit_text_input);
        TextInputLayout til = (TextInputLayout) findViewById(R.id.text_input_layout_acount);

        EditText editName = findViewById(R.id.acount_name);
        TextInputLayout tilName = (TextInputLayout) findViewById(R.id.text_input_layout_name);
        editName.setText("Trần Hữu Tuấn");
        editName.setInputType(InputType.TYPE_NULL);


        EditText editID = findViewById(R.id.Cmnd_Cccd);
        TextInputLayout tilID = (TextInputLayout) findViewById(R.id.text_input_layout_ID);

        til.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() < 1) {
                    til.setErrorEnabled(true);
                    til.setError("Quý khách chưa nhập số tài khoản");
                }
                if (s.length() > 0) {
                    til.setError(null);
                    til.setErrorEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    til.setHint("Số tài khoản");
                } else {
                    til.setHint("Nhập số tài khoản");
                }
            }
        });


        tilID.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() < 1) {
                    tilID.setErrorEnabled(true);
                    tilID.setError("Quý khách chưa nhập CMND/CCCD");
                }
                if (s.length() > 0) {
                    tilID.setError(null);
                    tilID.setErrorEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editID.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tilID.setHint("CMND/CCCD");
                } else {
                    tilID.setHint("Nhập CMND/CCCD");
                }
            }
        });
    }



}