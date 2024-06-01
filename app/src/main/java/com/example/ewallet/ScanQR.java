package com.example.ewallet;


import android.content.Intent;
import android.widget.Toast;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;

// ScanQR class extending CaptureActivity
public class ScanQR extends CaptureActivity {

    // Overriding onActivityResult method to handle the result of barcode scanning
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Parsing the result using IntentIntegrator
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            // Check if the result is not null
            if (result.getContents() == null) {
                // If the scanned result is null, display "Scan cancelled" message
                Toast.makeText(this, "Scan cancelled", Toast.LENGTH_LONG).show();
            } else {
                // If the scanned result is not null, display the scanned content
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            // If the result is null, call the superclass method for handling the result
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
