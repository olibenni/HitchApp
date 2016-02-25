package com.myapp.hitch3.dao;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by olafurma on 22.2.2016.
 *
 * Holds an object sent from the server. Each Ride has an pickUp and dropOff postal code
 * and a designated ID for that corresponding passenger.
 */
public class RidesDAO {
    private int pickup;
    private int dropOff;
    private int id;

    // Create a new empty Ride
    public RidesDAO(){};

    /**
     * Creates a new Ride from a JSONObject
     * @param jsonObject        A JSONObject in a Ride format
     * @throws JSONException
     */
    public RidesDAO(JSONObject jsonObject) throws JSONException
    {
        this.pickup  = jsonObject.getInt("pickup");
        this.dropOff = jsonObject.getInt("dropOff");
        this.id      = jsonObject.getInt("id");
    }

    /**
     * Creates a new Ride from pickup, dropOff and an id
     * @param pickup        Integer, postal code of the location a passenger wants to depart from
     * @param dropOff       Integer, postal code of the location a passenger wants to travel to
     * @param id            Integer, id of the corresponding passenger
     */
    public RidesDAO(int pickup, int dropOff, int id)
    {
        this.pickup = pickup;
        this.dropOff = dropOff;
        this.id = id;
    }

    // Setters and getters
    public int getPickup() {
        return pickup;
    }

    public void setPickup(int pickup) {
        this.pickup = pickup;
    }

    public int getDropOff() {
        return this.dropOff;
    }

    public void setDropOff(int dropOff) {
        this.dropOff = dropOff;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
