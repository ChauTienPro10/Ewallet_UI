package com.example.ewallet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ewallet.adapter.ContactsAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import Entities.Member;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    // Declare UI elements
    private RecyclerView.Adapter adapterContact;
    private RecyclerView recyclerViewConatact;
    private ConstraintLayout btn_Transaction;
    private ImageView getMoney, mtoOpenCard;
    private ImageView toDeposit;
    private TextView mName;
    TextView mBalance;
    private ConstraintLayout sendMoney;
    FloatingActionButton scanqr;
    LinearLayout mToProfile, btn_history;
    Gson gson = new Gson();

    // This method is called when the activity is first created.
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        mName = findViewById(R.id.nameMember);
        mBalance = findViewById(R.id.textView);
        mtoOpenCard = findViewById(R.id.toOpencard);
        // Handle click on open card image
        mtoOpenCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, opencard.class);
                startActivity(intent);
            }
        });

        // Button history transaction
        btn_history = findViewById(R.id.btnHistory);
        btn_history.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, HistoryTransactionPage.class)));

        // Test Auth
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        mName.setText(sharedPreferences.getString("fullName", ""));
        getMember();

        // Test ETH
        LinearLayout testETH = findViewById(R.id.testETH);
        testETH.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ETH_Wallet.class)));

        // Contact list
        initRecyclerView();
        toDeposit = findViewById(R.id.DepositImg);
        // Handle click on deposit image
        toDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TransactionPage.class);
                startActivity(intent);
            }
        });

        // Handle click on send money layout
        mToProfile = findViewById(R.id.toProfile);
        sendMoney = findViewById(R.id.imageSend);
        sendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TranferMoney.class);
                startActivity(intent);
            }
        });

        // Handle click on scan QR floating action button
        scanqr = findViewById(R.id.scanQR);
        scanqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize QR code scanner
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

    // Method to initialize the RecyclerView for contacts
    private void initRecyclerView() {
        ArrayList<ContactsDomain> items = new ArrayList<>();
        items.add(new ContactsDomain("David", "user_1"));
        items.add(new ContactsDomain("Alice", "user_2"));
        items.add(new ContactsDomain("Rose", "user_3"));
        items.add(new ContactsDomain("Sara", "user_4"));
        recyclerViewConatact = findViewById(R.id.viewList);
        recyclerViewConatact.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapterContact = new ContactsAdapter(items);
        recyclerViewConatact.setAdapter(adapterContact);
    }

    // Method to open a form dialog
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

    // Handle QR code scanning result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Scan cancelled", Toast.LENGTH_LONG).show();
            } else {
                // Process the scanned QR code result
                Log.d("chauduong", "onActivityResult: " + result.getContents());
                String[] parts = result.getContents().split("\n");
                String name = parts[0];
                String phone = parts[1];
                String amountString = parts[2];
                Intent intent = new Intent(MainActivity.this, InforTranfer.class);
                intent.putExtra("name", name);
                intent.putExtra("phone", phone);
                intent.putExtra("amountExtra", amountString);
                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    // Method to retrieve member information
    private void getMember() {
        ApiService apiService = ApiService.ApiUtils.getApiService(MainActivity.this);
        apiService.getBalance().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String responseBody;
                responseBody = response.body();
                mBalance.setText(responseBody);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }
}
