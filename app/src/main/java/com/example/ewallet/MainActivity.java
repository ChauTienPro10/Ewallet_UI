package com.example.ewallet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    private RecyclerView.Adapter adapterContact;
    private RecyclerView recyclerViewConatact;
    private ConstraintLayout btn_Transaction;

    private ImageView getMoney;
    private ImageView toDeposit;
    FloatingActionButton scanqr;
    Gson gson = new Gson();
    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Contact list

        initRecyclerView();
        toDeposit=findViewById(R.id.DepositImg);
        toDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, TransactionPage.class );
                startActivity(intent);
            }
        });


        scanqr=findViewById(R.id.scanQR);
        getMoney=findViewById(R.id.get_money);


        btn_Transaction = findViewById(R.id.btn_Transaction);
        ImageView imageProfile = findViewById(R.id.imageProfile);


        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfilePage.class);
                startActivity(intent);
            }
        });


        getMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, GenerateQrCode.class );
                startActivity(intent);
            }
        });


        btn_Transaction.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Chuyển đến TransactionPageActivity
                Intent intent = new Intent(MainActivity.this,TransactionPage.class);
                startActivity(intent);
            }
        });
        scanqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setPrompt("Scan a barcode");
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                integrator.setCameraId(0); // Use a specific camera of the device
                integrator.setOrientationLocked(true);
                integrator.setBeepEnabled(true);
                integrator.setCaptureActivity(ScanQR.class);
                integrator.initiateScan();

            }
        });
    }

    private void initRecyclerView() {
        ArrayList<ContactsDomain> items=new ArrayList<>();
        items.add(new ContactsDomain("David","user_1"));
        items.add(new ContactsDomain("Alice","user_2"));
        items.add(new ContactsDomain("Rose","user_3"));
        items.add(new ContactsDomain("Sara","user_4"));
        items.add(new ContactsDomain("David","user_5"));

        recyclerViewConatact=findViewById(R.id.viewList);
        recyclerViewConatact.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        adapterContact=new ContactsAdapter(items);
        recyclerViewConatact.setAdapter(adapterContact);
    }


    private void openFormDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Data");

        // Inflate the layout for the dialog
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_qr_form, null);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) final EditText etName = view.findViewById(R.id.et_name);

        builder.setView(view);
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = etName.getText().toString();
                Toast.makeText(MainActivity.this, "Entered name: " + name, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Scan cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                ApiService apiService = ApiService.ApiUtils.getApiService(this);
                apiService.sendQrData(result.getContents()).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Toast.makeText(MainActivity.this, "send success !"+response.body(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "incorrect!"+t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }



}