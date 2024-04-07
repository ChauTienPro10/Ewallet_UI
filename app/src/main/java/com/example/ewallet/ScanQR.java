package com.example.ewallet;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;

public class ScanQR extends CaptureActivity {
//    private static final int PERMISSION_REQUEST_CAMERA = 1;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_scan_qr);
//
//
//    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
//            } else {
//                initQRCodeScanner();
//            }
//        } else {
//            initQRCodeScanner();
//        }
//    }
//    private void initQRCodeScanner() {
//
//        IntentIntegrator integrator = new IntentIntegrator(ScanQR.this);
//        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
//        integrator.setPrompt("Scan");
//        integrator.setCameraId(0);
//        integrator.setBeepEnabled(true);
//        integrator.setOrientationLocked(true);
//        integrator.setBarcodeImageEnabled(false);
//        integrator.initiateScan();
//    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == PERMISSION_REQUEST_CAMERA) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                initQRCodeScanner();
//            } else {
//                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_LONG).show();
//                finish();
//            }
//        }
//    }
//
//
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Scan cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}