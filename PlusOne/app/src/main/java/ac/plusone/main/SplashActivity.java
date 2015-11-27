package ac.plusone.main;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ac.plusone.R;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_TIME=2000;

    private JSONParser jParser;
    private JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.br_activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new JsonLoadingTask().execute();
            }
        }, SPLASH_TIME);

    }

    private class JsonLoadingTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            jParser = new JSONParser("MainBoardServlet", null);
            json = jParser.getJSONText();
            return json;
        }

        @Override
        protected void onPostExecute(Object o) {

            JSONArray array1 = null;
            ArrayList<BoardVO> MainBoardList = new ArrayList<BoardVO>();

            try {
                array1 = new JSONArray(json.getString("MainBoard"));
                for (int i = 0; i < array1.length(); i++) {
                    JSONObject insideObject = array1.getJSONObject(i);

                    BoardVO vo = new BoardVO();
                    vo.setTitle(insideObject.getString("title"));
                    vo.setContent(insideObject.getString("content"));
                    vo.setWriter(insideObject.getString("writer"));
                    vo.setWriter_id(insideObject.getString("writer_id"));
                    vo.setCategory(insideObject.getString("category"));
                    vo.setDate(insideObject.getString("date"));
                    vo.setNum(insideObject.getInt("num"));

                    MainBoardList.add(vo);
                }

                overridePendingTransition(0, android.R.anim.fade_in);
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.putParcelableArrayListExtra("MainBoard", MainBoardList);
                startActivity(intent);
                finish();

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException ie) {
                ie.printStackTrace();
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            }
        }
    }

}
