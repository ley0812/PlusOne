package ac.plusone.map;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ac.plusone.R;

/**
 * Created by Admin on 2015-11-20.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<MyItem> itemlist;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }

    public class ListViewHolder extends ViewHolder{
        TextView listTitle, listContent;
        CardView cardView;

        public ListViewHolder(View v){
            super(v);
            this.listTitle = (TextView) v.findViewById(R.id.list_title);
            this.listContent = (TextView) v.findViewById(R.id.list_content);
            this.cardView = (CardView) v.findViewById(R.id.card_view);
        }
    }

    // Constructor
    public RecyclerAdapter(ArrayList<MyItem> vo) {
        this.itemlist = vo;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.map_item_layout, viewGroup, false);
        return new ListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        final ListViewHolder holder = (ListViewHolder) viewHolder;
//        holder.listTitle.setText(itemlist.get(position).get);
//        holder.listContent.setText(itemlist.get(position).getContent());

        final int index = position;

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemlist.size();
    }


}