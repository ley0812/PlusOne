package ac.plusone.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ac.plusone.R;

/**
 * Created by MinJeong on 2015-11-16.
 */
public class RecyclerCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<CommentVO> commentList;
    private BoardVO board;
    private Context context;

    private JSONParser jParser;
    private JSONObject json;

    private ArrayList<BasicNameValuePair> nameValuePairs;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }

    public class ContentHolder extends ViewHolder {
        private TextView category, title, writer, date, content;

        public ContentHolder(View v) {
            super(v);
            this.category = (TextView) v.findViewById(R.id.content_category);
            this.title = (TextView) v.findViewById(R.id.content_title);
            this.writer = (TextView) v.findViewById(R.id.content_writer);
            this.date = (TextView) v.findViewById(R.id.content_date);
            this.content = (TextView) v.findViewById(R.id.content_content);
        }
    }

    public class CommentHolder extends ViewHolder {
        TextView writer, date, content;
        ImageButton delete;

        public CommentHolder(View v) {
            super(v);
            this.writer = (TextView) v.findViewById(R.id.comment_writer);
            this.date = (TextView) v.findViewById(R.id.comment_date);
            this.content = (TextView) v.findViewById(R.id.comment_content);
            this.delete = (ImageButton) v.findViewById(R.id.comment_delete);
        }
    }

    // Constructor
    public RecyclerCommentAdapter(BoardVO vo, ArrayList<CommentVO> commentList, Context context) {
        this.board = vo;
        this.commentList = commentList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        if(viewType == 0) {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.br_content_card, viewGroup, false);
            return new ContentHolder(v);
        } else {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.br_comment_card, viewGroup, false);
            return new CommentHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if(position==0) {
            ContentHolder holder = (ContentHolder) viewHolder;
            holder.category.setText(board.getCategory());
            holder.title.setText(board.getTitle());
            holder.writer.setText(board.getWriter());
            holder.date.setText(board.getDate());
            holder.content.setText(board.getContent());
        } else {
            CommentHolder commentHolder = (CommentHolder) viewHolder;
            commentHolder.writer.setText(commentList.get(position-1).getWriter());
            commentHolder.date.setText(commentList.get(position-1).getDate());
            commentHolder.content.setText(commentList.get(position-1).getContent());

            final int idx = position;

            SharedPreferences userPref = PreferenceManager.getDefaultSharedPreferences(context);
            String userID = userPref.getString("currentUser", "");
            if(!userID.equals("") && userID.equals(commentList.get(idx-1).getWriter_id())) {

            }else {
                commentHolder.delete.setVisibility(View.GONE);
            }

            commentHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nameValuePairs = new ArrayList<BasicNameValuePair>();
                    nameValuePairs.add(new BasicNameValuePair("num_comment",
                            commentList.get(idx - 1).getNum_comment()));
                    nameValuePairs.add(new BasicNameValuePair("position", String.valueOf(idx-1) ));

                    new CommentDeleteTask().execute();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return commentList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private class CommentDeleteTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            jParser = new JSONParser("CommentDeleteServlet", nameValuePairs);
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
                    result = insideObject.getString("commentDelete");
                }

                if (result.equals("Success")) {
                    String idx = nameValuePairs.get(1).getValue();
                    commentList.remove(Integer.parseInt(idx));
                    notifyItemRemoved(Integer.parseInt(idx));
                } else {
                    Toast.makeText(context, "comment delete fail", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException ne) {
                ne.printStackTrace();
            }
        }
    }

}

