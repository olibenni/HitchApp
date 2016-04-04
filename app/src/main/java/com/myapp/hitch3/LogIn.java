package com.myapp.hitch3;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.myapp.hitch3.util.API;

import java.io.IOException;

/**
 * Created by olafurma 24.1.2016
 *
 * Login activity
 *
 * Currently has only one image button that logs into the application by receiving a sessionId
 * from the server and storing it.
 *
 * TODO: Handle errors in some consistent manner
 */
public class LogIn extends AppCompatActivity {
    private final LogIn self = this;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup for Facebook log in
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        // Register a callback when a facebook login attempt has been made.
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    // On a successful facebook login
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        try {
                            logIn();
                        } catch (IOException e) {
                            System.err.println(e.getMessage());
                        }
                    }
                    // Canceling will exit the application
                    @Override
                    public void onCancel() {
                        System.exit(0);
                    }
                    @Override
                    public void onError(FacebookException exception) {
                        System.err.println(exception.getMessage());
                    }
                }
        );

        // Allow network access, needed to make http requests
        // This will be fixed when we call it with async (using threads)
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_log_in);
    }

    // Hook up the callbackmanager for facebook
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    // Logs in and opens up the Role activity
    public void onLogIn(View view) throws IOException {
        logIn();
    }

    public void logIn() throws IOException{
        API.logIn();
        Intent intent = new Intent(self, Role.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(self);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(self);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        try {
            API.cancelRide();
        } catch (IOException e) {
            finish();
        }
    }
}
