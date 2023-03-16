package com.example.familymapclient.data;

import java.util.List;
import java.util.Map;
import java.util.Set;

import model.AuthToken;
import model.Event;
import model.Person;

public class DataCache {

    private static DataCache instance;

    public DataCache getInstance() {
        if (instance == null) {
            instance = new DataCache();
        }
        return instance;
    }

    private DataCache() {
        settings = new Settings();
    }

    // Unfiltered data from server
    AuthToken authToken;
    String userPersonID;
    Map<String, Person> people;             // personID -> Person
    Map<String, Event> events;              // eventID -> Event
    Map<String, List<Event>> personEvents;   // personID -> Their events
    Set<String> paternalAncestors;          // Set of personID's
    Set<String> maternalAncestors;          // Set of personID's

    Settings settings;


    // TODO Store marker color for each event type

    // Filtered data
    // TODO Add separate way to store filtered data




}
