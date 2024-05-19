package com.example.ewallet;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ewallet.adapter.pindialogAdapter;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class opencard extends AppCompatActivity{
    private androidx.coordinatorlayout.widget.CoordinatorLayout mSubmit;
    private CheckBox mcheckbox;
    private EditText mpin,mauthenpin;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opencard);
        mSubmit=findViewById(R.id.submitlayout);
        mcheckbox=findViewById(R.id.checkBox);
        mpin=findViewById(R.id.pinInput);
        mauthenpin=findViewById(R.id.pininputauthen);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mcheckbox.isChecked()){
                    GradientDrawable borderDrawable = new GradientDrawable();
                    borderDrawable.setStroke(2, Color.RED); // Đặt độ dày và màu sắc cho border
                    borderDrawable.setColor(Color.WHITE); // Đặt màu nền cho EditText
                    mcheckbox.setBackground(borderDrawable);
                } else if (mpin.getText().toString().length()!=6) {
                    GradientDrawable borderDrawable = new GradientDrawable();
                    borderDrawable.setStroke(2, Color.RED); // Đặt độ dày và màu sắc cho border
                    borderDrawable.setColor(Color.WHITE); // Đặt màu nền cho EditText
                    mpin.setHint("incorrect");
                    mpin.setBackground(borderDrawable);
                } else if (mauthenpin.getText().toString().length()!=6 ||
                        !mauthenpin.getText().toString().equals(mpin.getText().toString())) {
                    GradientDrawable borderDrawable = new GradientDrawable();
                    borderDrawable.setStroke(2, Color.RED); // Đặt độ dày và màu sắc cho border
                    borderDrawable.setColor(Color.WHITE); // Đặt màu nền cho EditText
                    mauthenpin.setHint("incorrect");
                    mauthenpin.setBackground(borderDrawable);
                }
                else{
                    CreatenewCard(mpin.getText().toString());
                }
            }
        });
    }

    public void CreatenewCard(String pin){
        ProgressDialog progressDialog = new ProgressDialog(opencard.this);
        progressDialog.setMessage("Processing...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        JsonObject ob=new JsonObject();
        ob.addProperty("pincode",pin);
        ApiService apiService=ApiService.ApiUtils.getApiService(this);
        apiService.openCard(ob).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String res=response.body().string();
                    if(res.equals("Activate card for this account success")){
                        Intent intent=new Intent(opencard.this, MainActivity.class );
                        progressDialog.dismiss();
                        startActivity(intent);
                    }
                } catch (IOException e) {
                    progressDialog.dismiss();
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }


}