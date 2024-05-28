package com.example.fatcamtel.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FatLocation {
    private String name;
    private LatLng location;
    private String state;

    // Default constructor required for calls to DataSnapshot.getValue(FatLocation.class)
    public FatLocation() {}

    public FatLocation(String name, LatLng location, String state) {
        this.name = name;
        this.location = location;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "FatLocation{" +
                "name='" + name + '\'' +
                ", location=" + location +
                ", state='" + state + '\'' +
                '}';
    }

    public enum State {
        WORKING,
        NOT_WORKING;
    }

    // Save location to Firebase
    public void saveToFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference locationsRef = database.getReference("locations");

        // Create a map to store the values
        Map<String, Object> locationValues = new HashMap<>();
        locationValues.put("name", name);
        locationValues.put("latitude", location.latitude);
        locationValues.put("longitude", location.longitude);
        locationValues.put("state", state);

        // Push the new location to Firebase
        locationsRef.push().setValue(locationValues);
    }

    // Retrieve all locations from Firebase
    public static void getAllLocationsFromFirebase(final FirebaseCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference locationsRef = database.getReference("locations");

        locationsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<FatLocation> locations = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String name = snapshot.child("name").getValue(String.class);
                    double latitude = snapshot.child("latitude").getValue(Double.class);
                    double longitude = snapshot.child("longitude").getValue(Double.class);
                    String state = snapshot.child("state").getValue(String.class);

                    LatLng location = new LatLng(latitude, longitude);
                    FatLocation fatLocation = new FatLocation(name, location, state);
                    locations.add(fatLocation);
                }
                callback.onCallback(locations);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    public interface FirebaseCallback {
        void onCallback(List<FatLocation> locations);
    }
    public void updateLocationInFirebase(String locationKey) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference locationsRef = database.getReference("locations").child(locationKey);

        // Create a map to store the updated values
        Map<String, Object> updatedValues = new HashMap<>();
        updatedValues.put("name", name);
        updatedValues.put("latitude", location.latitude);
        updatedValues.put("longitude", location.longitude);
        updatedValues.put("state", state);

        // Update the location in Firebase
        locationsRef.updateChildren(updatedValues);
    }

}
