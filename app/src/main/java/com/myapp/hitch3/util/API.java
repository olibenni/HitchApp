package com.myapp.hitch3.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by olafurma on 17.2.2016.
 */
public class API {

    public static void newPassenger(int pickup, int dropOff, int phone) throws IOException {
        System.out.println("SENDING");
        URL url = new URL("http://10.0.2.2:8080/newRide?from="+pickup+"&to="+dropOff+"&phone="+phone);
        System.out.println("URL SUCCESS");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        System.out.println("DISCONNECTING");
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            System.out.println(readStream(in));
        }
        finally {
            urlConnection.disconnect();
        }
    }

    public static void logIn()throws IOException {
        URL url = new URL("http://10.0.2.2:8080/fromPassenger?info='hello'");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
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
