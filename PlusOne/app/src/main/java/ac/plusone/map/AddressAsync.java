package ac.plusone.map;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddressAsync extends AsyncTask<String, String, ArrayList<Address>> {
    private ArrayList<Address> address;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        address = new ArrayList<Address>();
    }
    @Override
    protected ArrayList<Address> doInBackground(String... params) {
        try {
            JSONParser jsonParser = new JSONParser();
            String url = "http://52.69.147.247:8080/GangWon/gangwon.do?identification=price&location=" + params[0] + "&limit=" + params[1];
            url = url.replaceAll(" ", "%20");
            JSONObject jsonObject = jsonParser.getJSONFromUrl(url);
            JSONArray jsonArray = jsonObject.getJSONArray("jiga");
            for(int i=0 ; i<jsonArray.length() ; i++){
                JSONObject jObject = jsonArray.getJSONObject(i);
                Log.e("지가테이블에서 제이슨 받아오는거 : ", new String(jObject.getString("address").getBytes("8859_1"), "utf-8"));
                address.add(new Address(new String(jObject.getString("address").getBytes("8859_1"), "utf-8"),
                        jObject.getInt("price"),new String(jObject.getString("date").getBytes("8859_1"), "utf-8"), jObject.getDouble("latitude"), jObject.getDouble("longitude")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return address;
    }

    @Override
    protected void onPostExecute(final ArrayList<Address> lists) {

    }
}


