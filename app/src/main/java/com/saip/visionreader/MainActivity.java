package com.saip.visionreader; // Make sure this is your package name

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.google.mlkit.vision.documentscanner.GmsDocumentScanner;
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions;
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning;
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult;

public class MainActivity extends AppCompatActivity {

    private ActivityResultLauncher<IntentSenderRequest> scannerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This activity now has no layout of its own. It's just a controller.

        initializeScannerLauncher();
        launchScanner(); // Launch the scanner as soon as this activity starts
    }

    private void initializeScannerLauncher() {
        scannerLauncher = registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                try {
                    GmsDocumentScanningResult scanningResult = GmsDocumentScanningResult.fromActivityResultIntent(result.getData());
                    if (scanningResult != null && !scanningResult.getPages().isEmpty()) {
                        android.net.Uri imageUri = scanningResult.getPages().get(0).getImageUri();

                        // THIS IS THE CORRECT LOGIC:
                        // Start the ConfirmationActivity and pass the image URI to it.
                        Intent confirmationIntent = new Intent(MainActivity.this, ConfirmationActivity.class);
                        confirmationIntent.putExtra("SCAN_URI", imageUri.toString());
                        startActivity(confirmationIntent);

                        // Close this activity so the user can't navigate back to it
                        finish();
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Failed to process scan result.", Toast.LENGTH_SHORT).show();
                    finish(); // Close if there's an error
                }
            } else {
                // If the user cancels the scan, close this activity and go back to the splash screen
                finish();
            }
        });
    }

    private void launchScanner() {
        GmsDocumentScannerOptions options = new GmsDocumentScannerOptions.Builder()
                .setScannerMode(GmsDocumentScannerOptions.SCANNER_MODE_FULL)
                .setResultFormats(GmsDocumentScannerOptions.RESULT_FORMAT_JPEG)
                .build();
        GmsDocumentScanner scanner = GmsDocumentScanning.getClient(options);

        scanner.getStartScanIntent(this)
                .addOnSuccessListener(intentSender -> {
                    scannerLauncher.launch(new IntentSenderRequest.Builder(intentSender).build());
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Scanner failed to start.", Toast.LENGTH_SHORT).show();
                    finish(); // Close if the scanner can't even start
                });
    }
}