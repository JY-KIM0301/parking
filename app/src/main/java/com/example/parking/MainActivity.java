package com.example.parking;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final int ACCESS_LOCATION_PERMISSION_REQUEST_CODE = 100;
    private FusedLocationSource locationSource;

    private FragHome fragHome;
    private FragStar fragStar;
    private FragInfo fragInfo;
    private FragFeedback fragFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = new Intent(this, LoadingActivity.class);
        startActivity(intent);

        fragHome = new FragHome();
        fragStar = new FragStar();
        fragInfo = new FragInfo();
        fragFeedback = new FragFeedback();


        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener(){
            @Override
            public void onTabSelected(@IdRes int tabId){
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                if (tabId == R.id.tab_home){
                    transaction.replace(R.id.contentContainer, fragHome).commit();
                }
                else if (tabId == R.id.tab_star){
                    transaction.replace(R.id.contentContainer, fragStar).commit();
                }
                else if(tabId == R.id.tab_info) {
                    transaction.replace(R.id.contentContainer, fragInfo).commit();
                }
                else if(tabId == R.id.tab_feedback){
                    transaction.replace(R.id.contentContainer, fragFeedback).commit();
                    }
                }
        });
    }



    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        locationSource = new FusedLocationSource(this, ACCESS_LOCATION_PERMISSION_REQUEST_CODE);
        naverMap.setLocationSource(locationSource);
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);

        Marker marker = new Marker();
        marker.setPosition(new LatLng(37.483428, 126.782933));  //부천남부역공영주차장
        marker.setIcon(OverlayImage.fromResource(R.drawable.parking_icon));
        marker.setWidth(100); marker.setHeight(140);
        marker.setMap(naverMap);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case ACCESS_LOCATION_PERMISSION_REQUEST_CODE:
                locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults);
                return;
        }
    }


}
