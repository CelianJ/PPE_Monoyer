package com.example.ppemonoyer.manager;

import android.content.Context;

import com.example.ppemonoyer.models.User;
import com.example.ppemonoyer.repository.UserRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

public class UserManager {

    private static volatile UserManager instance;
    private static UserRepository userRepository;

    private UserManager() {
        userRepository = UserRepository.getInstance();
    }

    public static UserManager getInstance() {
        UserManager result = instance;
        if (result != null) {
            return result;
        }
        synchronized(UserRepository.class) {
            if (instance == null) {
                instance = new UserManager();
            }
            return instance;
        }
    }

    public static FirebaseUser getCurrentUser(){
        return UserRepository.getInstance().getCurrentUser();
    }

    public static Boolean isCurrentUserLogged(){
        return (UserManager.getCurrentUser() != null);
    }

    public static Task<Void> signOut(Context context){
        return UserRepository.getInstance().signOut(context);
    }

    public void createUser(){
        userRepository.createUser();
    }

    public static Task<User> getUserData(){
        // Get the user from Firestore and cast it to a User model Object
        return userRepository.getUserData().continueWith(task -> task.getResult().toObject(User.class)) ;
    }

    public static Task<Void> updateUsername(String username){
        return userRepository.updateUsername(username);
    }

    public static Task<Void> deleteUser(Context context){
        // Delete the user account from the Auth
        return UserRepository.getInstance().deleteUser(context).addOnCompleteListener(task -> {
            // Once done, delete the user datas from Firestore
            UserRepository.getInstance().deleteUserFromFirestore();
        });
    }

}
