package com.cdkj.link_community.adapters;

import android.content.Context;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;

/**
 * Created by cdkj on 2018/4/29.
 */

public class MapAdapter implements AMap.InfoWindowAdapter {

    private Context mContext;

    public MapAdapter(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }


}
