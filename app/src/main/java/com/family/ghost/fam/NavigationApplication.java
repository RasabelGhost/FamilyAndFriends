package com.family.ghost.fam;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;


import android.graphics.BitmapFactory;

import com.mapbox.mapboxsdk.Mapbox;
import com.squareup.leakcanary.LeakCanary;

import timber.log.Timber;

public class NavigationApplication extends Application {

  private static final String LOG_TAG = NavigationApplication.class.getSimpleName();
  private static final String DEFAULT_MAPBOX_ACCESS_TOKEN = "pk.eyJ1IjoicmFzYWJlbCIsImEiOiJjamh2OHl0aTYwdHpoM3ZxaXV6eXd1OW5zIn0.XrvUsHArOxa7kpRNCCR9sQ";



//  @Override
//  protected void attachBaseContext(Context context) {
//    super.attachBaseContext(context);
//    MultiDex.install(this);
//  }
  @Override
  public void onCreate() {
    super.onCreate();

    // Leak canary
    if (LeakCanary.isInAnalyzerProcess(this)) {
      return;
    }
    LeakCanary.install(this);

    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    }

    // Set access token
    String mapboxAccessToken = Utils.getMapboxAccessToken(getApplicationContext());
    if (TextUtils.isEmpty(mapboxAccessToken) || mapboxAccessToken.equals(DEFAULT_MAPBOX_ACCESS_TOKEN)) {
      Log.w(LOG_TAG, "Warning: access token isn't set.");
    }

    Mapbox.getInstance(getApplicationContext(), mapboxAccessToken);
  }


}
