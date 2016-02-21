package com.myapp.hitch3.util;


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by olafurma on 17.2.2016.
 */
public class API {
    static CookieManager cookieManager = new CookieManager();

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
