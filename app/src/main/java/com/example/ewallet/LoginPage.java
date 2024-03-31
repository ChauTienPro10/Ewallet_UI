package com.example.ewallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;

import Entities.LoginRequest;
import Entities.LoginResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginPage extends AppCompatActivity {
    EditText mUsername;
    EditText mPassword;
    Button mSubmit;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        mUsername=findViewById(R.id.username);
        mPassword=findViewById(R.id.password);
        mSubmit=findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);


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

    }
    public void callApiLogin(String username, String password) {

        LoginRequest loginRequest = new LoginRequest(username, password);
        ApiService.apiService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if (response.isSuccessful()) {

                    LoginResponse loginResponse=response.body();

                    Toast.makeText(LoginPage.this, "login success !"+loginResponse.getUsername(), Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(LoginPage.this, MainActivity.class );
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginPage.this, "Username or password incorrect!", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                Toast.makeText(LoginPage.this, "API call failure", Toast.LENGTH_SHORT).show();
                // Xử lý lỗi kết nối hoặc yêu cầu không thành công
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}