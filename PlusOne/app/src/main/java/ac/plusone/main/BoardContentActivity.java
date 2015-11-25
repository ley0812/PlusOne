package ac.plusone.main;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import ac.plusone.R;

public class BoardContentActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private RecyclerCommentAdapter adapter;
    private ArrayList<CommentVO> commentList;
    private CoordinatorLayout coordinatorLayout;
    private BoardVO board;
    private CommentVO newComment;
    AlertDialog.Builder builder;

    private JSONParser jParser;
    private JSONObject json;

    private ArrayList<BasicNameValuePair> numValuePair;
    private ArrayList<BasicNameValuePair> nameValuePairs;

    private EditText edtComment;
    private Button btnComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.br_activity_board_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.content_toolbar);

        edtComment = (EditText)findViewById(R.id.edit_comment);
        edtComment.setMovementMethod(new ScrollingMovementMethod());
        btnComment = (Button)findViewById(R.id.btn_comment);

        board = getIntent().getParcelableExtra("boardContent");
        numValuePair = new ArrayList<BasicNameValuePair>();
        numValuePair.add(new BasicNameValuePair("num", String.valueOf(board.getNum())));

        toolbar.setTitle(board.getTitle());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.content_coordinate);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_comment);

        new CommentLoadingTask().execute();

        btnComment.setOnClickListener(this);

        builder = new AlertDialog.Builder(BoardContentActivity.this);
        builder.setTitle("해당 글을 삭제하시겠습니까?");
        builder.setMessage("");

        builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new ContentDeleteTask().execute();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.content_menu, menu);

        SharedPreferences userPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String userID = userPref.getString("currentUser", "");

        if(!userID.equals("") && board.getWriter_id().equals(userID)) {

        }else {
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.content_update:
                Intent intent = new Intent(BoardContentActivity.this, BoardUpdateActivity.class);
                intent.putExtra("boardContent", board);
                startActivity(intent);
                finish();
                break;
            case R.id.content_delete:
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        SharedPreferences userPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String userID = userPref.getString("currentUser", "");
        if(!userID.equals("")){
            newComment = new CommentVO();
            nameValuePairs = new ArrayList<BasicNameValuePair>();
            String userName = userPref.getString("currentUserName", "");
            nameValuePairs.add(new BasicNameValuePair("num", String.valueOf(board.getNum()) ));
            nameValuePairs.add(new BasicNameValuePair("writer", userName));
            nameValuePairs.add(new BasicNameValuePair("writer_id", userID));
            nameValuePairs.add(new BasicNameValuePair("content", edtComment.getText().toString()));

            newComment.setWriter_id(userID);
            newComment.setWriter(userName);
            newComment.setContent(edtComment.getText().toString());
            newComment.setDate(new Date().toString());

            new CommentWriteTask().execute();
        }else {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, getString(R.string.please_login), Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }


    private class CommentLoadingTask extends AsyncTask {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(BoardContentActivity.this);
            dialog .getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setIndeterminate(true);
            dialog.setMessage("잠시만 기다려주세요.");
            dialog.show();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            jParser = new JSONParser("CommentServlet", numValuePair);
            json = jParser.getJSONText();
            return json;
        }

        @Override
        protected void onPostExecute(Object o) {
            dialog.dismiss();

            JSONArray array = null;
            commentList = new ArrayList<CommentVO>();

            try {
                array = new JSONArray(json.getString("comment"));
                for (int i = 0; i < array.length(); i++) {
                    JSONObject insideObject = array.getJSONObject(i);

                    CommentVO vo = new CommentVO();
                    vo.setNum_comment(insideObject.getString("num_comment"));
                    vo.setContent(insideObject.getString("content"));
                    vo.setWriter(insideObject.getString("writer"));
                    vo.setWriter_id(insideObject.getString("writer_id"));
                    vo.setDate(insideObject.getString("date"));

                    commentList.add(vo);
                }

                recyclerView.setLayoutManager(new LinearLayoutManager(getLayoutInflater().getContext()));
                adapter = new RecyclerCommentAdapter(board, commentList, getApplicationContext());
                recyclerView.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            }
        }
    }

    private class ContentDeleteTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            jParser = new JSONParser("BoardDeleteServlet", numValuePair);
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
                    Intent intent = new Intent(BoardContentActivity.this, BoardActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "delete Fail", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            }
        }
    }

    private class CommentWriteTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            jParser = new JSONParser("CommentWriteServlet", nameValuePairs);
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
                    edtComment.setText("");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edtComment.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    commentList.add(newComment);
                    adapter.notifyItemChanged(commentList.size());
                    recyclerView.smoothScrollToPosition(commentList.size());
                } else {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "comment write Fail", Snackbar.LENGTH_LONG);
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
