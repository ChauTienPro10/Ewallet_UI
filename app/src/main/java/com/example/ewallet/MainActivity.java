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

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        mName = findViewById(R.id.nameMember);
        mBalance = findViewById(R.id.textView);
        mtoOpenCard = findViewById(R.id.toOpencard);
        sendMoney = findViewById(R.id.imageSend);
        toDeposit = findViewById(R.id.DepositImg);
        mToProfile = findViewById(R.id.toProfile);
        getMoney = findViewById(R.id.get_money);
        btn_Transaction = findViewById(R.id.btn_Transaction);
        scanqr = findViewById(R.id.scanQR);
        btn_history = findViewById(R.id.btnHistory);

        // Set click listeners
        // Open opencard activity
        mtoOpenCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, opencard.class);
                startActivity(intent);
            }
        });

        // Open history transaction activity
        btn_history.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, HistoryTransactionPage.class)));

        // Retrieve user data from SharedPreferences and display
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        mName.setText(sharedPreferences.getString("fullName", ""));
        getMember(); // Call method to get member information

        // Open ETH wallet activity
        LinearLayout testETH = findViewById(R.id.testETH);
        testETH.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ETH_Wallet.class)));

        // Initialize RecyclerView for contacts
        initRecyclerView();

        // Deposit money
        toDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TransactionPage.class);
                startActivity(intent);
            }
        });

        // Send money
        sendMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TranferMoney.class);
                startActivity(intent);
            }
        });

        // Generate QR code
        getMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GenerateQrCode.class);
                startActivity(intent);
            }
        });

        // Navigate to profile page
        mToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfilePage.class);
                startActivity(intent);
            }
        });

        // Navigate to transaction page
        btn_Transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TransactionPage.class);
                startActivity(intent);
            }
        });

        // Scan QR code
        scanqr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize IntentIntegrator for scanning
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setPrompt("Scan a barcode"); // Set prompt message
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE); // Set barcode format
                integrator.setCameraId(0); // Use rear camera
                integrator.setOrientationLocked(true); // Lock orientation
                integrator.setBeepEnabled(true); // Enable beep sound on scan
                integrator.setCaptureActivity(ScanQR.class); // Set custom capture activity
                integrator.initiateScan(); // Start scanning
            }
        });
    }

    // Method to initialize the RecyclerView for displaying contacts
    private void initRecyclerView() {
        // Create a list of sample contacts
        ArrayList<ContactsDomain> items = new ArrayList<>();
        items.add(new ContactsDomain("David", "user_1"));
        items.add(new ContactsDomain("Alice", "user_2"));
        items.add(new ContactsDomain("Rose", "user_3"));
        items.add(new ContactsDomain("Sara", "user_4"));

        // Find the RecyclerView in the layout and set its layout manager and adapter
        recyclerViewConatact = findViewById(R.id.viewList);
        recyclerViewConatact.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapterContact = new ContactsAdapter(items); // Create an adapter with the contact list
        recyclerViewConatact.setAdapter(adapterContact);
    }

    // Method to open a dialog for entering data
    private void openFormDialog() {
        // Create a dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Data"); // Set dialog title

        // Inflate the layout for the dialog
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_qr_form, null);
        final EditText etName = view.findViewById(R.id.et_name); // Find the EditText in the layout
        builder.setView(view); // Set the inflated view to the dialog builder

        // Set up positive and negative buttons for the dialog
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = etName.getText().toString(); // Get the entered name
                Toast.makeText(MainActivity.this, "Entered name: " + name, Toast.LENGTH_SHORT).show(); // Show a toast with the entered name
                dialog.dismiss(); // Dismiss the dialog
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Dismiss the dialog if canceled
            }
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Method to handle the result of QR code scanning
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Parse the result using IntentIntegrator
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                // Show a toast if scan was canceled
                Toast.makeText(this, "Scan cancelled", Toast.LENGTH_LONG).show();
            } else {
                // Extract data from the scanned QR code
                String[] parts = result.getContents().split("\n");
                String name = parts[0];
                String phone = parts[1];
                String amountString = parts[2];

                // Start InforTranfer activity and pass the extracted data
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

    // Method to retrieve member information from the API
    private void getMember() {
        // Create an instance of ApiService to interact with the API
        ApiService apiService = ApiService.ApiUtils.getApiService(MainActivity.this);
        // Make an asynchronous call to get the member's balance
        apiService.getBalance().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // Handle successful response
                String responseBody;
                responseBody = response.body();
                mBalance.setText(responseBody); // Set member's balance on the UI
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Handle failure
            }
        });
    }
}