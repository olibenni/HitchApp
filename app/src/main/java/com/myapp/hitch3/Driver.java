package com.myapp.hitch3;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.myapp.hitch3.dao.RidesDAO;
import com.myapp.hitch3.util.API;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by olafurma on 24.1.2016.
 *
 * Driver activity
 *
 * When entering Driver activity it fetches, every 5 seconds, the list of available rides
 * and updates it view with the rides.
 * When clicking a text box popup/dialog/modal appears where the Driver can send a message to the
 * corresponding passenger.
 */
public class Driver extends AppCompatActivity {
    private List<RidesDAO> rides;      // The rides from the server
    private Timer timer = new Timer(); // Used to update the rides from the server
    private final Driver self = this;  // Saves this scope, used when running another thread

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        registerClickCallback(); // Sets the handle for the click event on the list of rides
    }

    /**
     * Clicking on a ride on the list activates a text box popup/dialog/modal
     * used to send a message to the corresponding passenger
     * TODO: Currently only shows a message detailing the passenger info. Create the modal
     */
    private void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.availableRides);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {

                TextView textView = (TextView) viewClicked;
                String message = "You clicked # " + position +
                        " which is string: " + textView.getText().toString() +
                        ". Its passengerId is: " + rides.get(position).getId();
                createMessage(rides.get(position).getId());
                Toast.makeText(Driver.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * When activity Driver is not created or paused (running in background)
     * it stops updating the rides list.
     * When Driver resumes it starts updating the rides list again
     *
     * fetches all rides from server every 5 seconds.
     */
    @Override
    protected void onResume() {
        super.onResume();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                fetchRides();
            }
        }, 0, 5000);
    }

    /**
     * When moving to another activity og moving Driver to the background it stops updating its
     * rides list.
     */
    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
        timer.purge();
    }

    /**
     * When moving to another activity og moving Driver to the background it stops updating its
     * rides list.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer.purge();
    }

    /**
     * Fetches all rides from the server and updates the view.
     */
    public void fetchRides() {
        try {
            rides = API.fetchRides();
            populateListView();
        } catch (JSONException e) {
            System.out.println("JSONException: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }

    /**
     * Updates the current view
     */
    private void populateListView() {
        // Dynamic changes in the UI must be handles by separated threads.
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Create a list of items
                String[] availableRides = new String[rides.size()];

                for (int i = 0; i < rides.size(); i++) {
                    String ride = "From: " + rides.get(i).getPickup() + " - To: " + rides.get(i).getDropOff();
                    availableRides[i] = ride;
                }

                // Build adapter
                // Pass self as current scope as "this" would be the scope of the UI thread.
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(self, R.layout.postal_codes, availableRides);

                // Configure the list view
                ListView list = (ListView) findViewById(R.id.availableRides);
                list.setAdapter(adapter);
            }
        });
    }

    /**
     * This function is called on when choosing a requested ride.
     * Opens up the CreateMessage activity and sends it the id of the user that owns
     * the requested ride that was chosen.
     *
     * @param id    Integer, the id of the user that was clicked on
     */
    public void createMessage(int id) {
        Intent intent = new Intent(this, CreateMessage.class);
        intent.putExtra("ID", id);
        startActivity(intent);
    }
}
