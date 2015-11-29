package ac.plusone.map;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class RealEstateSearchAsync extends AsyncTask<String, String, ArrayList<RealEstate>> {
    private ArrayList<RealEstate> myItem;
    private Context mContext;
    private ProgressDialog dialog;

    public RealEstateSearchAsync(Context mContext){
        this.mContext = mContext;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(mContext);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setIndeterminate(true);
        dialog.setMessage("검색중 입니다.");
        dialog.show();
        myItem = new ArrayList<RealEstate>();
    }
    @Override
    protected ArrayList<RealEstate> doInBackground(String... params) {
        try {
            JSONParser jsonParser = new JSONParser();
            String url = "http://113.198.82.72/GangWon/gangwon.do?identification=realestatesearch&keyword="+params[0]+"&limit="+params[1];
            url = url.replaceAll(" ", "%20");
            JSONObject jsonObject = jsonParser.getJSONFromUrl(url);
            JSONArray jsonArray = jsonObject.getJSONArray("realestate");
            for(int i=0 ; i<jsonArray.length() ; i++){
                JSONObject jObject = jsonArray.getJSONObject(i);
                Log.e("제이슨 받아오는거 : ", new String(jObject.getString("name").getBytes("8859_1"),"euc-kr"));
                myItem.add(new RealEstate(new String(jObject.getString("name").getBytes("8859_1"),"euc-kr"), new String(jObject.getString("address").getBytes("8859_1"),"euc-kr"), new String(jObject.getString("detail_address").getBytes("8859_1"),"euc-kr"),
                        new String(jObject.getString("road_address").getBytes("8859_1"),"euc-kr"), new String(jObject.getString("master").getBytes("8859_1"),"euc-kr"), new String(jObject.getString("phone").getBytes("8859_1"),"euc-kr"),
                        jObject.getDouble("latitude"), jObject.getDouble("longitude")));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return myItem;
    }

    @Override
    protected void onPostExecute(final ArrayList<RealEstate> lists) {
        if(dialog!=null) {
            dialog.dismiss();
        }
    }
}


