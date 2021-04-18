package com.example.parking.activity;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.parking.adapter.DataAdapter;
import com.example.parking.fragment.FragFeedback;
import com.example.parking.fragment.FragHome;
import com.example.parking.fragment.FragInfo;
import com.example.parking.fragment.FragStar;
import com.example.parking.model.Model;
import com.example.parking.R;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final int ACCESS_LOCATION_PERMISSION_REQUEST_CODE = 100;
    private FusedLocationSource locationSource;

    private FragHome fragHome;
    private FragStar fragStar;
    private FragInfo fragInfo;
    private FragFeedback fragFeedback;
    private InfoWindow infoWindow;
    NaverMap navermap;

    ArrayList<String> parking_name = new ArrayList<>(), parking_cnt = new ArrayList<>(),
            parking_x = new ArrayList<>(), parking_y = new ArrayList<>(), parking_price = new ArrayList<>();

    ArrayList<InfoWindow> wList = new ArrayList<>();
    List<Model> wlist;
    double my_x, my_y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataAdapter dataAdapter = new DataAdapter(MainActivity.this);
        dataAdapter.createDatabase();
        dataAdapter.open();

        wlist = dataAdapter.getTableData();
        dataAdapter.close();
        for (int i = 0; i < wlist.size(); i++) {
            Log.e("db", wlist.get(i).getCode() + " / " + wlist.get(i).getCount());
        }
        add();

        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = new Intent(this, LoadingActivity.class);
        startActivity(intent);

        fragHome = new FragHome();
        fragStar = new FragStar();
        fragInfo = new FragInfo();
        fragFeedback = new FragFeedback();

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                if (tabId == R.id.tab_home) {
                    transaction.replace(R.id.contentContainer, fragHome).commit();
                } else if (tabId == R.id.tab_star) {
                    transaction.replace(R.id.contentContainer, fragStar).commit();
                } else if (tabId == R.id.tab_info) {
                    transaction.replace(R.id.contentContainer, fragInfo).commit();
                } else if (tabId == R.id.tab_feedback) {
                    transaction.replace(R.id.contentContainer, fragFeedback).commit();
                }
            }
        });
    }

    private void add() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location location = null;
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER); //GPS정보 가져오기
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER); //현재 네트워크 상태 값 알아오기
        if (!isGPSEnabled && !isNetworkEnabled) {
        } else {
            if (isNetworkEnabled) { //퍼미션 체크
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 60000, 10, locationListener);
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) { //위도,경도 저장
                        my_x = location.getLatitude();
                        my_y = location.getLongitude();
                    }
                }
            }
            if (isGPSEnabled) {
                if (location == null) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 10, locationListener);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            my_x = location.getLatitude();
                            my_y = location.getLongitude();
                        }
                    }
                }
            }

        }
        parking_name.add("역곡청암 노상 공영주차장");
        parking_name.add("가톨릭대학교 성심교정 주차장");
        parking_name.add("소사종합시장 공영주차장");
        parking_name.add("송내북부공영주차장");
        parking_name.add("부천남부역공영주차장");

        parking_cnt.add("42면");
        parking_cnt.add("300면");
        parking_cnt.add("100면");
        parking_cnt.add("65면");
        parking_cnt.add("48면");

        parking_x.add("37.488901");
        parking_y.add("126.813085");
        parking_x.add("37.485927");
        parking_y.add("126.801970");
        parking_x.add("37.479048");
        parking_y.add("126.793487");
        parking_x.add("37.488769");
        parking_y.add("126.756266");
        parking_x.add("37.483428");
        parking_y.add("126.782933");

        parking_price.add("1시간당 3,000원 \n운영시간 : 00:00~24:00");
        parking_price.add("1일당 2,000원 \n운영시간 : 00:00~24:00");
        parking_price.add("1시간당 1,400원 \n운영시간 : 00:00~24:00");
        parking_price.add("1시간당 2,100원 \n운영시간 : 00:00~24:00");
        parking_price.add("1시간당 2,100원 \n운영시간 : 08:00~24:00");
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            my_x = location.getLatitude();
            my_y = location.getLongitude();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        locationSource = new FusedLocationSource(this, ACCESS_LOCATION_PERMISSION_REQUEST_CODE);
        naverMap.setLocationSource(locationSource);
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);
        navermap = naverMap;
        naverMap.addOnLocationChangeListener(new NaverMap.OnLocationChangeListener() {
            @Override
            public void onLocationChange(@NonNull Location location) {
                CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(my_x, my_y));
                navermap.moveCamera(cameraUpdate);

                addMarket();

                navermap.removeOnLocationChangeListener(this);
            }
        });


    }

    private void addMarket() {
        wList.clear();
        for (int i = 0; i < parking_x.size(); i++) {
            infoWindow = new InfoWindow();
            infoWindow.setPosition(new LatLng(Double.parseDouble(parking_x.get(i)), Double.parseDouble(parking_y.get(i))));
            infoWindow.setAdapter(new InfoWindowAdapter(this));
            infoWindow.setTag(i);
            infoWindow.setOnClickListener(new Overlay.OnClickListener() {
                @Override
                public boolean onClick(@NonNull Overlay overlay) {
                    infoWindow.close();
                    return false;
                }
            });
            wList.add(infoWindow); //정보창 추가

            final Marker marker = new Marker();
            marker.setPosition(new LatLng(Double.parseDouble(parking_x.get(i)), Double.parseDouble(parking_y.get(i))));
            marker.setIcon(OverlayImage.fromResource(R.drawable.parking_icon));
            marker.setWidth(100);
            marker.setHeight(140);
            marker.setAnchor(new PointF(1, 1));
            marker.setOnClickListener(new Overlay.OnClickListener() {
                @Override
                public boolean onClick(@NonNull Overlay overlay) {
                    for (int i = 0; i < wList.size(); i++) {
                        wList.get(i).close();
                    }
                    infoWindow.open(marker);
                    return true;
                }
            });
            marker.setTag(Integer.toString(i));
            marker.setMap(navermap);//마커 추가
        }
    }

    private class InfoWindowAdapter extends InfoWindow.ViewAdapter {//커스텀 정보창
        @NonNull
        private final Context context;
        private View rootView;
        private ImageView icon;
        private TextView text;
        String value, cd;

        private InfoWindowAdapter(@NonNull Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(@NonNull InfoWindow infoWindow) {
            if (rootView == null) {
                rootView = View.inflate(context, R.layout.listview_text, null);
                text = rootView.findViewById(R.id.title);
            }

            if (infoWindow.getMarker() != null) {
                int xx = Integer.parseInt(infoWindow.getMarker().getTag().toString()); // 클릭한 마커의 index 값

                if (parking_name.get(xx).equals("역곡청암 노상 공영주차장")) {
                    cd = "YN";
                } else if (parking_name.get(xx).equals("가톨릭대학교 성심교정 주차장")) {
                    cd = "CS";
                } else if (parking_name.get(xx).equals("소사종합시장 공영주차장")) {
                    cd = "SJ";
                } else if (parking_name.get(xx).equals("송내남부공영주차장")) {
                    cd = "SN";
                } else if (parking_name.get(xx).equals("부천남부역공영주차장")) {
                    cd = "BN";
                }

                String date1 = new SimpleDateFormat("EEE", Locale.KOREAN).format(new Date()); // 요일을 구함
                int date2 = Integer.parseInt(new SimpleDateFormat("HH", Locale.KOREAN).format(new Date())); // 시간을 구함
                int date3 = Integer.parseInt(new SimpleDateFormat("mm", Locale.KOREAN).format(new Date())); //분을 구함
                if (date3 != 0) {
                    date2++;
                }
                if (date1.equals("월") || date1.equals("화") || date1.equals("수") || date1.equals("목")) {
                    cd = cd + "_M";
                } else if (date1.equals("금")) {
                    cd = cd + "_F";
                } else if (date1.equals("토")) {
                    cd = cd + "_W1";
                } else if (date1.equals("일")) {
                    cd = cd + "_W2";
                }
                if (date2 > 6 && date2 <= 9) { // 6시보다 크고 9시와 같거나 큼
                    cd = cd + "_T1";
                } else if (date2 > 9 && date2 <= 12) { // 9시보다 크고 12시와 같거나 큼
                    cd = cd + "_T2";
                } else if (date2 == 13) { // 13시
                    cd = cd + "_T3";
                } else if (date2 > 13 && date2 <= 17) { // 13시보다 크고 17시와 같거나 큼
                    cd = cd + "_T4";
                } else if (date2 == 18) { // 18시
                    cd = cd + "_T5";
                } else if (date2 > 18 && date2 <= 24 || date2 == 0) { // 18시보다 크고 24시와 같거나 큼
                    cd = cd + "_T6";
                } else if (date2 > 0 && date2 <= 6) { // 0시보다 크고 6시와 같거나 큼
                    cd = cd + "_T7";
                }
                for (int i = 0; i < wlist.size(); i++) { // 클릭한 주차장들의 코드를 계산
                    if (cd.equals(wlist.get(i).getCode())) { // 디비의 코드값과 같으면
                        value = wlist.get(i).getCount(); // 주차대수를 확보
                    }
                }
                if(value.equals("이용중지")){
                    text.setText(parking_name.get(xx) + "\n주차요금 : " + parking_price.get(xx) + "\n총 " + parking_cnt.get(xx) + "\n현재 주차대수 : " + value);
                }else {
                    text.setText(parking_name.get(xx) + "\n주차요금 : " + parking_price.get(xx) + "\n총 " + parking_cnt.get(xx) + "\n현재 주차대수 : " + value + "대(" + date1 + "요일, " + date2 + "시(24시 기준))");
                }
            } else {
                text.setText("");
            }

            return rootView;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case ACCESS_LOCATION_PERMISSION_REQUEST_CODE:
                locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults);
                return;
        }
    }


}
