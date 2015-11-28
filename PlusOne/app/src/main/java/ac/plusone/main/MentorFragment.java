package ac.plusone.main;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
 * Created by MinJeong on 2015-11-17.
 */
public class MentorFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private ArrayList<BoardVO> boardList;
    private Handler handler;

    private JSONParser jParser;
    private JSONObject json;
    private ArrayList<BasicNameValuePair> nameValuePairs;
    private ArrayList<BoardVO> mentorList;
    private String lastDate;

    int size=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.br_fragment_qna, container, false);

        handler = new Handler();

        Bundle args = getArguments();
        this.boardList = args.getParcelableArrayList("MentorList");
        lastDate = boardList.get(boardList.size()-1).getDate();

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setHasFixedSize(true);
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
                            boardList.add(mentorList.get(i - (start + 1)));
                            adapter.notifyItemInserted(boardList.size());
                        }
                        adapter.setLoaded();
                    }
                }, 2000);
            }
        });

        return rootView;
    }

    public static MentorFragment newInstance() {
        return new MentorFragment();
    }

    private class JsonLoadingTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            nameValuePairs = new ArrayList<BasicNameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("category", "멘토"));
            nameValuePairs.add(new BasicNameValuePair("lastDate", lastDate));

            jParser = new JSONParser("BoardMoreServlet", nameValuePairs);
            json = jParser.getJSONText();
            return json;
        }

        @Override
        protected void onPostExecute(Object o) {
            JSONArray array1 = null;
            mentorList = new ArrayList<BoardVO>();

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

                    mentorList.add(vo);
                }

                size = mentorList.size();
                lastDate = mentorList.get(size - 1).getDate();

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