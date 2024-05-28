package com.example.fatcamtel.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fatcamtel.R;
import com.example.fatcamtel.model.FatLocation;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    private List<FatLocation> locationList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(FatLocation location);
    }

    public LocationAdapter(List<FatLocation> locationList, OnItemClickListener listener) {
        this.locationList = locationList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_item, parent, false);
        return new LocationViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        FatLocation location = locationList.get(position);
        holder.nameTextView.setText(location.getName());
        holder.locationTextView.setText("Lat: " + location.getLocation().latitude + ", Lng: " + location.getLocation().longitude);
        holder.stateTextView.setText(location.getState().toString());
        holder.itemView.setTag(location);
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    static class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView locationTextView;
        TextView stateTextView;

        LocationViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            stateTextView = itemView.findViewById(R.id.stateTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FatLocation location = (FatLocation) v.getTag();
                    if (location != null) {
                        listener.onItemClick(location);
                    }
                }
            });
        }
    }
}
