package com.example.fatcamtel.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fatcamtel.adapter.LocationAdapter;
import com.example.fatcamtel.databinding.FragmentDashboardBinding;
import com.example.fatcamtel.model.FatLocation;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;


public class DashboardFragment extends Fragment {
    private LocationAdapter locationAdapter;
    private List<FatLocation> locationList;
    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize the list of locations
        locationList = new ArrayList<>();
        locationList.add(new FatLocation("Yaounde", new LatLng(3.8480, 11.5021), "Working"));
        locationList.add(new FatLocation("Douala", new LatLng(4.0503, 9.7679), "Not Working"));
        // Add more locations as needed

        // Initialize the LocationAdapter with the list of locations
        locationAdapter = new LocationAdapter(locationList);

        // Set up the RecyclerView
        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(locationAdapter);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}