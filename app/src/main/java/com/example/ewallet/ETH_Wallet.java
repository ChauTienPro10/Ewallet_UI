package com.example.ewallet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ewallet.adapter.pindialogAdapter;
import com.google.gson.JsonObject;

import java.io.IOException;

import Entities.EtherWallet;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ETH_Wallet extends AppCompatActivity implements pindialogAdapter.PinDialogListener {
    ConstraintLayout btn_back_eth;
    TextView mAddress;
    private TextView mTotal;
    TextView mName;
    private Button mLink;
    pindialogAdapter dialogFragment = new pindialogAdapter();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eth_wallet);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        mTotal=findViewById(R.id.textView48);
        mLink=findViewById(R.id.linkButton);
        mName=findViewById(R.id.textName);
        mName.setText(sharedPreferences.getString("fullName", ""));
        btn_back_eth=findViewById(R.id.btn_back_eth);
        mAddress=findViewById(R.id.check);
        btn_back_eth.setOnClickListener(v -> startActivity(new Intent(ETH_Wallet.this, MainActivity.class)));
        if(!sharedPreferences.getString("AddETH", "").equals("")){
            mAddress.setText(sharedPreferences.getString("AddETH", "").substring(0,10)+"...");
            mTotal.setText(sharedPreferences.getString("totalETH", "")+ " eth");
        }
        else{
            showPinDialog();

        }
        mLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ETH_Wallet.this, LinkAcount.class);
                startActivity(intent);
            }
        });

    }


    
    private void getInfETH(){
        ProgressDialog progressDialog = new ProgressDialog(ETH_Wallet.this);
        progressDialog.setMessage("Processing...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        ApiService apiService=ApiService.ApiUtils.getApiService(ETH_Wallet.this);
        apiService.getInfEth().enqueue(new Callback<EtherWallet>() {
            @Override
            public void onResponse(Call<EtherWallet> call, Response<EtherWallet> response) {
                EtherWallet etherwallet=response.body();
                if (etherwallet!=null){
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("AddETH",etherwallet.getAddress().substring(0,10)+"...");
                    editor.apply();
                    Toast.makeText(ETH_Wallet.this, "success", Toast.LENGTH_SHORT).show();
                    mAddress.setText(sharedPreferences.getString("AddETH", "").substring(0,10)+"...");
                    getbalance(apiService);
                    progressDialog.dismiss();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<EtherWallet> call, Throwable t) {

            }
        });
    }

    @Override
    public void onPinEntered(String pin) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("pincode", pin);
        ApiService apiService=ApiService.ApiUtils.getApiService(ETH_Wallet.this);
        apiService.authenPin(jsonObject).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                getInfETH();
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
        ProgressDialog progressDialog_getPin = new ProgressDialog(ETH_Wallet.this);
        progressDialog_getPin.setMessage("Processing...");
        progressDialog_getPin.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog_getPin.show();
        ApiService apiService=ApiService.ApiUtils.getApiService(ETH_Wallet.this);
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


    private void getbalance(ApiService apiService){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_authen_pass, null);
        EditText editText = dialogView.findViewById(R.id.edit_text);

        builder.setView(dialogView)
                .setTitle("Enter Password")
                .setPositiveButton("OK", (dialog, which) -> {

                   String pass=editText.getText().toString();
                   JsonObject jsonObject=new JsonObject();
                   jsonObject.addProperty("password",pass);
                    ProgressDialog progressDialog_getBalance = new ProgressDialog(ETH_Wallet.this);
                    progressDialog_getBalance.setMessage("Processing...");
                    progressDialog_getBalance.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog_getBalance.show();
                   apiService.getbalanceETH(jsonObject).enqueue(new Callback<ResponseBody>() {
                       @Override
                       public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            String total=null;
                           try {
                               total=response.body().string();
                               mTotal.setText(total+" eth");
                               SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                               SharedPreferences.Editor editor = sharedPreferences.edit();
                               editor.putString("totalETH",total);
                               editor.apply();
                           } catch (IOException e) {
                               throw new RuntimeException(e);
                           }
                           progressDialog_getBalance.dismiss();
                       }

                       @Override
                       public void onFailure(Call<ResponseBody> call, Throwable t) {
                           progressDialog_getBalance.dismiss();
                       }
                   });

                })
                .setNegativeButton("Cancel", (dialog, which) -> {

                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}