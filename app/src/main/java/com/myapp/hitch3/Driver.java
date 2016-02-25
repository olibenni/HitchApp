package com.myapp.hitch3;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class Driver extends AppCompatActivity {
    private List<RidesDAO> rides;
    private Timer timer = new Timer();
    private final Driver self = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                fetchRides();
            }
        }, 0, 5000);
        registerClickCallback();
    }
    private void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.availableRides);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {

                TextView textView = (TextView) viewClicked;
                String message = "You clicked # " + position +
                        " which is string: " + textView.getText().toString() +
                        ". Its passengerId is: " + rides.get(position).getId();

                Toast.makeText(Driver.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
        System.out.println("Number of timed tasks purged: " + timer.purge());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        System.out.println("Number of timed tasks purged: " + timer.purge());
    }

    public void fetchRides(){
        System.out.println("API fetching rides");
        try {
            rides = API.fetchRides();
            populateListView();
        } catch (JSONException e) {
            System.out.println("JSONException: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }

    private void populateListView() {
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
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(self, R.layout.postal_codes, availableRides);

                // Configure the list view
                ListView list = (ListView) findViewById(R.id.availableRides);
                list.setAdapter(adapter);
            }
        });
    }
}
