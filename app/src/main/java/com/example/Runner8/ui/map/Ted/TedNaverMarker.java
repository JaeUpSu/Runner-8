package com.example.Runner8.ui.map.Ted;

import android.graphics.Bitmap;

import com.naver.maps.map.overlay.OverlayImage;

import org.jetbrains.annotations.NotNull;

import ted.gun0912.clustering.TedMarker;
import ted.gun0912.clustering.geometry.TedLatLng;

public class TedNaverMarker implements TedMarker<OverlayImage> {


    @NotNull
    @Override
    public TedLatLng getPosition() {
        return null;
    }

    @Override
    public void setPosition(@NotNull TedLatLng tedLatLng) {
        TedMarker tedMarker = new TedNaverMarker();
    }

    @Override
    public OverlayImage fromBitmap(@NotNull Bitmap bitmap) {
        return null;
    }

    @Override
    public void setImageDescriptor(OverlayImage overlayImage) {

    }

    @Override
    public void setVisible(boolean b) {

    }
}
