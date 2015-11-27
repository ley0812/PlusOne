package ac.plusone.main;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import ac.plusone.R;

/**
 * Created by MinJeong on 2015-10-26.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private ArrayList<BoardVO> boardList;
    private LayoutInflater inflater;

    private int visibleThreshole = 3;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }

    public class ListViewHolder extends ViewHolder {
        TextView listTitle, listWriter, listContent;
        CardView cardView;

        public ListViewHolder(View v) {
            super(v);
            this.listTitle = (TextView) v.findViewById(R.id.list_title);
            this.listWriter = (TextView) v.findViewById(R.id.list_writer);
            this.listContent = (TextView) v.findViewById(R.id.list_content);
            this.cardView = (CardView) v.findViewById(R.id.card_view);
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }

    // Constructor
    public RecyclerAdapter(ArrayList<BoardVO> vo, LayoutInflater inflater,
                           RecyclerView recyclerView) {
        this.boardList = vo;
        this.inflater = inflater;

        if(recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager
                    = (LinearLayoutManager) recyclerView.getLayoutManager();

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                    if(!loading && totalItemCount <= (lastVisibleItem + visibleThreshole)) {
                        if(onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }

                        loading = true;
                    }
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder vh;

        if(viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.br_board_list_card, viewGroup, false);

            vh = new ListViewHolder(v);
        } else {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.br_progressbar_item, viewGroup, false);

            vh = new ProgressViewHolder(v);

        }

        return vh;
    }

    @Override
     public int getItemViewType(int position) {
        return boardList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        if(viewHolder instanceof ListViewHolder) {
            ((ListViewHolder) viewHolder).listTitle.setText(boardList.get(position).getTitle());
            ((ListViewHolder) viewHolder).listWriter.setText(boardList.get(position).getWriter());
            ((ListViewHolder) viewHolder).listContent.setText(boardList.get(position).getContent());

            final int index = position;

            ((ListViewHolder) viewHolder).cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(inflater.getContext(), BoardContentActivity.class);
                    intent.putExtra("boardContent", boardList.get(index));
                    inflater.getContext().startActivity(intent);
                }
            });
        } else {
            ((ProgressViewHolder) viewHolder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return boardList.size();
    }

    public void setLoaded() {
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


}