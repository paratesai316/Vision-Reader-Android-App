package com.saip.visionreader; // Make sure this is your package name

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ResultsActivity extends AppCompatActivity {

    private TextToSpeech tts;
    private TextRecognizer textRecognizer;
    private TextView recognizedTextView;
    // Single, correct declaration for all three buttons
    private Button playButton, pauseButton, scanNewButton;

    private List<String> sentences;
    private int currentSentenceIndex = 0;
    private boolean isPaused = true; // Start in a paused state

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        recognizedTextView = findViewById(R.id.recognized_text_view);
        playButton = findViewById(R.id.play_button);
        pauseButton = findViewById(R.id.pause_button);
        scanNewButton = findViewById(R.id.scan_new_button);

        // Initially, user can only play.
        updateButtonStates(false);

        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.US);
                tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onStart(String utteranceId) {
                        runOnUiThread(() -> updateButtonStates(true));
                    }

                    @Override
                    public void onDone(String utteranceId) {
                        // When one sentence finishes, automatically play the next
                        runOnUiThread(() -> {
                            if (!isPaused && currentSentenceIndex < sentences.size() - 1) {
                                currentSentenceIndex++;
                                speakCurrentSentence();
                            } else {
                                // Reached the end of the text
                                isPaused = true;
                                updateButtonStates(false);
                            }
                        });
                    }

                    @Override
                    public void onError(String utteranceId) {
                        runOnUiThread(() -> updateButtonStates(false));
                    }
                });

                loadAndProcessImage();
            }
        });

        playButton.setOnClickListener(v -> {
            if (isPaused) {
                isPaused = false;
                // If we are at the end, restart from the beginning
                if (currentSentenceIndex >= sentences.size() - 1) {
                    currentSentenceIndex = 0;
                }
                speakCurrentSentence();
            }
        });

        pauseButton.setOnClickListener(v -> {
            if (!isPaused) {
                isPaused = true;
                tts.stop();
                updateButtonStates(false);
            }
        });

        scanNewButton.setOnClickListener(v -> {
            // Stop any text-to-speech before leaving the screen
            if (tts != null) {
                tts.stop();
            }
            // Create an Intent to go back to MainActivity, which starts the scanner
            Intent intent = new Intent(ResultsActivity.this, MainActivity.class);
            // These flags clear the previous activities (Confirmation, Results) from memory
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    private void loadAndProcessImage() {
        String uriString = getIntent().getStringExtra("SCAN_URI");
        if (uriString != null) {
            Uri scannedImageUri = Uri.parse(uriString);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), scannedImageUri);
                InputImage inputImage = InputImage.fromBitmap(bitmap, 0);
                textRecognizer.process(inputImage)
                        .addOnSuccessListener(visionText -> {
                            String fullText = visionText.getText();
                            if (fullText.isEmpty()) {
                                recognizedTextView.setText("No text found in the document.");
                                playButton.setEnabled(false);
                                pauseButton.setEnabled(false);
                            } else {
                                recognizedTextView.setText(fullText);
                                sentences = Arrays.asList(fullText.split("(?<=[.!?])\\s*"));
                            }
                        });
            } catch (Exception e) {
                recognizedTextView.setText("Failed to load image.");
            }
        }
    }

    private void speakCurrentSentence() {
        if (sentences != null && currentSentenceIndex < sentences.size()) {
            String sentence = sentences.get(currentSentenceIndex);
            tts.speak(sentence, TextToSpeech.QUEUE_FLUSH, null, "SENTENCE_ID_" + currentSentenceIndex);
        }
    }

    private void updateButtonStates(boolean isPlaying) {
        playButton.setEnabled(!isPlaying);
        pauseButton.setEnabled(isPlaying);
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