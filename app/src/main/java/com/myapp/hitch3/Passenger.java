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

import com.myapp.hitch3.util.API;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class Passenger extends AppCompatActivity {

    private final int[] postalCodes = new int[] {101, 103, 104, 105, 107, 108, 109, 110, 111, 112, 113};
    private boolean shouldSend = false;
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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        populateListView();
        registerClickCallback();
    }

    private void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.postalCodes);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {

                TextView textView = (TextView) viewClicked;
                String message = "You clicked # " + position +
                        " which is string: " + textView.getText().toString();

                System.out.println(shouldSend);
                System.out.println("ShouldSend");
                if (shouldSend) {
                    dropOffPostalCode = postalCodes[position];
                    System.out.println("PREPARE SEND");
                    try {
                        API.newPassenger(pickupPostalCode, dropOffPostalCode);
                    } catch (IOException e) {
                        System.out.print("FAIL");
                    }
                } else {
                    pickupPostalCode = postalCodes[position];
                }

                shouldSend = !shouldSend;

                Toast.makeText(Passenger.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void populateListView() {
        // Create a list of items
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

}
