package com.example.familymapclient.communication;

import com.example.familymapclient.util.Serializer;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import requests.AllEventsRequest;
import requests.AllPersonsRequest;
import requests.LoginRequest;
import requests.RegisterRequest;
import responses.AllEventsResponse;
import responses.AllPersonsResponse;
import responses.RegisterLoginResponse;

public class ServerProxy {
    public static final String LOCAL_SERVER_HOST = "localhost";     // IP Address
    public static final String EMULATOR_SERVER_HOST = "10.0.2.2";   // IP Address
    public static final String DEFAULT_SERVER_PORT = "8080";        // PORT Number
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String AUTHORIZATION = "Authorization";
    public static final String ACCEPT = "Accept";
    public static final String JSON_TYPE = "Application/json";

    private final String serverHost;
    private final String serverPort;

    public ServerProxy(String serverHost, String serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public RegisterLoginResponse login(LoginRequest request) {
        RegisterLoginResponse registerLoginResponse = null;

        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/login");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(GET);

            // Indicate that there will be a request body
            http.setDoOutput(true);

            http.addRequestProperty(ACCEPT, JSON_TYPE);

            // Connect to the server and send the request
            http.connect();

            // Write the request
            String reqData =
            "{\n" + "\"username\":" + request.getUsername() +
                    ",\n\"password\":" + request.getPassword() + "\n" +
                    "}";
            OutputStream reqBody = http.getOutputStream();
            Serializer.writeString(reqData, reqBody);
            reqBody.close();

            // Process the response
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream resBody = http.getInputStream();
                String resData = Serializer.readString(resBody);
                System.out.println(resData);

                Gson gson = new Gson();
                registerLoginResponse =  gson.fromJson(resData, RegisterLoginResponse.class);
            } else {
                System.out.println("ERROR: " + http.getResponseMessage());
                InputStream resBody = http.getErrorStream();
                String resData = Serializer.readString(resBody);
                System.out.println(resData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return registerLoginResponse;
    }

    public RegisterLoginResponse register(RegisterRequest request) {
        RegisterLoginResponse registerLoginResponse = null;

        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/user/register");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(GET);

            // Indicate that there will be a request body
            http.setDoOutput(true);

            http.addRequestProperty(ACCEPT, JSON_TYPE);

            // Connect to the server and send the request
            http.connect();

            // Write the request
            String reqData =
                    "{\n" +
                            "\t\"username\":" + request.getUsername() + ",\n" +
                            "\t\"password\":" + request.getPassword() + ",\n" +
                            "\t\"email\":" + request.getEmail() + ",\n" +
                            "\t\"firstName\":" + request.getFirstName() + ",\n" +
                            "\t\"lastName\":" + request.getLastName() + ",\n" +
                            "\t\"gender\":" + request.getGender() + "\n" +
                            "}";
            OutputStream reqBody = http.getOutputStream();
            Serializer.writeString(reqData, reqBody);
            reqBody.close();

            // Process the response
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream resBody = http.getInputStream();
                String resData = Serializer.readString(resBody);
                System.out.println(resData);

                Gson gson = new Gson();
                registerLoginResponse =  gson.fromJson(resData, RegisterLoginResponse.class);
            } else {
                System.out.println("ERROR: " + http.getResponseMessage());
                InputStream resBody = http.getErrorStream();
                String resData = Serializer.readString(resBody);
                System.out.println(resData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return registerLoginResponse;
    }

    public AllPersonsResponse getPeople(AllPersonsRequest request) {
        AllPersonsResponse allPersonsResponse = null;

        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/person");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(GET);

            // Indicate that there will NOT be a request body
            http.setDoOutput(false);

            http.addRequestProperty(AUTHORIZATION, request.getAuthtoken());

            http.addRequestProperty(ACCEPT, JSON_TYPE);

            // Connect to the server and send the request
            http.connect();

            // Process the response
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream resBody = http.getInputStream();
                String resData = Serializer.readString(resBody);
                System.out.println(resData);

                Gson gson = new Gson();
                allPersonsResponse =  gson.fromJson(resData, AllPersonsResponse.class);
            } else {
                System.out.println("ERROR: " + http.getResponseMessage());
                InputStream resBody = http.getErrorStream();
                String resData = Serializer.readString(resBody);
                System.out.println(resData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return allPersonsResponse;
    }

    public AllEventsResponse getEvents(AllEventsRequest request) {
        AllEventsResponse allEventsResponse = null;

        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/event");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(GET);

            // Indicate that there will NOT be a request body
            http.setDoOutput(false);

            http.addRequestProperty(AUTHORIZATION, request.getAuthtoken());

            http.addRequestProperty(ACCEPT, JSON_TYPE);

            // Connect to the server and send the request
            http.connect();

            // Process the response
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream resBody = http.getInputStream();
                String resData = Serializer.readString(resBody);
                System.out.println(resData);

                Gson gson = new Gson();
                allEventsResponse =  gson.fromJson(resData, AllEventsResponse.class);
            } else {
                System.out.println("ERROR: " + http.getResponseMessage());
                InputStream resBody = http.getErrorStream();
                String resData = Serializer.readString(resBody);
                System.out.println(resData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return allEventsResponse;
    }
}
