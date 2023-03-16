package com.example.familymapclient.communication;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
    public static final String SERVER_HOST = "10.0.2.2";  // IP Address
    public static final String SERVER_PORT = "FIXME";  // PORT Number FIXME!
    private final String GET = "GET";
    private final String AUTHORIZATION = "Authorization";
    private final String ACCEPT = "Accept";
    private final String JSON_TYPE = "Application/json";

    public ServerProxy() {
    }

    RegisterLoginResponse login(LoginRequest request) {
        RegisterLoginResponse registerLoginResponse = null;

        try {
            URL url = new URL("http://" + SERVER_HOST + ":" + SERVER_PORT + "/user/login");
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
                    "}";    // FIXME MAKE SURE THE FORMATTING IS CORRECT!
            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);
            reqBody.close();

            // Process the response
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream resBody = http.getInputStream();
                String resData = readString(resBody);
                System.out.println(resData);

                Gson gson = new Gson();
                registerLoginResponse =  gson.fromJson(resData, RegisterLoginResponse.class);
            } else {
                System.out.println("ERROR: " + http.getResponseMessage());
                InputStream resBody = http.getErrorStream();
                String resData = readString(resBody);
                System.out.println(resData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return registerLoginResponse;
    }

    RegisterLoginResponse register(RegisterRequest request) {
        RegisterLoginResponse registerLoginResponse = null;

        try {
            URL url = new URL("http://" + SERVER_HOST + ":" + SERVER_PORT + "/user/register");
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
                            "}";    // FIXME MAKE SURE THE FORMATTING IS CORRECT!
            OutputStream reqBody = http.getOutputStream();
            writeString(reqData, reqBody);
            reqBody.close();

            // Process the response
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream resBody = http.getInputStream();
                String resData = readString(resBody);
                System.out.println(resData);

                Gson gson = new Gson();
                registerLoginResponse =  gson.fromJson(resData, RegisterLoginResponse.class);
            } else {
                System.out.println("ERROR: " + http.getResponseMessage());
                InputStream resBody = http.getErrorStream();
                String resData = readString(resBody);
                System.out.println(resData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return registerLoginResponse;
    }

    AllPersonsResponse getPeople(AllPersonsRequest request) {
        AllPersonsResponse allPersonsResponse = null;

        try {
            URL url = new URL("http://" + SERVER_HOST + ":" + SERVER_PORT + "/person");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(GET);

            // Indicate that there will be a request body
            http.setDoOutput(false);

            http.addRequestProperty(AUTHORIZATION, request.getAuthtoken());

            http.addRequestProperty(ACCEPT, JSON_TYPE);

            // Connect to the server and send the request
            http.connect();

            // Process the response
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream resBody = http.getInputStream();
                String resData = readString(resBody);
                System.out.println(resData);

                Gson gson = new Gson();
                allPersonsResponse =  gson.fromJson(resData, AllPersonsResponse.class);
            } else {
                System.out.println("ERROR: " + http.getResponseMessage());
                InputStream resBody = http.getErrorStream();
                String resData = readString(resBody);
                System.out.println(resData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return allPersonsResponse;
    }

    AllEventsResponse getEvents(AllEventsRequest request) {
        AllEventsResponse allEventsResponse = null;

        try {
            URL url = new URL("http://" + SERVER_HOST + ":" + SERVER_PORT + "/event");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(GET);

            // Indicate that there will be a request body
            http.setDoOutput(false);

            http.addRequestProperty(AUTHORIZATION, request.getAuthtoken());

            http.addRequestProperty(ACCEPT, JSON_TYPE);

            // Connect to the server and send the request
            http.connect();

            // Process the response
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream resBody = http.getInputStream();
                String resData = readString(resBody);
                System.out.println(resData);

                Gson gson = new Gson();
                allEventsResponse =  gson.fromJson(resData, AllEventsResponse.class);
            } else {
                System.out.println("ERROR: " + http.getResponseMessage());
                InputStream resBody = http.getErrorStream();
                String resData = readString(resBody);
                System.out.println(resData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return allEventsResponse;
    }


    private String readString(InputStream inputStream) throws IOException {
        InputStreamReader inputReader = new InputStreamReader(inputStream);
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[1024];
        int len;
        while ((len = inputReader.read(buffer)) > 0) {
            builder.append(buffer, 0, len);
        }

        return builder.toString();
    }

    private void writeString(String str, OutputStream outputStream) throws IOException {
        OutputStreamWriter outWriter = new OutputStreamWriter(outputStream);
        outWriter.write(str);
        outWriter.flush();
    }
}
