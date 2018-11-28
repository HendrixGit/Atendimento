package com.atendimento.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.atendimento.R;
import com.atendimento.adapter.AdapterEnderecos;
import com.atendimento.bases.BaseActivity;
import com.atendimento.model.Endereco;
import com.atendimento.util.RecyclerItemClickListener;
import com.atendimento.util.SimpleDividerItemDecoration;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapaActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private EditText destino;
    private ImageView imageViewPesquisarEndereco;
    private TextView minhaLocalizacao;
    private RecyclerView recyclerViewEnderecos;
    private List<Endereco> enderecosLista;
    private AdapterEnderecos adapterEnderecos;
    private LatLng localizacaoAtual;
    private ProgressBar progressBarMapa;
    private Button ok;
    private Endereco enderecoParametro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        toolbarBase = findViewById(R.id.toolbar);
        toolbarBase.setTitle("Definir Local Empresa");
        toolbarBase.setTitleTextColor(getResources().getColor(R.color.colorBranco));

        progressBarMapa = findViewById(R.id.progressBarMapa);
        ok = findViewById(R.id.buttonOKMapa);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            minhaLocalizacao.setBackground(getResources().getDrawable(R.drawable.selector));
        }
        recyclerViewEnderecos = findViewById(R.id.recyclerViewEnderecos);
        RecyclerView.LayoutManager layoutManager   = new LinearLayoutManager(getApplicationContext());
        recyclerViewEnderecos.setLayoutManager(layoutManager);
        recyclerViewEnderecos.setHasFixedSize(true);
        recyclerViewEnderecos.setVisibility(View.GONE);
        recyclerViewEnderecos.addItemDecoration(new SimpleDividerItemDecoration(this));
        recyclerViewEnderecos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerViewEnderecos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                selecionarEnderecoEmpresa(enderecosLista.get(position));
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
                        }
                )
        );

        imageViewPesquisarEndereco = findViewById(R.id.imageViewPesqEndereco);
        imageViewPesquisarEndereco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carregarEnderecos(destino.getText().toString());
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
                if(charSequence.toString().equals("")){
                    recyclerViewEnderecos.setVisibility(View.GONE);
                }
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
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Address address = buscarEnderecoLatLong(latLng);
                adcionarMarcador("Local: " + address.getAddressLine(0), latLng.latitude, latLng.longitude);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);
    }

    private Address buscarEnderecoLatLong(LatLng localizacao){
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> listaEnderecos = null;
        try {
            listaEnderecos = geocoder.getFromLocation(localizacao.latitude, localizacao.longitude, 1);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return listaEnderecos.get(0);
    }

    private Address carregarEnderecos(String endereco){
        try {
            String textoPesquisa = endereco.toLowerCase();
            textoPesquisa = Normalizer.normalize(textoPesquisa, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> listaEnderecos = geocoder.getFromLocationName(textoPesquisa, Integer.MAX_VALUE, localizacaoAtual.latitude, localizacaoAtual.longitude,localizacaoAtual.latitude, localizacaoAtual.longitude);
            enderecosLista   = new ArrayList<>();
            adapterEnderecos = new AdapterEnderecos(getApplication(), enderecosLista);
            if (listaEnderecos != null && listaEnderecos.size() > 0){
                for (Address address : listaEnderecos){
                    Endereco enderecoEmpresa = carregaObjetoEndereco(address);
                    enderecosLista.add(enderecoEmpresa);
                    recyclerViewEnderecos.setVisibility(View.VISIBLE);
                    recyclerViewEnderecos.setAdapter(adapterEnderecos);
                    adapterEnderecos.notifyDataSetChanged();
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Endereco carregaObjetoEndereco(Address address){
        Endereco enderecoEmpresa = new Endereco();
        enderecoEmpresa.setTextoPesquisado(address.getFeatureName());
        enderecoEmpresa.setEndereco(address.getAddressLine(0));
        enderecoEmpresa.setCidade(address.getSubAdminArea());
        enderecoEmpresa.setLatitude(address.getLatitude());
        enderecoEmpresa.setLongitude(address.getLongitude());
        enderecoEmpresa.setPais(address.getCountryName());
        Log.i("local", address.toString());
        return enderecoEmpresa;
    }

    private void selecionarEnderecoEmpresa(Endereco endereco){
        if (!destino.getText().equals("") || destino.getText() != null){
            if (endereco != null){
                Double latitude  = endereco.getLatitude();
                Double longitude = endereco.getLongitude();
                adcionarMarcador(endereco.getEndereco(), latitude, longitude);
                recyclerViewEnderecos.setVisibility(View.GONE);
            }
        }
    }

    private void recuperaLocalizacao() {
        progressBarMapa.setVisibility(View.VISIBLE);
        recyclerViewEnderecos.setVisibility(View.GONE);
        destino.setText("");
        escondeTeclado();
        locationManager = (LocationManager) this.getSystemService(getApplicationContext().LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Double latitude  = location.getLatitude();
                Double longitude = location.getLongitude();
                localizacaoAtual = new LatLng(latitude, longitude);
                Address address =  buscarEnderecoLatLong(localizacaoAtual);
                adcionarMarcador("Minha Localização: " + address.getAddressLine(0), latitude, longitude);
                progressBarMapa.setVisibility(View.GONE);
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

    private void escondeTeclado() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
