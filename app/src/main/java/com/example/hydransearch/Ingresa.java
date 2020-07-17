package com.example.hydransearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Ingresa extends AppCompatActivity {
        private FusedLocationProviderClient mfused;
        Button mBottonSubir, bt_buscar, modificar;
        EditText ID,caudal,estado, bastidor;
        DatabaseReference  mRootReference;
        TextView latid, longi;
        String minombre = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresa);
        mfused = LocationServices.getFusedLocationProviderClient(this);
        mBottonSubir = findViewById(R.id.button_modificar);
        ID =(EditText) findViewById(R.id.ID);
        latid = (TextView) findViewById(R.id.Latitud);
        longi = (TextView) findViewById(R.id.Longitud);
        bt_buscar = (Button) findViewById(R.id.bt_buscar);
        modificar = (Button) findViewById(R.id.button_modificar);
        caudal = (EditText) findViewById(R.id.Caudal);
        estado = (EditText) findViewById(R.id.Estado);
        bastidor = (EditText) findViewById(R.id.Bastidor);
        mRootReference = FirebaseDatabase.getInstance().getReference();

        buscar();

        modificacion();


    }

       private void modificacion() {
        modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ver_estados();
                minombre = ID.getText().toString();
                String estado1 = estado.getText().toString();
                String bastidor1 = bastidor.getText().toString();
                int caudal1 = Integer.parseInt(caudal.getText().toString());
                Map<String, Object> actualiza =new HashMap<>();

                actualiza.put("Caudal",caudal1);
                actualiza.put("Estado",estado1);
                actualiza.put("Bastidor",bastidor1);



                mRootReference.child("Grifos").child(minombre).updateChildren(actualiza);

            }
        });
    }


    private void ver_estados() {
        String caudal1 = caudal.getText().toString();
        int value=0;
        if (!"".equals(caudal1)){
            value = Integer.parseInt(caudal1);
        }
        String bastidor_aux = bastidor.getText().toString();


        if ((bastidor_aux.equals("bueno") || (bastidor_aux.equals("Bueno"))) && (value >= 7)) {
            String estado2 = "Bueno";
            estado.setText(estado2);
        }
        else if (bastidor_aux.equals("malo") || (value < 7)||bastidor_aux.equals("Malo")){
            String escudo_mal = "Malo";
            estado.setText(escudo_mal);

        }

    }

    private void buscar() {
        bt_buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                minombre = ID.getText().toString();


                mRootReference.child("Grifos").child(minombre).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String caudal1 = dataSnapshot.child("Caudal").getValue().toString();
                            caudal.setText(caudal1);
                            String esta1 = dataSnapshot.child("Estado").getValue().toString();
                            estado.setText(esta1);
                            String bastion1 = dataSnapshot.child("Bastidor").getValue().toString();
                            bastidor.setText(bastion1);
                            String lat = dataSnapshot.child("Latitud").getValue().toString();
                            latid.setText(lat);
                            String longit = dataSnapshot.child("Longitud").getValue().toString();
                            longi.setText(longit);

                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });
    }

    private void Posicion() {
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
