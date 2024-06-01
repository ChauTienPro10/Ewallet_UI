package com.example.ewallet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ewallet.adapter.pindialogAdapter;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InforTranfer extends AppCompatActivity implements pindialogAdapter.PinDialogListener {
    // Declare UI elements
    private TextView mName;
    private TextView mPhone;
    private LinearLayout mSubmit;
    private EditText mAmount;
    private EditText mNote;

    private String phone; // Variable to store the phone number from intent
    private final DecimalFormat decimalFormat = new DecimalFormat("#,###"); // Format for displaying the amount

    private pindialogAdapter dialogFragment = new pindialogAdapter(); // PIN dialog fragment instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor_tranfer);

        // Retrieve data from the intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        phone = intent.getStringExtra("phone");

        // Initialize UI components
        mName = findViewById(R.id.textView29);
        mPhone = findViewById(R.id.textView43);
        mSubmit = findViewById(R.id.buttonConfirm);
        mAmount = findViewById(R.id.editTextText);
        mNote = findViewById(R.id.editTextText2);

        // Set the name and phone values to their respective TextViews
        mName.setText(name);
        mPhone.setText(phone);

        // Check if there's an amountExtra in the intent and set it to mAmount EditText
        if (intent.getStringExtra("amountExtra") != null) {
            mAmount.setText(intent.getStringExtra("amountExtra"));
        }

        // Add a TextWatcher to format the amount input with commas
        mAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed before text changes
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No action needed during text changes
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Remove the TextWatcher to avoid infinite loop
                mAmount.removeTextChangedListener(this);

                try {
                    // Remove commas from the input
                    String originalString = s.toString().replaceAll(",", "");

                    // Parse the input to a double value
                    double value = decimalFormat.parse(originalString).doubleValue();

                    // Format the value back to a string with commas
                    String formattedString = decimalFormat.format(value).replaceAll("\\.", ",");

                    // Set the formatted string back to the EditText
                    mAmount.setText(formattedString);
                    mAmount.setSelection(formattedString.length()); // Move the cursor to the end
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // Add the TextWatcher back to the EditText
                mAmount.addTextChangedListener(this);
            }
        });

        // Set click listener for the submit button
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPinDialog(); // Show the PIN dialog when the submit button is clicked
            }
        });
    }

    // Method to show the PIN dialog
    private void showPinDialog() {
        try {
            // Show the PIN dialog fragment
            dialogFragment.show(getSupportFragmentManager(), "pin_dialog");
        } catch (Exception e) {
            Log.d("dialogTien", "showPinDialog: " + e.toString()); // Log any exceptions
        }
    }

    // Callback method when PIN is entered
    @Override
    public void onPinEntered(String pin) {
        // Create a JSON object to send the PIN for authentication
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("pincode", pin);

        // Get the API service instance
        ApiService apiService = ApiService.ApiUtils.getApiService(InforTranfer.this);

        // Make an asynchronous call to authenticate the PIN
        apiService.authenPin(jsonObject).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                // If the PIN is authenticated successfully, proceed to transfer
                setTransfer(apiService);
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                // Handle failure in PIN authentication
            }
        });
    }

    // Callback method for authentication
    @Override
    public void onAuthentication() {
        getPincode(); // Retrieve the PIN code from the server
        dialogFragment.dismiss(); // Dismiss the PIN dialog
        Toast.makeText(InforTranfer.this, "Authenticate success!", Toast.LENGTH_SHORT).show(); // Show success message
    }

    // Method to get the PIN code from the server
    private void getPincode() {
        // Get the API service instance
        ApiService apiService = ApiService.ApiUtils.getApiService(InforTranfer.this);

        // Make an asynchronous call to get the PIN code
        apiService.getPin().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    // Get the PIN from the response body
                    String pin = response.body().string();
                    if (!pin.equals("can't authentication")) {
                        onPinEntered(pin); // Use the obtained PIN for authentication
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e); // Handle exceptions
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Handle failure in getting the PIN code
            }
        });
    }

    // Method to set the transfer details and make the API call
    private void setTransfer(ApiService apiService) {
        // Show a progress dialog during the transfer process
        ProgressDialog progressDialog = new ProgressDialog(InforTranfer.this);
        progressDialog.setMessage("Processing...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        // Create a JSON object with the transfer details
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("amount", mAmount.getText().toString());
        jsonObject.addProperty("phone", phone);
        jsonObject.addProperty("note", mNote.getText().toString());

        Log.d("tientest", "setTransfer: " + mAmount.getText().toString() + " -" + phone); // Log transfer details

        // Make an asynchronous API call to transfer
        apiService.transfer(jsonObject).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    // Get the response string from the response body
                    String responseString = response.body().string();
                    Toast.makeText(InforTranfer.this, responseString, Toast.LENGTH_SHORT).show(); // Show response message

                    progressDialog.dismiss(); // Dismiss the progress dialog

                    // Navigate to the successful money transfer activity
                    Intent intent = new Intent(InforTranfer.this, Successful_money_transfer.class);
                    intent.putExtra("receiver", mName.getText());
                    intent.putExtra("amount", mAmount.getText() + " vnd");
                    intent.putExtra("note", mNote.getText().toString());
                    startActivity(intent);
                } catch (IOException e) {
                    throw new RuntimeException(e); // Handle exceptions
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("tientest", "setTransfer: " + t.toString()); // Log any failure
                progressDialog.dismiss(); // Dismiss the progress dialog
            }
        });
    }
}
