package org.phantancy.fgocalc.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.util.BaseUtils;
import org.phantancy.fgocalc.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by PY on 2016/10/31.
 */
public class MapActivity extends BaseActivity implements
        View.OnClickListener,
        AMap.OnMyLocationChangeListener,
        AMap.OnMarkerClickListener {

    @BindView(R.id.am_mv_map)
    MapView amMvMap;
    private AMap aMap;
    private MyLocationStyle myLocationStyle;
    private final int LOC_PERMISSION = 1;
    private double latitude,longitude;
    private boolean firstLoc = true;
    private double[] latNearby = {0.0003,0,-0.0003,0,0.0005,-0.0005,-0.0005,0.0005,0.0008,-0.0008,-0.0008,0.0008,0.0005,0,-0.0005};
    private double[] lonNearby = {0,0.0003,0,-0.0003,0.0005,0.0005,-0.0005,-0.0005,0.0004,0.0004,-0.0004,-0.0004,0,0.0005,0};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acty_map);
        ButterKnife.bind(this);
        mContext = this;
        amMvMap.onCreate(savedInstanceState);// 此方法必须重写
        init();
    }

    private void init() {
        if (!BaseUtils.isConn(mContext)) {
            ToastUtils.displayShortToast(mContext,"需要网络，请打开网络");
        }else{
            //有sim卡则用sim卡，否则检查gps
            if (BaseUtils.hasSimCard(mContext)) {
                initMap();
            }else{
                if (!BaseUtils.isGPSOPen(mContext)) {
                    ToastUtils.displayShortToast(mContext,"需要GPS，请打开GPS");
                }else{
                    initMap();
                }
            }
        }
        setListener();
    }

    private void setListener() {

    }

    private void initMap(){
        if (aMap == null) {
            AMapOptions aOptions = new AMapOptions();
            CameraPosition cp = new CameraPosition.Builder().bearing(0).tilt(30).build();
            aOptions.camera(cp);
            aMap = amMvMap.getMap();
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(mContext,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},LOC_PERMISSION);
            }else{
                setUpMap();
            }
        }
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        // 如果要设置定位的默认状态，可以在此处进行设置
        myLocationStyle = new MyLocationStyle();
        //        自定义自己
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                fromResource(R.mipmap.guda));
//        aMap.setMyLocationStyle(myLocationStyle);
        myLocationStyle.interval(10000);//定位的频次自定义
        // 定位、且将视角移动到地图中心点，定位点依照设备方向旋转，  并且会跟随设备移动。
        aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE));
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.moveCamera(CameraUpdateFactory.zoomTo(20));
        aMap.moveCamera(CameraUpdateFactory.changeTilt(30));
        aMap.showBuildings(true);//是否显示建筑物
//        aMap.showIndoorMap(true);//室内模式
        //设置SDK 自带定位消息监听
        aMap.setOnMyLocationChangeListener(this);
        aMap.setOnMarkerClickListener(this);
    }

    private void setMarker(){
        if (firstLoc) {
            firstLoc = false;
            int num = (int)(Math.random() * 15 + 10);
            for(int i = 0;i < num;i ++){
                LatLng latLng = new LatLng(latitude + latNearby[i], longitude + lonNearby[i]);
                MarkerOptions markerOption = new MarkerOptions();
                markerOption.position(latLng);
                markerOption.title("你要当我的master？？");
                markerOption.draggable(true);//设置Marker可拖动
                int extract = (int) (Math.random() * 188 + 1);
                int resId = getResources().getIdentifier("image" + extract,"mipmap",mContext.getPackageName());
                markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(),resId)));
                // 将Marker设置为贴地显示，可以双指下拉地图查看效果
                markerOption.setFlat(true);//设置marker平贴地图效果
                aMap.addMarker(markerOption);
            }
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (aMap != null) {
            Intent intent = new Intent(mContext,CatchActivity.class);
            startActivity(intent);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOC_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setUpMap();
                }else{
                    ToastUtils.displayShortToast(mContext,"您拒绝了权限");
                }
                break;
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        amMvMap.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        amMvMap.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        amMvMap.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        amMvMap.onDestroy();
    }

    @Override
    public void onMyLocationChange(Location location) {
        // 定位回调监听
        if(location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Log.e("amap", "onMyLocationChange 定位成功， lat: " + location.getLatitude() + " lon: " + location.getLongitude());
            setMarker();
            Bundle bundle = location.getExtras();
            if(bundle != null) {
                int errorCode = bundle.getInt(MyLocationStyle.ERROR_CODE);
                String errorInfo = bundle.getString(MyLocationStyle.ERROR_INFO);
                // 定位类型，可能为GPS WIFI等，具体可以参考官网的定位SDK介绍
                int locationType = bundle.getInt(MyLocationStyle.LOCATION_TYPE);

                /*
                errorCode
                errorInfo
                locationType
                */
                Log.e("amap", "定位信息， code: " + errorCode + " errorInfo: " + errorInfo + " locationType: " + locationType );
            } else {
                Log.e("amap", "定位信息， bundle is null ");

            }

        } else {
            Log.e("amap", "定位失败");
        }
    }
}
