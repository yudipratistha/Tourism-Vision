package com.example.destinationrecognizer.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.destinationrecognizer.R;
import com.example.destinationrecognizer.VisionActivity;
import com.example.destinationrecognizer.model.LandmarkModel;
import com.example.destinationrecognizer.view.CustomMapView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class LandmarkFragment extends Fragment {
    private List<LandmarkModel> landmarks;
    private View rootView;
    CustomMapView mMapView;
    private GoogleMap googleMap;

    public LandmarkFragment() {
    }

    public static LandmarkFragment newInstance() {
        LandmarkFragment fragment = new LandmarkFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_landmark, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        landmarks = ((VisionActivity) getActivity()).getLandmarks();
        if(landmarks.size()==0)createLandmarkNullView();
        else {
            for (LandmarkModel landmark : this.landmarks) {
                createLandmarkView(landmark, savedInstanceState);
            }
        }
    }

    public void createLandmarkNullView(){
        LinearLayout rootLayout = (LinearLayout) rootView.findViewById(R.id.landmarkLayout);
        LayoutInflater inflater = LayoutInflater.from(getActivity().getApplicationContext());
        LinearLayout newLayout = (LinearLayout) inflater.inflate(R.layout.landmark, null, false);
        rootLayout.addView(newLayout);
        TextView landmarkTextView = newLayout.findViewById(R.id.landmark);
        TextView scoreTextView = newLayout.findViewById(R.id.landmarkScore);
        TextView latLngText = newLayout.findViewById(R.id.latLng);
        mMapView = (CustomMapView) newLayout.findViewById(R.id.landmarkMap);
        scoreTextView.setVisibility(View.GONE);
        latLngText.setVisibility(View.GONE);
        mMapView.setVisibility(View.GONE);
        landmarkTextView.setText("Landmark Not Found");
    }

    public void createLandmarkView(final LandmarkModel landmark, @Nullable Bundle savedInstanceState) {
        LinearLayout rootLayout = (LinearLayout) rootView.findViewById(R.id.landmarkLayout);
        LayoutInflater inflater = LayoutInflater.from(getActivity().getApplicationContext());
        LinearLayout newLayout = (LinearLayout) inflater.inflate(R.layout.landmark, null, false);
        rootLayout.addView(newLayout);

        TextView landmarkTextView = newLayout.findViewById(R.id.landmark);
        TextView scoreTextView = newLayout.findViewById(R.id.landmarkScore);
        TextView latLngText = newLayout.findViewById(R.id.latLng);
        landmarkTextView.setText(landmark.getName());
        scoreTextView.setText(String.valueOf(landmark.getPercentage()) + "%");
        latLngText.setText("Location: " + landmark.getLat() + ", " + landmark.getLng());
        landmarkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUrl("https://www.google.com/search?q="+landmark.getName());
            }
        });

        mMapView = (CustomMapView) newLayout.findViewById(R.id.landmarkMap);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try { MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) { e.printStackTrace(); }
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                googleMap.setMyLocationEnabled(true);
                LatLng latlng = new LatLng(landmark.getLat(), landmark.getLng());
                googleMap.addMarker(new MarkerOptions().position(latlng));
                CameraPosition cameraPosition = new CameraPosition.Builder().target(latlng).zoom(12).build();
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });
    }

    public void goToUrl(String url){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(url));
        startActivity(browserIntent);
    }

    @Override
    public void onResume() {
        super.onResume();
        landmarks = ((VisionActivity) getActivity()).getLandmarks();
        if(landmarks.size()!=0) mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        landmarks = ((VisionActivity) getActivity()).getLandmarks();
        if(landmarks.size()!=0) mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        landmarks = ((VisionActivity) getActivity()).getLandmarks();
        if(landmarks.size()!=0) mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        landmarks = ((VisionActivity) getActivity()).getLandmarks();
        if(landmarks.size()!=0) mMapView.onLowMemory();
    }
}
