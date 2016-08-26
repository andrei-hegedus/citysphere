package com.bndiapps.citysphere;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = MapsActivity.class.getSimpleName();
    public static final String ACTION_CLOSE_POLL_NOTIFICATION = "close-poll-notification";
    private GoogleMap mMap;
    private BottomSheetBehavior bottomSheetBehavior;
    private GoogleApiClient googleApiClient;
    private Location lastLocation;
    private FloatingActionButton actionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        configureBottomSheetBehavior();
        configureRateButtons();

        buildGoogleApiClient();

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

        goToCurrentLocation();
        checkForPolls();
    }


    private void checkForPolls() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("polls");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap polls = (HashMap) dataSnapshot.getValue();
                List<Poll> pollList = Poll.fromMap(polls);
                for(Poll poll : pollList) {
                    Log.d(TAG, poll.toString());
                }
                if(pollList.size()>0){
                    showPollNotification(pollList.get(0));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void goToCurrentLocation() {
        if (lastLocation == null) {
            return;
        }
        LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
    }

    private void configureBottomSheetBehavior() {
        actionButton = (FloatingActionButton) findViewById(R.id.rate_btn);
        actionButton.setEnabled(false);
        LinearLayout bottom = (LinearLayout) findViewById(R.id.bottom_layout);
        bottomSheetBehavior = BottomSheetBehavior.from(bottom);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCurrentLocation();
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }

    private void showPollNotification(Poll poll) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MapsActivity.this);
        builder.setSmallIcon(R.mipmap.ic_launcher).setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)).setContentTitle("New poll")
                .setContentText(poll.getTitle()).setPriority(NotificationCompat.PRIORITY_MAX)
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE);

        int notificationId = (int) Math.random();
        Intent iOK = new Intent(this, PollNotificationService.class);
        iOK.putExtra(PollNotificationService.NOTIFICATION_ID, notificationId);
        iOK.putExtra(PollNotificationService.POLL, poll);
        PendingIntent iOKPendingIntent = PendingIntent.getService(this, 1, iOK, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action okAction = new NotificationCompat.Action.Builder(R.drawable.ic_menu_forward, "OK", iOKPendingIntent).build();
        builder.addAction(okAction);

        Intent iCancel = new Intent(this, PollNotificationService.class);
        iOK.putExtra(PollNotificationService.NOTIFICATION_ID, notificationId);
        PendingIntent iCancelPendingIntent = PendingIntent.getService(this, 1, iOK, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action cancelAction = new NotificationCompat.Action.Builder(android.R.drawable.ic_menu_close_clear_cancel, "No Thanks", iCancelPendingIntent).build();
        builder.addAction(cancelAction);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(notificationId, builder.build());
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
}
