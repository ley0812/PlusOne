package ac.plusone.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ac.plusone.R;

public class BoardActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private JSONParser jParser;
    private JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.br_activity_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager)findViewById(R.id.pager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        new JsonLoadingTask().execute();

        SharedPreferences userPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String userID = userPref.getString("currentUser", "");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userID.equals("")) {
                    Toast.makeText(BoardActivity.this, "로그인 해 주세요.", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(BoardActivity.this, BoardWriteActivity.class);
                    intent.putExtra("currentPage", viewPager.getCurrentItem());
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private class JsonLoadingTask extends AsyncTask {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(BoardActivity.this);
            dialog .getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setIndeterminate(true);
            dialog.setMessage("잠시만 기다려주세요.");
            dialog.show();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            jParser = new JSONParser("BoardServlet", null);
            json = jParser.getJSONText();
            return json;
        }

        @Override
        protected void onPostExecute(Object o) {
            if(dialog != null) dialog.dismiss();

            JSONArray array1 = null;
            JSONArray array2 = null;
            JSONArray array3 = null;
            ArrayList<BoardVO> MentorList = new ArrayList<BoardVO>();
            ArrayList<BoardVO> MenteeList = new ArrayList<BoardVO>();
            ArrayList<BoardVO> QnAList = new ArrayList<BoardVO>();

            try {
                array1 = new JSONArray(json.getString("Mentor"));
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

                    MentorList.add(vo);
                }

                array2 = new JSONArray(json.getString("Mentee"));
                for (int i = 0; i < array2.length(); i++) {
                    JSONObject insideObject = array2.getJSONObject(i);

                    BoardVO vo = new BoardVO();
                    vo.setTitle(insideObject.getString("title"));
                    vo.setContent(insideObject.getString("content"));
                    vo.setWriter(insideObject.getString("writer"));
                    vo.setWriter_id(insideObject.getString("writer_id"));
                    vo.setCategory(insideObject.getString("category"));
                    vo.setDate(insideObject.getString("date"));
                    vo.setNum(insideObject.getInt("num"));

                    MenteeList.add(vo);
                }

                array3 = new JSONArray(json.getString("QnA"));
                for (int i = 0; i < array3.length(); i++) {
                    JSONObject insideObject = array3.getJSONObject(i);

                    BoardVO vo = new BoardVO();
                    vo.setTitle(insideObject.getString("title"));
                    vo.setContent(insideObject.getString("content"));
                    vo.setWriter(insideObject.getString("writer"));
                    vo.setWriter_id(insideObject.getString("writer_id"));
                    vo.setCategory(insideObject.getString("category"));
                    vo.setDate(insideObject.getString("date"));
                    vo.setNum(insideObject.getInt("num"));

                    QnAList.add(vo);
                }

                if(!MentorList.isEmpty() && !MenteeList.isEmpty() && !QnAList.isEmpty()) {
                    Intent intent = new Intent();
                    intent.putParcelableArrayListExtra("MentorList", MentorList);
                    intent.putParcelableArrayListExtra("MenteeList", MenteeList);
                    intent.putParcelableArrayListExtra("QnAList", QnAList);

                    BoardFragmentAdapter adapter = new BoardFragmentAdapter(getSupportFragmentManager(), intent);
                    viewPager.setAdapter(adapter);
                    viewPager.setCurrentItem(getIntent().getIntExtra("currentPage", 0));
                    tabLayout.setupWithViewPager(viewPager);
                }

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
