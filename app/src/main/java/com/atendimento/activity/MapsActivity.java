package com.atendimento.activity;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.atendimento.R;
import com.atendimento.config.ConfiguracaoFirebase;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
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
        // Add a marker in Sydney and move the camera
        mMap = googleMap;
        LatLng vot = new LatLng(-20.415768, -49.974491);
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(vot);
        circleOptions.radius(500);//em metros
        circleOptions.fillColor(Color.BLUE);
        circleOptions.fillColor(Color.argb(82, 119, 188, 1));
        circleOptions.strokeWidth(10);
        circleOptions.strokeColor(Color.BLACK);

        mMap.addCircle(circleOptions);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                Double latitude  = latLng.latitude;
                Double longitude = latLng.longitude;

                Toast.makeText(getApplicationContext(), latitude + " - " + longitude, Toast.LENGTH_SHORT ).show();

                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("onClick")
                        .snippet("Description")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_user)));
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Double latitude  = latLng.latitude;
                Double longitude = latLng.longitude;

                Toast.makeText(getApplicationContext(), latitude + " - " + longitude, Toast.LENGTH_SHORT ).show();

                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("onLongClick")
                        .snippet("Description")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_user)));
            }
        });

        mMap.addMarker(new MarkerOptions().position(vot).title("Marker")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.close)));

        //icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        //zoom de 2.0 ate 21.0
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(vot, 15));
    }
}
