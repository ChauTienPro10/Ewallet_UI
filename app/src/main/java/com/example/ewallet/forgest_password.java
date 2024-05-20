package com.example.ewallet;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class forgest_password extends AppCompatActivity {
    private EditText mEmail,mCode,mnewpass,mPassAgain;
    private Button mGetCode, mSubmit;
    private ProgressBar mproBar1,mProBar2;
    private androidx.cardview.widget.CardView card1,card2;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgest_password);
        mEmail=findViewById(R.id.email);
        mCode=findViewById(R.id.code);
        mnewpass=findViewById(R.id.newpassword);
        mPassAgain=findViewById(R.id.pass_again);
        mGetCode=findViewById(R.id.getcode);
        mSubmit=findViewById(R.id.Submit);
        mproBar1=findViewById(R.id.progressBar);
        mProBar2=findViewById(R.id.progressBar2);
        mproBar1.setVisibility(View.GONE);
        mProBar2.setVisibility(View.GONE);
        card1=findViewById(R.id.cardview1);
        card2=findViewById(R.id.cardview2);
        card2.setVisibility(View.GONE);
        mGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEmail.getText().toString().equals("")){
                    GradientDrawable borderDrawable = new GradientDrawable();
                    borderDrawable.setStroke(2, Color.RED); // Đặt độ dày và màu sắc cho border
                    borderDrawable.setColor(Color.WHITE); // Đặt màu nền cho EditText
                    mEmail.setBackground(borderDrawable);
                }
                else{
                    getcode();
                }

            }
        });
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCode.getText().toString().equals("")){
                    GradientDrawable borderDrawable = new GradientDrawable();
                    borderDrawable.setStroke(2, Color.RED); // Đặt độ dày và màu sắc cho border
                    borderDrawable.setColor(Color.WHITE); // Đặt màu nền cho EditText
                    mCode.setBackground(borderDrawable);
                } else if (mnewpass.getText().toString().equals("")) {
                    GradientDrawable borderDrawable = new GradientDrawable();
                    borderDrawable.setStroke(2, Color.RED); // Đặt độ dày và màu sắc cho border
                    borderDrawable.setColor(Color.WHITE); // Đặt màu nền cho EditText
                    mnewpass.setBackground(borderDrawable);
                } else if (!mPassAgain.getText().toString().equals(mnewpass.getText().toString())) {
                    GradientDrawable borderDrawable = new GradientDrawable();
                    borderDrawable.setStroke(2, Color.RED); // Đặt độ dày và màu sắc cho border
                    borderDrawable.setColor(Color.WHITE); // Đặt màu nền cho EditText
                    mPassAgain.setBackground(borderDrawable);
                }
                else{
                    changePass();

                }
            }
        });

    }
    private void getcode(){
        mproBar1.setVisibility(View.VISIBLE);
        JsonObject js=new JsonObject();
        js.addProperty("email",mEmail.getText().toString());
        ApiService apiService=ApiService.ApiUtils.getApiService(this);
        apiService.getCode(js).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String res=response.body().string();
                    if(res.equals("Mail Sent Successfully...")){
                        card1.setVisibility(View.GONE);
                        card2.setVisibility(View.VISIBLE);
                        mproBar1.setVisibility(View.GONE);
                    }
                    else{
                        mproBar1.setVisibility(View.GONE);
                    }
                } catch (IOException e) {
                    mproBar1.setVisibility(View.GONE);
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mproBar1.setVisibility(View.GONE);
            }
        });

    }
    private void changePass(){
        mProBar2.setVisibility(View.VISIBLE);
        JsonObject js=new JsonObject();
        js.addProperty("email",mEmail.getText().toString());
        js.addProperty("code",mCode.getText().toString());
        js.addProperty("newpassword",mnewpass.getText().toString());
        ApiService apiService=ApiService.ApiUtils.getApiService(this);
        apiService.authen_changepassword(js).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String res=response.body().string();
                    if(res.equals("ok")){
                        mProBar2.setVisibility(View.GONE);
                        Intent intent=new Intent(forgest_password.this,LoginPage.class);
                        startActivity(intent);
                    }
                } catch (IOException e) {
                    mProBar2.setVisibility(View.GONE);
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mProBar2.setVisibility(View.GONE);
            }
        });
    }
}