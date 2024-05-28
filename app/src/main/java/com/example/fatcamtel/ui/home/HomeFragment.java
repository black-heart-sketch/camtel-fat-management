package com.example.fatcamtel.ui.home;

import android.app.AlertDialog;
import android.app.Notification;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fatcamtel.R;
import com.example.fatcamtel.adapter.LocationAdapter;
import com.example.fatcamtel.databinding.FragmentHomeBinding;
import com.example.fatcamtel.model.FatLocation;

import com.example.fatcamtel.ui.notifications.NotificationsFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements LocationAdapter.OnItemClickListener {

    private FragmentHomeBinding binding;
    private LocationAdapter locationAdapter;
    private List<FatLocation> locationList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize the list of locations
        locationList = new ArrayList<>();

        // Initialize the LocationAdapter with the list of locations and the click listener
        locationAdapter = new LocationAdapter(locationList, this);

        // Set up the RecyclerView
        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(locationAdapter);

        // Fetch locations from Firebase
        FatLocation.getAllLocationsFromFirebase(new FatLocation.FirebaseCallback() {
            @Override
            public void onCallback(List<FatLocation> locations) {
                locationList.clear();
                locationList.addAll(locations);
                locationAdapter.notifyDataSetChanged();
            }
        });

        // Set up the Add button
        ConstraintLayout addFat = binding.addFat;
        addFat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddLocationDialog();
            }
        });

        return root;
    }

    private void showAddLocationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_location, null);
        builder.setView(dialogView);

        EditText editTextName = dialogView.findViewById(R.id.editTextName);
        EditText editTextLatitude = dialogView.findViewById(R.id.editTextLatitude);
        EditText editTextLongitude = dialogView.findViewById(R.id.editTextLongitude);
        EditText editTextState = dialogView.findViewById(R.id.editTextState);
        Button buttonAddLocation = dialogView.findViewById(R.id.buttonAddLocation);

        AlertDialog dialog = builder.create();

        buttonAddLocation.setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            double latitude = Double.parseDouble(editTextLatitude.getText().toString());
            double longitude = Double.parseDouble(editTextLongitude.getText().toString());
            String state = editTextState.getText().toString();

            LatLng location = new LatLng(latitude, longitude);
            FatLocation newLocation = new FatLocation(name, location, state);

            locationList.add(newLocation);
            newLocation.saveToFirebase();
            locationAdapter.notifyItemInserted(locationList.size() - 1);
            dialog.dismiss();
        });

        dialog.show();
    }

    @Override
    public void onItemClick(FatLocation location) {
        double latitude = location.getLocation().latitude;
        double longitude = location.getLocation().longitude;

        Bundle bundle = new Bundle();
        bundle.putDouble("latitude", latitude);
        bundle.putDouble("longitude", longitude);

        NotificationsFragment detailFragment = new NotificationsFragment();
        detailFragment.setArguments(bundle);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.map, detailFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
