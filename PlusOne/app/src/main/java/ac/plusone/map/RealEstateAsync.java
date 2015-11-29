package ac.plusone.map;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RealEstateAsync extends AsyncTask<Double, String, ArrayList<RealEstate>> {
    private ArrayList<RealEstate> myItem;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        myItem = new ArrayList<RealEstate>();
    }
    @Override
    protected ArrayList<RealEstate> doInBackground(Double... params) {
        try {
                JSONParser jsonParser = new JSONParser();
            String url = "http://52.69.147.247:8080/GangWon/gangwon.do?identification=realestate&min_latitude=" + params[0] + "&max_latitude=" + params[1]
                    + "&min_longitude=" + params[2] + "&max_longitude=" + params[3];
            JSONObject jsonObject = jsonParser.getJSONFromUrl(url);
            JSONArray jsonArray = jsonObject.getJSONArray("realestate");
            for(int i=0 ; i<jsonArray.length() ; i++){
                JSONObject jObject = jsonArray.getJSONObject(i);
                myItem.add(new RealEstate(new String(jObject.getString("name").getBytes("8859_1"),"utf-8"), new String(jObject.getString("address").getBytes("8859_1"),"utf-8"), new String(jObject.getString("detail_address").getBytes("8859_1"),"utf-8"),
                        new String(jObject.getString("road_address").getBytes("8859_1"),"utf-8"), new String(jObject.getString("master").getBytes("8859_1"),"utf-8"), new String(jObject.getString("phone").getBytes("8859_1"),"utf-8"),
                        jObject.getDouble("latitude"), jObject.getDouble("longitude")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myItem;
    }

    @Override
    protected void onPostExecute(final ArrayList<RealEstate> lists) {

    }
}


