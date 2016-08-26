package com.bndiapps.citysphere;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by andrei on 8/26/16.
 */
public class RateActivity extends AppCompatActivity{

  private BottomSheetBehavior bottomSheetBehavior;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.rate_screen);
    configureBottomSheetBehavior();
    configureRateButtons();
  }

  private void configureBottomSheetBehavior() {
    FloatingActionButton btn = (FloatingActionButton)findViewById(R.id.rate_btn);
    LinearLayout bottom = (LinearLayout)findViewById(R.id.bottom_layout);
    bottomSheetBehavior = BottomSheetBehavior.from(bottom);
    btn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
      }
    });
  }

  private void configureRateButtons() {
    View.OnClickListener rateButtonListener = new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(RateActivity.this, "Rating: "+v.getTag().toString(), Toast.LENGTH_SHORT).show();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
      }
    };

    ImageButton hateBtn = (ImageButton)findViewById(R.id.hate_rating_img);
    ImageButton dislikeBtn = (ImageButton)findViewById(R.id.dislike_rating_img);
    ImageButton ummBtn = (ImageButton)findViewById(R.id.umm_rating_img);
    ImageButton likeBtn = (ImageButton)findViewById(R.id.like_rating_img);
    ImageButton loveBtn = (ImageButton)findViewById(R.id.love_rating_img);
    hateBtn.setOnClickListener(rateButtonListener);
    dislikeBtn.setOnClickListener(rateButtonListener);
    ummBtn.setOnClickListener(rateButtonListener);
    likeBtn.setOnClickListener(rateButtonListener);
    loveBtn.setOnClickListener(rateButtonListener);
  }
}
