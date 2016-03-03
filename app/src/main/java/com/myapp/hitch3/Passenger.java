package com.myapp.hitch3;

import android.content.Intent;
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
import com.myapp.hitch3.util.API;

import java.io.IOException;

/**
 * Created by olafurma 24.1.2016
 *
 * Passenger activity
 *
 * When entering passenger activity a list is populated with available places to travel from and to
 * First click registers the pickup location and the second click registers the dropOff location.
 *
 * Also on the second click, both the pickup and dropOff locations are sent to the server to be
 * registered under the current user.
 */
public class Passenger extends AppCompatActivity {

    private final int[] postalCodes = new int[] {101, 103, 104, 105, 107, 108, 109, 110, 111, 112, 113};
    private boolean shouldSend = false; // Alternates between clicks.
    private int pickupPostalCode;
    private int dropOffPostalCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMessages();
            }
        });

        populateListView();
        registerClickCallback();
    }

    /**
     * Handles a click on the list of places
     * If click is a even number click it sends a request to the server
     * registering the ride
     */
    private void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.postalCodes);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {

                TextView textView = (TextView) viewClicked;
                String message = "You clicked # " + position +
                        " which is string: " + textView.getText().toString();

                if (shouldSend) {
                    dropOffPostalCode = postalCodes[position];
                    try {
                        API.newPassenger(pickupPostalCode, dropOffPostalCode);
                    } catch (IOException e) {
                        System.out.print("FAIL"); // TODO: log or handle
                    }
                    goToMessages();
                } else {
                    pickupPostalCode = postalCodes[position];
                }

                shouldSend = !shouldSend;
                // TODO: should not send current message, change to something more informative
                Toast.makeText(Passenger.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Sets the list af available places
     */
    private void populateListView() {
        // Create a list of places
        String[] places = {
                "101 - Miðbær",
                "103 - Háleiti",
                "104 - Laugardalur",
                "105 - Hlíðar",
                "107 - Vesturbær",
                "108 - Fossvogur",
                "109 - Bakkar og Seljahverfi",
                "110 - Árbær",
                "111 - Hólar og Fell",
                "112 - Grafarvogur",
                "113 - Grafarholt"
        };

        // Build adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.postal_codes, places);

        // Configure the list view
        ListView list = (ListView) findViewById(R.id.postalCodes);
        list.setAdapter(adapter);
    }

    /**
     * Opens up ReadMessages activity
     */
    public void goToMessages() {
        Intent intent = new Intent(this, ReadMessages.class);
        startActivity(intent);
    }

    /**
     * Sends a request to cancel the users ride request
     * @throws IOException
     */
    public void onCancelClick(View view) throws IOException {
        API.cancelRide();
        Toast.makeText(Passenger.this, "Your ride has been cancelled", Toast.LENGTH_LONG).show();
    }
}
