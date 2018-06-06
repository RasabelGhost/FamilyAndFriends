package com.family.ghost.fam.materialcamera;

import android.app.Fragment;
import android.support.annotation.NonNull;

import com.family.ghost.fam.materialcamera.internal.BaseCaptureActivity;
import com.family.ghost.fam.materialcamera.internal.CameraFragment;

public class CaptureActivity extends BaseCaptureActivity {

  @Override
  @NonNull
  public Fragment getFragment() {
    return CameraFragment.newInstance();
  }

  @Override
  public void onShowStillshot(String outputUri) {

  }
}
