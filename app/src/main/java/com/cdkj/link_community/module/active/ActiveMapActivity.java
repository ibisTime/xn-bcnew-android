package com.cdkj.link_community.module.active;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.cdkj.baselibrary.base.AbsBaseLoadActivity;
import com.cdkj.link_community.R;
import com.cdkj.link_community.databinding.ActivityActiveMapBinding;
import com.cdkj.link_community.databinding.LayoutMarkerInfoWindowBinding;
import com.cdkj.link_community.utils.AMapUtil;

/**
 * Created by cdkj on 2018/4/29.
 */

public class ActiveMapActivity extends AbsBaseLoadActivity implements AMap.InfoWindowAdapter, GeocodeSearch.OnGeocodeSearchListener {

    private AMap aMap;

    private View infoWindow;
    private GeocodeSearch geocodeSearch;

    private Double latitude;
    private Double longitude;

    private ActivityActiveMapBinding mBinding;
    private LayoutMarkerInfoWindowBinding mMarkerInfoBinding;

    /**
     * @param context
     * @param latitude
     * @param longitude
     */
    public static void open(Context context, String latitude, String longitude) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, ActiveMapActivity.class);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        context.startActivity(intent);
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_active_map, null, false);

        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        //定义了一个地图view
        mBinding.map.onCreate(savedInstanceState);// 此方法须覆写，虚拟机需要在很多情况下保存地图绘制的当前状态。

        if (getIntent() != null){
            latitude = Double.parseDouble(getIntent().getStringExtra("latitude"));
            longitude = Double.parseDouble(getIntent().getStringExtra("longitude"));

            init();
            setMarker();
        }



    }

    private void init() {
        mBaseBinding.titleView.setMidTitle("导航");

        //初始化地图控制器对象
        if (aMap == null) {
            aMap = mBinding.map.getMap();
        }

        //地理搜索类
        geocodeSearch = new GeocodeSearch(this);
        geocodeSearch.setOnGeocodeSearchListener(this);

        mMarkerInfoBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.layout_marker_info_window, null, false);
    }

    private void setMarker() {

        LatLng latLng = new LatLng(latitude,longitude);

        Marker marker = aMap.addMarker(new MarkerOptions()
                .title(" ")
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(),R.drawable.active_map))));

        marker.showInfoWindow();

        aMap.setInfoWindowAdapter(this);
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mBinding.map.onDestroy()，销毁地图
        mBinding.map.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mBinding.map.onResume ()，重新绘制加载地图
        mBinding.map.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mBinding.map.onPause ()，暂停地图的绘制
        mBinding.map.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mBinding.map.onSaveInstanceState (outState)，保存地图当前的状态
        mBinding.map.onSaveInstanceState(outState);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return getInfoWindowView(marker);
    }

    @Override
    public View getInfoContents(Marker marker) {
        return getInfoWindowView(marker);
    }

    /**
     * 自定义View并且绑定数据方法
     * @param marker 点击的Marker对象
     * @return  返回自定义窗口的视图
     */
    private View getInfoWindowView(Marker marker) {
        infoWindow = mMarkerInfoBinding.getRoot();
        infoWindow.setBackgroundResource(R.drawable.common_map_infowindow_bg);
        render(marker, infoWindow);
        return infoWindow;
    }

    /**
     * 自定义infowinfow窗口
     */
    public void render(Marker marker, View view) {
        //如果想修改自定义Infow中内容，请通过view找到它并修改
        getAddressByLatlng(marker.getPosition());

        mMarkerInfoBinding.tvNavi.setOnClickListener(v -> {
            /**
             * 调用高德导航
             */
            if (AMapUtil.isInstallByRead("com.autonavi.minimap")){
                Toast.makeText(this, "即将用高德地图打开导航", Toast.LENGTH_SHORT).show();
                AMapUtil.goToNaviActivity(this,"连接社",null,latitude+"",longitude+"","1","2");
            }else {
                Toast.makeText(this, "请下载安装高德地图，方可导航", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAddressByLatlng(LatLng latLng) {
        //逆地理编码查询条件：逆地理编码查询的地理坐标点、查询范围、坐标类型。
        LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 500f, GeocodeSearch.AMAP);
        //异步查询
        geocodeSearch.getFromLocationAsyn(query);
    }

    //得到逆地理编码异步查询结果
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
        String formatAddress = regeocodeAddress.getFormatAddress();
        mMarkerInfoBinding.tvAddress.setText(formatAddress);
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }
}
