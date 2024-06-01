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
    // Declare UI components
    private EditText mEmail, mCode, mNewPass, mPassAgain;
    private Button mGetCode, mSubmit;
    private ProgressBar mProBar1, mProBar2;
    private androidx.cardview.widget.CardView card1, card2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgest_password);

        // Initialize UI components
        mEmail = findViewById(R.id.email);
        mCode = findViewById(R.id.code);
        mNewPass = findViewById(R.id.newpassword);
        mPassAgain = findViewById(R.id.pass_again);
        mGetCode = findViewById(R.id.getcode);
        mSubmit = findViewById(R.id.Submit);
        mProBar1 = findViewById(R.id.progressBar);
        mProBar2 = findViewById(R.id.progressBar2);

        // Initially hide the progress bars
        mProBar1.setVisibility(View.GONE);
        mProBar2.setVisibility(View.GONE);

        // Initially hide the second card view
        card1 = findViewById(R.id.cardview1);
        card2 = findViewById(R.id.cardview2);
        card2.setVisibility(View.GONE);

        // Set click listener for the "Get Code" button
        mGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the email field is empty
                if (mEmail.getText().toString().equals("")) {
                    GradientDrawable borderDrawable = new GradientDrawable();
                    borderDrawable.setStroke(2, Color.RED); // Set border thickness and color
                    borderDrawable.setColor(Color.WHITE); // Set background color
                    mEmail.setBackground(borderDrawable); // Apply the border to the email field
                } else {
                    getCode(); // Call the method to get the code
                }
            }
        });

        // Set click listener for the "Submit" button
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate the input fields
                if (mCode.getText().toString().equals("")) {
                    applyErrorStyle(mCode);
                } else if (mNewPass.getText().toString().equals("")) {
                    applyErrorStyle(mNewPass);
                } else if (!mPassAgain.getText().toString().equals(mNewPass.getText().toString())) {
                    applyErrorStyle(mPassAgain);
                } else {
                    changePass(); // Call the method to change the password
                }
            }
        });
    }

    // Method to apply error style to the EditText
    private void applyErrorStyle(EditText editText) {
        GradientDrawable borderDrawable = new GradientDrawable();
        borderDrawable.setStroke(2, Color.RED); // Set border thickness and color
        borderDrawable.setColor(Color.WHITE); // Set background color
        editText.setBackground(borderDrawable); // Apply the border to the EditText
    }

    // Method to get the code for password reset
    private void getCode() {
        mProBar1.setVisibility(View.VISIBLE); // Show the progress bar
        JsonObject js = new JsonObject();
        js.addProperty("email", mEmail.getText().toString()); // Add email property to JSON object
        ApiService apiService = ApiService.ApiUtils.getApiService(this);
        apiService.getCode(js).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String res = response.body().string();
                    if (res.equals("Mail Sent Successfully...")) {
                        card1.setVisibility(View.GONE); // Hide the first card view
                        card2.setVisibility(View.VISIBLE); // Show the second card view
                        mProBar1.setVisibility(View.GONE); // Hide the progress bar
                    } else {
                        mProBar1.setVisibility(View.GONE); // Hide the progress bar
                    }
                } catch (IOException e) {
                    mProBar1.setVisibility(View.GONE); // Hide the progress bar
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mProBar1.setVisibility(View.GONE); // Hide the progress bar
            }
        });
    }

    // Method to change the password
    private void changePass() {
        mProBar2.setVisibility(View.VISIBLE); // Show the progress bar
        JsonObject js = new JsonObject();
        js.addProperty("email", mEmail.getText().toString()); // Add email property to JSON object
        js.addProperty("code", mCode.getText().toString()); // Add code property to JSON object
        js.addProperty("newpassword", mNewPass.getText().toString()); // Add new password property to JSON object
        ApiService apiService = ApiService.ApiUtils.getApiService(this);
        apiService.authen_changepassword(js).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String res = response.body().string();
                    if (res.equals("ok")) {
                        mProBar2.setVisibility(View.GONE); // Hide the progress bar
                        Intent intent = new Intent(forgest_password.this, LoginPage.class);
                        startActivity(intent); // Navigate to the login page
                    }
                } catch (IOException e) {
                    mProBar2.setVisibility(View.GONE); // Hide the progress bar
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mProBar2.setVisibility(View.GONE); // Hide the progress bar
            }
        });
    }
}
