package ac.plusone.main;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

public class BoardUpdateActivity extends AppCompatActivity {

    private Spinner spinner;
    private Button update, cancel;
    private EditText title, content;

    private BoardVO board;

    private JSONParser jParser;
    private JSONObject json;

    private ArrayList<BasicNameValuePair> updateContentPair;

    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.br_activity_update);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        board = getIntent().getParcelableExtra("boardContent");

        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.update_coordinate);

        update = (Button)findViewById(R.id.btn_update);
        cancel = (Button)findViewById(R.id.btn_update_cancel);

        title = (EditText)findViewById(R.id.update_title);
        content = (EditText)findViewById(R.id.update_content);

        title.setText(board.getTitle());
        content.setText(board.getContent());

        spinner = (Spinner) findViewById(R.id.spinner_update);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.board_category, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        if(board.getCategory().equals("멘토")) {
            spinner.setSelection(0);
        }else if(board.getCategory().equals("멘티")) {
            spinner.setSelection(1);
        }else {
            spinner.setSelection(2);
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BoardUpdateActivity.this, BoardContentActivity.class);
                intent.putExtra("boardContent", board);
                startActivity(intent);
                finish();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateContentPair = new ArrayList<BasicNameValuePair>();
                updateContentPair.add(new BasicNameValuePair("title", title.getText().toString()));
                updateContentPair.add(new BasicNameValuePair("content", content.getText().toString()));
                updateContentPair.add(new BasicNameValuePair("category", spinner.getSelectedItem().toString()));
                updateContentPair.add(new BasicNameValuePair("num", String.valueOf(board.getNum())));

                new JsonLoadingTask().execute();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(BoardUpdateActivity.this, BoardContentActivity.class);
        intent.putExtra("boardContent", board);
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
            jParser = new JSONParser("BoardUpdateServlet", updateContentPair);
            json = jParser.getJSONText();
            return json;
        }

        @Override
        protected void onPostExecute(Object o) {
            JSONArray array = null;
            BoardVO vo = null;

            try {
                array = new JSONArray(json.getString("updateBoard"));
                for (int i = 0; i < array.length(); i++) {
                    JSONObject insideObject = array.getJSONObject(i);

                    vo = new BoardVO();
                    vo.setTitle(insideObject.getString("title"));
                    vo.setContent(insideObject.getString("content"));
                    vo.setWriter(insideObject.getString("writer"));
                    vo.setWriter_id(insideObject.getString("writer_id"));
                    vo.setCategory(insideObject.getString("category"));
                    vo.setDate(insideObject.getString("date"));
                    vo.setNum(insideObject.getInt("num"));
                }

                if(vo != null) {
                    Intent intent = new Intent(BoardUpdateActivity.this, BoardContentActivity.class);
                    intent.putExtra("boardContent", vo);
                    startActivity(intent);
                    finish();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "update Fail", Snackbar.LENGTH_LONG);
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
