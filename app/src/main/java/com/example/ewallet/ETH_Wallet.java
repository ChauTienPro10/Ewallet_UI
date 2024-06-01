package com.example.ewallet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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

    // Declare UI components
    ConstraintLayout btn_back_eth;
    TextView mAddress;
    private TextView mTotal;
    TextView mName;
    private Button mLink;

    // Dialog fragment for PIN entry
    pindialogAdapter dialogFragment = new pindialogAdapter();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eth_wallet);

        // Shared Preferences for storing and retrieving user data
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Initialize UI components
        mTotal = findViewById(R.id.textView48);
        mLink = findViewById(R.id.linkButton);
        mName = findViewById(R.id.textName);

        // Set the full name from SharedPreferences
        mName.setText(sharedPreferences.getString("fullName", ""));
        btn_back_eth = findViewById(R.id.btn_back_eth);
        mAddress = findViewById(R.id.check);

        // Set back button action
        btn_back_eth.setOnClickListener(v -> startActivity(new Intent(ETH_Wallet.this, MainActivity.class)));

        // Check if Ethereum address is already stored
        if (!sharedPreferences.getString("AddETH", "").equals("")) {
            // Display a shortened version of the Ethereum address
            mAddress.setText(sharedPreferences.getString("AddETH", "").substring(0, 10) + "...");
        } else {
            // Show PIN dialog if no Ethereum address is stored
            showPinDialog();
        }

        // Link button click listener
        mLink.setOnClickListener(v -> {
            if (mAddress.getText().toString().equals("not link")) {
                // Start LinkAcount activity if not linked
                Intent intent = new Intent(ETH_Wallet.this, LinkAcount.class);
                startActivity(intent);
            } else {
                // Show notification dialog if already linked
                openDialog(Gravity.CENTER);
            }
        });
    }

    // Method to retrieve Ethereum wallet info from the server
    private void getInfETH() {
        ProgressDialog progressDialog = new ProgressDialog(ETH_Wallet.this);
        progressDialog.setMessage("Processing...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        // Create an instance of ApiService
        ApiService apiService = ApiService.ApiUtils.getApiService(ETH_Wallet.this);

        // Make an asynchronous request to get Ethereum wallet info
        apiService.getInfEth().enqueue(new Callback<EtherWallet>() {
            @Override
            public void onResponse(Call<EtherWallet> call, Response<EtherWallet> response) {
                EtherWallet etherwallet = response.body();
                if (etherwallet != null) {
                    // Save Ethereum address to SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("AddETH", etherwallet.getAddress().substring(0, 10) + "...");
                    editor.apply();

                    // Notify user of success and update UI
                    Toast.makeText(ETH_Wallet.this, "success", Toast.LENGTH_SHORT).show();
                    mAddress.setText(sharedPreferences.getString("AddETH", "").substring(0, 10) + "...");

                    // Fetch wallet balance
                    getbalance(apiService);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<EtherWallet> call, Throwable t) {
                // Handle failure
                progressDialog.dismiss();
            }
        });
    }

    // Callback method for PIN entry dialog
    @Override
    public void onPinEntered(String pin) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("pincode", pin);

        // Create an instance of ApiService
        ApiService apiService = ApiService.ApiUtils.getApiService(ETH_Wallet.this);

        // Make an asynchronous request to authenticate PIN
        apiService.authenPin(jsonObject).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.body() == true) {
                    // Fetch Ethereum wallet info if authentication is successful
                    getInfETH();
                } else {
                    // Notify user of authentication failure and navigate back to main activity
                    Toast.makeText(ETH_Wallet.this, "Authen failure", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ETH_Wallet.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                // Handle failure
            }
        });
    }

    // Callback method for successful authentication
    @Override
    public void onAuthentication() {
        // Fetch PIN code and dismiss the dialog
        getPincode();
        dialogFragment.dismiss();
    }

    // Method to show PIN entry dialog
    private void showPinDialog() {
        try {
            dialogFragment.show(getSupportFragmentManager(), "pin_dialog");
        } catch (Exception e) {
            Log.d("dialogTien", "showPinDialog: " + e.toString());
        }
    }

    // Method to retrieve PIN code from the server
    private void getPincode() {
        ProgressDialog progressDialog_getPin = new ProgressDialog(ETH_Wallet.this);
        progressDialog_getPin.setMessage("Processing...");
        progressDialog_getPin.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog_getPin.show();

        // Create an instance of ApiService
        ApiService apiService = ApiService.ApiUtils.getApiService(ETH_Wallet.this);

        // Make an asynchronous request to get PIN
        apiService.getPin().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String pin = response.body().string();
                    if (!pin.equals("can't authentication")) {
                        // Authenticate PIN if retrieval is successful
                        onPinEntered(pin);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                progressDialog_getPin.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Handle failure
                progressDialog_getPin.dismiss();
            }
        });
    }

    // Method to get wallet balance (implementation needed)
    private void getbalance(ApiService apiService) {
        // Implement this method to get balance
    }

    // Method to show a notification dialog
    public void openDialog(int gravity) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_dialog_notificaton);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        // Set dialog cancelable based on gravity
        dialog.setCancelable(Gravity.CENTER == gravity);

        // Initialize dialog UI components
        TextView mTitle = dialog.findViewById(R.id.title);
        mTitle.setText("Notify");
        TextView mContent = dialog.findViewById(R.id.content);
        mContent.setText("You were linked to ETH");
        Button cancel = dialog.findViewById(R.id.btn_cancel);
        Button confirm = dialog.findViewById(R.id.btn_confirm);

        // Set cancel button action
        cancel.setOnClickListener(v -> dialog.dismiss());

        // Set confirm button action
        confirm.setOnClickListener(v -> {
            goLink();
            dialog.dismiss();
        });

        dialog.show();
    }

    // Method to handle link confirmation (implementation needed)
    private void goLink() {
        // Implement this method to handle link confirmation
    }
}
