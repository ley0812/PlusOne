package ac.plusone.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MyItem implements ClusterItem{
    private final LatLng mPosition;
    private int price;
    private String address;
    private String date;

    public MyItem(String address, int price, String date, double lat, double lng) {
        mPosition = new LatLng(lat, lng);
        this.address = address;
        this.price = price;
        this.date = date;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
}