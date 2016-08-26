package com.bndiapps.citysphere;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener, ClusterManager.OnClusterClickListener<Rating>,
    ClusterManager.OnClusterItemClickListener<Rating> {

  private static final double DEFAULT_LAT = 46.422378;
  private static final double DEFAULT_LONG = 23.561202;
  private static final float DEFAULT_ZOOM = 8;

  private GoogleMap mMap;
  private BottomSheetBehavior bottomSheetBehavior;
  private GoogleApiClient googleApiClient;
  private Location lastLocation;
  private FloatingActionButton actionButton;
  private ClusterManager<Rating> clusterManager;
  private RatingController ratingController;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maps);

    configureBottomSheetBehavior();
    configureRateButtons();

    buildGoogleApiClient();

    ratingController = new RatingController(this);
//    ratingController.addDummyRatings();

    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        .findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);
  }

  @Override
  protected void onStart() {
    super.onStart();
    googleApiClient.connect();
  }

  @Override
  protected void onPause() {
    googleApiClient.disconnect();
    super.onPause();
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

    mMap.getUiSettings().setMyLocationButtonEnabled(false);
    mMap.setMyLocationEnabled(true);
    goToPosition(new LatLng(DEFAULT_LAT, DEFAULT_LONG), DEFAULT_ZOOM);

    this.clusterManager = new ClusterManager<>(this, mMap);
    RatingRenderer renderer = new RatingRenderer(this, mMap, clusterManager);
    clusterManager.setRenderer(renderer);

    mMap.setOnCameraChangeListener(clusterManager);
    mMap.setOnMarkerClickListener(clusterManager);

    clusterManager.setOnClusterClickListener(this);
    clusterManager.setOnClusterItemClickListener(this);

    clusterManager.addItems(ratingController.getRatings());
    clusterManager.cluster();
  }

  private void goToCurrentLocation() {
    if (lastLocation == null) {
      return;
    }
    LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
    goToPosition(latLng, 17);
  }

  private void goToPosition(LatLng latLng, float zoom) {
    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
  }

  private void configureBottomSheetBehavior() {
    actionButton = (FloatingActionButton) findViewById(R.id.rate_btn);
    actionButton.setEnabled(false);
    final LinearLayout bottom = (LinearLayout) findViewById(R.id.bottom_layout);
    bottomSheetBehavior = BottomSheetBehavior.from(bottom);

    final TextView moreTextView = (TextView) findViewById(R.id.more_text);
    final View moreLayout = findViewById(R.id.more_layout);

    actionButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        goToCurrentLocation();

        ((TextView) findViewById(R.id.rate_text)).setTextColor(getResources().getColor(android.R.color.white));
        ((TextView) findViewById(R.id.dislike_text)).setTextColor(getResources().getColor(android.R.color.white));
        ((TextView) findViewById(R.id.umm_text)).setTextColor(getResources().getColor(android.R.color.white));
        ((TextView) findViewById(R.id.like_text)).setTextColor(getResources().getColor(android.R.color.white));
        ((TextView) findViewById(R.id.love_text)).setTextColor(getResources().getColor(android.R.color.white));

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
      }
    });

//    moreTextView.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        moreTextView.setVisibility(View.GONE);
//        moreLayout.setVisibility(View.VISIBLE);
//      }
//    });
  }

  private void configureRateButtons() {
    final Integer[] selected = new Integer[1];

    ImageButton hateBtn = (ImageButton) findViewById(R.id.hate_rating_img);
    ImageButton dislikeBtn = (ImageButton) findViewById(R.id.dislike_rating_img);
    final ImageButton ummBtn = (ImageButton) findViewById(R.id.umm_rating_img);
    ImageButton likeBtn = (ImageButton) findViewById(R.id.like_rating_img);
    ImageButton loveBtn = (ImageButton) findViewById(R.id.love_rating_img);

    final TextView hateText = (TextView) findViewById(R.id.hate_text);
    final TextView dislikeText = (TextView) findViewById(R.id.dislike_text);
    final TextView ummText = (TextView) findViewById(R.id.umm_text);
    final TextView likeText = (TextView) findViewById(R.id.like_text);
    final TextView loveText = (TextView) findViewById(R.id.love_text);

    final TextView rateText = (TextView) findViewById(R.id.rate_text);

    hateText.setTextColor(getResources().getColor(android.R.color.white));
    dislikeText.setTextColor(getResources().getColor(android.R.color.white));
    ummText.setTextColor(getResources().getColor(android.R.color.white));
    likeText.setTextColor(getResources().getColor(android.R.color.white));
    loveText.setTextColor(getResources().getColor(android.R.color.white));

    View.OnClickListener rateButtonListener = new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        int rate = Integer.parseInt((String) v.getTag());
        selected[0] = rate;

        hateText.setTextColor(getResources().getColor(android.R.color.white));
        dislikeText.setTextColor(getResources().getColor(android.R.color.white));
        ummText.setTextColor(getResources().getColor(android.R.color.white));
        likeText.setTextColor(getResources().getColor(android.R.color.white));
        loveText.setTextColor(getResources().getColor(android.R.color.white));

        TextView textView;
        switch (rate) {
          case 1:
            textView = hateText;
            break;
          case 2:
            textView = dislikeText;
            break;
          case 3:
            textView = ummText;
            break;
          case 4:
            textView = likeText;
            break;
          default:
            textView = loveText;
        }

        textView.setTextColor(getResources().getColor(R.color.colorAccent));
      }
    };

    hateBtn.setOnClickListener(rateButtonListener);
    dislikeBtn.setOnClickListener(rateButtonListener);
    ummBtn.setOnClickListener(rateButtonListener);
    likeBtn.setOnClickListener(rateButtonListener);
    loveBtn.setOnClickListener(rateButtonListener);

    rateText.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        saveRating(selected[0]);
      }
    });
  }

  private void saveRating(int rate) {
    Rating rating = new Rating(lastLocation.getLatitude(), lastLocation.getLongitude(), rate);
    ratingController.addRating(rating);
    clusterManager.addItem(rating);
    clusterManager.cluster();
    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
  }

  @Override
  public void onLocationChanged(Location location) {
    lastLocation = location;
    if (lastLocation != null) {
      actionButton.setEnabled(true);
    }
  }

  @Override
  public void onConnected(@Nullable Bundle bundle) {
    LocationRequest locationRequest = LocationRequest.create();
    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    locationRequest.setInterval(100); // Update location every second

    LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

    if (lastLocation != null) {
      actionButton.setEnabled(true);
    }
  }

  @Override
  public void onConnectionSuspended(int i) {

  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    buildGoogleApiClient();
  }

  synchronized void buildGoogleApiClient() {
    googleApiClient = new GoogleApiClient.Builder(this)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(LocationServices.API)
        .build();
  }

  @Override
  public boolean onClusterClick(Cluster<Rating> cluster) {
    float zoom = mMap.getCameraPosition().zoom;
    zoom++;

    LatLng latLng = cluster.getPosition();
    goToPosition(latLng, zoom);

    return true;
  }

  @Override
  public boolean onClusterItemClick(Rating rating) {
    return true;
  }

  @Override
  public void onBackPressed() {
    if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
      bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
    else {
      super.onBackPressed();
    }
  }
}
