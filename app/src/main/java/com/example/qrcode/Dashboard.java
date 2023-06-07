package com.example.qrcode;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class Dashboard extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 200;
    private DatabaseReference databaseReference;
    private CardView cardView;
    private LinearLayout expansionLayout;
    private TextView titleTextView;
    private boolean isExpanded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        cardView = findViewById(R.id.cardView);
        expansionLayout = findViewById(R.id.expansionLayout);
        titleTextView = findViewById(R.id.titleTextView);


        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleExpansion();
            }
        });
        Button scanButton = findViewById(R.id.scan_button);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanQRCode();
            }
        });

        Button viewDataButton = findViewById(R.id.view_data_button);
        viewDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, ViewDataActivity.class));
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("QRCodeData");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    private void scanQRCode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            String scannedData = result.getContents();
            Log.d("ScannedData", scannedData);
            saveScannedDataToFirebase(scannedData);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void saveScannedDataToFirebase(String scannedData) {
        String key = databaseReference.push().getKey();
        databaseReference.child(key).setValue(scannedData)
                .addOnSuccessListener(aVoid -> Toast.makeText(Dashboard.this, "Data saved successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(Dashboard.this, "Failed to save data", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission granted
            } else {
                // Camera permission denied
            }
        }
    }
    private void toggleExpansion() {
        if (isExpanded) {
            expansionLayout.setVisibility(View.GONE);
            titleTextView.setText("QR code menu");
            isExpanded = false;
        } else {
            expansionLayout.setVisibility(View.VISIBLE);
            titleTextView.setText("Select you choice");
            isExpanded = true;
        }
    }
}
