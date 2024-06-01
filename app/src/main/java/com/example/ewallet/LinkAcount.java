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

public class LinkAcount extends AppCompatActivity implements pindialogAdapter.PinDialogListener {
    // Declare UI elements
    private com.google.android.material.textfield.TextInputEditText mAddress, mKey, mPassword;
    private CheckBox mCheckbox;
    private View mSubmit;
    pindialogAdapter dialogFragment = new pindialogAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_acount);

        // Initialize UI elements
        mAddress = findViewById(R.id.edit_text_input);
        mKey = findViewById(R.id.acount_name);
        mCheckbox = findViewById(R.id.checkBox);
        mSubmit = findViewById(R.id.btn_continue);

        // Set click listener for the submit button
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate the input fields and checkbox
                if (!mAddress.getText().toString().contains("0x")) {
                    setEditTextError(mAddress); // Set error border for address field if validation fails
                } else if (!mKey.getText().toString().contains("0x")) {
                    setEditTextError(mKey); // Set error border for key field if validation fails
                } else if (!mCheckbox.isChecked()) {
                    setCheckboxError(mCheckbox); // Set error border for checkbox if validation fails
                } else {
                    showPinDialog(); // Show pin dialog if all validations pass
                }
            }
        });
    }

    // Method to set error border for EditText fields
    private void setEditTextError(EditText editText) {
        GradientDrawable borderDrawable = new GradientDrawable();
        borderDrawable.setStroke(2, Color.RED); // Set border thickness and color to red
        borderDrawable.setColor(Color.WHITE); // Set background color to white
        editText.setBackground(borderDrawable); // Apply the drawable to the EditText
    }

    // Method to set error border for Checkbox
    private void setCheckboxError(CheckBox checkBox) {
        GradientDrawable borderDrawable = new GradientDrawable();
        borderDrawable.setStroke(2, Color.RED); // Set border thickness and color to red
        borderDrawable.setColor(Color.WHITE); // Set background color to white
        checkBox.setBackground(borderDrawable); // Apply the drawable to the CheckBox
    }

    // Method to handle the linking process after PIN verification
    private void link(String pin) {
        // Display a progress dialog to indicate the linking process is ongoing
        ProgressDialog progressDialog = new ProgressDialog(LinkAcount.this);
        progressDialog.setMessage("Processing...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        // Create JSON object with address, key, and pin
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("address", mAddress.getText().toString());
        jsonObject.addProperty("pin", pin);
        jsonObject.addProperty("key", mKey.getText().toString());

        // Initialize the API service
        ApiService apiService = ApiService.ApiUtils.getApiService(this);

        // Make asynchronous API call to link account to Ethereum
        apiService.linktoETH(jsonObject).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    // Read the response from the server
                    String res = response.body().string();
                    if (res.equals("link success!")) {
                        // Save partial Ethereum address to SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("AddETH", mAddress.getText().toString().substring(0, 10) + "...");
                        editor.apply();

                        // Dismiss the progress dialog
                        progressDialog.dismiss();

                        // Navigate to ETH_Wallet activity
                        Intent intent = new Intent(LinkAcount.this, ETH_Wallet.class);
                        startActivity(intent);
                    }
                    // Show a Toast message with the server response
                    Toast.makeText(LinkAcount.this, res, Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                // Dismiss the progress dialog
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Show a Toast message indicating failure and dismiss the progress dialog
                Toast.makeText(LinkAcount.this, "Try again!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    // Callback method when PIN is entered
    @Override
    public void onPinEntered(String pin) {
        Log.d("pincode", "onPinEntered: " + pin); // Log the entered pin for debugging

        // Create JSON object with entered PIN
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("pincode", pin);

        // Initialize the API service
        ApiService apiService = ApiService.ApiUtils.getApiService(LinkAcount.this);

        // Make asynchronous API call to authenticate the PIN
        apiService.authenPin(jsonObject).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.body() == true) {
                    link(pin); // Proceed with linking if PIN authentication is successful
                } else {
                    // Show a Toast message indicating PIN authentication failure
                    Toast.makeText(LinkAcount.this, "failure", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                // Handle failure case (additional handling can be added here)
            }
        });
    }

    @Override
    public void onAuthentication() {
        getPincode(); // Retrieve PIN code when authentication is triggered
    }

    // Method to show the PIN dialog
    private void showPinDialog() {
        try {
            dialogFragment.show(getSupportFragmentManager(), "pin_dialog"); // Display the PIN dialog
        } catch (Exception e) {
            Log.d("dialogTien", "showPinDialog: " + e.toString()); // Log any exception that occurs
        }
    }

    // Method to get PIN code from the server
    private void getPincode() {
        // Display a progress dialog to indicate the process of retrieving the PIN
        ProgressDialog progressDialog_getPin = new ProgressDialog(this);
        progressDialog_getPin.setMessage("Processing...");
        progressDialog_getPin.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog_getPin.show();

        // Initialize the API service
        ApiService apiService = ApiService.ApiUtils.getApiService(this);

        // Make asynchronous API call to get the PIN
        apiService.getPin().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    // Read the PIN from the server response
                    String pin = response.body().string();
                    if (!pin.equals("can't authentication")) {
                        onPinEntered(pin); // Proceed with the obtained PIN
                        progressDialog_getPin.dismiss(); // Dismiss the progress dialog
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog_getPin.dismiss(); // Dismiss the progress dialog on failure
            }
        });
    }
}
