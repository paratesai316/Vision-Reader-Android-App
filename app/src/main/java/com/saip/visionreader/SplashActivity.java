package com.saip.visionreader; // Make sure this is your package name

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    private TextToSpeech tts;
    private View splashLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashLayout = findViewById(R.id.splash_layout);

        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.US);
                // Set a listener to know when speaking is done
                tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onStart(String utteranceId) {}

                    @Override
                    public void onDone(String utteranceId) {
                        // When the intro speech is finished, launch the scanner
                        if ("INTRO_DONE".equals(utteranceId)) {
                            startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        }
                    }

                    @Override
                    public void onError(String utteranceId) {}
                });
            }
        });

        splashLayout.setOnClickListener(v -> {
            // Speak the welcome message
            String text = "Vision Reader is ready, please point the camera at the document";
            // Use a unique ID to track when this speech is done
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "INTRO_DONE");
        });
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