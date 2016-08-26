package com.bndiapps.citysphere;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alexandra.iacob on 26/08/16.
 */
public class RatingController {

  public List<Rating> getRatings() {
   List<Rating> ratings = new ArrayList<>();
    ratings.add(new Rating(46.796304, 25.442658, 5));
    ratings.add(new Rating(46.794973, 25.436418, 2));
    ratings.add(new Rating(46.792990, 25.439358, 1));
    ratings.add(new Rating(46.793724, 25.448391, 4));
    ratings.add(new Rating(46.802920, 25.451417, 2));
    ratings.add(new Rating(46.812613, 25.460322, 3));
    ratings.add(new Rating(46.823273, 25.446246, 4));
    ratings.add(new Rating(46.828485, 25.463154, 3));
    ratings.add(new Rating(46.828705, 25.478497, 5));
    ratings.add(new Rating(46.835869, 25.491285, 4));

    return ratings;
  }

  public void addRating(Rating rating) {

  }
}
