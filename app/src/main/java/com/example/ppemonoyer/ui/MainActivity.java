package com.example.ppemonoyer.ui;

import androidx.annotation.Nullable;

import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.example.ppemonoyer.R;

import com.example.ppemonoyer.databinding.ActivityMainBinding;
import com.example.ppemonoyer.manager.UserManager;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;


import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private static final int RC_SIGN_IN = 123;
    private UserManager userManager = UserManager.getInstance();

    private Button loginButton;
    private Button testButton;
    private Button parametreButton;


    @Override
    protected ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginButton = findViewById(R.id.loginButton);
        testButton = findViewById(R.id.testButton);
        parametreButton = findViewById(R.id.parametreButton);

        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLoginButton();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    private void setupListeners() {
        // Login Button
        loginButton.setOnClickListener(view -> {
            if(userManager.isCurrentUserLogged()){
                startProfileActivity();
            }else{
                startSignInActivity();
            }
        });

        testButton.setOnClickListener(view -> {
            if(userManager.isCurrentUserLogged()){
                startDebutFinActivity();
            }else{
                showSnackBar(getString(R.string.error_not_connected));
            }
        });

        parametreButton.setOnClickListener(view -> {
                startParametersActivity();
        });
    }


    private void startSignInActivity() {

        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Collections.singletonList(new AuthUI.IdpConfig.EmailBuilder().build());

        // Launch the activity
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false, true)
                        .setLogo(R.drawable.ic_launcher_foreground)
                        .build(),
                RC_SIGN_IN);
    }

    // Launching Profile Activity
    private void startProfileActivity(){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    private void startParametersActivity(){
        Intent intent = new Intent(this, ParametersActivity.class);
        startActivity(intent);
    }

    private void startDebutFinActivity(){
        Intent intent = new Intent(this, DebutFin.class);
        startActivity(intent);
    }

    // Update Login Button when activity is resuming
    private void updateLoginButton(){
        loginButton.setText(userManager.isCurrentUserLogged() ? getString(R.string.button_login_text_logged) : getString(R.string.button_login_text_not_logged));
    }

    // Show Snack Bar with a message
    private void showSnackBar( String message){
        Snackbar.make(binding.mainLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    // Method that handles response after SignIn Activity close
    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data){

        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {
            // SUCCESS
            if (resultCode == RESULT_OK) {
                userManager.createUser();
                showSnackBar(getString(R.string.connection_succeed));
            } else {
                // ERRORS
                if (response == null) {
                    showSnackBar(getString(R.string.error_authentication_canceled));
                } else if (response.getError()!= null) {
                    if(response.getError().getErrorCode() == ErrorCodes.NO_NETWORK){
                        showSnackBar(getString(R.string.error_no_internet));
                    } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                        showSnackBar(getString(R.string.error_unknown_error));
                    }
                }
            }
        }
    }
}