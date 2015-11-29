package ac.plusone.map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.maps.android.clustering.Cluster;

import java.util.ArrayList;
import java.util.Collection;

import ac.plusone.R;
import ac.plusone.main.MapActivity;

public class JigaRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Address> itemarray;
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }

    public class ListViewHolder extends ViewHolder {
        TextView land_name, land_sage, land_sage_avr;
        ImageView calculator;
        CardView cardView;

        public ListViewHolder(View v) {
            super(v);
            this.land_name = (TextView) v.findViewById(R.id.land_name);
            this.land_sage = (TextView) v.findViewById(R.id.land_sage);
            this.land_sage_avr = (TextView) v.findViewById(R.id.land_sage_avr);
            this.calculator = (ImageView)v.findViewById(R.id.calculator);
        }
    }

    // Constructor
    public JigaRecyclerAdapter(ArrayList<Address> vo, Context context) {
        this.itemarray = vo;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.map_jigaitem_layout, viewGroup, false);
        return new ListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        final ListViewHolder holder = (ListViewHolder) viewHolder;

        final int index = position;

        ((ListViewHolder) viewHolder).land_name.setText(itemarray.get(position).getLocation());
        ((ListViewHolder) viewHolder).land_sage.setText("지가 : "+itemarray.get(position).getPrice()+"  ");
        double sum = 0.0f;
        for(int i=0 ; i<itemarray.size() ; i++){
            sum = sum + itemarray.get(i).getPrice();
        }
        double avr = sum/itemarray.size();
        ((ListViewHolder) viewHolder).land_sage_avr.setText("근방평균지가 : "+(int)(avr));

        holder.calculator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChildDialog dialog = new ChildDialog(context, itemarray.get(position));
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemarray.size();
    }

}
