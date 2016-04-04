package com.myapp.hitch3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by olafurma 24.1.2016
 *
 * Role activity
 *
 * This activity allows a user to choose between coming a driver or a passenger
 */
public class Role extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role);
    }

    // Opens up the driver activity
    public void onDriver(View view){
        Intent intent = new Intent(this, Driver.class);
        startActivity(intent);
    }

    // Opens up the passenger activity
    public void onPassenger(View view){
        Intent intent = new Intent(this, Passenger.class);
        startActivity(intent);
    }

}
