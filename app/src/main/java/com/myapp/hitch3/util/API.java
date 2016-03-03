package com.myapp.hitch3.util;

import com.myapp.hitch3.dao.RidesDAO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by olafurma on 17.2.2016.
 *
 * All HTTP Requests go through this class.
 * API handles all cookies and sessionId.
 * So all requests will have their sessionId/cookies managed from the server response.
 */
public class API {
    static CookieManager cookieManager = new CookieManager();

    /**
     * Sends a request to create a new ride to the server.
     * @param pickup        Integer, postal code of the location a passenger wants to depart from.
     * @param dropOff       Integer, postal code of the location a passenger wants to travel to.
     * @throws IOException
     */
    public static void newPassenger(int pickup, int dropOff) throws IOException {
        URL url = new URL("http://10.0.2.2:8080/rides/newRide?from="+pickup+"&to="+dropOff);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("PUT");
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            System.out.println(readStream(in));
        }
        finally {
            urlConnection.disconnect();
        }
    }

    /**
     * Cancels the ride for the current user.
     * @throws IOException
     */
    public static void cancelRide() throws IOException{
        URL url = new URL("http://10.0.2.2:8080/rides/cancelRide");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("DELETE");
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            System.out.println(readStream(in));
        }
        finally {
            urlConnection.disconnect();
        }
    }

    /**
     * Fetches all available rides on the server.
     * @return List<RidesDAO>       A list of all rides on the server.
     * @throws IOException
     * @throws JSONException
     */
    public static List<RidesDAO> fetchRides() throws IOException, JSONException{
        List<RidesDAO> ridesDAOs = new ArrayList<RidesDAO>();
        URL url = new URL("http://10.0.2.2:8080/rides");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            // Takes the response from the server, parses it to a string and parses that string to
            // a JSON array.
            JSONArray jsonArray = new JSONArray(readStream(in));
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                // Creates a new RidesDAO from the JSONObject and adds to a list of RidesDAOs.
                ridesDAOs.add( new RidesDAO(jsonObject) );
            }
        }
        finally {
            urlConnection.disconnect();
            return ridesDAOs;
        }
    }

    /**
     * Currently makes a request that returns the sessionId for further requests.
     * Sets the cookies for this session. Prints out the sessionId for fun.
     * @throws IOException
     */
    // TODO: Change documentation when facebook login has been implemented
    public static void logIn()throws IOException {
        //Needed to set the sessionId received from the server
        CookieHandler.setDefault(cookieManager);

        URL url = new URL("http://10.0.2.2:8080/login");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.connect();

        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            System.out.println(readStream(in));
        }
        finally {
            urlConnection.disconnect();
        }
    }

    /**
     * Send a message to a user with corresponding id.
     * @param message       String, message to be sent to a user
     * @param id            Integer, id that identifies a user
     * @throws IOException
     */
    public static void sendMessage(String message, int id) throws IOException{
        URL url = new URL("http://10.0.2.2:8080/messages/createMessage?message="+message+"&id="+id);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.connect();

        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            System.out.println(readStream(in));
        }
        finally {
            urlConnection.disconnect();
        }
    }

    /**
     * Fetches all messages from the server that have been sent to the current user.
     * @return List<String>         A list of all messages that have been sent to the current user
     * @throws IOException
     */
    public static List<String> fetchMessages() throws IOException {
        List<String> messages = new ArrayList<String>();
        URL url = new URL("http://10.0.2.2:8080/messages");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.connect();

        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String response = readStream(in);
            String[] messageList = response.substring(1, response.length() - 1).split(",");
            messages.addAll(Arrays.asList(messageList));
        }
        finally {
            urlConnection.disconnect();
            return messages;
        }
    }

    /**
     * readStream is used to handle all responses from a server and convert them to a string
     * that is more manageable.
     * @param is    InputStream from the server.
     * @return      String representation of the servers response.
     */
    private static String readStream(InputStream is) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            int i = is.read();
            while(i != -1) {
                bo.write(i);
                i = is.read();
            }
            return bo.toString();
        } catch (IOException e) {
            return "";
        }
    }
}
