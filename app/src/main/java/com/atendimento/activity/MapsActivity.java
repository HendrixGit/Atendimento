package com.atendimento.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
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
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private LatLng latLngUsuario;

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
        mMap = googleMap;;
        LatLng cecap1 = new LatLng(-20.407734, -49.992736);
        LatLng cecap2 = new LatLng(-20.406975, -49.987906);
        LatLng cecap3 = new LatLng(-20.411033, -49.987007);
        final LatLng vot = cecap1;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

//        CircleOptions circleOptions = new CircleOptions();
//        circleOptions.center(vot);
//        circleOptions.radius(500);//em metros
//        circleOptions.fillColor(Color.BLUE);
//        circleOptions.fillColor(Color.argb(82, 119, 188, 1));
//        circleOptions.strokeWidth(10);
//        circleOptions.strokeColor(Color.BLACK);
//        mMap.addCircle(circleOptions);

        final PolygonOptions polygonOptions = new PolygonOptions();
        polygonOptions.add(cecap1);
        polygonOptions.add(cecap2);
        polygonOptions.add(cecap3);

        mMap.addPolygon(polygonOptions);
        mMap.setTrafficEnabled(true);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
               latLngUsuario = new LatLng(location.getLongitude(), location.getLatitude());
               Log.d("location", String.valueOf(latLngUsuario.latitude));
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ){
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0,
                    0,//se mover 10 metros atualiza
                    locationListener
            );
        }



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

                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.add(vot, latLng);
                polygonOptions.fillColor(Color.BLUE);
                polygonOptions.strokeWidth(20);
                mMap.addPolyline(polylineOptions);
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

        mMap.addMarker(new MarkerOptions().position(vot).title("Default Position")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.close)));

        //icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        //zoom de 2.0 ate 21.0
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(vot, 15));

    }
}
