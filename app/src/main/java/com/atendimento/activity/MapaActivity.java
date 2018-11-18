package com.atendimento.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.atendimento.R;
import com.atendimento.bases.BaseActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.Normalizer;
import java.util.List;
import java.util.Locale;

public class MapaActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private EditText destino;
    private ImageView imageViewPesquisarEndereco;
    private TextView minhaLocalizacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        toolbarBase = findViewById(R.id.toolbar);
        toolbarBase.setTitle("Definir Local Empresa");
        toolbarBase.setTitleTextColor(getResources().getColor(R.color.colorBranco));
        destino = findViewById(R.id.editTextPesqEndereco);
        destino.setCursorVisible(false);
        destino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                destino.setCursorVisible(true);
            }
        });
        minhaLocalizacao = findViewById(R.id.textViewPesquisaEndereco);
        minhaLocalizacao.setText(" " + getResources().getString(R.string.localizacao));
        minhaLocalizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recuperaLocalizacao();
            }
        });

        imageViewPesquisarEndereco = findViewById(R.id.imageViewPesqEndereco);
        imageViewPesquisarEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buscarEndereco(destino.getText().toString());
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);

        destino.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String textoPesquisa = charSequence.toString().toLowerCase();
                textoPesquisa = Normalizer.normalize(textoPesquisa, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
                Log.d("key", textoPesquisa);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        recuperaLocalizacao();
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);
    }

    private void buscarEndereco(String endereco){
        if (!destino.getText().equals("") || destino.getText() != null){
            Address enderecoFinal = recuperarEndereco(endereco);
            if (enderecoFinal != null){
                Double latitude  = enderecoFinal.getLatitude();
                Double longitude = enderecoFinal.getLongitude();
                adcionarMarcador("Destino", latitude, longitude);
            }
        }
    }

    private Address recuperarEndereco(String endereco){
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> listaEnderecos = geocoder.getFromLocationName(endereco, 1);
            if (listaEnderecos != null && listaEnderecos.size() > 0){
                Address address = listaEnderecos.get(0);
                return address;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void recuperaLocalizacao() {
        locationManager = (LocationManager) this.getSystemService(getApplicationContext().LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Double latitude  = location.getLatitude();
                Double longitude = location.getLongitude();

                adcionarMarcador("Minha Localização", latitude, longitude);
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    1000,
                    10,
                    locationListener
            );
        }
    }
    private void adcionarMarcador(String descLocalizacao, Double parLatitude, Double parLongitude){
        LatLng localizacao = new LatLng(parLatitude, parLongitude);

        mMap.clear();
        mMap.addMarker(
                new MarkerOptions()
                        .position(localizacao)
                        .title(descLocalizacao)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_user))
        );

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(localizacao, 18));
    }
}
