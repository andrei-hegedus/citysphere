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
}
