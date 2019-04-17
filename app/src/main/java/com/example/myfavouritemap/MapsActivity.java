package com.example.myfavouritemap;

import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private ArrayList<MapDataDetails> MapData;
    private GoogleMap mMap;
    private Geocoder geocoder;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        MapData = new ArrayList<MapDataDetails>();
        MapData.add(new MapDataDetails(22.718435, 75.855217, "High","pothole1"));
        MapData.add(new MapDataDetails(22.7001, 75.8471, "Medium","pothole2"));
        MapData.add(new MapDataDetails(22.7164, 75.8490, "Low","pothole3"));
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
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMinZoomPreference(11);
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        // Add a marker in Indore and move the camera
        LatLng indore = new LatLng(22.7196, 75.8577);
        for (int i = 0; i < MapData.size(); i++) {
            MapDataDetails obj = MapData.get(i);
            LatLng location = new LatLng(obj.getLat(), obj.getLang());
            Marker m=mMap.addMarker(new MarkerOptions().position(location).
                    title("Severity : " + obj.getSeverity()).snippet(ShowLocationInfo(obj.getLat(),obj.getLang())));

            InfoWindowAdapter customInfoWindow = new InfoWindowAdapter(this);
            mMap.setInfoWindowAdapter(customInfoWindow);

            m.setTag(obj);
            m.showInfoWindow();

        }
        //mMap.addMarker(new MarkerOptions().position(indore).title("Marker in Indore"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(indore));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(indore, 12.0f));
    }

    private String ShowLocationInfo(double Lat, double Lang)
    {
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> addresses;
        String fulladdress="";
        try {

            addresses = geocoder.getFromLocation(Lat, Lang, 1);

            String address = addresses.get(0).getAddressLine(0);
            //String area = addresses.get(0).getLocality();
            //String city = addresses.get(0).getAdminArea();
            //String country = addresses.get(0).getCountryName();
            //String postalcode = addresses.get(0).getPostalCode();

            fulladdress = address; //+ ", " + area + ", " + city + ", " + country + ", " + postalcode;

        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return fulladdress;

    }

}
