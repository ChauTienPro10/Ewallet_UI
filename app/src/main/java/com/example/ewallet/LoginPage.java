package com.example.ewallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import Entities.LoginRequest;
import Entities.LoginResponse;
import Entities.Member;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPage extends AppCompatActivity {
    // Declare UI elements
    EditText mUsername;
    EditText mPassword;
    Button mSubmit;
    TextView signupText;
    private ProgressBar progressBar;

    TextView mForgest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        // Initialize UI elements
        mUsername=findViewById(R.id.username);
        mPassword=findViewById(R.id.password);
        mSubmit=findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE); // Hide progress bar initially
        signupText = findViewById(R.id.signupText);
        mForgest=findViewById(R.id.Fingerprint);

        // Set OnClickListener for login button
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide the keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                // Check if username or password is empty
                if(mUsername.getText().toString().isEmpty() || mPassword.getText().toString().isEmpty()){
                    Toast.makeText(LoginPage.this, "Please fill enough information !", Toast.LENGTH_SHORT).show();
                }
                else{
                    // Show progress bar and call login API
                    progressBar.setVisibility(View.VISIBLE);
                    callApiLogin(mUsername.getText().toString(),mPassword.getText().toString());
                }
            }
        });

        // Set OnClickListener for signup text
        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to SignUpPage
                Intent intent=new Intent(LoginPage.this, SignUpPage.class );
                startActivity(intent);
            }
        });

        // Set OnClickListener for forgot password text
        mForgest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to forgot password page
                Intent intent=new Intent(LoginPage.this, forgest_password.class );
                startActivity(intent);
            }
        });

    }

    // Method to call login API
    public void callApiLogin(String username, String password) {
        // Create LoginRequest object with username and password
        LoginRequest loginRequest = new LoginRequest(username, password);
        // Get ApiService instance
        ApiService apiService = ApiService.ApiUtils.getApiService(this);
        // Enqueue login API call
        apiService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    // If login is successful
                    LoginResponse loginResponse=response.body();
                    // Save JWT token and user ID in SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.putString("jwt",loginResponse.getJwt() );
                    editor.putInt("user_id", (int) loginResponse.getId());
                    editor.apply();
                    // Show progress dialog
                    ProgressDialog progressDialog = new ProgressDialog(LoginPage.this);
                    progressDialog.setMessage("waiting...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.show();

                    // Call API to get member details
                    apiService.getMember().enqueue(new Callback<Member>() {
                        @Override
                        public void onResponse(Call<Member> call, Response<Member> response) {
                            // If member details received
                            Member mem=response.body();
                            // Save member details in SharedPreferences
                            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("fullName",mem.getFname().toString()+" "+mem.getLname().toString());
                            editor.putString("mailshare",mem.getEmail());
                            editor.apply();
                            // Show login success message
                            Toast.makeText(LoginPage.this, "login success !"+loginResponse.getUsername(), Toast.LENGTH_SHORT).show();
                            // Redirect to MainActivity
                            Intent intent=new Intent(LoginPage.this, MainActivity.class );
                            progressDialog.dismiss();
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<Member> call, Throwable t) {
                            progressDialog.dismiss();
                        }
                    });

                } else {
                    // If login failed, show error message
                    Toast.makeText(LoginPage.this, "Username or password incorrect!", Toast.LENGTH_SHORT).show();
                }
                // Hide progress bar
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                // If API call failed, show error message
                Toast.makeText(LoginPage.this, "API call failure", Toast.LENGTH_SHORT).show();
                // Hide progress bar
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
