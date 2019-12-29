package com.example.minibus.ui.map;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.minibus.MainNavigationActivity;
import com.example.minibus.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private MapView mMapView;
    private GoogleMap mMap;

    boolean first=true;

    private static final String TAG= MainNavigationActivity.class.getSimpleName();
    private HashMap<String, Marker> mMarkers = new HashMap<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);
       // final TextView textView = root.findViewById(R.id.text_dashboard);

        mMapView = (MapView) root.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);


        return root;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap=googleMap;

        subscribeToUpdates();
        mMap.setMaxZoomPreference(16);
        // For showing a move to my location button
        mMap.setMyLocationEnabled(false);

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);





    }

    private void subscribeToUpdates() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(getString(R.string.firebase_path));
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                String key = dataSnapshot.getKey();
                HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
                double lat = Double.parseDouble(value.get("latitude").toString());
                double lng = Double.parseDouble(value.get("longitude").toString());
                LatLng location = new LatLng(lat, lng);
                mMarkers.put(key, mMap.addMarker(new MarkerOptions().title(key).position(location)));
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (Marker marker : mMarkers.values()) {
                    builder.include(marker.getPosition());
                }
                if(key.equalsIgnoreCase("26m0008")){
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 300));
                }else if(first){
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 300));
                    first=false;
                }
               // mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 300));
               // setMarker(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                String key = dataSnapshot.getKey();
                HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
                double lat = Double.parseDouble(value.get("latitude").toString());
                double lng = Double.parseDouble(value.get("longitude").toString());
                LatLng location = new LatLng(lat, lng);
                mMarkers.get(key).setPosition(location);
                // mMarkers.put(key, mMap.addMarker(new MarkerOptions().title(key).position(location)));
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (Marker marker : mMarkers.values()) {
                    builder.include(marker.getPosition());
                }

               // mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 300));
                //setMarker(dataSnapshot);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                HashMap<String, Object> value = (HashMap<String, Object>) dataSnapshot.getValue();
                double lat = Double.parseDouble(value.get("latitude").toString());
                double lng = Double.parseDouble(value.get("longitude").toString());
                LatLng location = new LatLng(lat, lng);
                mMarkers.get(key).remove();
                //mMarkers.put(key, mMap.addMarker(new MarkerOptions().title(key).position(location)));
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (Marker marker : mMarkers.values()) {
                    builder.include(marker.getPosition());
                }
               // mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 300));
              // setMarker(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.d(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}