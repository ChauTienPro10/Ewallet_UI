package com.example.ewallet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import Entities.LoginResponse;
import Entities.Member;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpPage extends AppCompatActivity {

    CountryCodePicker countryCodePicker;


    EditText fullNameEditText, emailEditText, usernameEditText, passwordEditText, firstNameEditText,lNameEditText;
    Button signUpButton;
    TextView loginText, signUpText;
    EditText phoneEditText;
    EditText confirmPass;
    private ProgressBar progressBar;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        //ccp
        countryCodePicker=findViewById(R.id.contryCodePicker);
        countryCodePicker.setAutoDetectedCountry(true);
        String DefaultCountry = countryCodePicker.getDefaultCountryName();
        String DefaultCountCode = countryCodePicker.getDefaultCountryName();
        String CountryCode = countryCodePicker.getDefaultCountryName();
        String CountryName = countryCodePicker.getDefaultCountryName();




        firstNameEditText = findViewById(R.id.firstNameEditText);
        lNameEditText = findViewById(R.id.lNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signUpButton = findViewById(R.id.signUpButton);
        loginText = findViewById(R.id.loginText);
        phoneEditText=findViewById(R.id.phone_number_edt);
        confirmPass=findViewById(R.id.rePasswordEditText);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);


        signUpButton.setOnClickListener(v -> {
            // Lấy dữ liệu từ EditText
            String firstName = firstNameEditText.getText().toString();
            String lastName = lNameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String country=countryCodePicker.getSelectedCountryName();
            String phone= phoneEditText.getText().toString();
            if(firstName.equals("")||lastName.equals("")||email.equals("")||username.equals("")
            || password.equals("")|| phone.equals("")){
                Toast.makeText(SignUpPage.this, "Please fill enough information! ", Toast.LENGTH_SHORT).show();
            }
            else{
                if(!password.equals(confirmPass.getText().toString())){
                    Toast.makeText(SignUpPage.this, "password is incorrect !", Toast.LENGTH_SHORT).show();
                }
                else{
                    progressBar.setVisibility(View.VISIBLE);
                    Member newMem=new Member(firstName,lastName,email,country,phone,username,password,0);
                    signup(newMem);
                }
            }


        });

        TextView loginText = findViewById(R.id.loginTextView);
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển về trang LoginPage khi nhấn vào "Already have an account? SignUp Now"
                Intent intent = new Intent(SignUpPage.this, LoginPage.class);
                startActivity(intent);
            }
        });
    }

    private void signup(Member member){
        ApiService apiService = ApiService.ApiUtils.getApiService(SignUpPage.this);
        apiService.register(member).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String textResponse=response.body().string();
                        Toast.makeText(SignUpPage.this,textResponse , Toast.LENGTH_SHORT).show();
                        if(textResponse.equals("you are regis an new account")){
                            Intent intent = new Intent(SignUpPage.this, LoginPage.class);

                            startActivity(intent);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    progressBar.setVisibility(View.GONE);


                } else {
                    Toast.makeText(SignUpPage.this, "Information is incorrect!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }

            }
            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Toast.makeText(SignUpPage.this, "API call failure", Toast.LENGTH_SHORT).show();
            }
        });
    }


}