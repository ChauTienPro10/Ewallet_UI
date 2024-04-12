package com.example.ewallet;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class generate_qr_code extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qr_code);

        EditText editText =findViewById(R.id.number_text);
        Button button = findViewById(R.id.generateButton);
        ImageView imageView = findViewById(R.id.idIVQRCode);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiFormatWriter multiFormatWriter =new MultiFormatWriter();

                try {
                    BitMatrix bitMatrix =multiFormatWriter.encode(editText.getText().toString(), BarcodeFormat.QR_CODE,300,300);

                    BarcodeEncoder barcodeEncoder =new BarcodeEncoder();
                    Bitmap bitmap =barcodeEncoder.createBitmap(bitMatrix);

                    imageView.setImageBitmap(bitmap);
                }catch (WriterException e){
                    throw new RuntimeException(e);
                }
            }
        });
    }
}