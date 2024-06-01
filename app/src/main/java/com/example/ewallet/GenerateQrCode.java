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
    // Declare UI components
    private ImageView imageView;
    private final DecimalFormat decimalFormat = new DecimalFormat("#,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr_code);

        // Initialize UI elements
        EditText editText_amount = findViewById(R.id.number_text);
        Button submit = findViewById(R.id.generateButton);
        imageView = findViewById(R.id.idIVQRCode);

        // Add a TextWatcher to format the amount input
        editText_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed before text is changed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No action needed during the text change
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Remove TextWatcher to avoid infinite loop
                editText_amount.removeTextChangedListener(this);

                try {
                    // Get the input string without commas
                    String originalString = s.toString().replaceAll(",", "");

                    // Parse the value to double
                    double value = decimalFormat.parse(originalString).doubleValue();

                    // Format the value and set it back to EditText
                    String formattedString = decimalFormat.format(value);
                    editText_amount.setText(formattedString);
                    editText_amount.setSelection(formattedString.length());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // Re-add TextWatcher after formatting is done
                editText_amount.addTextChangedListener(this);
            }
        });

        // Set a click listener on the submit button to generate the QR code
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the amount entered by the user
                String amount = editText_amount.getText().toString();

                // Create a JSON object with the amount
                JsonObject json = new JsonObject();
                json.addProperty("amount", amount);

                // Get the API service instance
                ApiService apiService = ApiService.ApiUtils.getApiService(GenerateQrCode.this);

                // Make an asynchronous call to create the QR code
                apiService.createQR(json).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            // Get the response body as a string
                            String responseBody = response.body().string();

                            // Decode the Base64 string to byte array
                            byte[] imageBytes = Base64.decode(responseBody, Base64.DEFAULT);

                            // Convert byte array to Bitmap
                            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                            // Set the Bitmap to ImageView
                            imageView.setImageBitmap(bitmap);

                            // Show success toast
                            Toast.makeText(GenerateQrCode.this, "Success", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        // Show failure toast
                        Toast.makeText(GenerateQrCode.this, "Incorrect!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
