# Vision Reader: Android OCR & TTS App

An accessible Android application designed for visually impaired users to scan documents and have the text read aloud to them. The app features a streamlined, voice-guided workflow from scanning to audio playback.

## Features

-   **Splash Screen:** A simple, clickable entry point to the application.
-   **Voice-Guided Workflow:** Audio prompts guide the user through every step.
-   **High-Quality Document Scanning:** Integrates Google's ML Kit Document Scanner for robust, AI-powered edge detection and image cleanup.
-   **Confirmation Step:** A dedicated screen with a large, accessible button to ensure the user wants to proceed after scanning.
-   **Results Display:** A clear, high-contrast screen displaying the recognized text.
-   **Copyable Text:** Users can easily long-press the recognized text to select and copy it.
-   **Audio Playback Controls:** Features large, distinct Play, Pause, and "Scan New" buttons for easy navigation and control of the text-to-speech audio.
-   **Natural TTS:** The app splits text into sentences for more natural-sounding playback that respects punctuation.

## How It Works & Technology Stack

This is a native Android application built entirely in **Java**. It does not rely on a traditional backend server; all processing is done on the device.

-   **Frontend (UI):**
    -   The user interface is built with standard **Android XML Layouts**.
    -   The app follows a multi-activity architecture to separate concerns:
        1.  `SplashActivity`: The initial landing screen.
        2.  `MainActivity`: A controller that launches the scanner.
        3.  `ConfirmationActivity`: A confirmation step after the scan.
        4.  `ResultsActivity`: Displays text and audio controls.

-   **Backend (On-Device Processing):**
    -   **Document Scanning:** The app uses the **Google ML Kit Document Scanner** library. This provides a pre-built, high-quality UI that handles automatic document detection, cropping, and perspective correction.
    -   **Text Recognition (OCR):** The scanned image is processed by the **Google ML Kit Text Recognition** library to extract text from the image.
    -   **Text-to-Speech (TTS):** The extracted text is spoken using Android's native **TextToSpeech (TTS) Engine**. The logic is enhanced to speak sentence-by-sentence to allow for a robust pause-and-resume functionality.

## Setup and Installation

To run this project on your own device:

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/YourUsername/Your-Repo-Name.git](https://github.com/YourUsername/Your-Repo-Name.git)
    ```
2.  **Open in Android Studio:**
    -   Open Android Studio.
    -   Select `File > Open` and navigate to the cloned project folder.
3.  **Sync Dependencies:**
    -   Android Studio will automatically detect the `build.gradle.kts` file. Allow it to sync and download all the required dependencies (including ML Kit libraries).
4.  **Run the App:**
    -   Connect a physical Android device or start an Android Emulator.
    -   **Note:** The ML Kit Document Scanner requires Google Play Services, which is standard on most physical devices but may need to be installed on some emulators.
    -   Click the **Run 'app' (▶️)** button.