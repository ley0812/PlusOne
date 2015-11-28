package ac.plusone.main;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Point;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SlidingDrawer;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.algo.GridBasedAlgorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ac.plusone.R;
import ac.plusone.map.Address;
import ac.plusone.map.AddressAsync;
import ac.plusone.map.MyItem;
import ac.plusone.map.OwnIconRendered;
import ac.plusone.map.RealEstate;
import ac.plusone.map.RealEstateAsync;

public class MapActivity extends FragmentActivity implements LocationListener {
    SupportMapFragment mapView;
    GoogleMap googleMap;
    SlidingDrawer slidingDrawer;
    RecyclerView recyclerView;

    LocationManager locationManager;
    ClusterManager<RealEstate> mClusterManager;
    Marker marker;

    Polygon ChulwonPoly;
    Polygon ChoonchunPoly;
    Polygon HongchunPoly;
    Polygon HoengseongPoly;
    Polygon HwachunPoly;
    Polygon InjePoly;
    Polygon PyeongChangPoly;
    Polygon YangGuPoly;
    Polygon JeongSunPoly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mapView = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_view);
        googleMap = mapView.getMap()    ;

        ActionBar bar = getActionBar();

//       MyPoint myPoint = findGeoPoint("서울시 은평구  불광동");
//       Log.e("위도 받아오기!!!!!!!!!", myPoint.getLat() + " ");
//       Log.e("경도 받아오기!!!!!!!!!", myPoint.getLon() + " ");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        slidingDrawer = (SlidingDrawer) findViewById(R.id.bottom);

        setUpClusterer();
        setMapListener();
        setPolygon();
        setPolygon2();
        setPolygon3();
        setPolygon4();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String provider = LocationManager.GPS_PROVIDER;
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    public void setMapListener() {
        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.531, 128.135), 8));
                googleMap.getUiSettings().setRotateGesturesEnabled(false);
                googleMap.getUiSettings().setTiltGesturesEnabled(false);
                googleMap.getUiSettings().setMapToolbarEnabled(false);
            }
        });

        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                LatLngBounds cur_location = getBoundsWithoutSpacing();
                LatLng northeast = cur_location.northeast;
                LatLng southwest = cur_location.southwest;
                Log.e("북동쪽의 위치 받아오기", "위도 : " + northeast.latitude + ", 경도 : " + northeast.longitude);
                Log.e("남서쪽의 위치 받아오기", "위도 : " + southwest.latitude + ", 경도 : " + southwest.longitude);
                Log.e("줌레벨 확인 : ", googleMap.getCameraPosition().zoom + "");

                if (googleMap.getCameraPosition().zoom > 9) {
                    ChulwonPoly.setVisible(false);
                    ChoonchunPoly.setVisible(false);
                    HongchunPoly.setVisible(false);
                    HoengseongPoly.setVisible(false);
                    HwachunPoly.setVisible(false);
                    InjePoly.setVisible(false);
                    PyeongChangPoly.setVisible(false);
                    YangGuPoly.setVisible(false);
                    JeongSunPoly.setVisible(false);
                } else {
                    ChulwonPoly.setVisible(true);
                    ChoonchunPoly.setVisible(true);
                    HongchunPoly.setVisible(true);
                    HoengseongPoly.setVisible(true);
                    HwachunPoly.setVisible(true);
                    InjePoly.setVisible(true);
                    PyeongChangPoly.setVisible(true);
                    YangGuPoly.setVisible(true);
                    JeongSunPoly.setVisible(true);
                }


              /*  if(googleMap.getCameraPosition().zoom > 18){
                    InMapAddressAsync InMapAddressAsync = new InMapAddressAsync();
                    ArrayList<MyItem> myItems = new ArrayList<MyItem>();
                    try {
                        myItems = InMapAddressAsync.execute(southwest.latitude, northeast.latitude, southwest.longitude, northeast.longitude).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    mClusterManager.onCameraChange(cameraPosition);
                    setUpClusterer();
                    setMarkers(myItems);
                }*/
                if (googleMap.getCameraPosition().zoom > 9) {

                    RealEstateAsync realEstateAsync = new RealEstateAsync();
                    ArrayList<RealEstate> myItems = new ArrayList<RealEstate>();
                    try {
                        myItems = realEstateAsync.execute(southwest.latitude, northeast.latitude, southwest.longitude, northeast.longitude).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    mClusterManager.clearItems();
                    setMarkers(myItems);
//                    mClusterManager.cluster();
                    mClusterManager.onCameraChange(cameraPosition);
                } else {
                    mClusterManager.clearItems();
                    mClusterManager.cluster();
                }
            }
        });
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                slidingDrawer.close();

                if (googleMap.getCameraPosition().zoom < 9) {
                    if (latLng.latitude > 38.11175215569212 && latLng.latitude < 38.32702988272195 && latLng.longitude > 127.22522992640732 && latLng.longitude < 127.49340523034334) {
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(38.238131, 127.380756), 10));//철원
                    } else if (latLng.latitude > 38.067277 && latLng.latitude < 38.220643 && latLng.longitude > 127.524584 && latLng.longitude < 127.850054) {
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(38.149400, 127.679766), 13)); //화천
                    } else if (latLng.latitude > 37.755419 && latLng.latitude < 38.010134 && latLng.longitude > 127.490307 && latLng.longitude < 127.876201) {
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.898602, 127.744366), 13)); // 춘천
                    } else if (latLng.latitude > 37.583634 && latLng.latitude < 37.803142 && latLng.longitude > 127.738414 && latLng.longitude < 128.507457) {
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.748869, 128.070751), 10)); // 홍천
                    }
                }
            }
        });
    }

    private void setMarkers(ArrayList<RealEstate> myItems) {
/*        for (int i = 0; i < myItems.size(); i++) {
            RealEstate myItem = myItems.get(i);
            setMapPosition(myItem.getPosition().latitude, myItem.getPosition().longitude); // 마커 추가
        }*/

        for (int i = 0; i < myItems.size(); i++) {
            RealEstate myItem = myItems.get(i);
            mClusterManager.addItem(myItem); // 클러스터 추가
        }
    }

    private void setUpClusterer() {
        // Declare a variable for the cluster manager.
        mClusterManager = new ClusterManager<RealEstate>(this, googleMap);
        mClusterManager.setAlgorithm(new GridBasedAlgorithm<RealEstate>());
        mClusterManager.setRenderer(new OwnIconRendered(this, googleMap, mClusterManager));
//        googleMap.setOnCameraChangeListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);
        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<RealEstate>() {
            @Override
            public boolean onClusterItemClick(RealEstate myItem) {
                slidingDrawer.close();
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                ac.plusone.map.RecyclerAdapter adapter = new ac.plusone.map.RecyclerAdapter(myItem, getApplicationContext());
                recyclerView.setAdapter(adapter);
                slidingDrawer.open();
                return true;
            }
        });
        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<RealEstate>() {
            @Override
            public boolean onClusterClick(Cluster<RealEstate> cluster) {
                slidingDrawer.close();
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                ac.plusone.map.RecyclerAdapter adapter = new ac.plusone.map.RecyclerAdapter(cluster, getApplicationContext());
                recyclerView.setAdapter(adapter);
                slidingDrawer.open();
                return false;
            }
        });
    }

    public void setMapPosition(double lat, double lng) {
        LatLng position = new LatLng(lat, lng);
        // 마커가 없는 경우 새로 생성하여 지도에 추가
        MarkerOptions options = new MarkerOptions();
        options.position(position);
        marker = googleMap.addMarker(options.visible(false));


        // zoom : 1~21 (값이 커질 수록 확대)
//        CameraUpdate camera = CameraUpdateFactory.newLatLngZoom(position, 14.5f);
//        googleMap.animateCamera(camera);

    }

    public MyPoint findGeoPoint(String address) {
        Geocoder geoCoder = new Geocoder(this);
        android.location.Address addr;
        MyPoint location = null;
        try {
            List<android.location.Address> listAddress = geoCoder.getFromLocationName(address, 1);
            if (listAddress.size() > 0) {
                addr = listAddress.get(0);
                double lat = (double) (addr.getLatitude()); // 빼주기
                double lng = (double) (addr.getLongitude());
                location = new MyPoint(lat, lng);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    class MyPoint {
        private double lat;
        private double lon;

        public MyPoint(double lat, double lon) {
            this.lat = lat;
            this.lon = lon;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }
    }

    public LatLngBounds getBoundsWithoutSpacing() {
        Projection projection = googleMap.getProjection();
        LatLngBounds bounds = projection.getVisibleRegion().latLngBounds;
        Point northeast = projection.toScreenLocation(bounds.northeast);
        Point toNortheast = new Point(northeast.x, northeast.y);
        Point southwest = projection.toScreenLocation(bounds.southwest);
        Point toSouthwest = new Point(southwest.x, southwest.y);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(projection.fromScreenLocation(toNortheast));
        builder.include(projection.fromScreenLocation(toSouthwest));
        return builder.build();
    }

    public void setPolygon() {
        ChulwonPoly = googleMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(38.104796, 127.415607),
                        new LatLng(38.109929, 127.420756),
                        new LatLng(38.118573, 127.422130),
                        new LatLng(38.129646, 127.442729),
                        new LatLng(38.124515, 127.450969),
                        new LatLng(38.128836, 127.462642),
                        new LatLng(38.135317, 127.493197),
                        new LatLng(38.148818, 127.496631),
                        new LatLng(38.161506, 127.489764),
                        new LatLng(38.178511, 127.501437),
                        new LatLng(38.188766, 127.499377),
                        new LatLng(38.193623, 127.516887),
                        new LatLng(38.200638, 127.532680),
                        new LatLng(38.207652, 127.534740),
                        new LatLng(38.234895, 127.529933),
                        new LatLng(38.233546, 127.554652),
                        new LatLng(38.257544, 127.577312),
                        new LatLng(38.259970, 127.595508),
                        new LatLng(38.282342, 127.597911),
                        new LatLng(38.286114, 127.608211),
                        new LatLng(38.266170, 127.619540),
                        new LatLng(38.258083, 127.653186),
                        new LatLng(38.265900, 127.668292),
                        new LatLng(38.246759, 127.694728),
                        new LatLng(38.254578, 127.714641),
                        new LatLng(38.278568, 127.730434),
                        new LatLng(38.277490, 127.750346),
                        new LatLng(38.282072, 127.782619),
                        new LatLng(38.278299, 127.799098),
                        new LatLng(38.284228, 127.817981),
                        new LatLng(38.279108, 127.828624),
                        new LatLng(38.281264, 127.838924),
                        new LatLng(38.277760, 127.853343),
                        new LatLng(38.292312, 127.867763),
                        new LatLng(38.320600, 127.872226),
                        new LatLng(38.316964, 127.852656),
                        new LatLng(38.310768, 127.824161),
                        new LatLng(38.346319, 127.787768),
                        new LatLng(38.346857, 127.778842),
                        new LatLng(38.339049, 127.757899),
                        new LatLng(38.337702, 127.576282),
                        new LatLng(38.302416, 127.505214),
                        new LatLng(38.320196, 127.471225),
                        new LatLng(38.321004, 127.441012),
                        new LatLng(38.335278, 127.409427),
                        new LatLng(38.335009, 127.374064),
                        new LatLng(38.328815, 127.362048),
                        new LatLng(38.329623, 127.342822),
                        new LatLng(38.322351, 127.325999),
                        new LatLng(38.318041, 127.299563),
                        new LatLng(38.324775, 127.287890),
                        new LatLng(38.329892, 127.235705),
                        new LatLng(38.307266, 127.152965),
                        new LatLng(38.315617, 127.140605),
                        new LatLng(38.299453, 127.117602),
                        new LatLng(38.283015, 127.093226),
                        new LatLng(38.280997, 127.095286),
                        new LatLng(38.280660, 127.103269),
                        new LatLng(38.279043, 127.104384),
                        new LatLng(38.275944, 127.100694),
                        new LatLng(38.273316, 127.101638),
                        new LatLng(38.271564, 127.109878),
                        new LatLng(38.269273, 127.110135),
                        new LatLng(38.268262, 127.108161),
                        new LatLng(38.252559, 127.110135),
                        new LatLng(38.248784, 127.110478),
                        new LatLng(38.244201, 127.107045),
                        new LatLng(38.233414, 127.126615),
                        new LatLng(38.236650, 127.141034),
                        new LatLng(38.242583, 127.147557),
                        new LatLng(38.237729, 127.161290),
                        new LatLng(38.232875, 127.160604),
                        new LatLng(38.226672, 127.158544),
                        new LatLng(38.218850, 127.160604),
                        new LatLng(38.205632, 127.165410),
                        new LatLng(38.197268, 127.164723),
                        new LatLng(38.190523, 127.174680),
                        new LatLng(38.193761, 127.188756),
                        new LatLng(38.183777, 127.201116),
                        new LatLng(38.177300, 127.197339),
                        new LatLng(38.178919, 127.186353),
                        new LatLng(38.168663, 127.185666),
                        new LatLng(38.158135, 127.193563),
                        new LatLng(38.154086, 127.201802),
                        new LatLng(38.148146, 127.195622),
                        new LatLng(38.148956, 127.206265),
                        new LatLng(38.138696, 127.221028),
                        new LatLng(38.147606, 127.223775),
                        new LatLng(38.147066, 127.233045),
                        new LatLng(38.156785, 127.236821),
                        new LatLng(38.162994, 127.253987),
                        new LatLng(38.170822, 127.258107),
                        new LatLng(38.176760, 127.265660),
                        new LatLng(38.181888, 127.268407),
                        new LatLng(38.182697, 127.283170),
                        new LatLng(38.175680, 127.295873),
                        new LatLng(38.169742, 127.286946),
                        new LatLng(38.160295, 127.284200),
                        new LatLng(38.153276, 127.286603),
                        new LatLng(38.137075, 127.273557),
                        new LatLng(38.126543, 127.276303),
                        new LatLng(38.117360, 127.282140),
                        new LatLng(38.115739, 127.293469),
                        new LatLng(38.117630, 127.309949),
                        new LatLng(38.100071, 127.314755),
                        new LatLng(38.092506, 127.338445),
                        new LatLng(38.100611, 127.341535),
                        new LatLng(38.104394, 127.351834),
                        new LatLng(38.111983, 127.371564),
                        new LatLng(38.112253, 127.370363),
                        new LatLng(38.118744, 127.379279),
                        new LatLng(38.114085, 127.392411),
                        new LatLng(38.112464, 127.403311),
                        new LatLng(38.104796, 127.415607))
                .strokeColor(0xFFF57F17)
                .fillColor(0x7FFFD740));
        ChulwonPoly.setStrokeWidth(10);
        ChoonchunPoly = googleMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(37.67852, 127.6453
                        ), new LatLng(37.67930, 127.6484
                        ), new LatLng(37.68250, 127.6507
                        ), new LatLng(37.68297, 127.6517
                        ), new LatLng(37.68436, 127.6520
                        ), new LatLng(37.68654, 127.6518
                        ), new LatLng(37.68779, 127.6520
                        ), new LatLng(37.68891, 127.6516
                        ), new LatLng(37.68983, 127.6520
                        ), new LatLng(37.69659, 127.6502
                        ), new LatLng(37.69737, 127.6513
                        ), new LatLng(37.69859, 127.6519
                        ), new LatLng(37.69941, 127.6606
                        ), new LatLng(37.695544, 127.670352
                        ), new LatLng(37.696936, 127.672541
                        ), new LatLng(37.695985, 127.674815
                        ), new LatLng(37.695340, 127.676060
                        ), new LatLng(37.696325, 127.677819
                        ), new LatLng(37.697751, 127.678849
                        ), new LatLng(37.697853, 127.680351
                        ), new LatLng(37.698735, 127.681639
                        ), new LatLng(37.699415, 127.681810
                        ), new LatLng(37.700196, 127.683312
                        ), new LatLng(37.701452, 127.683484
                        ), new LatLng(37.701825, 127.682969
                        ), new LatLng(37.705289, 127.682840
                        ), new LatLng(37.707190, 127.683613
                        ), new LatLng(37.707631, 127.681467
                        ), new LatLng(37.707496, 127.680780
                        ), new LatLng(37.710857, 127.679278
                        ), new LatLng(37.711807, 127.679965
                        ), new LatLng(37.711570, 127.681553
                        ), new LatLng(37.713946, 127.685458


                        ), new LatLng(37.71540, 127.6875
                        ), new LatLng(37.71655, 127.6892
                        ), new LatLng(37.71669, 127.6912
                        ), new LatLng(37.71716, 127.6948
                        ), new LatLng(37.71618, 127.6965
                        ), new LatLng(37.71683, 127.6981
                        ), new LatLng(37.71645, 127.6997
                        ), new LatLng(37.71845, 127.7001
                        ), new LatLng(37.71924, 127.7027
                        ), new LatLng(37.72117, 127.7051
                        ), new LatLng(37.72117, 127.7080
                        ), new LatLng(37.72205, 127.7080
                        ), new LatLng(37.72266, 127.7096
                        ), new LatLng(37.72440, 127.7090
                        ), new LatLng(37.72531, 127.7107
                        ), new LatLng(37.72626, 127.7104
                        ), new LatLng(37.72759, 127.7111
                        ), new LatLng(37.72731, 127.7121
                        ), new LatLng(37.72806, 127.7151
                        ), new LatLng(37.72928, 127.7158
                        ), new LatLng(37.72938, 127.7170
                        ), new LatLng(37.73013, 127.7180
                        ), new LatLng(37.73118, 127.7182
                        ), new LatLng(37.73186, 127.7193
                        ), new LatLng(37.73268, 127.7201
                        ), new LatLng(37.73274, 127.7249
                        ), new LatLng(37.73186, 127.7265
                        ), new LatLng(37.73807, 127.7374
                        ), new LatLng(37.73933, 127.7374
                        ), new LatLng(37.74130, 127.7424
                        ), new LatLng(37.74130, 127.7448
                        ), new LatLng(37.73814, 127.7494
                        ), new LatLng(37.73614, 127.7499
                        ), new LatLng(37.73590, 127.7510
                        ), new LatLng(37.73336, 127.7515
                        ), new LatLng(37.73241, 127.7549
                        ), new LatLng(37.73492, 127.7576
                        ), new LatLng(37.73488, 127.7586
                        ), new LatLng(37.73563, 127.7585
                        ), new LatLng(37.73672, 127.7600
                        ), new LatLng(37.73661, 127.7608
                        ), new LatLng(37.73767, 127.7646
                        ), new LatLng(37.73750, 127.7664
                        ), new LatLng(37.73794, 127.7680
                        ), new LatLng(37.73750, 127.7687
                        ), new LatLng(37.73784, 127.7724
                        ), new LatLng(37.73899, 127.7735
                        ), new LatLng(37.73960, 127.7763
                        ), new LatLng(37.74031, 127.7769
                        ), new LatLng(37.74038, 127.7797
                        ), new LatLng(37.74164, 127.7806
                        ), new LatLng(37.74086, 127.7853
                        ), new LatLng(37.73977, 127.7852
                        ), new LatLng(37.73828, 127.7870
                        ), new LatLng(37.73722, 127.7868
                        ), new LatLng(37.73600, 127.7877
                        ), new LatLng(37.73689, 127.7896
                        ), new LatLng(37.73600, 127.7906
                        ), new LatLng(37.73515, 127.7945
                        ), new LatLng(37.73699, 127.7987
                        ), new LatLng(37.73665, 127.8002
                        ), new LatLng(37.73753, 127.8003
                        ), new LatLng(37.73818, 127.8015
                        ), new LatLng(37.73909, 127.8010
                        ), new LatLng(37.73994, 127.8016
                        ), new LatLng(37.74160, 127.8007
                        ), new LatLng(37.74282, 127.8040
                        ), new LatLng(37.74218, 127.8056
                        ), new LatLng(37.74235, 127.8067
                        ), new LatLng(37.74374, 127.8067
                        ), new LatLng(37.74452, 127.8060
                        ), new LatLng(37.74601, 127.8061
                        ), new LatLng(37.74608, 127.8102
                        ), new LatLng(37.74859, 127.8142
                        ), new LatLng(37.74859, 127.8165
                        ), new LatLng(37.74717, 127.8172
                        ), new LatLng(37.74744, 127.8188
                        ), new LatLng(37.74187, 127.8259
                        ), new LatLng(37.74154, 127.8296
                        ), new LatLng(37.74157, 127.8316
                        ), new LatLng(37.74225, 127.8318
                        ), new LatLng(37.74167, 127.8323
                        ), new LatLng(37.74466, 127.8349
                        ), new LatLng(37.74513, 127.8356
                        ), new LatLng(37.74598, 127.8355
                        ), new LatLng(37.74663, 127.8379
                        ), new LatLng(37.74775, 127.8386
                        ), new LatLng(37.74744, 127.8396
                        ), new LatLng(37.74805, 127.8412
                        ), new LatLng(37.74778, 127.8416
                        ), new LatLng(37.74876, 127.8425
                        ), new LatLng(37.74951, 127.8440
                        ), new LatLng(37.75148, 127.8452
                        ), new LatLng(37.75348, 127.8433
                        ), new LatLng(37.75402, 127.8408
                        ), new LatLng(37.75538, 127.8393
                        ), new LatLng(37.75714, 127.8417
                        ), new LatLng(37.75687, 127.8423
                        ), new LatLng(37.75691, 127.8438
                        ), new LatLng(37.75769, 127.8454
                        ), new LatLng(37.75691, 127.8465
                        ), new LatLng(37.75809, 127.8481
                        ), new LatLng(37.75918, 127.8479
                        ), new LatLng(37.75921, 127.8505
                        ), new LatLng(37.75993, 127.8508
                        ), new LatLng(37.76865, 127.8589
                        ), new LatLng(37.77217, 127.8527
                        ), new LatLng(37.79110, 127.8500
                        ), new LatLng(37.79558, 127.8340
                        ), new LatLng(37.80497, 127.8342
                        ), new LatLng(37.80931, 127.8296
                        ), new LatLng(37.81999, 127.8323
                        ), new LatLng(37.83643, 127.8151
                        ), new LatLng(37.84128, 127.8176
                        ), new LatLng(37.84212, 127.8254
                        ), new LatLng(37.84700, 127.8304
                        ), new LatLng(37.84212, 127.8440
                        ), new LatLng(37.83897, 127.8468
                        ), new LatLng(37.83517, 127.8479
                        ), new LatLng(37.83409, 127.8648
                        ), new LatLng(37.83589, 127.8672
                        ), new LatLng(37.83843, 127.8685
                        ), new LatLng(37.84087, 127.8749
                        ), new LatLng(37.84233, 127.8755
                        ), new LatLng(37.84727, 127.8841
                        ), new LatLng(37.85412, 127.8819
                        ), new LatLng(37.86276, 127.8898
                        ), new LatLng(37.86394, 127.8935
                        ), new LatLng(37.85873, 127.9052
                        ), new LatLng(37.84524, 127.8971
                        ), new LatLng(37.84449, 127.8986
                        ), new LatLng(37.84571, 127.9105
                        ), new LatLng(37.84632, 127.9121
                        ), new LatLng(37.84419, 127.9180
                        ), new LatLng(37.84148, 127.9175
                        ), new LatLng(37.83700, 127.9188
                        ), new LatLng(37.83585, 127.9206
                        ), new LatLng(37.83602, 127.9288
                        ), new LatLng(37.82931, 127.9307
                        ), new LatLng(37.82823, 127.9411
                        ), new LatLng(37.83168, 127.9451
                        ), new LatLng(37.84324, 127.9379
                        ), new LatLng(37.84002, 127.9498
                        ), new LatLng(37.84067, 127.9500
                        ), new LatLng(37.84348, 127.9446
                        ), new LatLng(37.84443, 127.9459
                        ), new LatLng(37.84626, 127.9451
                        ), new LatLng(37.84632, 127.9414
                        ), new LatLng(37.85104, 127.9445
                        ), new LatLng(37.85276, 127.9431
                        ), new LatLng(37.85727, 127.9433
                        ), new LatLng(37.86564, 127.9539
                        ), new LatLng(37.86520, 127.9566
                        ), new LatLng(37.87814, 127.9669
                        ), new LatLng(37.88220, 127.9751
                        ), new LatLng(37.89138, 127.9760
                        ), new LatLng(37.89802, 127.9697
                        ), new LatLng(37.90110, 127.9684
                        ), new LatLng(37.90347, 127.9708
                        ), new LatLng(37.90364, 127.9741
                        ), new LatLng(37.90615, 127.9808
                        ), new LatLng(37.90862, 127.9805
                        ), new LatLng(37.91055, 127.9845
                        ), new LatLng(37.91170, 127.9857
                        ), new LatLng(37.91282, 127.9845
                        ), new LatLng(37.91803, 127.9856
                        ), new LatLng(37.92057, 127.9849
                        ), new LatLng(37.92257, 127.9858
                        ), new LatLng(37.92710, 127.9838
                        ), new LatLng(37.93069, 127.9882
                        ), new LatLng(37.93959, 127.9896
                        ), new LatLng(37.94186, 127.9907
                        ), new LatLng(37.942802, 127.990743
                        ), new LatLng(37.948555, 127.986451
                        ), new LatLng(37.959383, 127.985807
                        ), new LatLng(37.961464, 127.979842
                        ), new LatLng(37.964983, 127.976752
                        ), new LatLng(37.972223, 127.980786
                        ), new LatLng(37.974321, 127.985679
                        ), new LatLng(37.979801, 127.985421
                        ), new LatLng(37.983184, 127.981730
                        ), new LatLng(37.985348, 127.981902
                        ), new LatLng(37.985822, 127.987824
                        ), new LatLng(37.991369, 127.994605
                        ), new LatLng(37.999824, 128.007823
                        ), new LatLng(37.998809, 128.013574
                        ), new LatLng(38.003679, 128.021127
                        ), new LatLng(38.010983, 128.021642
                        ), new LatLng(38.014094, 128.026276
                        ), new LatLng(38.017610, 128.026963
                        ), new LatLng(38.018286, 128.025676
                        ), new LatLng(38.018016, 128.024045
                        ), new LatLng(38.019233, 128.021727
                        ), new LatLng(38.017001, 128.017350
                        ), new LatLng(38.019368, 128.015805
                        ), new LatLng(38.020517, 128.012286
                        ), new LatLng(38.016866, 128.007308
                        ), new LatLng(38.012065, 128.009024
                        ), new LatLng(38.010036, 128.007651
                        ), new LatLng(38.008278, 127.998896
                        ), new LatLng(38.000500, 128.002072
                        ), new LatLng(38.002394, 127.988597
                        ), new LatLng(38.004829, 127.988425
                        ), new LatLng(38.005370, 127.984820
                        ), new LatLng(38.017204, 127.983704
                        ), new LatLng(38.025521, 127.988683
                        ), new LatLng(38.027346, 127.984134
                        ), new LatLng(38.032755, 127.984992
                        ), new LatLng(38.03119, 127.9703
                        ), new LatLng(38.03177, 127.9675
                        ), new LatLng(38.03136, 127.9645
                        ), new LatLng(38.03288, 127.9647
                        ), new LatLng(38.03457, 127.9621
                        ), new LatLng(38.03447, 127.9609
                        ), new LatLng(38.03640, 127.9601
                        ), new LatLng(38.03799, 127.9575
                        ), new LatLng(38.03883, 127.9552
                        ), new LatLng(38.03704, 127.9501
                        ), new LatLng(38.03474, 127.9493
                        ), new LatLng(38.03440, 127.9475
                        ), new LatLng(38.02916, 127.9454
                        ), new LatLng(38.02599, 127.9467
                        ), new LatLng(38.02402, 127.9472
                        ), new LatLng(38.01993, 127.9500
                        ), new LatLng(38.01831, 127.9448
                        ), new LatLng(38.01716, 127.9446
                        ), new LatLng(38.01574, 127.9415
                        ), new LatLng(38.01463, 127.9410
                        ), new LatLng(38.01463, 127.9386
                        ), new LatLng(38.01415, 127.9373
                        ), new LatLng(38.01436, 127.9360
                        ), new LatLng(38.01405, 127.9349
                        ), new LatLng(38.01550, 127.9338
                        ), new LatLng(38.01564, 127.9324
                        ), new LatLng(38.01909, 127.9279
                        ), new LatLng(38.01882, 127.9269
                        ), new LatLng(38.01993, 127.9253
                        ), new LatLng(38.01973, 127.9245
                        ), new LatLng(38.02047, 127.9238
                        ), new LatLng(38.02004, 127.9235
                        ), new LatLng(38.01983, 127.9223
                        ), new LatLng(38.02250, 127.9194
                        ), new LatLng(38.02592, 127.9186
                        ), new LatLng(38.02636, 127.9147
                        ), new LatLng(38.02396, 127.9084
                        ), new LatLng(38.02808, 127.9029
                        ), new LatLng(38.03467, 127.9036
                        ), new LatLng(38.03744, 127.9017
                        ), new LatLng(38.04052, 127.9016
                        ), new LatLng(38.04731, 127.9036
                        ), new LatLng(38.05221, 127.8939
                        ), new LatLng(38.05140, 127.8918
                        ), new LatLng(38.05306, 127.8876
                        ), new LatLng(38.04890, 127.8855
                        ), new LatLng(38.04640, 127.8859
                        ), new LatLng(38.04647, 127.8798
                        ), new LatLng(38.04931, 127.8745
                        ), new LatLng(38.04897, 127.8702
                        ), new LatLng(38.05046, 127.8676
                        ), new LatLng(38.05019, 127.8597
                        ), new LatLng(38.05607, 127.8593
                        ), new LatLng(38.05728, 127.8581
                        ), new LatLng(38.05485, 127.8497
                        ), new LatLng(38.04883, 127.8529
                        ), new LatLng(38.04410, 127.8529
                        ), new LatLng(38.03701, 127.8500
                        ), new LatLng(38.03200, 127.8506
                        ), new LatLng(38.03052, 127.8545
                        ), new LatLng(38.02700, 127.8575
                        ), new LatLng(38.02281, 127.8573
                        ), new LatLng(38.01557, 127.8522
                        ), new LatLng(38.01266, 127.8393
                        ), new LatLng(38.00550, 127.8366
                        ), new LatLng(38.00759, 127.8246
                        ), new LatLng(38.00293, 127.8168
                        ), new LatLng(38.00502, 127.8149
                        ), new LatLng(38.00739, 127.8062
                        ), new LatLng(38.00509, 127.8001
                        ), new LatLng(38.00029, 127.7973
                        ), new LatLng(37.99231, 127.8008
                        ), new LatLng(37.99434, 127.7755
                        ), new LatLng(37.99927, 127.7709
                        ), new LatLng(38.00665, 127.7816
                        ), new LatLng(38.02254, 127.7860
                        ), new LatLng(38.02490, 127.7836
                        ), new LatLng(38.02348, 127.7662
                        ), new LatLng(38.02855, 127.7591
                        ), new LatLng(38.03444, 127.7602
                        ), new LatLng(38.04147, 127.7562
                        ), new LatLng(38.04512, 127.7504
                        ), new LatLng(38.04458, 127.7483
                        ), new LatLng(38.03234, 127.7371
                        ), new LatLng(38.03106, 127.7369
                        ), new LatLng(38.02903, 127.7318
                        ), new LatLng(38.02200, 127.7264
                        ), new LatLng(38.02132, 127.7213
                        ), new LatLng(38.02903, 127.7108
                        ), new LatLng(38.03194, 127.7106
                        ), new LatLng(38.03680, 127.7053
                        ), new LatLng(38.03721, 127.7018
                        ), new LatLng(38.03903, 127.7007
                        ), new LatLng(38.03910, 127.6990
                        ), new LatLng(38.03619, 127.6982
                        ), new LatLng(38.03714, 127.6964
                        ), new LatLng(38.03545, 127.6929
                        ), new LatLng(38.03728, 127.6759
                        ), new LatLng(38.03065, 127.6639
                        ), new LatLng(38.02903, 127.6555
                        ), new LatLng(38.02497, 127.6534
                        ), new LatLng(38.04295, 127.6484
                        ), new LatLng(38.05079, 127.6538
                        ), new LatLng(38.05620, 127.6508
                        ), new LatLng(38.05742, 127.6469
                        ), new LatLng(38.05269, 127.6407
                        ), new LatLng(38.05039, 127.6306
                        ), new LatLng(38.05147, 127.6232
                        ), new LatLng(38.05809, 127.6179
                        ), new LatLng(38.05863, 127.6134
                        ), new LatLng(38.05742, 127.6125
                        ), new LatLng(38.05722, 127.6066
                        ), new LatLng(38.06546, 127.6029
                        ), new LatLng(38.06924, 127.6045
                        ), new LatLng(38.07404, 127.6031
                        ), new LatLng(38.07472, 127.6005
                        ), new LatLng(38.08235, 127.5944
                        ), new LatLng(38.08458, 127.5857
                        ), new LatLng(38.08249, 127.5785
                        ), new LatLng(38.09181, 127.5646
                        ), new LatLng(38.09269, 127.5587
                        ), new LatLng(38.08201, 127.5590
                        ), new LatLng(38.07593, 127.5714
                        ), new LatLng(38.07053, 127.5690
                        ), new LatLng(38.06600, 127.5683
                        ), new LatLng(38.05465, 127.5793
                        ), new LatLng(38.05181, 127.5755
                        ), new LatLng(38.04437, 127.5725
                        ), new LatLng(38.04140, 127.5682
                        ), new LatLng(38.03390, 127.5696
                        ), new LatLng(38.02213, 127.5644
                        ), new LatLng(38.02984, 127.5495
                        ), new LatLng(38.02822, 127.5380
                        ), new LatLng(38.02348, 127.5332
                        ), new LatLng(38.02010, 127.5317
                        ), new LatLng(38.01016, 127.5370
                        ), new LatLng(37.99799, 127.5308
                        ), new LatLng(37.98832, 127.5422
                        ), new LatLng(37.97600, 127.5353
                        ), new LatLng(37.97255, 127.5368
                        ), new LatLng(37.96633, 127.5529
                        ), new LatLng(37.96890, 127.5569
                        ), new LatLng(37.96294, 127.5710
                        ), new LatLng(37.96470, 127.5844
                        ), new LatLng(37.96085, 127.5899
                        ), new LatLng(37.96112, 127.6044
                        ), new LatLng(37.94846, 127.6082
                        ), new LatLng(37.94650, 127.6057
                        ), new LatLng(37.94163, 127.6065
                        ), new LatLng(37.93797, 127.6094
                        ), new LatLng(37.93384, 127.6204
                        ), new LatLng(37.92653, 127.6151
                        ), new LatLng(37.92524, 127.6093
                        ), new LatLng(37.91739, 127.6106
                        ), new LatLng(37.91319, 127.6178
                        ), new LatLng(37.90547, 127.6160
                        ), new LatLng(37.89897, 127.6079
                        ), new LatLng(37.88319, 127.6114
                        ), new LatLng(37.88156, 127.6070
                        ), new LatLng(37.87479, 127.6054
                        ), new LatLng(37.87377, 127.5921
                        ), new LatLng(37.87695, 127.5886
                        ), new LatLng(37.87628, 127.5828
                        ), new LatLng(37.87424, 127.5816
                        ), new LatLng(37.87479, 127.5795
                        ), new LatLng(37.86889, 127.5738
                        ), new LatLng(37.86666, 127.5683
                        ), new LatLng(37.86435, 127.5694
                        ), new LatLng(37.86259, 127.5647
                        ), new LatLng(37.85940, 127.5674
                        ), new LatLng(37.85846, 127.5640
                        ), new LatLng(37.85466, 127.5605
                        ), new LatLng(37.85561, 127.5581
                        ), new LatLng(37.85392, 127.5551
                        ), new LatLng(37.85181, 127.5505
                        ), new LatLng(37.84639, 127.5491
                        ), new LatLng(37.84517, 127.5380
                        ), new LatLng(37.83745, 127.5273
                        ), new LatLng(37.82741, 127.5241
                        ), new LatLng(37.82131, 127.5264
                        ), new LatLng(37.81901, 127.5345
                        ), new LatLng(37.81012, 127.5380
                        ), new LatLng(37.79283, 127.5213
                        ), new LatLng(37.79113, 127.5226
                        ), new LatLng(37.78998, 127.5213
                        ), new LatLng(37.76339, 127.5450
                        ), new LatLng(37.75742, 127.5433
                        ), new LatLng(37.73495, 127.5088
                        ), new LatLng(37.72280, 127.5064
                        ), new LatLng(37.71608, 127.5121
                        ), new LatLng(37.72653, 127.5262
                        ), new LatLng(37.72647, 127.5296
                        ), new LatLng(37.72545, 127.5308
                        ), new LatLng(37.72361, 127.5309
                        ), new LatLng(37.72185, 127.5340
                        ), new LatLng(37.71981, 127.5349
                        ), new LatLng(37.71954, 127.5357
                        ), new LatLng(37.72015, 127.5391
                        ), new LatLng(37.71947, 127.5413
                        ), new LatLng(37.72450, 127.5496
                        ), new LatLng(37.72850, 127.5511
                        ), new LatLng(37.72905, 127.5582
                        ), new LatLng(37.72816, 127.5593
                        ), new LatLng(37.72837, 127.5636
                        ), new LatLng(37.73101, 127.5650
                        ), new LatLng(37.73319, 127.5728
                        ), new LatLng(37.72110, 127.5923
                        ), new LatLng(37.71941, 127.5943
                        ), new LatLng(37.71845, 127.5938
                        ), new LatLng(37.71778, 127.5927
                        ), new LatLng(37.70970, 127.5910
                        ), new LatLng(37.70725, 127.5940
                        ), new LatLng(37.70569, 127.5939
                        ), new LatLng(37.70413, 127.5962
                        ), new LatLng(37.70596, 127.6053
                        ), new LatLng(37.70406, 127.6081
                        ), new LatLng(37.69788, 127.6112
                        ), new LatLng(37.68681, 127.6082
                        ), new LatLng(37.67716, 127.6076
                        ), new LatLng(37.67519, 127.6104
                        ), new LatLng(37.67893, 127.6212
                        ), new LatLng(37.68321, 127.6237
                        ), new LatLng(37.68929, 127.6228
                        ), new LatLng(37.69197, 127.6234
                        ), new LatLng(37.69476, 127.6255
                        ), new LatLng(37.69778, 127.6311
                        ), new LatLng(37.69876, 127.6410
                        ), new LatLng(37.69598, 127.6425
                        ), new LatLng(37.68698, 127.6416
                        ), new LatLng(37.68654, 127.6403
                        ), new LatLng(37.68430, 127.6399
                        ), new LatLng(37.68171, 127.6405
                        ), new LatLng(37.67968, 127.6419
                        ), new LatLng(37.67852, 127.6453))
                .strokeColor(0xFFF57F17)
                .fillColor(0x7FFFD740));
        ChoonchunPoly.setStrokeWidth(10);
    }

    public void setPolygon2() {

        HongchunPoly = googleMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(37.736080, 128.496413
                        ), new LatLng(37.731736, 128.501220
                        ), new LatLng(37.730649, 128.517013
                        ), new LatLng(37.738252, 128.530745
                        ), new LatLng(37.750469, 128.537955
                        ), new LatLng(37.764880, 128.538218
                        ), new LatLng(37.779263, 128.527575
                        ), new LatLng(37.808293, 128.528605
                        ), new LatLng(37.814531, 128.543368
                        ), new LatLng(37.809920, 128.584567
                        ), new LatLng(37.827549, 128.600703
                        ), new LatLng(37.867672, 128.587313
                        ), new LatLng(37.888538, 128.569117
                        ), new LatLng(37.891519, 128.558817
                        ), new LatLng(37.877428, 128.527918
                        ), new LatLng(37.896395, 128.448267
                        ), new LatLng(37.895041, 128.425951
                        ), new LatLng(37.889622, 128.415308
                        ), new LatLng(37.858456, 128.400202
                        ), new LatLng(37.857643, 128.376513
                        ), new LatLng(37.845444, 128.364153
                        ), new LatLng(37.839751, 128.361750
                        ), new LatLng(37.838124, 128.366213
                        ), new LatLng(37.833785, 128.361407
                        ), new LatLng(37.833243, 128.355227
                        ), new LatLng(37.827006, 128.342181
                        ), new LatLng(37.833243, 128.332911
                        ), new LatLng(37.826193, 128.310938
                        ), new LatLng(37.833785, 128.306475
                        ), new LatLng(37.842462, 128.310252
                        ), new LatLng(37.862522, 128.302012
                        ), new LatLng(37.870111, 128.293086
                        ), new LatLng(37.865504, 128.286219
                        ), new LatLng(37.864962, 128.275919
                        ), new LatLng(37.857643, 128.258753
                        ), new LatLng(37.840564, 128.253947
                        ), new LatLng(37.838395, 128.246050
                        ), new LatLng(37.833514, 128.246050
                        ), new LatLng(37.830260, 128.240557
                        ), new LatLng(37.826735, 128.238841
                        ), new LatLng(37.823481, 128.226138
                        ), new LatLng(37.805851, 128.205195
                        ), new LatLng(37.807750, 128.197298
                        ), new LatLng(37.824023, 128.191462
                        ), new LatLng(37.831345, 128.178759
                        ), new LatLng(37.847613, 128.180132
                        ), new LatLng(37.857372, 128.176356
                        ), new LatLng(37.861438, 128.171206
                        ), new LatLng(37.869840, 128.167429
                        ), new LatLng(37.879596, 128.173266
                        ), new LatLng(37.883932, 128.171549
                        ), new LatLng(37.885287, 128.165713
                        ), new LatLng(37.894499, 128.161936
                        ), new LatLng(37.896666, 128.133440
                        ), new LatLng(37.897208, 128.112841
                        ), new LatLng(37.899646, 128.105975
                        ), new LatLng(37.897479, 128.090868
                        ), new LatLng(37.916441, 128.057223
                        ), new LatLng(37.919420, 128.041430
                        ), new LatLng(37.926733, 128.038340
                        ), new LatLng(37.931066, 128.040057
                        ), new LatLng(37.947041, 128.027354
                        ), new LatLng(37.940779, 128.010288
                        ), new LatLng(37.940532, 127.999324
                        ), new LatLng(37.943240, 127.993487
                        ), new LatLng(37.940532, 127.989024
                        ), new LatLng(37.927534, 127.983874
                        ), new LatLng(37.909659, 127.980098
                        ), new LatLng(37.906814, 127.980098
                        ), new LatLng(37.904647, 127.970141
                        ), new LatLng(37.901126, 127.967738
                        ), new LatLng(37.892998, 127.974605
                        ), new LatLng(37.882973, 127.973231
                        ), new LatLng(37.866170, 127.954692
                        ), new LatLng(37.856955, 127.941989
                        ), new LatLng(37.851533, 127.943019
                        ), new LatLng(37.846111, 127.940272
                        ), new LatLng(37.844756, 127.944735
                        ), new LatLng(37.841502, 127.949199
                        ), new LatLng(37.843942, 127.937182
                        ), new LatLng(37.832554, 127.944392
                        ), new LatLng(37.828758, 127.940616
                        ), new LatLng(37.830114, 127.931003
                        ), new LatLng(37.835808, 127.928599
                        ), new LatLng(37.836622, 127.919330
                        ), new LatLng(37.844756, 127.915896
                        ), new LatLng(37.845569, 127.896670
                        ), new LatLng(37.859394, 127.904910
                        ), new LatLng(37.864002, 127.892550
                        ), new LatLng(37.855328, 127.880877
                        ), new LatLng(37.848009, 127.881564
                        ), new LatLng(37.834181, 127.862681
                        ), new LatLng(37.835537, 127.846888
                        ), new LatLng(37.840146, 127.845172
                        ), new LatLng(37.846924, 127.830066
                        ), new LatLng(37.842858, 127.824572
                        ), new LatLng(37.842587, 127.817019
                        ), new LatLng(37.836893, 127.813929
                        ), new LatLng(37.820894, 127.831782
                        ), new LatLng(37.810045, 127.827319
                        ), new LatLng(37.805434, 127.834186
                        ), new LatLng(37.796481, 127.833842
                        ), new LatLng(37.791598, 127.848262
                        ), new LatLng(37.773147, 127.851008
                        ), new LatLng(37.769347, 127.857875
                        ), new LatLng(37.758220, 127.844485
                        ), new LatLng(37.755234, 127.837619
                        ), new LatLng(37.752248, 127.844142
                        ), new LatLng(37.742746, 127.831096
                        ), new LatLng(37.749533, 127.813929
                        ), new LatLng(37.746819, 127.804660
                        ), new LatLng(37.743832, 127.806376
                        ), new LatLng(37.743289, 127.799510
                        ), new LatLng(37.738674, 127.800197
                        ), new LatLng(37.737316, 127.788524
                        ), new LatLng(37.742203, 127.783717
                        ), new LatLng(37.738131, 127.758311
                        ), new LatLng(37.733787, 127.752475
                        ), new LatLng(37.741932, 127.743205
                        ), new LatLng(37.732972, 127.725352
                        ), new LatLng(37.732972, 127.718829
                        ), new LatLng(37.718308, 127.698230
                        ), new LatLng(37.712877, 127.679690
                        ), new LatLng(37.708259, 127.680034
                        ), new LatLng(37.701197, 127.683124
                        ), new LatLng(37.696307, 127.676257
                        ), new LatLng(37.700110, 127.657374
                        ), new LatLng(37.697937, 127.650165
                        ), new LatLng(37.684082, 127.650508
                        ), new LatLng(37.680006, 127.646045
                        ), new LatLng(37.682180, 127.640552
                        ), new LatLng(37.699500, 127.632716
                        ), new LatLng(37.693524, 127.623446
                        ), new LatLng(37.682928, 127.622760
                        ), new LatLng(37.678581, 127.619670
                        ), new LatLng(37.676135, 127.608684
                        ), new LatLng(37.697327, 127.611087
                        ), new LatLng(37.706020, 127.604907
                        ), new LatLng(37.704661, 127.597697
                        ), new LatLng(37.712810, 127.590144
                        ), new LatLng(37.719871, 127.592891
                        ), new LatLng(37.726932, 127.587054
                        ), new LatLng(37.733177, 127.573321
                        ), new LatLng(37.733177, 127.567485
                        ), new LatLng(37.728289, 127.564738
                        ), new LatLng(37.727475, 127.559245
                        ), new LatLng(37.720143, 127.562678
                        ), new LatLng(37.713896, 127.557528
                        ), new LatLng(37.711995, 127.559932
                        ), new LatLng(37.706834, 127.554095
                        ), new LatLng(37.669613, 127.547572
                        ), new LatLng(37.662004, 127.553752
                        ), new LatLng(37.659557, 127.546885
                        ), new LatLng(37.652491, 127.542766
                        ), new LatLng(37.650860, 127.535899
                        ), new LatLng(37.641345, 127.537616
                        ), new LatLng(37.638355, 127.548259
                        ), new LatLng(37.638355, 127.548259
                        ), new LatLng(37.634005, 127.576068
                        ), new LatLng(37.637267, 127.581904
                        ), new LatLng(37.642976, 127.584308
                        ), new LatLng(37.651131, 127.601817
                        ), new LatLng(37.650044, 127.609370
                        ), new LatLng(37.642161, 127.618983
                        ), new LatLng(37.639442, 127.620700
                        ), new LatLng(37.644064, 127.625850
                        ), new LatLng(37.639442, 127.629283
                        ), new LatLng(37.640530, 127.633403
                        ), new LatLng(37.636723, 127.631686
                        ), new LatLng(37.633733, 127.632373
                        ), new LatLng(37.636180, 127.636149
                        ), new LatLng(37.632101, 127.640269
                        ), new LatLng(37.632373, 127.645419
                        ), new LatLng(37.622584, 127.650226
                        ), new LatLng(37.624488, 127.662929
                        ), new LatLng(37.619593, 127.664302
                        ), new LatLng(37.599467, 127.697261
                        ), new LatLng(37.589402, 127.697947
                        ), new LatLng(37.586137, 127.706187
                        ), new LatLng(37.588553, 127.743980
                        ), new LatLng(37.591001, 127.752906
                        ), new LatLng(37.576581, 127.779685
                        ), new LatLng(37.581207, 127.788612
                        ), new LatLng(37.580118, 127.803718
                        ), new LatLng(37.575765, 127.801315
                        ), new LatLng(37.572500, 127.809555
                        ), new LatLng(37.564880, 127.812301
                        ), new LatLng(37.551000, 127.857276
                        ), new LatLng(37.556443, 127.872726
                        ), new LatLng(37.568146, 127.873413
                        ), new LatLng(37.571683, 127.882339
                        ), new LatLng(37.576853, 127.884742
                        ), new LatLng(37.579030, 127.887489
                        ), new LatLng(37.596986, 127.891609
                        ), new LatLng(37.608138, 127.925254
                        ), new LatLng(37.615481, 127.946540
                        ), new LatLng(37.632340, 127.958557
                        ), new LatLng(37.637234, 127.982589
                        ), new LatLng(37.628534, 127.989456
                        ), new LatLng(37.624727, 128.014862
                        ), new LatLng(37.617113, 128.012802
                        ), new LatLng(37.613033, 128.018295
                        ), new LatLng(37.621736, 128.028938
                        ), new LatLng(37.622008, 128.034088
                        ), new LatLng(37.624727, 128.040954
                        ), new LatLng(37.622008, 128.044387
                        ), new LatLng(37.627446, 128.044731
                        ), new LatLng(37.624727, 128.052627
                        ), new LatLng(37.632612, 128.065330
                        ), new LatLng(37.648380, 128.074600
                        ), new LatLng(37.653545, 128.096572
                        ), new LatLng(37.648924, 128.095886
                        ), new LatLng(37.645390, 128.098289
                        ), new LatLng(37.653001, 128.113052
                        ), new LatLng(37.672298, 128.107215
                        ), new LatLng(37.679091, 128.113739
                        ), new LatLng(37.682352, 128.133995
                        ), new LatLng(37.661155, 128.151161
                        ), new LatLng(37.666591, 128.162834
                        ), new LatLng(37.660340, 128.164894
                        ), new LatLng(37.647565, 128.189613
                        ), new LatLng(37.651914, 128.200256
                        ), new LatLng(37.627718, 128.230812
                        ), new LatLng(37.626902, 128.240081
                        ), new LatLng(37.633428, 128.270980
                        ), new LatLng(37.652729, 128.285057
                        ), new LatLng(37.677189, 128.275100
                        ), new LatLng(37.701369, 128.309776
                        ), new LatLng(37.690231, 128.321792
                        ), new LatLng(37.695393, 128.335525
                        ), new LatLng(37.700554, 128.341705
                        ), new LatLng(37.697837, 128.361618
                        ), new LatLng(37.689959, 128.370201
                        ), new LatLng(37.692676, 128.377067
                        ), new LatLng(37.688329, 128.407966
                        ), new LatLng(37.700011, 128.437149
                        ), new LatLng(37.740203, 128.459465
                        ), new LatLng(37.744276, 128.465988
                        ), new LatLng(37.736131, 128.486587
                        ), new LatLng(37.738303, 128.494483
                        ), new LatLng(37.736080, 128.496413))
                .strokeColor(0xFFF57F17)
                .fillColor(0x7FFFD740));
        HongchunPoly.setStrokeWidth(10);
        HoengseongPoly = googleMap.addPolygon(new PolygonOptions()
                .add(new LatLng(37.303461, 128.117432
                ), new LatLng(37.316022, 128.114342
                ), new LatLng(37.319572, 128.120521
                ), new LatLng(37.324486, 128.120521
                ), new LatLng(37.327762, 128.129105
                ), new LatLng(37.326670, 128.137344
                ), new LatLng(37.322029, 128.141121
                ), new LatLng(37.325578, 128.147644
                ), new LatLng(37.327762, 128.145584
                ), new LatLng(37.330219, 128.153480
                ), new LatLng(37.326397, 128.154167
                ), new LatLng(37.323667, 128.165840
                ), new LatLng(37.324486, 128.170990
                ), new LatLng(37.337863, 128.166870
                ), new LatLng(37.339773, 128.182320
                ), new LatLng(37.342776, 128.185753
                ), new LatLng(37.358059, 128.181633
                ), new LatLng(37.361061, 128.185066
                ), new LatLng(37.369793, 128.181633
                ), new LatLng(37.375795, 128.177856
                ), new LatLng(37.381252, 128.182320
                ), new LatLng(37.386162, 128.181976
                ), new LatLng(37.389708, 128.177170
                ), new LatLng(37.393800, 128.182320
                ), new LatLng(37.393800, 128.189186
                ), new LatLng(37.390800, 128.195023
                ), new LatLng(37.398710, 128.205322
                ), new LatLng(37.391072, 128.234505
                ), new LatLng(37.400073, 128.246521
                ), new LatLng(37.398982, 128.261970
                ), new LatLng(37.401437, 128.267807
                ), new LatLng(37.412891, 128.260254
                ), new LatLng(37.422162, 128.255447
                ), new LatLng(37.424889, 128.258537
                ), new LatLng(37.443699, 128.250297
                ), new LatLng(37.449696, 128.249954
                ), new LatLng(37.455964, 128.247208
                ), new LatLng(37.463050, 128.250297
                ), new LatLng(37.469862, 128.244118
                ), new LatLng(37.481033, 128.243774
                ), new LatLng(37.482123, 128.250641
                ), new LatLng(37.481851, 128.253731
                ), new LatLng(37.484575, 128.265060
                ), new LatLng(37.484751, 128.265841
                ), new LatLng(37.492923, 128.268244
                ), new LatLng(37.503274, 128.281977
                ), new LatLng(37.510082, 128.295367
                ), new LatLng(37.524515, 128.300860
                ), new LatLng(37.528327, 128.304637
                ), new LatLng(37.535133, 128.297770
                ), new LatLng(37.538673, 128.300517
                ), new LatLng(37.547656, 128.296397
                ), new LatLng(37.556638, 128.301890
                ), new LatLng(37.576232, 128.289874
                ), new LatLng(37.578408, 128.283351
                ), new LatLng(37.586299, 128.284381
                ), new LatLng(37.589291, 128.278544
                ), new LatLng(37.598268, 128.275111
                ), new LatLng(37.604252, 128.280261
                ), new LatLng(37.618667, 128.278887
                ), new LatLng(37.631991, 128.271334
                ), new LatLng(37.626281, 128.239749
                ), new LatLng(37.627912, 128.229106
                ), new LatLng(37.650749, 128.201640
                ), new LatLng(37.647215, 128.189624
                ), new LatLng(37.654011, 128.174517
                ), new LatLng(37.659719, 128.163874
                ), new LatLng(37.665698, 128.163188
                ), new LatLng(37.662165, 128.157695
                ), new LatLng(37.661621, 128.149798
                ), new LatLng(37.682274, 128.134005
                ), new LatLng(37.678198, 128.114779
                ), new LatLng(37.673851, 128.107913
                ), new LatLng(37.651293, 128.112033
                ), new LatLng(37.645312, 128.098300
                ), new LatLng(37.649118, 128.094866
                ), new LatLng(37.652380, 128.096240
                ), new LatLng(37.653195, 128.090747
                ), new LatLng(37.647759, 128.073580
                ), new LatLng(37.633350, 128.066027
                ), new LatLng(37.624105, 128.051265
                ), new LatLng(37.627640, 128.045771
                ), new LatLng(37.621930, 128.044055
                ), new LatLng(37.625737, 128.038562
                ), new LatLng(37.622746, 128.034442
                ), new LatLng(37.622202, 128.028605
                ), new LatLng(37.614044, 128.019335
                ), new LatLng(37.616219, 128.013156
                ), new LatLng(37.623018, 128.012126
                ), new LatLng(37.624649, 128.015902
                ), new LatLng(37.628184, 128.009036
                ), new LatLng(37.628456, 127.987406
                ), new LatLng(37.636885, 127.982600
                ), new LatLng(37.635797, 127.970584
                ), new LatLng(37.631719, 127.959254
                ), new LatLng(37.627912, 127.954104
                ), new LatLng(37.616219, 127.947238
                ), new LatLng(37.609964, 127.924578
                ), new LatLng(37.597452, 127.892649
                ), new LatLng(37.594460, 127.889559
                ), new LatLng(37.584394, 127.890933
                ), new LatLng(37.581401, 127.886813
                ), new LatLng(37.577048, 127.888873
                ), new LatLng(37.576504, 127.883036
                ), new LatLng(37.571334, 127.881663
                ), new LatLng(37.567524, 127.872393
                ), new LatLng(37.555821, 127.871363
                ), new LatLng(37.552011, 127.856944
                ), new LatLng(37.543845, 127.856257
                ), new LatLng(37.538128, 127.840808
                ), new LatLng(37.537311, 127.827761
                ), new LatLng(37.541123, 127.819178
                ), new LatLng(37.536495, 127.814715
                ), new LatLng(37.535406, 127.805445
                ), new LatLng(37.529688, 127.797549
                ), new LatLng(37.520431, 127.791369
                ), new LatLng(37.514984, 127.782443
                ), new LatLng(37.506270, 127.771800
                ), new LatLng(37.503818, 127.758754
                ), new LatLng(37.494557, 127.756350
                ), new LatLng(37.492378, 127.769740
                ), new LatLng(37.491833, 127.774546
                ), new LatLng(37.488020, 127.777293
                ), new LatLng(37.475760, 127.785533
                ), new LatLng(37.475760, 127.785533
                ), new LatLng(37.468130, 127.800296
                ), new LatLng(37.464043, 127.793086
                ), new LatLng(37.458593, 127.796176
                ), new LatLng(37.443875, 127.795146
                ), new LatLng(37.439241, 127.795489
                ), new LatLng(37.436515, 127.799609
                ), new LatLng(37.438150, 127.804759
                ), new LatLng(37.441149, 127.806475
                ), new LatLng(37.444147, 127.812999
                ), new LatLng(37.449053, 127.814372
                ), new LatLng(37.449599, 127.822612
                ), new LatLng(37.443329, 127.825015
                ), new LatLng(37.440058, 127.834628
                ), new LatLng(37.436515, 127.837718
                ), new LatLng(37.429972, 127.838748
                ), new LatLng(37.424246, 127.844928
                ), new LatLng(37.414430, 127.846987
                ), new LatLng(37.402158, 127.850764
                ), new LatLng(37.397522, 127.852824
                ), new LatLng(37.399703, 127.857630
                ), new LatLng(37.399703, 127.865870
                ), new LatLng(37.418248, 127.871363
                ), new LatLng(37.421247, 127.868273
                ), new LatLng(37.425064, 127.864497
                ), new LatLng(37.431607, 127.864497
                ), new LatLng(37.443057, 127.863467
                ), new LatLng(37.451779, 127.872393
                ), new LatLng(37.457230, 127.867930
                ), new LatLng(37.469220, 127.876170
                ), new LatLng(37.466768, 127.887843
                ), new LatLng(37.474125, 127.902606
                ), new LatLng(37.490744, 127.913249
                ), new LatLng(37.493195, 127.918742
                ), new LatLng(37.497009, 127.918055
                ), new LatLng(37.502729, 127.928698
                ), new LatLng(37.501095, 127.933848
                ), new LatLng(37.493195, 127.933161
                ), new LatLng(37.485568, 127.931445
                ), new LatLng(37.483116, 127.937281
                ), new LatLng(37.480392, 127.934878
                ), new LatLng(37.458593, 127.949298
                ), new LatLng(37.455322, 127.949641
                ), new LatLng(37.453142, 127.959597
                ), new LatLng(37.451506, 127.964404
                ), new LatLng(37.461318, 127.982943
                ), new LatLng(37.455867, 127.995303
                ), new LatLng(37.460228, 128.001826
                ), new LatLng(37.452869, 128.008006
                ), new LatLng(37.446600, 128.012126
                ), new LatLng(37.447418, 128.021395
                ), new LatLng(37.447146, 128.039248
                ), new LatLng(37.443602, 128.041651
                ), new LatLng(37.444692, 128.057788
                ), new LatLng(37.441421, 128.061564
                ), new LatLng(37.437060, 128.061564
                ), new LatLng(37.430244, 128.069804
                ), new LatLng(37.430244, 128.075297
                ), new LatLng(37.426155, 128.076670
                ), new LatLng(37.423701, 128.083537
                ), new LatLng(37.416339, 128.086970
                ), new LatLng(37.411430, 128.094180
                ), new LatLng(37.406522, 128.095553
                ), new LatLng(37.402431, 128.073237
                ), new LatLng(37.387975, 128.066371
                ), new LatLng(37.370514, 128.072551
                ), new LatLng(37.359873, 128.057788
                ), new LatLng(37.351413, 128.034098
                ), new LatLng(37.332579, 128.033412
                ), new LatLng(37.320294, 128.039935
                ), new LatLng(37.314014, 128.045771
                ), new LatLng(37.311829, 128.063624
                ), new LatLng(37.303637, 128.068087
                ), new LatLng(37.297082, 128.089030
                ), new LatLng(37.303461, 128.117432)).strokeColor(0xFFF57F17)
                .fillColor(0x7FFFD740));
        HoengseongPoly.setStrokeWidth(10);
    }

    public void setPolygon3() {
        HwachunPoly = googleMap.addPolygon(new PolygonOptions()
                .add(new LatLng(37.998164, 127.530286
                ), new LatLng(38.011014, 127.537152
                ), new LatLng(38.020210, 127.530972
                ), new LatLng(38.028594, 127.537496
                ), new LatLng(38.030082, 127.548310
                ), new LatLng(38.022374, 127.563931
                ), new LatLng(38.034003, 127.569596
                ), new LatLng(38.041709, 127.568223
                ), new LatLng(38.044278, 127.571999
                ), new LatLng(38.051713, 127.574918
                ), new LatLng(38.054957, 127.579553
                ), new LatLng(38.066040, 127.568395
                ), new LatLng(38.075770, 127.571656
                ), new LatLng(38.081851, 127.559640
                ), new LatLng(38.092660, 127.558095
                ), new LatLng(38.091714, 127.563760
                ), new LatLng(38.082392, 127.577664
                ), new LatLng(38.090025, 127.566935
                ), new LatLng(38.082594, 127.578265
                ), new LatLng(38.084486, 127.585475
                ), new LatLng(38.082189, 127.593715
                ), new LatLng(38.074892, 127.599894
                ), new LatLng(38.074216, 127.603156
                ), new LatLng(38.057593, 127.605388
                ), new LatLng(38.058269, 127.617061
                ), new LatLng(38.050699, 127.624099
                ), new LatLng(38.050835, 127.635085
                ), new LatLng(38.057593, 127.645900
                ), new LatLng(38.052051, 127.653624
                ), new LatLng(38.042994, 127.648646
                ), new LatLng(38.025282, 127.652423
                ), new LatLng(38.037045, 127.674739
                ), new LatLng(38.034476, 127.686755
                ), new LatLng(38.037586, 127.701518
                ), new LatLng(38.022577, 127.720916
                ), new LatLng(38.022036, 127.725894
                ), new LatLng(38.045157, 127.748382
                ), new LatLng(38.041642, 127.755591
                ), new LatLng(38.034882, 127.759540
                ), new LatLng(38.028527, 127.757995
                ), new LatLng(38.023659, 127.766234
                ), new LatLng(38.025146, 127.783400
                ), new LatLng(38.022983, 127.786147
                ), new LatLng(38.006618, 127.781169
                ), new LatLng(37.999855, 127.770526
                ), new LatLng(37.994714, 127.775332
                ), new LatLng(37.993091, 127.800223
                ), new LatLng(38.001072, 127.797133
                ), new LatLng(38.007971, 127.805888
                ), new LatLng(38.003642, 127.817218
                ), new LatLng(38.008782, 127.824084
                ), new LatLng(38.006077, 127.836272
                ), new LatLng(38.012705, 127.838675
                ), new LatLng(38.016086, 127.850863
                ), new LatLng(38.026634, 127.857215
                ), new LatLng(38.032042, 127.850863
                ), new LatLng(38.042318, 127.851035
                ), new LatLng(38.048807, 127.852408
                ), new LatLng(38.054755, 127.849490
                ), new LatLng(38.057458, 127.858073
                ), new LatLng(38.051105, 127.859103
                ), new LatLng(38.046914, 127.885539
                ), new LatLng(38.053944, 127.886912
                ), new LatLng(38.047590, 127.903048
                ), new LatLng(38.057863, 127.910602
                ), new LatLng(38.074216, 127.907855
                ), new LatLng(38.092457, 127.894637
                ), new LatLng(38.095024, 127.886912
                ), new LatLng(38.109748, 127.884166
                ), new LatLng(38.115286, 127.887256
                ), new LatLng(38.119067, 127.877471
                ), new LatLng(38.135407, 127.874896
                ), new LatLng(38.145262, 127.869574
                ), new LatLng(38.154307, 127.881076
                ), new LatLng(38.181569, 127.882621
                ), new LatLng(38.191149, 127.872321
                ), new LatLng(38.197085, 127.881076
                ), new LatLng(38.205314, 127.877814
                ), new LatLng(38.207202, 127.866313
                ), new LatLng(38.205449, 127.860305
                ), new LatLng(38.208821, 127.850520
                ), new LatLng(38.227163, 127.853953
                ), new LatLng(38.235523, 127.859103
                ), new LatLng(38.243208, 127.858416
                ), new LatLng(38.246714, 127.860133
                ), new LatLng(38.261003, 127.852408
                ), new LatLng(38.276771, 127.853438
                ), new LatLng(38.281622, 127.837645
                ), new LatLng(38.278388, 127.829062
                ), new LatLng(38.283778, 127.818419
                ), new LatLng(38.277445, 127.800567
                ), new LatLng(38.282700, 127.778766
                ), new LatLng(38.277175, 127.750613
                ), new LatLng(38.280140, 127.734477
                ), new LatLng(38.258711, 127.714736
                ), new LatLng(38.254263, 127.715766
                ), new LatLng(38.246714, 127.694823
                ), new LatLng(38.265855, 127.668731
                ), new LatLng(38.258981, 127.653796
                ), new LatLng(38.265451, 127.620665
                ), new LatLng(38.285665, 127.609336
                ), new LatLng(38.282835, 127.598006
                ), new LatLng(38.260059, 127.596118
                ), new LatLng(38.257768, 127.578265
                ), new LatLng(38.233770, 127.556807
                ), new LatLng(38.234714, 127.529342
                ), new LatLng(38.206798, 127.535865
                ), new LatLng(38.200053, 127.533118
                ), new LatLng(38.193038, 127.507884
                ), new LatLng(38.188720, 127.499301
                ), new LatLng(38.177926, 127.502047
                ), new LatLng(38.161731, 127.490889
                ), new LatLng(38.149312, 127.496554
                ), new LatLng(38.135271, 127.493808
                ), new LatLng(38.128385, 127.464625
                ), new LatLng(38.124199, 127.451922
                ), new LatLng(38.129600, 127.442653
                ), new LatLng(38.118392, 127.423083
                ), new LatLng(38.109613, 127.421367
                ), new LatLng(38.108667, 127.432696
                ), new LatLng(38.100022, 127.440078
                ), new LatLng(38.099887, 127.443854
                ), new LatLng(38.095159, 127.439219
                ), new LatLng(38.077730, 127.443854
                ), new LatLng(38.057728, 127.437674
                ), new LatLng(38.050159, 127.453811
                ), new LatLng(38.040560, 127.446601
                ), new LatLng(38.031772, 127.451579
                ), new LatLng(38.018926, 127.454669
                ), new LatLng(38.017303, 127.465655
                ), new LatLng(38.004454, 127.505481
                ), new LatLng(38.001884, 127.506854
                ), new LatLng(37.997961, 127.529342
                ), new LatLng(38.010946, 127.537238
                ), new LatLng(37.998164, 127.530286)).strokeColor(0xFFF57F17)
                .fillColor(0x7FFFD740));
        HwachunPoly.setStrokeWidth(10);
        InjePoly = googleMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(37.806573, 128.204161
                        ), new LatLng(37.823389, 128.225103
                        ), new LatLng(37.826643, 128.239180
                        ), new LatLng(37.834507, 128.247076
                        ), new LatLng(37.834507, 128.247076
                        ), new LatLng(37.858636, 128.258749
                        ), new LatLng(37.870833, 128.294111
                        ), new LatLng(37.845353, 128.305784
                        ), new LatLng(37.842641, 128.309904
                        ), new LatLng(37.831525, 128.305441
                        ), new LatLng(37.831525, 128.305441
                        ), new LatLng(37.834778, 128.332563
                        ), new LatLng(37.827457, 128.342863
                        ), new LatLng(37.837761, 128.365179
                        ), new LatLng(37.857009, 128.376852
                        ), new LatLng(37.858636, 128.399168
                        ), new LatLng(37.889530, 128.414618
                        ), new LatLng(37.894678, 128.426291
                        ), new LatLng(37.896033, 128.447920
                        ), new LatLng(37.929891, 128.468863
                        ), new LatLng(37.946679, 128.467146
                        ), new LatLng(37.956154, 128.475042
                        ), new LatLng(37.980242, 128.506628
                        ), new LatLng(38.004323, 128.503195
                        ), new LatLng(38.011357, 128.509375
                        ), new LatLng(38.028126, 128.512121
                        ), new LatLng(38.037861, 128.498388
                        ), new LatLng(38.054083, 128.489805
                        ), new LatLng(38.058408, 128.407751
                        ), new LatLng(38.093813, 128.390928
                        ), new LatLng(38.098406, 128.404661
                        ), new LatLng(38.117316, 128.418051
                        ), new LatLng(38.105700, 128.458220
                        ), new LatLng(38.132981, 128.475386
                        ), new LatLng(38.146752, 128.444487
                        ), new LatLng(38.164570, 128.443113
                        ), new LatLng(38.181573, 128.409468
                        ), new LatLng(38.198843, 128.430410
                        ), new LatLng(38.223931, 128.440024
                        ), new LatLng(38.236606, 128.427321
                        ), new LatLng(38.246583, 128.432814
                        ), new LatLng(38.263298, 128.407751
                        ), new LatLng(38.251976, 128.389555
                        ), new LatLng(38.237685, 128.382689
                        ), new LatLng(38.251706, 128.347326
                        ), new LatLng(38.257907, 128.359686
                        ), new LatLng(38.261681, 128.356253
                        ), new LatLng(38.261142, 128.315054
                        ), new LatLng(38.336584, 128.301664
                        ), new LatLng(38.353278, 128.262869
                        ), new LatLng(38.388539, 128.257032
                        ), new LatLng(38.393921, 128.223730
                        ), new LatLng(38.376429, 128.206564
                        ), new LatLng(38.354086, 128.155752
                        ), new LatLng(38.318539, 128.178755
                        ), new LatLng(38.307494, 128.166395
                        ), new LatLng(38.295639, 128.171888
                        ), new LatLng(38.283513, 128.167769
                        ), new LatLng(38.281357, 128.171888
                        ), new LatLng(38.277045, 128.165022
                        ), new LatLng(38.255481, 128.158842
                        ), new LatLng(38.254133, 128.144423
                        ), new LatLng(38.233370, 128.144423
                        ), new LatLng(38.220694, 128.125540
                        ), new LatLng(38.189129, 128.117300
                        ), new LatLng(38.178875, 128.098761
                        ), new LatLng(38.155931, 128.085371
                        ), new LatLng(38.138651, 128.085714
                        ), new LatLng(38.130280, 128.096014
                        ), new LatLng(38.104079, 128.099104
                        ), new LatLng(38.098136, 128.085371
                        ), new LatLng(38.068410, 128.115584
                        ), new LatLng(38.060841, 128.111120
                        ), new LatLng(38.048946, 128.120390
                        ), new LatLng(38.029207, 128.101164
                        ), new LatLng(38.035427, 128.078848
                        ), new LatLng(38.048135, 128.071295
                        ), new LatLng(38.051109, 128.060309
                        ), new LatLng(38.018930, 128.059622
                        ), new LatLng(38.021364, 128.045546
                        ), new LatLng(38.011898, 128.021513
                        ), new LatLng(37.999454, 128.013960
                        ), new LatLng(38.000536, 128.005034
                        ), new LatLng(37.986466, 127.986151
                        ), new LatLng(37.985925, 127.981001
                        ), new LatLng(37.982949, 127.980314
                        ), new LatLng(37.979972, 127.984778
                        ), new LatLng(37.975101, 127.984778
                        ), new LatLng(37.972394, 127.979284
                        ), new LatLng(37.964274, 127.976881
                        ), new LatLng(37.960214, 127.985121
                        ), new LatLng(37.942617, 127.989241
                        ), new LatLng(37.940181, 128.000570
                        ), new LatLng(37.946137, 128.026663
                        ), new LatLng(37.919058, 128.042456
                        ), new LatLng(37.897388, 128.089834
                        ), new LatLng(37.895220, 128.160559
                        ), new LatLng(37.885195, 128.165365
                        ), new LatLng(37.884111, 128.173605
                        ), new LatLng(37.871646, 128.166739
                        ), new LatLng(37.861617, 128.169485
                        ), new LatLng(37.857280, 128.176695
                        ), new LatLng(37.832067, 128.178412
                        ), new LatLng(37.823932, 128.191115
                        ), new LatLng(37.808743, 128.196264
                        ), new LatLng(37.806573, 128.204161)).strokeColor(0xFFF57F17)
                .fillColor(0x7FFFD740));
        InjePoly.setStrokeWidth(10);
    }

    public void setPolygon4() {
        PyeongChangPoly = googleMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(37.316498, 128.330384
                        ), new LatLng(37.322232, 128.331071
                        ), new LatLng(37.320048, 128.324547
                        ), new LatLng(37.347073, 128.323518
                        ), new LatLng(37.371087, 128.322831
                        ), new LatLng(37.375180, 128.326951
                        ), new LatLng(37.379545, 128.324891
                        ), new LatLng(37.378999, 128.316308
                        ), new LatLng(37.387456, 128.294678
                        ), new LatLng(37.399458, 128.290215
                        ), new LatLng(37.400549, 128.267213
                        ), new LatLng(37.412821, 128.257256
                        ), new LatLng(37.423455, 128.258630
                        ), new LatLng(37.479329, 128.244897
                        ), new LatLng(37.483960, 128.266526
                        ), new LatLng(37.511471, 128.297082
                        ), new LatLng(37.527537, 128.303948
                        ), new LatLng(37.551221, 128.297425
                        ), new LatLng(37.556393, 128.302918
                        ), new LatLng(37.578163, 128.284379
                        ), new LatLng(37.585237, 128.285409
                        ), new LatLng(37.589318, 128.278542
                        ), new LatLng(37.598023, 128.276826
                        ), new LatLng(37.603192, 128.280945
                        ), new LatLng(37.617878, 128.279572
                        ), new LatLng(37.632018, 128.272362
                        ), new LatLng(37.652407, 128.285065
                        ), new LatLng(37.676052, 128.276482
                        ), new LatLng(37.684747, 128.288155
                        ), new LatLng(37.700775, 128.309098
                        ), new LatLng(37.689637, 128.322488
                        ), new LatLng(37.694255, 128.338280
                        ), new LatLng(37.699960, 128.341370
                        ), new LatLng(37.696429, 128.363000
                        ), new LatLng(37.689909, 128.372269
                        ), new LatLng(37.688007, 128.412095
                        ), new LatLng(37.699145, 128.438531
                        ), new LatLng(37.743139, 128.468400
                        ), new LatLng(37.730378, 128.517838
                        ), new LatLng(37.748840, 128.539468
                        ), new LatLng(37.765398, 128.539468
                        ), new LatLng(37.779238, 128.528138
                        ), new LatLng(37.807454, 128.530541
                        ), new LatLng(37.814235, 128.544274
                        ), new LatLng(37.808267, 128.586503
                        ), new LatLng(37.767569, 128.594056
                        ), new LatLng(37.766212, 128.609849
                        ), new LatLng(37.772997, 128.610879
                        ), new LatLng(37.775982, 128.624612
                        ), new LatLng(37.781138, 128.625642
                        ), new LatLng(37.797145, 128.660317
                        ), new LatLng(37.786836, 128.683320
                        ), new LatLng(37.783580, 128.720399
                        ), new LatLng(37.749926, 128.745804
                        ), new LatLng(37.724947, 128.757134
                        ), new LatLng(37.679856, 128.763314
                        ), new LatLng(37.673606, 128.758164
                        ), new LatLng(37.656484, 128.739281
                        ), new LatLng(37.638543, 128.718682
                        ), new LatLng(37.631202, 128.728638
                        ), new LatLng(37.632018, 128.734818
                        ), new LatLng(37.627396, 128.731042
                        ), new LatLng(37.612167, 128.730355
                        ), new LatLng(37.611079, 128.720055
                        ), new LatLng(37.607815, 128.720055
                        ), new LatLng(37.608087, 128.723832
                        ), new LatLng(37.606727, 128.723832
                        ), new LatLng(37.605911, 128.719712
                        ), new LatLng(37.596663, 128.716622
                        ), new LatLng(37.593943, 128.705636
                        ), new LatLng(37.591767, 128.681603
                        ), new LatLng(37.579796, 128.674737
                        ), new LatLng(37.570544, 128.676110
                        ), new LatLng(37.569728, 128.667870
                        ), new LatLng(37.562108, 128.668214
                        ), new LatLng(37.558298, 128.675423
                        ), new LatLng(37.554488, 128.669930
                        ), new LatLng(37.558570, 128.665467
                        ), new LatLng(37.556937, 128.665467
                        ), new LatLng(37.556937, 128.663407
                        ), new LatLng(37.560203, 128.662034
                        ), new LatLng(37.559387, 128.659974
                        ), new LatLng(37.553127, 128.659630
                        ), new LatLng(37.555304, 128.656884
                        ), new LatLng(37.558842, 128.654824
                        ), new LatLng(37.554216, 128.654137
                        ), new LatLng(37.553943, 128.645898
                        ), new LatLng(37.547139, 128.634911
                        ), new LatLng(37.555304, 128.626328
                        ), new LatLng(37.563741, 128.625642
                        ), new LatLng(37.569728, 128.618775
                        ), new LatLng(37.574898, 128.616715
                        ), new LatLng(37.579796, 128.594056
                        ), new LatLng(37.559931, 128.590279
                        ), new LatLng(37.540878, 128.588906
                        ), new LatLng(37.534888, 128.577576
                        ), new LatLng(37.529715, 128.583070
                        ), new LatLng(37.521819, 128.584443
                        ), new LatLng(37.498398, 128.577233
                        ), new LatLng(37.492133, 128.584443
                        ), new LatLng(37.485459, 128.583072
                        ), new LatLng(37.475923, 128.571056
                        ), new LatLng(37.463661, 128.552173
                        ), new LatLng(37.465296, 128.520930
                        ), new LatLng(37.436951, 128.521617
                        ), new LatLng(37.418957, 128.510631
                        ), new LatLng(37.407231, 128.520244
                        ), new LatLng(37.402594, 128.528484
                        ), new LatLng(37.400685, 128.538097
                        ), new LatLng(37.372042, 128.556979
                        ), new LatLng(37.359763, 128.553889
                        ), new LatLng(37.353214, 128.548396
                        ), new LatLng(37.351576, 128.560069
                        ), new LatLng(37.345845, 128.565906
                        ), new LatLng(37.315543, 128.579982
                        ), new LatLng(37.310628, 128.586848
                        ), new LatLng(37.299431, 128.581012
                        ), new LatLng(37.293150, 128.584788
                        ), new LatLng(37.287960, 128.584788
                        ), new LatLng(37.281950, 128.595431
                        ), new LatLng(37.272935, 128.581699
                        ), new LatLng(37.279219, 128.573116
                        ), new LatLng(37.277853, 128.567966
                        ), new LatLng(37.287960, 128.553889
                        ), new LatLng(37.287414, 128.541873
                        ), new LatLng(37.283589, 128.536723
                        ), new LatLng(37.290418, 128.529857
                        ), new LatLng(37.284136, 128.519557
                        ), new LatLng(37.290145, 128.498271
                        ), new LatLng(37.304074, 128.496555
                        ), new LatLng(37.304074, 128.476985
                        ), new LatLng(37.307897, 128.472179
                        ), new LatLng(37.306532, 128.463939
                        ), new LatLng(37.311447, 128.458102
                        ), new LatLng(37.316908, 128.463596
                        ), new LatLng(37.321550, 128.463939
                        ), new LatLng(37.335200, 128.451236
                        ), new LatLng(37.339840, 128.433726
                        ), new LatLng(37.346664, 128.423427
                        ), new LatLng(37.341478, 128.415874
                        ), new LatLng(37.337929, 128.410037
                        ), new LatLng(37.329740, 128.412784
                        ), new LatLng(37.316635, 128.402484
                        ), new LatLng(37.313631, 128.406261
                        ), new LatLng(37.296700, 128.407977
                        ), new LatLng(37.295335, 128.402484
                        ), new LatLng(37.297519, 128.398021
                        ), new LatLng(37.291784, 128.389095
                        ), new LatLng(37.289052, 128.379481
                        ), new LatLng(37.290145, 128.371928
                        ), new LatLng(37.296154, 128.373302
                        ), new LatLng(37.297246, 128.365405
                        ), new LatLng(37.308443, 128.356822
                        ), new LatLng(37.316635, 128.335880)).strokeColor(0xFFF57F17)
                .fillColor(0x7FFFD740));
        PyeongChangPoly.setStrokeWidth(10);
        YangGuPoly = googleMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(38.353988, 128.156626
                        ), new LatLng(38.316825, 128.177225
                        ), new LatLng(38.255922, 128.160059
                        ), new LatLng(38.253765, 128.142893
                        ), new LatLng(38.234351, 128.142893
                        ), new LatLng(38.220327, 128.125040
                        ), new LatLng(38.189571, 128.117487
                        ), new LatLng(38.179317, 128.099634
                        ), new LatLng(38.157183, 128.084528
                        ), new LatLng(38.135583, 128.086588
                        ), new LatLng(38.129102, 128.096201
                        ), new LatLng(38.105873, 128.096888
                        ), new LatLng(38.096687, 128.084528
                        ), new LatLng(38.071826, 128.111994
                        ), new LatLng(38.061014, 128.111307
                        ), new LatLng(38.049660, 128.120234
                        ), new LatLng(38.030191, 128.100321
                        ), new LatLng(38.036681, 128.078348
                        ), new LatLng(38.050741, 128.070795
                        ), new LatLng(38.051822, 128.059122
                        ), new LatLng(38.031814, 128.055689
                        ), new LatLng(38.019914, 128.057749
                        ), new LatLng(38.020996, 128.042643
                        ), new LatLng(38.015587, 128.025477
                        ), new LatLng(38.020996, 128.011744
                        ), new LatLng(38.010177, 127.999384
                        ), new LatLng(38.001520, 127.999384
                        ), new LatLng(38.008013, 127.984278
                        ), new LatLng(38.032895, 127.983591
                        ), new LatLng(38.041548, 127.954066
                        ), new LatLng(38.020996, 127.945826
                        ), new LatLng(38.027487, 127.916300
                        ), new LatLng(38.027487, 127.904627
                        ), new LatLng(38.027487, 127.904627
                        ), new LatLng(38.094525, 127.895701
                        ), new LatLng(38.104792, 127.881281
                        ), new LatLng(38.119311, 127.876904
                        ), new LatLng(38.134571, 127.875015
                        ), new LatLng(38.144157, 127.869179
                        ), new LatLng(38.148612, 127.871067
                        ), new LatLng(38.153876, 127.881367
                        ), new LatLng(38.163055, 127.878964
                        ), new LatLng(38.171422, 127.884113
                        ), new LatLng(38.181543, 127.882054
                        ), new LatLng(38.185726, 127.873985
                        ), new LatLng(38.191123, 127.872784
                        ), new LatLng(38.197059, 127.880852
                        ), new LatLng(38.204748, 127.878449
                        ), new LatLng(38.207311, 127.865917
                        ), new LatLng(38.205288, 127.860424
                        ), new LatLng(38.207716, 127.854588
                        ), new LatLng(38.206772, 127.851841
                        ), new LatLng(38.208660, 127.850468
                        ), new LatLng(38.212707, 127.852528
                        ), new LatLng(38.226598, 127.853901
                        ), new LatLng(38.230508, 127.858021
                        ), new LatLng(38.243857, 127.858708
                        ), new LatLng(38.247766, 127.860253
                        ), new LatLng(38.260977, 127.853043
                        ), new LatLng(38.277150, 127.853729
                        ), new LatLng(38.292645, 127.867977
                        ), new LatLng(38.320798, 127.871754
                        ), new LatLng(38.331841, 127.884628
                        ), new LatLng(38.332110, 127.894413
                        ), new LatLng(38.320798, 127.948830
                        ), new LatLng(38.317701, 128.012001
                        ), new LatLng(38.310023, 128.053372
                        ), new LatLng(38.335073, 128.114311
                        ), new LatLng(38.336150, 128.127873
                        ), new LatLng(38.353988, 128.156626)).strokeColor(0xFFF57F17)
                .fillColor(0x7FFFD740));
        YangGuPoly.setStrokeWidth(10);
        JeongSunPoly = googleMap.addPolygon(new PolygonOptions()
                .add(
                        new LatLng(37.146623, 128.919552
                        ), new LatLng(37.144160, 128.907879
                        ), new LatLng(37.148812, 128.887623
                        ), new LatLng(37.164956, 128.878696
                        ), new LatLng(37.172616, 128.880413
                        ), new LatLng(37.177814, 128.872517
                        ), new LatLng(37.173163, 128.858097
                        ), new LatLng(37.164409, 128.852261
                        ), new LatLng(37.170428, 128.833035
                        ), new LatLng(37.187661, 128.832691
                        ), new LatLng(37.186020, 128.811062
                        ), new LatLng(37.192037, 128.810375
                        ), new LatLng(37.189029, 128.800419
                        ), new LatLng(37.190123, 128.792866
                        ), new LatLng(37.201062, 128.783596
                        ), new LatLng(37.206804, 128.759563
                        ), new LatLng(37.213093, 128.750637
                        ), new LatLng(37.212000, 128.734501
                        ), new LatLng(37.199968, 128.727634
                        ), new LatLng(37.197507, 128.714588
                        ), new LatLng(37.190943, 128.712185
                        ), new LatLng(37.185473, 128.696049
                        ), new LatLng(37.180549, 128.690212
                        ), new LatLng(37.186567, 128.675106
                        ), new LatLng(37.184379, 128.662746
                        ), new LatLng(37.190396, 128.658970
                        ), new LatLng(37.188755, 128.653820
                        ), new LatLng(37.183558, 128.651073
                        ), new LatLng(37.185199, 128.644894
                        ), new LatLng(37.185746, 128.638371
                        ), new LatLng(37.195319, 128.634251
                        ), new LatLng(37.196413, 128.629444
                        ), new LatLng(37.207351, 128.616055
                        ), new LatLng(37.212000, 128.621891
                        ), new LatLng(37.216921, 128.617771
                        ), new LatLng(37.217194, 128.603695
                        ), new LatLng(37.213914, 128.599918
                        ), new LatLng(37.219382, 128.583782
                        ), new LatLng(37.218561, 128.577946
                        ), new LatLng(37.229770, 128.576572
                        ), new LatLng(37.233870, 128.571423
                        ), new LatLng(37.246170, 128.563526
                        ), new LatLng(37.252729, 128.567303
                        ), new LatLng(37.253822, 128.561810
                        ), new LatLng(37.270490, 128.567646
                        ), new LatLng(37.270216, 128.573826
                        ), new LatLng(37.267757, 128.575886
                        ), new LatLng(37.268850, 128.581036
                        ), new LatLng(37.273495, 128.582066
                        ), new LatLng(37.282237, 128.595455
                        ), new LatLng(37.288792, 128.584812
                        ), new LatLng(37.299444, 128.580349
                        ), new LatLng(37.310368, 128.586872
                        ), new LatLng(37.325931, 128.570736
                        ), new LatLng(37.351862, 128.559406
                        ), new LatLng(37.352954, 128.549450
                        ), new LatLng(37.357048, 128.549793
                        ), new LatLng(37.359504, 128.553226
                        ), new LatLng(37.372055, 128.556660
                        ), new LatLng(37.400698, 128.537777
                        ), new LatLng(37.402335, 128.528164
                        ), new LatLng(37.406971, 128.521641
                        ), new LatLng(37.404789, 128.514431
                        ), new LatLng(37.409153, 128.517521
                        ), new LatLng(37.419242, 128.508938
                        ), new LatLng(37.421696, 128.513058
                        ), new LatLng(37.437236, 128.521297
                        ), new LatLng(37.465309, 128.520611
                        ), new LatLng(37.463674, 128.551853
                        ), new LatLng(37.476481, 128.567989
                        ), new LatLng(37.491465, 128.557346
                        ), new LatLng(37.497186, 128.565243
                        ), new LatLng(37.497730, 128.576572
                        ), new LatLng(37.517067, 128.578976
                        ), new LatLng(37.521969, 128.584812
                        ), new LatLng(37.530137, 128.583096
                        ), new LatLng(37.296986, 128.857067
                        ), new LatLng(37.291251, 128.874233
                        ), new LatLng(37.279232, 128.876636
                        ), new LatLng(37.277593, 128.884876
                        ), new LatLng(37.273495, 128.886250
                        ), new LatLng(37.265298, 128.880413
                        ), new LatLng(37.262839, 128.885906
                        ), new LatLng(37.242070, 128.900669
                        ), new LatLng(37.218561, 128.906162
                        ), new LatLng(37.212000, 128.917492
                        ), new LatLng(37.198601, 128.913372
                        ), new LatLng(37.196139, 128.907192
                        ), new LatLng(37.183832, 128.912685
                        ), new LatLng(37.168239, 128.911655
                        ), new LatLng(37.155927, 128.910282)).strokeColor(0xFFF57F17)
                .fillColor(0x7FFFD740));
        JeongSunPoly.setStrokeWidth(10);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
