package com.example.Runner8

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.widget.TextView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.MarkerIcons
import ted.gun0912.clustering.naver.TedNaverClustering

class Cluster(context : Context, naverMap : NaverMap, naverItem : Collection<NaverItem>,  marker : Marker){

    var naverMap : NaverMap
    var naverItem : Collection<NaverItem>
    var context : Context
    var marker : Marker
    init {
        this.naverMap = naverMap
        this.naverItem = naverItem
        this.context = context
        this.marker = marker
    }

    fun clustering(){
        Log.i("clustering", "clustering");
        TedNaverClustering.with<NaverItem>(context, naverMap)
                .customMarker { clusterItem ->
                    marker.position = clusterItem.position
                    marker.apply {
                        icon = MarkerIcons.RED
                        Log.i("RED", icon.id);
                    }
                }
                .customCluster {
                    TextView(context).apply {
                        setBackgroundColor(Color.GREEN)
                        setTextColor(Color.WHITE)
                        text = "${it.size}개"
                        setPadding(10, 10, 10, 10)
                    }
                }
                .items(naverItem)
                .make()

        Log.i("marker", marker.icon.id);
    }

}