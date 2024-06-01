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

public class opencard extends AppCompatActivity {
    // Declare UI elements
    private androidx.coordinatorlayout.widget.CoordinatorLayout mSubmit;
    private CheckBox mcheckbox;
    private EditText mpin, mauthenpin;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opencard);

        // Initialize UI elements
        mSubmit=findViewById(R.id.submitlayout);
        mcheckbox=findViewById(R.id.checkBox);
        mpin=findViewById(R.id.pinInput);
        mauthenpin=findViewById(R.id.pininputauthen);

        // Set onClickListener for submit button
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if checkbox is checked
                if(!mcheckbox.isChecked()){
                    // Change checkbox background to indicate error
                    GradientDrawable borderDrawable = new GradientDrawable();
                    borderDrawable.setStroke(2, Color.RED); // Set border thickness and color
                    borderDrawable.setColor(Color.WHITE); // Set background color for CheckBox
                    mcheckbox.setBackground(borderDrawable);
                } else if (mpin.getText().toString().length()!=6) {
                    // Check if PIN length is correct
                    GradientDrawable borderDrawable = new GradientDrawable();
                    borderDrawable.setStroke(2, Color.RED); // Set border thickness and color
                    borderDrawable.setColor(Color.WHITE); // Set background color for EditText
                    mpin.setHint("incorrect"); // Set hint to indicate incorrect PIN
                    mpin.setBackground(borderDrawable);
                } else if (mauthenpin.getText().toString().length()!=6 ||
                        !mauthenpin.getText().toString().equals(mpin.getText().toString())) {
                    // Check if authentication PIN is correct
                    GradientDrawable borderDrawable = new GradientDrawable();
                    borderDrawable.setStroke(2, Color.RED); // Set border thickness and color
                    borderDrawable.setColor(Color.WHITE); // Set background color for EditText
                    mauthenpin.setHint("incorrect"); // Set hint to indicate incorrect authentication PIN
                    mauthenpin.setBackground(borderDrawable);
                }
                else{
                    // If all checks passed, create a new card
                    CreatenewCard(mpin.getText().toString());
                }
            }
        });
    }

    // Method to create a new card
    public void CreatenewCard(String pin){
        ProgressDialog progressDialog = new ProgressDialog(opencard.this);
        progressDialog.setMessage("Processing..."); // Set message for ProgressDialog
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Set ProgressDialog style to spinner
        progressDialog.show(); // Show ProgressDialog

        // Create JSON object with PIN
        JsonObject ob=new JsonObject();
        ob.addProperty("pincode",pin);

        // Call API to open card
        ApiService apiService=ApiService.ApiUtils.getApiService(this);
        apiService.openCard(ob).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    // Process response from API
                    String res=response.body().string();
                    if(res.equals("Activate card for this account success")){
                        // If card activation successful, navigate to MainActivity
                        Intent intent=new Intent(opencard.this, MainActivity.class );
                        progressDialog.dismiss(); // Dismiss ProgressDialog
                        startActivity(intent); // Start MainActivity
                    }
                } catch (IOException e) {
                    progressDialog.dismiss(); // Dismiss ProgressDialog if an exception occurs
                    throw new RuntimeException(e); // Throw a runtime exception
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss(); // Dismiss ProgressDialog on API call failure
            }
        });
    }
}
