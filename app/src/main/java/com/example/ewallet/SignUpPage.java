package com.example.ewallet;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;

public class SignUpPage extends AppCompatActivity {

    EditText fullNameEditText, emailEditText, usernameEditText, passwordEditText;
    Button signUpButton;
    TextView loginText, signUpText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);
        fullNameEditText = findViewById(R.id.fullNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signUpButton = findViewById(R.id.signUpButton);
        loginText = findViewById(R.id.loginText);

        signUpButton.setOnClickListener(v -> {
            // Lấy dữ liệu từ EditText
            String fullName = fullNameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            // Tạo JSON object
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("fullName", fullName);
                jsonObject.put("email", email);
                jsonObject.put("username", username);
                jsonObject.put("password", password);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Hiển thị dữ liệu JSON
            if (jsonObject.length() > 0) {
                String jsonData = jsonObject.toString();
                // In ra dữ liệu JSON lên Log
                Log.d("SignUpData", jsonData);
                Toast.makeText(SignUpPage.this, jsonData, Toast.LENGTH_SHORT).show();
            }
        });
    }


}