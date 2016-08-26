package com.bndiapps.citysphere;

import android.content.Context;
import android.support.annotation.DrawableRes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

/**
 * Class used to manage the shop/cluster UI.
 *
 * @author alexandra.iacob
 */
public class RatingRenderer extends DefaultClusterRenderer<Rating> {

  public RatingRenderer(Context context, GoogleMap map, ClusterManager<Rating> clusterManager) {
    super(context, map, clusterManager);
  }

  @Override
  protected void onBeforeClusterItemRendered(Rating item, MarkerOptions markerOptions) {
    int drawable = getDrawableForRating(item.getRating());
    markerOptions.icon(BitmapDescriptorFactory.fromResource(drawable));
  }

  @Override
  protected void onBeforeClusterRendered(Cluster<Rating> cluster, MarkerOptions markerOptions) {
    int drawable = getDrawableForRating(getRatingForCluster(cluster));
    markerOptions.icon(BitmapDescriptorFactory.fromResource(drawable));
  }

  private int getRatingForCluster(Cluster<Rating> cluster) {
    int sum = 0;
    for (Rating rating : cluster.getItems()) {
      sum += rating.getRating();
    }
    return Math.round(sum / cluster.getSize());
  }

  private @DrawableRes int getDrawableForRating(int rating) {
    switch (rating) {
      case 1:
        return R.drawable.storm;
      case 2:
        return R.drawable.rain;
      case 3:
        return R.drawable.cloud;
      case 4:
        return R.drawable.sun_n_cloud;
      default:
        return R.drawable.sun;
    }
  }
}
