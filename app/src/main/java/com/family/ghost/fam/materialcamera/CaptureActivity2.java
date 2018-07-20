package com.family.ghost.fam.materialcamera;

import android.app.Fragment;
import android.support.annotation.NonNull;

import com.family.ghost.fam.materialcamera.internal.BaseCaptureActivity;
import com.family.ghost.fam.materialcamera.internal.Camera2Fragment;


public class CaptureActivity2 extends BaseCaptureActivity {

  @Override
  @NonNull
  public Fragment getFragment() {
    return Camera2Fragment.newInstance();
  }

  @Override
  public void onShowStillshot(String outputUri) {

  }
}
