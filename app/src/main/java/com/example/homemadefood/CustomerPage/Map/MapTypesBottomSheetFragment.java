package com.example.homemadefood.CustomerPage.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.homemadefood.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class MapTypesBottomSheetFragment extends BottomSheetDialogFragment {
    private GoogleMap googleMap;

    public void setGoogleMap(GoogleMap map) {
        this.googleMap = map;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map_types_bottom_sheet_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton normalButton = view.findViewById(R.id.normalMapButton);
        normalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (googleMap != null) {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    dismiss();
                }
            }
        });

        ImageButton satelliteButton = view.findViewById(R.id.satelliteMapButton);
        satelliteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (googleMap != null) {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    dismiss();
                }
            }
        });

        ImageButton hybridButton = view.findViewById(R.id.hybridMapButton);
        hybridButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (googleMap != null) {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    dismiss();
                }
            }
        });

        ImageButton terrainButton = view.findViewById(R.id.terrainMapButton);
        terrainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (googleMap != null) {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    dismiss();
                }
            }
        });


    }
}