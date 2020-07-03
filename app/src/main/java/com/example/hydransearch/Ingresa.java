package com.example.hydransearch;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Objects;

public class Ingresa extends AppCompatActivity {
        private FusedLocationProviderClient mfused;
        Button mBottonSubir;
        TextView latid;
        TextView longi;
        DatabaseReference  mRootReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresa);
        mfused = LocationServices.getFusedLocationProviderClient(this);
        mBottonSubir = findViewById(R.id.mBottonSubir);
        latid =findViewById(R.id.lat);
        longi = findViewById(R.id.Longitud);
        mRootReference = FirebaseDatabase.getInstance().getReference();

        mfused.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            latid.setText(""+location.getLatitude());
                            longi.setText(""+location.getLongitude());

                        }
                    }
                });





    }

}
