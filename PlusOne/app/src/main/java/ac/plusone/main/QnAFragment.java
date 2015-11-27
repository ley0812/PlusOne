package ac.plusone.main;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ac.plusone.R;

/**
 * Created by MinJeong on 2015-11-11.
 */
public class QnAFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private ArrayList<BoardVO> boardList;
    private Handler handler;

    private JSONParser jParser;
    private JSONObject json;
    private ArrayList<BasicNameValuePair> nameValuePairs;
    private ArrayList<BoardVO> QnAList;
    private String lastNum;
    int size = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.br_fragment_qna, container, false);

        handler = new Handler();

        Bundle args = getArguments();
        this.boardList = args.getParcelableArrayList("QnAList");
        lastNum = String.valueOf(boardList.get(boardList.size()-1).getNum());

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        adapter = new RecyclerAdapter(boardList, inflater, recyclerView);
        recyclerView.setAdapter(adapter);

        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                boardList.add(null);
                adapter.notifyItemInserted(boardList.size() - 1);

                new JsonLoadingTask().execute();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        boardList.remove(boardList.size() - 1);
                        adapter.notifyItemRemoved(boardList.size());

                        int start = boardList.size();
                        int end = start + size;

                        for (int i = start + 1; i <= end; i++) {
                            boardList.add(QnAList.get(i - (start + 1)));
                            adapter.notifyItemInserted(boardList.size());
                        }
                        adapter.setLoaded();

                    }
                }, 2000);
            }
        });

        return rootView;
    }

    public static QnAFragment newInstance() {
        return new QnAFragment();
    }


    private class JsonLoadingTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            nameValuePairs = new ArrayList<BasicNameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("category", "Q&A"));
            nameValuePairs.add(new BasicNameValuePair("num", lastNum));

            jParser = new JSONParser("BoardMoreServlet", nameValuePairs);
            json = jParser.getJSONText();
            return json;
        }

        @Override
        protected void onPostExecute(Object o) {
            JSONArray array1 = null;
            QnAList = new ArrayList<BoardVO>();

            try {
                array1 = new JSONArray(json.getString("Q&A"));
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

                    QnAList.add(vo);
                }

                size = QnAList.size();
                lastNum = String.valueOf(QnAList.get(size-1).getNum());

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            } catch(ArrayIndexOutOfBoundsException ae) {
                ae.printStackTrace();
            }
        }
    }


}
