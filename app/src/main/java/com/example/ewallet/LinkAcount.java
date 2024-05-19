package com.example.ewallet;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ewallet.adapter.pindialogAdapter;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LinkAcount extends AppCompatActivity implements pindialogAdapter.PinDialogListener{
    private com.google.android.material.textfield.TextInputEditText mAddress,mKey,mPassword;
    private CheckBox mCheckbox;
    private View mSubmit;
    pindialogAdapter dialogFragment = new pindialogAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_acount);
        mAddress=findViewById(R.id.edit_text_input);
        mKey=findViewById(R.id.acount_name);

        mCheckbox=findViewById(R.id.checkBox);
        mSubmit=findViewById(R.id.btn_continue);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mAddress.getText().toString().contains("0x")){
                    GradientDrawable borderDrawable = new GradientDrawable();
                    borderDrawable.setStroke(2, Color.RED); // Đặt độ dày và màu sắc cho border
                    borderDrawable.setColor(Color.WHITE); // Đặt màu nền cho EditText
                    mAddress.setBackground(borderDrawable);

                } else if (!mKey.getText().toString().contains("0x")) {
                    GradientDrawable borderDrawable = new GradientDrawable();
                    borderDrawable.setStroke(2, Color.RED); // Đặt độ dày và màu sắc cho border
                    borderDrawable.setColor(Color.WHITE); // Đặt màu nền cho EditText
                    mAddress.setBackground(borderDrawable);

                } else if (!mCheckbox.isChecked()) {
                    GradientDrawable borderDrawable = new GradientDrawable();
                    borderDrawable.setStroke(2, Color.RED); // Đặt độ dày và màu sắc cho border
                    borderDrawable.setColor(Color.WHITE); // Đặt màu nền cho EditText
                    mCheckbox.setBackground(borderDrawable);
                } else{
                    showPinDialog();
                }

            }
        });



    }
    private void link(String pin){
        ProgressDialog progressDialog = new ProgressDialog(LinkAcount.this);
        progressDialog.setMessage("Processing...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("address",mAddress.getText().toString());
        jsonObject.addProperty("pin",pin);
        jsonObject.addProperty("key",mKey.getText().toString());
        ApiService apiService=ApiService.ApiUtils.getApiService(this);
        apiService.linktoETH(jsonObject).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String res=response.body().string();
                    if(res.equals("link success!")){
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("AddETH",mAddress.getText().toString().substring(0,10)+"...");
                        editor.apply();
                        progressDialog.dismiss();
                        Intent intent = new Intent(LinkAcount.this, ETH_Wallet.class);
                        startActivity(intent);
                    }
                    Toast.makeText(LinkAcount.this, res, Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(LinkAcount.this, "Try again!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onPinEntered(String pin) {
        Log.d("pincode", "onPinEntered: "+pin);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("pincode", pin);
        ApiService apiService=ApiService.ApiUtils.getApiService(LinkAcount.this);
        apiService.authenPin(jsonObject).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.body()==true){
                    link(pin);
                }
                else{
                    Toast.makeText(LinkAcount.this, "failure" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }

    @Override
    public void onAuthentication() {
        getPincode();
    }
    private void showPinDialog() {
        try{
            dialogFragment.show(getSupportFragmentManager(), "pin_dialog");
        }
        catch (Exception e){
            Log.d("dialogTien", "showPinDialog: "+e.toString());
        }
    }
    private void getPincode(){
        ProgressDialog progressDialog_getPin = new ProgressDialog(this);
        progressDialog_getPin.setMessage("Processing...");
        progressDialog_getPin.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog_getPin.show();
        ApiService apiService=ApiService.ApiUtils.getApiService(this);
        apiService.getPin().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String pin=response.body().string();
                    if(!pin.equals("can't authentication")){
                        onPinEntered(pin);
                        progressDialog_getPin.dismiss();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog_getPin.dismiss();
            }
        });

    }
}