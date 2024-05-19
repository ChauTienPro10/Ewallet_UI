package com.example.ewallet;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenerateQrCode extends AppCompatActivity {
    ImageView imageView;
    final DecimalFormat decimalFormat = new DecimalFormat("#,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr_code);

        EditText editText_amount =findViewById(R.id.number_text);
        Button submit = findViewById(R.id.generateButton);
        imageView = findViewById(R.id.idIVQRCode);


        editText_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editText_amount.removeTextChangedListener(this);

                try {
                    String originalString = s.toString().replaceAll(",", "");

                    double value = decimalFormat.parse(originalString).doubleValue();

                    String formattedString = decimalFormat.format(value);
                    editText_amount.setText(formattedString);
                    editText_amount.setSelection(formattedString.length());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                editText_amount.addTextChangedListener(this);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount =editText_amount.getText().toString();
                JsonObject json = new JsonObject();
                json.addProperty("amount", amount);
                ApiService apiService = ApiService.ApiUtils.getApiService(GenerateQrCode.this);
                apiService.createQR(json).enqueue(new Callback<ResponseBody>(){

                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String responseBody;
                        try {
                            responseBody = response.body().string();
                            byte[] imageBytes = Base64.decode(responseBody, Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                            imageView.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        Toast.makeText(GenerateQrCode.this, "Succsess" , Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(GenerateQrCode.this, "incorrect!", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
    }
}