package ac.plusone.guide;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ac.plusone.R;


/**
 * Created by jang on 2015-11-18.
 */
class GuideMyAdapter extends RecyclerView.Adapter<GuideMyAdapter.ViewHolder> {
    private ArrayList<MyData> mDataset;
    private Intent intent;
    private LayoutInflater inflater;
    private int cur_page;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        CardView cardView;
        public TextView tv_cardname;
        public TextView tv_cardsub;

        public ViewHolder(View view) {
            super(view);
            this.tv_cardname = (TextView)view.findViewById(R.id.tv_cardname);
            this.tv_cardsub = (TextView)view.findViewById(R.id.tv_cardsub);
            this.cardView = (CardView)view.findViewById(R.id.g_cardview);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public GuideMyAdapter(ArrayList<MyData> myDataset, LayoutInflater inflater , int cur_page) {
        this.mDataset = myDataset;
        this.inflater = inflater;
        this.cur_page = cur_page;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public GuideMyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.g_guide_card, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final int index = position;

        if(cur_page == 1){
            holder.tv_cardname.setText(mDataset.get(position).name);
            holder.tv_cardsub.setText(mDataset.get(position).namesub);

            holder.cardView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    intent = new Intent(inflater.getContext(), GuideContentview.class);
                    intent.putExtra("sub", mDataset.get(index).namesub);
                    intent.putExtra("text", mDataset.get(index).text);
                    inflater.getContext().startActivity(intent);
                }
            });

        } else if(cur_page == 2){


        } else if(cur_page == 3){

        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}