package com.bndiapps.citysphere;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

  private static final int MY_PERMISSIONS_REQUEST = 1000;
  private GoogleMap mMap;
  private BottomSheetBehavior bottomSheetBehavior;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maps);

    configureBottomSheetBehavior();
    configureRateButtons();

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

    // Add a marker in Sydney and move the camera
    LatLng sydney = new LatLng(-34, 151);
    mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
    mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    mMap.getUiSettings().setCompassEnabled(false);
    mMap.getUiSettings().setMapToolbarEnabled(true);
    mMap.getUiSettings().setMyLocationButtonEnabled(true);

    goToCurrentLocation();
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    switch (requestCode) {
      case MY_PERMISSIONS_REQUEST: {
        if (grantResults.length > 0
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

          goToCurrentLocation();

        }
      }
    }
  }

  private void goToCurrentLocation() {
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this,
          new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
          MY_PERMISSIONS_REQUEST);
      return;
    }
    mMap.setMyLocationEnabled(true);
  }

  private void configureBottomSheetBehavior() {
    FloatingActionButton btn = (FloatingActionButton) findViewById(R.id.rate_btn);
    LinearLayout bottom = (LinearLayout) findViewById(R.id.bottom_layout);
    bottomSheetBehavior = BottomSheetBehavior.from(bottom);
    btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(MapsActivity.this, "Hello", Toast.LENGTH_SHORT).show();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
      }
    });
  }

  private void configureRateButtons() {
    View.OnClickListener rateButtonListener = new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(MapsActivity.this, "Rating: " + v.getTag().toString(), Toast.LENGTH_SHORT).show();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
      }
    };

    ImageButton hateBtn = (ImageButton) findViewById(R.id.hate_rating_img);
    ImageButton dislikeBtn = (ImageButton) findViewById(R.id.dislike_rating_img);
    ImageButton ummBtn = (ImageButton) findViewById(R.id.umm_rating_img);
    ImageButton likeBtn = (ImageButton) findViewById(R.id.like_rating_img);
    ImageButton loveBtn = (ImageButton) findViewById(R.id.love_rating_img);
    hateBtn.setOnClickListener(rateButtonListener);
    dislikeBtn.setOnClickListener(rateButtonListener);
    ummBtn.setOnClickListener(rateButtonListener);
    likeBtn.setOnClickListener(rateButtonListener);
    loveBtn.setOnClickListener(rateButtonListener);
  }
}
