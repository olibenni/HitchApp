package com.myapp.hitch3.dao;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by olafurma on 22.2.2016.
 */
public class RidesDAO {
    private int pickup;
    private int dropOff;
    private int id;

    public RidesDAO(){};

    public RidesDAO(JSONObject jsonObject) throws JSONException
    {
        this.pickup  = jsonObject.getInt("pickup");
        this.dropOff = jsonObject.getInt("dropOff");
        this.id      = jsonObject.getInt("id");
    }

    public RidesDAO(int pickup, int dropOff, int id)
    {
        this.pickup = pickup;
        this.dropOff = dropOff;
        this.id = id;
    }

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

    public void getId(int id) {
        this.id = id;
    }
}
