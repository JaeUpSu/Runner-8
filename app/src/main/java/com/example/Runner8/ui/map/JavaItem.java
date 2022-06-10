package com.example.Runner8.ui.map;


import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;

import org.jetbrains.annotations.NotNull;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import ted.gun0912.clustering.TedMap;
import ted.gun0912.clustering.clustering.TedClusterItem;
import ted.gun0912.clustering.geometry.TedCameraPosition;
import ted.gun0912.clustering.geometry.TedLatLng;
import ted.gun0912.clustering.geometry.TedLatLngBounds;
import ted.gun0912.clustering.naver.TedNaverMarker;

public class JavaItem implements TedClusterItem , TedMap<Marker, TedNaverMarker, OverlayImage> {

    private LatLng latLng;

    public JavaItem(LatLng latLng){
        this.latLng = latLng;
    }

    public LatLng getLatLng(){
        return latLng;
    }

    @NotNull
    @Override
    public TedLatLng getTedLatLng() {
        return new TedLatLng(latLng.latitude, latLng.longitude);
    }


    @Override
    public void addMarker(@NotNull TedNaverMarker tedNaverMarker) {

    }

    @Override
    public void addMarkerClickListener(@NotNull TedNaverMarker tedNaverMarker, @NotNull Function1<? super TedNaverMarker, Unit> function1) {

    }

    @Override
    public void addOnCameraIdleListener(@NotNull Function1<? super TedCameraPosition, Unit> function1) {

    }

    @NotNull
    @Override
    public TedCameraPosition getCameraPosition() {
        return null;
    }

    @NotNull
    @Override
    public TedNaverMarker getMarker() {
        return null;
    }

    @NotNull
    @Override
    public TedNaverMarker getMarker(Marker marker) {
        return null;
    }

    @NotNull
    @Override
    public TedLatLngBounds getVisibleLatLngBounds() {
        return null;
    }

    @Override
    public void moveToCenter(@NotNull TedLatLng tedLatLng) {

    }

    @Override
    public void removeMarker(@NotNull TedNaverMarker tedNaverMarker) {

    }
}
