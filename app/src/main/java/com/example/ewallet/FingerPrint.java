package com.example.ewallet;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class FingerPrint {
    BiometricPrompt biometricPrompt;

    androidx.biometric.BiometricPrompt.PromptInfo promptInfo;
    AppCompatActivity activity;
    Executor executor;

    boolean authen=false;

    public FingerPrint(AppCompatActivity activity) {
        this.activity = activity;
        executor= ContextCompat.getMainExecutor(this.activity);

    }

    public Boolean can_authen(){
        androidx.biometric.BiometricManager biometricManager= BiometricManager.from(activity);
        switch(biometricManager.canAuthenticate()){
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
               return false;

            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                return false;

            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                return  false;

            case BiometricManager.BIOMETRIC_SUCCESS:
                return true;

            default:return false;
        }
    }

    public  void Authenticate(AuthenticationCallback callback){
        if(!this.can_authen()){
            callback.onAuthenticationFailure();
            return ;
        }

        biometricPrompt=new BiometricPrompt(this.activity, this.executor, new BiometricPrompt.AuthenticationCallback() {
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
        biometricPrompt.authenticate(new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Authenticate")
                .setSubtitle("please scan your fingerprint")
                .setNegativeButtonText("cancel")
                .build());
    }

    public void setAuthenticate(boolean bool){
        this.authen=bool;
    }
    public boolean getAuthenticate(){
        return this.authen;
    }




}
