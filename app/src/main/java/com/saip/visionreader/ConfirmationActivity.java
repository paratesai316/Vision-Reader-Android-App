package com.saip.visionreader; // Make sure this is your package name

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class ConfirmationActivity extends AppCompatActivity {

    private TextToSpeech tts;
    private Button proceedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        proceedButton = findViewById(R.id.proceed_button);
        // Get the URI string that MainActivity sent
        String uriString = getIntent().getStringExtra("SCAN_URI");

        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.US);
                // Play the audio cue
                speak("please click on the bottom right button to proceed");
            }
        });

        proceedButton.setOnClickListener(v -> {
            // Launch the ResultsActivity and pass the URI to it
            Intent resultsIntent = new Intent(ConfirmationActivity.this, ResultsActivity.class);
            resultsIntent.putExtra("SCAN_URI", uriString);
            startActivity(resultsIntent);
        });
    }

    private void speak(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}