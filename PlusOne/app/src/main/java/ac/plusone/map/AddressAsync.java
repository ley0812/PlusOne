package ac.plusone.map;

import android.os.AsyncTask;

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
            String url = "http://223.194.135.12:80/GangWon/gangwon.do?identification=price&location=" + params[0] + "&limit=" + params[1];
            JSONObject jsonObject = jsonParser.getJSONFromUrl(url);
            JSONArray jsonArray = jsonObject.getJSONArray("jiga");
            for(int i=0 ; i<jsonArray.length() ; i++){
                JSONObject jObject = jsonArray.getJSONObject(i);
                address.add(new Address(jObject.getString("address"),jObject.getInt("price"), jObject.getString("date"), jObject.getDouble("latitude"), jObject.getDouble("longitude")));
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


