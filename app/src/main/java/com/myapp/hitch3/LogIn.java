package com.myapp.hitch3;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.myapp.hitch3.util.API;
import java.io.IOException;
import com.facebook.FacebookSdk;

/**
 * Created by olafurma 24.1.2016
 *
 * Login activity
 *
 * Currently has only one image button that logs into the application by receiving a sessionId
 * from the server and storing it.
 */
public class LogIn extends AppCompatActivity {
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup for Facebook log in, hasn't been implemented fully.
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        System.out.println("SUCCESS");
                    }

                    @Override
                    public void onCancel() {
                        System.out.println("CANCEL");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        System.out.println("FAIL");
                    }
                });

        // Allow network access, needed to make http requests
        // This will be fixed when we call it with async (using threads)
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_log_in);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_log_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Logs in and opens up the Role activity
    public void onLogIn(View view) throws IOException {
        API.logIn();
        Intent intent = new Intent(this, Role.class);
        startActivity(intent);
    }
}
