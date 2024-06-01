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
        mUsername=findViewById(R.id.username);
        mPassword=findViewById(R.id.password);
        mSubmit=findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        signupText = findViewById(R.id.signupText);
        mForgest=findViewById(R.id.Fingerprint);


        mSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                if(mUsername.getText().toString().isEmpty() || mPassword.getText().toString().isEmpty()){
                    Toast.makeText(LoginPage.this, "Please fill enough information !", Toast.LENGTH_SHORT).show();

                }
                else{
                    progressBar.setVisibility(View.VISIBLE);
                    callApiLogin(mUsername.getText().toString(),mPassword.getText().toString());

                }

            }
        });

        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //di den trang dang ky
                Intent intent=new Intent(LoginPage.this, SignUpPage.class );
                startActivity(intent);
            }
        });

        mForgest.setOnClickListener(new View.OnClickListener() { // di den xu ly quen mat khau
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginPage.this, forgest_password.class );
                startActivity(intent);

            }
        });

    }
    public void callApiLogin(String username, String password) { // goi APi dang nhap

        LoginRequest loginRequest = new LoginRequest(username, password);
        ApiService apiService = ApiService.ApiUtils.getApiService(this);
        apiService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if (response.isSuccessful()) {

                    LoginResponse loginResponse=response.body();
                    // Luu thong tin can thiet vao SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.putString("jwt",loginResponse.getJwt() );
                    editor.putInt("user_id", (int) loginResponse.getId());
                    editor.apply();
                    ProgressDialog progressDialog = new ProgressDialog(LoginPage.this);
                    progressDialog.setMessage("waiting...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.show();

                    apiService.getMember().enqueue(new Callback<Member>() {
                        @Override
                        public void onResponse(Call<Member> call, Response<Member> response) {
                            Member mem=response.body();
                            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("fullName",mem.getFname().toString()+" "+mem.getLname().toString());
                            editor.putString("mailshare",mem.getEmail());

                            editor.apply();
                            Toast.makeText(LoginPage.this, "login success !"+loginResponse.getUsername(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(LoginPage.this, "Username or password incorrect!", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                Toast.makeText(LoginPage.this, "API call failure", Toast.LENGTH_SHORT).show();
             
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}