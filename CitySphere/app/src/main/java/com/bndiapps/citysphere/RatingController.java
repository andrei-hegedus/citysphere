package com.bndiapps.citysphere;

import android.content.Context;

import com.bndiapps.citysphere.repo.RatingsRepo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexandra.iacob on 26/08/16.
 */
public class RatingController {

  private final RatingsRepo ratingsRepo;

  public RatingController(Context context) {
    ratingsRepo = new RatingsRepo(context);
  }

  public List<Rating> getRatings() {
    return ratingsRepo.getAll();
  }

  public void addRating(Rating rating) {
    ratingsRepo.add(rating);
  }

  public void addDummyRatings() {
    addRating(new Rating(46.450362, 23.566704, 5));
    addRating(new Rating(46.442201, 23.575630, 2));
    addRating(new Rating(46.442555, 23.538551, 1));
    addRating(new Rating(46.438888, 23.546448, 4));
    addRating(new Rating(46.428359, 23.574600, 2));
    addRating(new Rating(46.434275, 23.548336, 3));
    addRating(new Rating(46.429424, 23.571510, 4));
    addRating(new Rating(46.409543, 23.591766, 3));
    addRating(new Rating(46.437554, 23.595200, 5));
    addRating(new Rating(46.420753, 23.544388, 4));
  }
}
