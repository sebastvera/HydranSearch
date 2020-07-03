package com.example.hydransearch;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.icu.text.AlphabeticIndex;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.BreakIterator;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;


    private CheckBox checksiniestro, checkgrifos, checkestados;
    private Button btnFiltrar, btnGenerarRuta;
    public DatabaseReference mDatabase;
    private ArrayList<Marker> tmpRealTimeMarkers = new ArrayList<>();
    private ArrayList<Marker> reaTimeMarkers = new ArrayList<>();
    private ArrayList<Marker> tmpRealTimeMarkers2 = new ArrayList<>();
    private ArrayList<Marker> reaTimeMarkers2 = new ArrayList<>();

    GoogleMapOptions options = new GoogleMapOptions();
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

        checksiniestro = findViewById(R.id.opcionSiniestro);
        checkgrifos = findViewById(R.id.opcionGrifo);
        checkestados = findViewById(R.id.opcionEstado);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

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

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMyLocationEnabled(true);
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        posicion();



        checkgrifos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    mDatabase.child("usuarios").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (Marker marker : reaTimeMarkers) {
                                marker.remove();
                            }
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Mapatraer mp = snapshot.getValue(Mapatraer.class);
                                double latitud = mp.getLatitud();
                                double longitud = mp.getLongitud();
                                String estado =mp.getEstado();
                                int id =mp.getID();
                                int caudal =mp.getCaudal();
                                String numCadena= String.valueOf(id);
                                String caudalst= String.valueOf(caudal);
                                String bastidor =mp.getBastidor();
                                LatLng s = new LatLng(latitud, longitud);
                                MarkerOptions markerOptions = new MarkerOptions();
                                if (estado.equals("bueno")){
                                    markerOptions.title("ID "+numCadena + " Caudal: " + caudalst + " Bastidor: "+ bastidor);
                                    markerOptions.position(s);
                                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.grifo));}

                                else {
                                    markerOptions.title(estado);
                                    markerOptions.position(s);
                                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.grifo_malo));

                                }


                                tmpRealTimeMarkers.add(mMap.addMarker(markerOptions));


                            }
                            reaTimeMarkers.clear();
                            reaTimeMarkers.addAll(tmpRealTimeMarkers);
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    new CountDownTimer(30000, 1000) {
                        BreakIterator mTextField;

                        public void onTick(long millisUntilFinished) {

                            mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                            posicion();
                        }

                        public void onFinish() {
                            mTextField.setText("done!");
                            posicion();
                            start();
                        }
                    };
                }
                else
                {
                    reaTimeMarkers.clear();
                    tmpRealTimeMarkers.clear();
                    mMap.clear();
                }
            }
        });

        checksiniestro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    ver_siniestro();
                }
                else
                {
                    mMap.clear();
                }
            }
        });
        checkestados.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    mDatabase.child("usuarios").removeValue();
                }
                else
                {
                    mMap.clear();
                }
            }
        });


    }

    private void posicion() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            Log.e("latitud: ", +location.getLatitude() + "longitud: " + location.getLongitude());
                            double currentLatitude = location.getLatitude();
                            double currentLongitude = location.getLongitude();
                            LatLng latLng = new LatLng(currentLatitude, currentLongitude);
                            float zoomLevel = 18.0f;
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));

                        }
                    }
                });
    }

    private void ver_siniestro() {
        mDatabase.child("siniestro").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (Marker marker : reaTimeMarkers2) {
                    marker.remove();
                }
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Mapatraer mp = snapshot.getValue(Mapatraer.class);
                    double latitud = mp.getLatitud();
                    double longitud = mp.getLongitud();
                    String estado =mp.getEstado();
                    LatLng s = new LatLng(latitud, longitud);
                    MarkerOptions markerOptions = new MarkerOptions();
                    if (estado.equals("bueno")){
                    markerOptions.title(estado);
                    markerOptions.position(s);
                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.firemen));}

                    else {
                        markerOptions.title(estado);
                        markerOptions.position(s);
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.fire));

                    }


                    tmpRealTimeMarkers2.add(mMap.addMarker(markerOptions));


                }
                reaTimeMarkers2.clear();
                reaTimeMarkers2.addAll(tmpRealTimeMarkers2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}

