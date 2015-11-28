package ac.plusone.map;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Admin on 2015-11-25.
 */
public class InMapAddressAsync extends AsyncTask<Double, String, ArrayList<MyItem>> {
    private ArrayList<MyItem> myItem;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        myItem = new ArrayList<MyItem>();
    }
    @Override
    protected ArrayList<MyItem> doInBackground(Double... params) {
        try {
            JSONParser jsonParser = new JSONParser();
            String url = "http://223.194.141.152:80/GangWon/gangwon.do?identification=map&min_latitude=" + params[0] + "&max_latitude=" + params[1]
                    + "&min_longitude=" + params[2] + "&max_longitude=" + params[3];
            JSONObject jsonObject = jsonParser.getJSONFromUrl(url);
            JSONArray jsonArray = jsonObject.getJSONArray("jiga");
            for(int i=0 ; i<jsonArray.length() ; i++){
                JSONObject jObject = jsonArray.getJSONObject(i);
                myItem.add(new MyItem(jObject.getString("address"),jObject.getInt("price"), jObject.getString("date"), jObject.getDouble("latitude"), jObject.getDouble("longitude")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myItem;
    }

    @Override
    protected void onPostExecute(final ArrayList<MyItem> lists) {

    }
}


