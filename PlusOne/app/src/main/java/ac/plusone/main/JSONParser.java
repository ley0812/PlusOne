package ac.plusone.main;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by MinJeong on 2015-11-16.
 */
public class JSONParser {

    //final String url = "http://223.194.141.152:8080/PlusOne/";
    final String url = "http://172.30.104.250:8080/PlusOne/";
    //final String url = "http://192.168.1.8:8080/PlusOne/";

    String servlet;
    ArrayList<BasicNameValuePair> values;

    public JSONParser(String servlet, ArrayList<BasicNameValuePair> values) {
        this.servlet = servlet;
        this.values = values;
    }

    public JSONObject getJSONText() {
        JSONObject object = null;

        try {
            String uuu = url+servlet+getQuery(values);
            String line = getStringFromUrl(uuu);
            object = new JSONObject(line);

        }catch (JSONException e) {
            e.printStackTrace();
        }catch(Exception e) {
            e.printStackTrace();
        }

        return object;
    }

    private String getQuery(ArrayList<BasicNameValuePair> values) throws UnsupportedEncodingException {
        if(values == null) {
            return "";
        }

        StringBuffer result = new StringBuffer();
        boolean first = true;

        for(BasicNameValuePair pair : values) {
            if(first) {
                result.append("?");
                first = false;
            }
            else result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }


    public String getStringFromUrl(String url) throws UnsupportedEncodingException {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(getInputStreamFromUrl(url), "UTF-8"));
        StringBuffer sb = new StringBuffer();

        try {
            String line = null;
            while((line = br.readLine()) != null) {
                sb.append(line);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public static InputStream getInputStreamFromUrl(String url) {
        InputStream contentStream = null;

        try {

            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

            writer.write(url);
            writer.flush();
            writer.close();
            os.close();

            contentStream = new BufferedInputStream(conn.getInputStream());

        }catch(Exception e) {
            e.printStackTrace();
        }
        return contentStream;
    }
}
