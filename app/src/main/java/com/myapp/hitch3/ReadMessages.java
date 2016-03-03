package com.myapp.hitch3;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.myapp.hitch3.util.API;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ReadMessages extends AppCompatActivity {
    private Timer timer = new Timer();
    private List<String> messages = new ArrayList<String>();
    private final ReadMessages self = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_messages);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                fetchMessages();
            }
        }, 0, 5000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
        timer.purge();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer.purge();
    }
    public void fetchMessages() {
        try {
            messages = API.fetchMessages();
            if(messages.size() > 0){
                showMessages();
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }

    public void showMessages() {
        // Dynamic changes in the UI must be handles by separated threads.
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Create a list of items
                String[] availableMessages = messages.toArray(new String[messages.size()]);

                // Build adapter
                // Pass self as current scope as "this" would be the scope of the UI thread.
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(self, R.layout.postal_codes, availableMessages);

                // Configure the list view
                ListView list = (ListView) findViewById(R.id.availableMessages);
                list.setAdapter(adapter);
            }
        });
    }

}
