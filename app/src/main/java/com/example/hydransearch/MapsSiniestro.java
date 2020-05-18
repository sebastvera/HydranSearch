package com.example.hydransearch;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static java.lang.Math.toRadians;

public class MapsSiniestro<b, a> extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public DatabaseReference mDatabase;
    private ArrayList<Marker> tmpRealTimeMarkers = new ArrayList<>();
    private ArrayList<Marker> reaTimeMarkers = new ArrayList<>();
    GoogleMapOptions options = new GoogleMapOptions();
    MarkerOptions markerOptions2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_siniestro);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        ver_siniestro();




  }
    private void ver_siniestro() {
        mDatabase.child("siniestro").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(Marker marker:reaTimeMarkers) {
                    marker.remove();
                }
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Mapatraer mp = snapshot.getValue(Mapatraer.class);
                    double latitud = mp.getLatitud();
                    double longitud = mp.getLongitud();
                    LatLng s = new LatLng(latitud, longitud);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(s);
                   markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.fire));
                    tmpRealTimeMarkers.add(mMap.addMarker(markerOptions));


                }
                reaTimeMarkers.clear();
                reaTimeMarkers.addAll(tmpRealTimeMarkers);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabase.child("usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(Marker marker:reaTimeMarkers) {
                    marker.remove();
                }
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Mapatraer mp = snapshot.getValue(Mapatraer.class);
                    Double latitud = mp.getLatitud();
                    Double longitud = mp.getLongitud();
                    MarkerOptions markerOptions = new MarkerOptions();
                    LatLng w = new LatLng(latitud,longitud);
                    markerOptions2.position(w);
                    markerOptions2.icon(BitmapDescriptorFactory.fromResource(R.drawable.grifo));
                    tmpRealTimeMarkers.add(mMap.addMarker(markerOptions2));


                }
                reaTimeMarkers.clear();
                reaTimeMarkers.addAll(tmpRealTimeMarkers);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}