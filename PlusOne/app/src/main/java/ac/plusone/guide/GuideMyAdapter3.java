package ac.plusone.guide;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ac.plusone.R;


/**
 * Created by jang on 2015-11-18.
 */
class GuideMyAdapter3 extends RecyclerView.Adapter<GuideMyAdapter3.ViewHolder> {
    private ArrayList<MyData3> mDataset;
    private Intent intent;
    private LayoutInflater inflater;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        public TextView tv_cardname;
        public TextView tv_cardtext;
        public ImageView Iv_cardimg;
        public ImageButton btn_play;

        public ViewHolder(View view) {
            super(view);
            this.cardView = (CardView) view.findViewById(R.id.g_cardview3);

            tv_cardname = (TextView) view.findViewById(R.id.tv_cardname3);
            tv_cardtext = (TextView) view.findViewById(R.id.tv_cardtext3);
            Iv_cardimg = (ImageView) view.findViewById(R.id.g_image3);
            btn_play = (ImageButton) view.findViewById(R.id.btn_play);

        }
    }

    public GuideMyAdapter3(ArrayList<MyData3> myDataset, LayoutInflater inflater) {
        this.mDataset = myDataset;
        this.inflater = inflater;

    }

    @Override
    public GuideMyAdapter3.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.g_guide3_card, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int index = position;

        holder.tv_cardname.setText(mDataset.get(position).name);
        holder.tv_cardtext.setText(mDataset.get(position).text);
        holder.Iv_cardimg.setImageResource(mDataset.get(position).img);

        holder.btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mDataset.get(index).url));
                inflater.getContext().startActivity(myIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
