package com.example.familymapclient.communication;

import static com.example.familymapclient.communication.ServerProxy.DEFAULT_SERVER_PORT;
import static com.example.familymapclient.communication.ServerProxy.LOCAL_SERVER_HOST;
import static com.example.familymapclient.communication.ServerProxy.POST;
import static com.example.familymapclient.communication.ServerProxy.ACCEPT;
import static com.example.familymapclient.communication.ServerProxy.JSON_TYPE;

import com.example.familymapclient.util.Serializer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

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


public class ServerProxyTest {
    private final String TEST_SERVER_HOST = LOCAL_SERVER_HOST;
    private final String TEST_SERVER_PORT = DEFAULT_SERVER_PORT;

    @BeforeEach
    public void setup() throws IOException {
        clear();
    }

    @AfterEach
    public void tearDown() throws IOException {
        clear();
    }

    @Test
    public void testLogin_Success() {
        // Register a user
        // Create the request
        RegisterRequest registerRequest = new RegisterRequest(
                "username", "password", "email",
                "firstName", "lastName", "m");

        // Get the response
        ServerProxy serverProxy = new ServerProxy(TEST_SERVER_HOST, TEST_SERVER_PORT);
        RegisterLoginResponse actualResponse = serverProxy.register(registerRequest);

        // Will be null if there was an error registering the user
        assertNotNull(actualResponse);
        assertTrue(actualResponse.isSuccess());
        assertEquals(actualResponse.getUsername(), "username");

        // Log the user in
        // Create the login request
        LoginRequest loginRequest = new LoginRequest("username", "password");
        RegisterLoginResponse loginResponse = serverProxy.login(loginRequest);

        assertNotNull(loginResponse);
        assertTrue(loginResponse.isSuccess());
    }

    @Test
    public void testLogin_Fail() {
        // Attempt to log in a nonexistent user
        // Create the login request
        LoginRequest loginRequest = new LoginRequest("username", "password");
        ServerProxy serverProxy = new ServerProxy(TEST_SERVER_HOST, TEST_SERVER_PORT);
        RegisterLoginResponse loginResponse = serverProxy.login(loginRequest);

        assertNull(loginResponse);
    }

    @Test
    public void testRegister_Success() {
        // Create the request
        RegisterRequest registerRequest = new RegisterRequest(
                "username", "password", "email",
                "firstName", "lastName", "m");

        // Get the response
        ServerProxy serverProxy = new ServerProxy(TEST_SERVER_HOST, TEST_SERVER_PORT);
        RegisterLoginResponse actualResponse = serverProxy.register(registerRequest);

        // Will be null if there was an error registering the user
        assertNotNull(actualResponse);
        assertTrue(actualResponse.isSuccess());
        assertEquals(actualResponse.getUsername(), "username");
    }

    @Test
    public void testRegister_Fail() {
        // Create the request (Invalid request)
        RegisterRequest registerRequest = new RegisterRequest(
                "username", "password", "email",
                "firstName", "lastName", "INCORRECT");

        // Get the response
        ServerProxy serverProxy = new ServerProxy(TEST_SERVER_HOST, TEST_SERVER_PORT);
        RegisterLoginResponse actualResponse = serverProxy.register(registerRequest);

        // Will be null if there was an error registering the user
        assertNull(actualResponse);
    }

    @Test
    public void testRegister_FailRegisterTwice() {
        // Create the request
        RegisterRequest registerRequest = new RegisterRequest(
                "username", "password", "email",
                "firstName", "lastName", "m");

        // Get the response
        ServerProxy serverProxy = new ServerProxy(TEST_SERVER_HOST, TEST_SERVER_PORT);
        RegisterLoginResponse actualResponse = serverProxy.register(registerRequest);

        // Will be null if there was an error registering the user
        assertNotNull(actualResponse);
        assertTrue(actualResponse.isSuccess());
        assertEquals(actualResponse.getUsername(), "username");

        // Attempt to register the user a second time
        actualResponse = serverProxy.register(registerRequest);

        assertNull(actualResponse);
    }

    @Test
    public void testGetPeople_Success() {
        load();

        // Login to get the Authtoken
        // Create the Login request
        LoginRequest loginRequest = new LoginRequest("sheila", "parker");

        // Get the AuthToken
        ServerProxy serverProxy = new ServerProxy(TEST_SERVER_HOST, TEST_SERVER_PORT);
        RegisterLoginResponse loginResponse = serverProxy.login(loginRequest);
        assertNotNull(loginResponse);
        String authtoken = loginResponse.getAuthtoken();

        // Create the request
        AllPersonsRequest allPersonsRequest = new AllPersonsRequest(authtoken);

        // Get the response
        AllPersonsResponse allPersonsResponse = serverProxy.getPeople(allPersonsRequest);

        assertNotNull(allPersonsResponse);
        assertEquals(8, allPersonsResponse.getData().length);
    }

    @Test
    public void testGetPeople_Fail() {
        // Attempt to get someone who doesn't exist
        String authtoken = "Not_a_real_authtoken";

        // Create the request
        AllPersonsRequest allPersonsRequest = new AllPersonsRequest(authtoken);

        // Get the response
        ServerProxy serverProxy = new ServerProxy(TEST_SERVER_HOST, TEST_SERVER_PORT);
        AllPersonsResponse allPersonsResponse = serverProxy.getPeople(allPersonsRequest);

        assertNull(allPersonsResponse);
    }

    @Test
    public void testGetEvents_Success() {
        load();

        // Login to get the Authtoken
        // Create the Login request
        LoginRequest loginRequest = new LoginRequest("sheila", "parker");

        // Get the AuthToken
        ServerProxy serverProxy = new ServerProxy(TEST_SERVER_HOST, TEST_SERVER_PORT);
        RegisterLoginResponse loginResponse = serverProxy.login(loginRequest);
        assertNotNull(loginResponse);
        String authtoken = loginResponse.getAuthtoken();

        // Create the request
        AllEventsRequest allEventsRequest = new AllEventsRequest(authtoken);

        // Get the response
        AllEventsResponse allEventsResponse = serverProxy.getEvents(allEventsRequest);

        assertNotNull(allEventsResponse);
        assertEquals(16, allEventsResponse.getData().length);
    }

    @Test
    public void testGetEvents_Fail() {
        // Attempt to get someone who doesn't exist
        String authtoken = "Not_a_real_authtoken";

        // Create the request
        AllEventsRequest allEventsRequest = new AllEventsRequest(authtoken);

        // Get the response
        ServerProxy serverProxy = new ServerProxy(TEST_SERVER_HOST, TEST_SERVER_PORT);
        AllEventsResponse allEventsResponse = serverProxy.getEvents(allEventsRequest);

        assertNull(allEventsResponse);
    }

    private void clear() throws IOException {
        try {
            // Clear the server
            URL url = new URL("http://" + TEST_SERVER_HOST + ":" + TEST_SERVER_PORT + "/clear");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(POST);

            // Indicate that there will NOT be a request body
            http.setDoOutput(false);

            http.addRequestProperty(ACCEPT, JSON_TYPE);

            // Connect to the server and send the request
            http.connect();

            // Process the response
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                InputStream resBody = http.getInputStream();
//                String resData = Serializer.readString(resBody);
//                System.out.println(resData);
            } else {
                System.out.println("ERROR: " + http.getResponseMessage());
                InputStream resBody = http.getErrorStream();
                String resData = Serializer.readString(resBody);
                System.out.println(resData);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
    }

    private void load() {
        // Load the data
        try {
            URL url = new URL("http://" + TEST_SERVER_HOST + ":" + TEST_SERVER_PORT + "/load");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(POST);

            // Indicate that there will be a request body
            http.setDoOutput(true);

            http.addRequestProperty(ACCEPT, JSON_TYPE);

            // Connect to the server and send the request
            http.connect();

            // Write the request
            String reqData = "{\n" +
                    "\n" +
                    "    \"users\": [\n" +
                    "\n" +
                    "        {\n" +
                    "\n" +
                    "            \"username\": \"sheila\",\n" +
                    "\n" +
                    "            \"password\": \"parker\",\n" +
                    "\n" +
                    "            \"email\": \"sheila@parker.com\",\n" +
                    "\n" +
                    "            \"firstName\": \"Sheila\",\n" +
                    "\n" +
                    "            \"lastName\": \"Parker\",\n" +
                    "\n" +
                    "            \"gender\": \"f\",\n" +
                    "\n" +
                    "            \"personID\": \"Sheila_Parker\"\n" +
                    "\n" +
                    "        },\n" +
                    "\n" +
                    "        {\n" +
                    "\n" +
                    "            \"username\": \"patrick\",\n" +
                    "\n" +
                    "            \"password\": \"spencer\",\n" +
                    "\n" +
                    "            \"email\": \"patrick@spencer.com\",\n" +
                    "\n" +
                    "            \"firstName\": \"Patrick\",\n" +
                    "\n" +
                    "            \"lastName\": \"Spencer\",\n" +
                    "\n" +
                    "            \"gender\": \"m\",\n" +
                    "\n" +
                    "            \"personID\": \"Patrick_Spencer\"\n" +
                    "\n" +
                    "        }\n" +
                    "\n" +
                    "    ],\n" +
                    "\n" +
                    "    \"persons\": [\n" +
                    "\n" +
                    "        {\n" +
                    "\n" +
                    "            \"firstName\": \"Sheila\",\n" +
                    "\n" +
                    "            \"lastName\": \"Parker\",\n" +
                    "\n" +
                    "            \"gender\": \"f\",\n" +
                    "\n" +
                    "            \"personID\": \"Sheila_Parker\",\n" +
                    "\n" +
                    "            \"spouseID\": \"Davis_Hyer\",\n" +
                    "\n" +
                    "            \"fatherID\": \"Blaine_McGary\",\n" +
                    "\n" +
                    "            \"motherID\": \"Betty_White\",\n" +
                    "\n" +
                    "            \"associatedUsername\": \"sheila\"\n" +
                    "\n" +
                    "        },\n" +
                    "\n" +
                    "        {\n" +
                    "\n" +
                    "            \"firstName\": \"Davis\",\n" +
                    "\n" +
                    "            \"lastName\": \"Hyer\",\n" +
                    "\n" +
                    "            \"gender\": \"m\",\n" +
                    "\n" +
                    "            \"personID\": \"Davis_Hyer\",\n" +
                    "\n" +
                    "            \"spouseID\": \"Sheila_Parker\",\n" +
                    "\n" +
                    "            \"associatedUsername\": \"sheila\"\n" +
                    "\n" +
                    "        },\n" +
                    "\n" +
                    "        {\n" +
                    "\n" +
                    "            \"firstName\": \"Blaine\",\n" +
                    "\n" +
                    "            \"lastName\": \"McGary\",\n" +
                    "\n" +
                    "            \"gender\": \"m\",\n" +
                    "\n" +
                    "            \"personID\": \"Blaine_McGary\",\n" +
                    "\n" +
                    "            \"fatherID\": \"Ken_Rodham\",\n" +
                    "\n" +
                    "            \"motherID\": \"Mrs_Rodham\",\n" +
                    "\n" +
                    "            \"spouseID\": \"Betty_White\",\n" +
                    "\n" +
                    "            \"associatedUsername\": \"sheila\"\n" +
                    "\n" +
                    "        },\n" +
                    "\n" +
                    "        {\n" +
                    "\n" +
                    "            \"firstName\": \"Betty\",\n" +
                    "\n" +
                    "            \"lastName\": \"White\",\n" +
                    "\n" +
                    "            \"gender\": \"f\",\n" +
                    "\n" +
                    "            \"personID\": \"Betty_White\",\n" +
                    "\n" +
                    "            \"fatherID\": \"Frank_Jones\",\n" +
                    "\n" +
                    "            \"motherID\": \"Mrs_Jones\",\n" +
                    "\n" +
                    "            \"spouseID\": \"Blaine_McGary\",\n" +
                    "\n" +
                    "            \"associatedUsername\": \"sheila\"\n" +
                    "\n" +
                    "        },\n" +
                    "\n" +
                    "        {\n" +
                    "\n" +
                    "            \"firstName\": \"Ken\",\n" +
                    "\n" +
                    "            \"lastName\": \"Rodham\",\n" +
                    "\n" +
                    "            \"gender\": \"m\",\n" +
                    "\n" +
                    "            \"personID\": \"Ken_Rodham\",\n" +
                    "\n" +
                    "            \"spouseID\": \"Mrs_Rodham\",\n" +
                    "\n" +
                    "            \"associatedUsername\": \"sheila\"\n" +
                    "\n" +
                    "        },\n" +
                    "\n" +
                    "        {\n" +
                    "\n" +
                    "            \"firstName\": \"Mrs\",\n" +
                    "\n" +
                    "            \"lastName\": \"Rodham\",\n" +
                    "\n" +
                    "            \"gender\": \"f\",\n" +
                    "\n" +
                    "            \"personID\": \"Mrs_Rodham\",\n" +
                    "\n" +
                    "            \"spouseID\": \"Ken_Rodham\",\n" +
                    "\n" +
                    "            \"associatedUsername\": \"sheila\"\n" +
                    "\n" +
                    "        },\n" +
                    "\n" +
                    "        {\n" +
                    "\n" +
                    "            \"firstName\": \"Frank\",\n" +
                    "\n" +
                    "            \"lastName\": \"Jones\",\n" +
                    "\n" +
                    "            \"gender\": \"m\",\n" +
                    "\n" +
                    "            \"personID\": \"Frank_Jones\",\n" +
                    "\n" +
                    "            \"spouseID\": \"Mrs_Jones\",\n" +
                    "\n" +
                    "            \"associatedUsername\": \"sheila\"\n" +
                    "\n" +
                    "        },\n" +
                    "\n" +
                    "        {\n" +
                    "\n" +
                    "            \"firstName\": \"Mrs\",\n" +
                    "\n" +
                    "            \"lastName\": \"Jones\",\n" +
                    "\n" +
                    "            \"gender\": \"f\",\n" +
                    "\n" +
                    "            \"personID\": \"Mrs_Jones\",\n" +
                    "\n" +
                    "            \"spouseID\": \"Frank_Jones\",\n" +
                    "\n" +
                    "            \"associatedUsername\": \"sheila\"\n" +
                    "\n" +
                    "        },\n" +
                    "\n" +
                    "        {\n" +
                    "\n" +
                    "            \"firstName\": \"Patrick\",\n" +
                    "\n" +
                    "            \"lastName\": \"Spencer\",\n" +
                    "\n" +
                    "            \"gender\": \"m\",\n" +
                    "\n" +
                    "            \"personID\": \"Patrick_Spencer\",\n" +
                    "\n" +
                    "            \"associatedUsername\": \"patrick\",\n" +
                    "\n" +
                    "            \"fatherID\": \"Happy_Birthday\",\n" +
                    "\n" +
                    "            \"motherID\": \"Golden_Boy\"\n" +
                    "\n" +
                    "        },\n" +
                    "\n" +
                    "        {\n" +
                    "\n" +
                    "            \"firstName\": \"Patrick\",\n" +
                    "\n" +
                    "            \"lastName\": \"Wilson\",\n" +
                    "\n" +
                    "            \"gender\": \"m\",\n" +
                    "\n" +
                    "            \"personID\": \"Happy_Birthday\",\n" +
                    "\n" +
                    "            \"associatedUsername\": \"patrick\",\n" +
                    "\n" +
                    "            \"spouseID\": \"Golden_Boy\"\n" +
                    "\n" +
                    "        },\n" +
                    "\n" +
                    "        {\n" +
                    "\n" +
                    "            \"firstName\": \"Spencer\",\n" +
                    "\n" +
                    "            \"lastName\": \"Seeger\",\n" +
                    "\n" +
                    "            \"gender\": \"f\",\n" +
                    "\n" +
                    "            \"personID\": \"Golden_Boy\",\n" +
                    "\n" +
                    "            \"associatedUsername\": \"patrick\",\n" +
                    "\n" +
                    "            \"spouseID\": \"Happy_Birthday\"\n" +
                    "\n" +
                    "        }\n" +
                    "\n" +
                    "    ],\n" +
                    "\n" +
                    "    \"events\": [\n" +
                    "\n" +
                    "        {\n" +
                    "\n" +
                    "            \"eventType\": \"birth\",\n" +
                    "\n" +
                    "            \"personID\": \"Sheila_Parker\",\n" +
                    "\n" +
                    "            \"city\": \"Melbourne\",\n" +
                    "\n" +
                    "            \"country\": \"Australia\",\n" +
                    "\n" +
                    "            \"latitude\": -36.1833,\n" +
                    "\n" +
                    "            \"longitude\": 144.9667,\n" +
                    "\n" +
                    "            \"year\": 1970,\n" +
                    "\n" +
                    "            \"eventID\": \"Sheila_Birth\",\n" +
                    "\n" +
                    "            \"associatedUsername\": \"sheila\"\n" +
                    "\n" +
                    "        },\n" +
                    "\n" +
                    "        {\n" +
                    "\n" +
                    "            \"eventType\": \"marriage\",\n" +
                    "\n" +
                    "            \"personID\": \"Sheila_Parker\",\n" +
                    "\n" +
                    "            \"city\": \"Los Angeles\",\n" +
                    "\n" +
                    "            \"country\": \"United States\",\n" +
                    "\n" +
                    "            \"latitude\": 34.0500,\n" +
                    "\n" +
                    "            \"longitude\": -117.7500,\n" +
                    "\n" +
                    "            \"year\": 2012,\n" +
                    "\n" +
                    "            \"eventID\": \"Sheila_Marriage\",\n" +
                    "\n" +
                    "            \"associatedUsername\": \"sheila\"\n" +
                    "\n" +
                    "        },\n" +
                    "\n" +
                    "        {\n" +
                    "\n" +
                    "            \"eventType\": \"completed asteroids\",\n" +
                    "\n" +
                    "            \"personID\": \"Sheila_Parker\",\n" +
                    "\n" +
                    "            \"city\": \"Qaanaaq\",\n" +
                    "\n" +
                    "            \"country\": \"Denmark\",\n" +
                    "\n" +
                    "            \"latitude\": 77.4667,\n" +
                    "\n" +
                    "            \"longitude\": -68.7667,\n" +
                    "\n" +
                    "            \"year\": 2014,\n" +
                    "\n" +
                    "            \"eventID\": \"Sheila_Asteroids\",\n" +
                    "\n" +
                    "            \"associatedUsername\": \"sheila\"\n" +
                    "\n" +
                    "        },\n" +
                    "\n" +
                    "        {\n" +
                    "\n" +
                    "            \"eventType\": \"COMPLETED ASTEROIDS\",\n" +
                    "\n" +
                    "            \"personID\": \"Sheila_Parker\",\n" +
                    "\n" +
                    "            \"city\": \"Qaanaaq\",\n" +
                    "\n" +
                    "            \"country\": \"Denmark\",\n" +
                    "\n" +
                    "            \"latitude\": 74.4667,\n" +
                    "\n" +
                    "            \"longitude\": -60.7667,\n" +
                    "\n" +
                    "            \"year\": 2014,\n" +
                    "\n" +
                    "            \"eventID\": \"Other_Asteroids\",\n" +
                    "\n" +
                    "            \"associatedUsername\": \"sheila\"\n" +
                    "\n" +
                    "        },\n" +
                    "\n" +
                    "        {\n" +
                    "\n" +
                    "            \"eventType\": \"death\",\n" +
                    "\n" +
                    "            \"personID\": \"Sheila_Parker\",\n" +
                    "\n" +
                    "            \"city\": \"Hohhot\",\n" +
                    "\n" +
                    "            \"country\": \"China\",\n" +
                    "\n" +
                    "            \"latitude\": 40.2444,\n" +
                    "\n" +
                    "            \"longitude\": 111.6608,\n" +
                    "\n" +
                    "            \"year\": 2015,\n" +
                    "\n" +
                    "            \"eventID\": \"Sheila_Death\",\n" +
                    "\n" +
                    "            \"associatedUsername\": \"sheila\"\n" +
                    "\n" +
                    "        },\n" +
                    "\n" +
                    "        {\n" +
                    "\n" +
                    "            \"eventType\": \"birth\",\n" +
                    "\n" +
                    "            \"personID\": \"Davis_Hyer\",\n" +
                    "\n" +
                    "            \"city\": \"Hakodate\",\n" +
                    "\n" +
                    "            \"country\": \"Japan\",\n" +
                    "\n" +
                    "            \"latitude\": 41.7667,\n" +
                    "\n" +
                    "            \"longitude\": 140.7333,\n" +
                    "\n" +
                    "            \"year\": 1970,\n" +
                    "\n" +
                    "            \"eventID\": \"Davis_Birth\",\n" +
                    "\n" +
                    "            \"associatedUsername\": \"sheila\"\n" +
                    "\n" +
                    "        },\n" +
                    "\n" +
                    "        {\n" +
                    "\n" +
                    "            \"eventType\": \"birth\",\n" +
                    "\n" +
                    "            \"personID\": \"Blaine_McGary\",\n" +
                    "\n" +
                    "            \"city\": \"Bratsk\",\n" +
                    "\n" +
                    "            \"country\": \"Russia\",\n" +
                    "\n" +
                    "            \"latitude\": 56.1167,\n" +
                    "\n" +
                    "            \"longitude\": 101.6000,\n" +
                    "\n" +
                    "            \"year\": 1948,\n" +
                    "\n" +
                    "            \"eventID\": \"Blaine_Birth\",\n" +
                    "\n" +
                    "            \"associatedUsername\": \"sheila\"\n" +
                    "\n" +
                    "        },\n" +
                    "\n" +
                    "        {\n" +
                    "\n" +
                    "            \"eventType\": \"death\",\n" +
                    "\n" +
                    "            \"personID\": \"Betty_White\",\n" +
                    "\n" +
                    "            \"city\": \"Birmingham\",\n" +
                    "\n" +
                    "            \"country\": \"United Kingdom\",\n" +
                    "\n" +
                    "            \"latitude\": 52.4833,\n" +
                    "\n" +
                    "            \"longitude\": -0.1000,\n" +
                    "\n" +
                    "            \"year\": 2017,\n" +
                    "\n" +
                    "            \"eventID\": \"Betty_Death\",\n" +
                    "\n" +
                    "            \"associatedUsername\": \"sheila\"\n" +
                    "\n" +
                    "        },\n" +
                    "\n" +
                    "        {\n" +
                    "\n" +
                    "            \"eventType\": \"Graduated from BYU\",\n" +
                    "\n" +
                    "            \"personID\": \"Ken_Rodham\",\n" +
                    "\n" +
                    "            \"country\": \"United States\",\n" +
                    "\n" +
                    "            \"city\": \"Provo\",\n" +
                    "\n" +
                    "            \"latitude\": 40.2338,\n" +
                    "\n" +
                    "            \"longitude\": -111.6585,\n" +
                    "\n" +
                    "            \"year\": 1879,\n" +
                    "\n" +
                    "            \"eventID\": \"BYU_graduation\",\n" +
                    "\n" +
                    "            \"associatedUsername\": \"sheila\"\n" +
                    "\n" +
                    "        },\n" +
                    "\n" +
                    "        {\n" +
                    "\n" +
                    "            \"eventType\": \"marriage\",\n" +
                    "\n" +
                    "            \"personID\": \"Ken_Rodham\",\n" +
                    "\n" +
                    "            \"country\": \"North Korea\",\n" +
                    "\n" +
                    "            \"city\": \"Wonsan\",\n" +
                    "\n" +
                    "            \"latitude\": 39.15,\n" +
                    "\n" +
                    "            \"longitude\": 127.45,\n" +
                    "\n" +
                    "            \"year\": 1895,\n" +
                    "\n" +
                    "            \"eventID\": \"Rodham_Marriage\",\n" +
                    "\n" +
                    "            \"associatedUsername\": \"sheila\"\n" +
                    "\n" +
                    "        },\n" +
                    "\n" +
                    "        {\n" +
                    "\n" +
                    "            \"eventType\": \"Did a backflip\",\n" +
                    "\n" +
                    "            \"personID\": \"Mrs_Rodham\",\n" +
                    "\n" +
                    "            \"country\": \"Mexico\",\n" +
                    "\n" +
                    "            \"city\": \"Mexicali\",\n" +
                    "\n" +
                    "            \"latitude\": 32.6667,\n" +
                    "\n" +
                    "            \"longitude\": -114.5333,\n" +
                    "\n" +
                    "            \"year\": 1890,\n" +
                    "\n" +
                    "            \"eventID\": \"Mrs_Rodham_Backflip\",\n" +
                    "\n" +
                    "            \"associatedUsername\": \"sheila\"\n" +
                    "\n" +
                    "        },\n" +
                    "\n" +
                    "        {\n" +
                    "\n" +
                    "            \"eventType\": \"learned Java\",\n" +
                    "\n" +
                    "            \"personID\": \"Mrs_Rodham\",\n" +
                    "\n" +
                    "            \"country\": \"Algeria\",\n" +
                    "\n" +
                    "            \"city\": \"Algiers\",\n" +
                    "\n" +
                    "            \"latitude\": 36.7667,\n" +
                    "\n" +
                    "            \"longitude\": 3.2167,\n" +
                    "\n" +
                    "            \"year\": 1890,\n" +
                    "\n" +
                    "            \"eventID\": \"Mrs_Rodham_Java\",\n" +
                    "\n" +
                    "            \"associatedUsername\": \"sheila\"\n" +
                    "\n" +
                    "        },\n" +
                    "\n" +
                    "        {\n" +
                    "\n" +
                    "            \"eventType\": \"Caught a frog\",\n" +
                    "\n" +
                    "            \"personID\": \"Frank_Jones\",\n" +
                    "\n" +
                    "            \"country\": \"Bahamas\",\n" +
                    "\n" +
                    "            \"city\": \"Nassau\",\n" +
                    "\n" +
                    "            \"latitude\": 25.0667,\n" +
                    "\n" +
                    "            \"longitude\": -76.6667,\n" +
                    "\n" +
                    "            \"year\": 1993,\n" +
                    "\n" +
                    "            \"eventID\": \"Jones_Frog\",\n" +
                    "\n" +
                    "            \"associatedUsername\": \"sheila\"\n" +
                    "\n" +
                    "        },\n" +
                    "\n" +
                    "        {\n" +
                    "\n" +
                    "            \"eventType\": \"marriage\",\n" +
                    "\n" +
                    "            \"personID\": \"Frank_Jones\",\n" +
                    "\n" +
                    "            \"country\": \"Ghana\",\n" +
                    "\n" +
                    "            \"city\": \"Tamale\",\n" +
                    "\n" +
                    "            \"latitude\": 9.4,\n" +
                    "\n" +
                    "            \"longitude\": 0.85,\n" +
                    "\n" +
                    "            \"year\": 1997,\n" +
                    "\n" +
                    "            \"eventID\": \"Jones_Marriage\",\n" +
                    "\n" +
                    "            \"associatedUsername\": \"sheila\"\n" +
                    "\n" +
                    "        },\n" +
                    "\n" +
                    "        {\n" +
                    "\n" +
                    "            \"eventType\": \"Ate Brazilian Barbecue\",\n" +
                    "\n" +
                    "            \"personID\": \"Mrs_Jones\",\n" +
                    "\n" +
                    "            \"country\": \"Brazil\",\n" +
                    "\n" +
                    "            \"city\": \"Curitiba\",\n" +
                    "\n" +
                    "            \"latitude\": -24.5833,\n" +
                    "\n" +
                    "            \"longitude\": -48.75,\n" +
                    "\n" +
                    "            \"year\": 2012,\n" +
                    "\n" +
                    "            \"eventID\": \"Mrs_Jones_Barbecue\",\n" +
                    "\n" +
                    "            \"associatedUsername\": \"sheila\"\n" +
                    "\n" +
                    "        },\n" +
                    "\n" +
                    "        {\n" +
                    "\n" +
                    "            \"eventType\": \"Learned to Surf\",\n" +
                    "\n" +
                    "            \"personID\": \"Mrs_Jones\",\n" +
                    "\n" +
                    "            \"country\": \"Australia\",\n" +
                    "\n" +
                    "            \"city\": \"Gold Coast\",\n" +
                    "\n" +
                    "            \"latitude\": -27.9833,\n" +
                    "\n" +
                    "            \"longitude\": 153.4,\n" +
                    "\n" +
                    "            \"year\": 2000,\n" +
                    "\n" +
                    "            \"eventID\": \"Mrs_Jones_Surf\",\n" +
                    "\n" +
                    "            \"associatedUsername\": \"sheila\"\n" +
                    "\n" +
                    "        },\n" +
                    "\n" +
                    "        {\n" +
                    "\n" +
                    "            \"eventType\": \"birth\",\n" +
                    "\n" +
                    "            \"personID\": \"Patrick_Spencer\",\n" +
                    "\n" +
                    "            \"city\": \"Grise Fiord\",\n" +
                    "\n" +
                    "            \"country\": \"Canada\",\n" +
                    "\n" +
                    "            \"latitude\": 76.4167,\n" +
                    "\n" +
                    "            \"longitude\": -81.1,\n" +
                    "\n" +
                    "            \"year\": 2016,\n" +
                    "\n" +
                    "            \"eventID\": \"Thanks_Woodfield\",\n" +
                    "\n" +
                    "            \"associatedUsername\": \"patrick\"\n" +
                    "\n" +
                    "        },\n" +
                    "\n" +
                    "        {\n" +
                    "\n" +
                    "            \"eventType\": \"marriage\",\n" +
                    "\n" +
                    "            \"personID\": \"Happy_Birthday\",\n" +
                    "\n" +
                    "            \"city\": \"Boise\",\n" +
                    "\n" +
                    "            \"country\": \"United States\",\n" +
                    "\n" +
                    "            \"latitude\": 43.6167,\n" +
                    "\n" +
                    "            \"longitude\": -115.8,\n" +
                    "\n" +
                    "            \"year\": 2016,\n" +
                    "\n" +
                    "            \"eventID\": \"True_Love\",\n" +
                    "\n" +
                    "            \"associatedUsername\": \"patrick\"\n" +
                    "\n" +
                    "        },\n" +
                    "\n" +
                    "        {\n" +
                    "\n" +
                    "            \"eventType\": \"marriage\",\n" +
                    "\n" +
                    "            \"personID\": \"Golden_Boy\",\n" +
                    "\n" +
                    "            \"city\": \"Boise\",\n" +
                    "\n" +
                    "            \"country\": \"United States\",\n" +
                    "\n" +
                    "            \"latitude\": 43.6167,\n" +
                    "\n" +
                    "            \"longitude\": -115.8,\n" +
                    "\n" +
                    "            \"year\": 2016,\n" +
                    "\n" +
                    "            \"eventID\": \"Together_Forever\",\n" +
                    "\n" +
                    "            \"associatedUsername\": \"patrick\"\n" +
                    "\n" +
                    "        }\n" +
                    "\n" +
                    "    ]\n" +
                    "\n" +
                    "}";

            OutputStream reqBody = http.getOutputStream();
            Serializer.writeString(reqData, reqBody);
            reqBody.close();

            // Process the response
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                InputStream resBody = http.getInputStream();
//                String resData = Serializer.readString(resBody);
//                System.out.println(resData);
            } else {
                System.out.println("ERROR: " + http.getResponseMessage());
                InputStream resBody = http.getErrorStream();
                String resData = Serializer.readString(resBody);
                System.out.println(resData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
