package com.myapp.hitch3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.myapp.hitch3.util.API;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ReadMessages extends AppCompatActivity {
    private List<String> messages;         // The messages from the server
    private Timer timer = new Timer();     // Used to update the rides from the server periodically
    private final ReadMessages self = this;// Saves this scope, used when running another thread

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_messages);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * When activity ReadMessages is not created yet or has been paused (running in background)
     * it stops updating the messages list.
     * When ReadMessages resumes, it starts updating the messages list again
     *
     * fetches all messages for passenger from server every 5 seconds.
     */
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

    /**
     * When moving to another activity og moving ReadMessages to the background it stops updating its
     * messages list.
     */
    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
        timer.purge();
    }

    /**
     * When moving to another activity og moving ReadMessages to the background it stops updating its
     * messages list.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer.purge();
    }

    /**
     * Fetches all messages to the corresponding passenger from the server and updates the view.
     */
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

    /**
     * Updates the current view
     */
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
