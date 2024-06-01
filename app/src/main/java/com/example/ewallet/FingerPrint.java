package com.example.ewallet;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;

public class FingerPrint {
    // Biometric prompt and prompt info
    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;

    // Activity context and executor for running tasks on the main thread
    AppCompatActivity activity;
    Executor executor;

    // Authentication state
    boolean authen = false;

    // Constructor initializes the activity and executor
    public FingerPrint(AppCompatActivity activity) {
        this.activity = activity;
        executor = ContextCompat.getMainExecutor(this.activity);
    }

    // Method to check if biometric authentication is available and can be used
    public Boolean can_authen() {
        BiometricManager biometricManager = BiometricManager.from(activity);
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                return false;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                return false;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                return false;
            case BiometricManager.BIOMETRIC_SUCCESS:
                return true;
            default:
                return false;
        }
    }

    // Method to start the biometric authentication process
    public void Authenticate(AuthenticationCallback callback) {
        // If authentication is not possible, call the failure callback and return
        if (!this.can_authen()) {
            callback.onAuthenticationFailure();
            return;
        }

        // Create a new biometric prompt with authentication callback handlers
        biometricPrompt = new BiometricPrompt(this.activity, this.executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                callback.onAuthenticationFailure();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                callback.onAuthenticationSuccess();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                callback.onAuthenticationFailure();
            }
        });

        // Build and display the biometric prompt
        biometricPrompt.authenticate(new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Authenticate")
                .setSubtitle("Please scan your fingerprint")
                .setNegativeButtonText("Cancel")
                .build());
    }

    // Setter for authentication state
    public void setAuthenticate(boolean bool) {
        this.authen = bool;
    }

    // Getter for authentication state
    public boolean getAuthenticate() {
        return this.authen;
    }
}