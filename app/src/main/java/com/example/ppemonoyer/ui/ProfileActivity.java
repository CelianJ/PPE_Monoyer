package com.example.ppemonoyer.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ppemonoyer.R;
import com.example.ppemonoyer.manager.UserManager;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    private Button updateButton;
    private Button signOutButton;
    private Button deleteButton;
    private ImageView profileImageView;
    private EditText usernameEditText;
    private EditText emailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        LinearLayout layout = findViewById(R.id.profil_LinearLayout);
        updateButton = findViewById(R.id.updateButton);
        signOutButton = findViewById(R.id.signOutButton);
        deleteButton = findViewById(R.id.deleteButton);
        profileImageView = findViewById(R.id.profileImageView);
        usernameEditText = findViewById(R.id.usernameEditText);
        emailTextView = findViewById(R.id.emailTextView);

        setupListeners();
        updateUIWithUserData();

        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideKeyboard(view);
                return false;
            }
        });
    }

    private void setupListeners(){
        updateButton.setOnClickListener(view -> { });
        signOutButton.setOnClickListener(view -> { });
        deleteButton.setOnClickListener(view -> { });

        // Sign out button
        signOutButton.setOnClickListener(view -> {
            UserManager.signOut(this).addOnSuccessListener(aVoid -> {
                finish();
            });
        });

        // Delete button
        deleteButton.setOnClickListener(view -> {

            new AlertDialog.Builder(this)
                    .setMessage(R.string.popup_message_confirmation_delete_account)
                    .setPositiveButton(R.string.popup_message_choice_yes, (dialogInterface, i) ->
                            UserManager.deleteUser(ProfileActivity.this)
                                    .addOnSuccessListener(aVoid -> {
                                                finish();
                                            }
                                    )
                    )
                    .setNegativeButton(R.string.popup_message_choice_no, null)
                    .show();

        });

        // Update button
        updateButton.setOnClickListener(view -> {
            UserManager.updateUsername(usernameEditText.getText().toString()).addOnSuccessListener(aVoid -> {
            });
        });

    }

    private void updateUIWithUserData(){
        if(UserManager.isCurrentUserLogged()){
            FirebaseUser user = UserManager.getCurrentUser();

            if(user.getPhotoUrl() != null){
                setProfilePicture(user.getPhotoUrl());
            }
            setTextUserData(user);
            getUserData();
        }
    }

    private void setProfilePicture(Uri profilePictureUrl){
        Glide.with(this)
                .load(profilePictureUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(profileImageView);
    }

    private void setTextUserData(FirebaseUser user){

        //Get email & username from User
        String email = TextUtils.isEmpty(user.getEmail()) ? getString(R.string.info_no_email_found) : user.getEmail();
        String username = TextUtils.isEmpty(user.getDisplayName()) ? getString(R.string.info_no_username_found) : user.getDisplayName();

        //Update views with data
        usernameEditText.setText(username);
        emailTextView.setText(email);
    }

    private void getUserData(){
        UserManager.getUserData().addOnSuccessListener(user -> {
            // Set the data with the user information
            String username = TextUtils.isEmpty(user.getUsername()) ? getString(R.string.info_no_username_found) : user.getUsername();
            usernameEditText.setText(username);
        });
    }

    private void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


}
