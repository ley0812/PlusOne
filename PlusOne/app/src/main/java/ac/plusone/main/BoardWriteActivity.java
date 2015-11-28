package ac.plusone.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ac.plusone.R;

public class BoardWriteActivity extends AppCompatActivity {

    private Spinner spinner;
    private Button write, cancel;
    private EditText title, content;
    private CoordinatorLayout coordinatorLayout;

    private JSONParser jParser;
    private JSONObject json;

    private ArrayList<BasicNameValuePair> nameValuePairs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.br_activity_write);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.write_coordinate);
        write = (Button)findViewById(R.id.btn_write);
        cancel = (Button)findViewById(R.id.btn_cancel);

        title = (EditText)findViewById(R.id.write_title);
        content = (EditText)findViewById(R.id.write_content);
        content.setMovementMethod(new ScrollingMovementMethod());

        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.board_category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BoardWriteActivity.this, BoardActivity.class);
                startActivity(intent);
                finish();
            }
        });

        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences userPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String userName = userPref.getString("currentUserName", "");
                String userID = userPref.getString("currentUser", "");

                nameValuePairs = new ArrayList<BasicNameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("writer", userName));
                nameValuePairs.add(new BasicNameValuePair("writer_id", userID));
                nameValuePairs.add(new BasicNameValuePair("title", title.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("content", content.getText().toString()));
                nameValuePairs.add(new BasicNameValuePair("category", spinner.getSelectedItem().toString()));

                new JsonLoadingTask().execute();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(BoardWriteActivity.this, BoardActivity.class);
        startActivity(intent);
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
        @Override
        protected Object doInBackground(Object[] params) {
            jParser = new JSONParser("BoardWriteServlet", nameValuePairs);
            json = jParser.getJSONText();
            return json;
        }

        @Override
        protected void onPostExecute(Object o) {
            JSONArray array = null;
            String result = "Fail";

            try {
                array = new JSONArray(json.getString("Result"));
                for (int i = 0; i < array.length(); i++) {
                    JSONObject insideObject = array.getJSONObject(i);
                    result = insideObject.getString("insertResult");
                }

                if(result.equals("Success")) {
                    Intent intent = new Intent(BoardWriteActivity.this, BoardActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "insert Fail.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            }
        }
    }

}
