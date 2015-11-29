package ac.plusone.map;

import android.content.Context;
import android.content.Intent;
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

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Cluster<RealEstate> itemlist;
    private ArrayList<RealEstate> itemarray;
    Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }

    public class ListViewHolder extends ViewHolder {
        TextView listName, listAddress, master, phone_num;
        ImageView phone;
        CardView cardView;

        public ListViewHolder(View v) {
            super(v);
            this.listName = (TextView) v.findViewById(R.id.list_name);
            this.listAddress = (TextView) v.findViewById(R.id.list_address);
            this.master = (TextView) v.findViewById(R.id.master);
            this.phone = (ImageView) v.findViewById(R.id.phone);
            this.phone_num = (TextView) v.findViewById(R.id.phone_num);
            this.cardView = (CardView) v.findViewById(R.id.card_view);
        }
    }

    // Constructor
    public RecyclerAdapter(Cluster<RealEstate> vo,  Context context) {
        this.itemlist = vo;
        Collection<RealEstate> items = itemlist.getItems();
        itemarray = new ArrayList<>();
        for (RealEstate realEstate : items) {
            itemarray.add(realEstate);
        }
        this.context = context;
    }

    public RecyclerAdapter(RealEstate myItem, Context context) {
        itemarray = new ArrayList<>();
        itemarray.add(myItem);
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.map_item_layout, viewGroup, false);
        return new ListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        final ListViewHolder holder = (ListViewHolder) viewHolder;
//        holder.listTitle.setText(itemlist.get(position).get);
//        holder.listContent.setText(itemlist.get(position).getContent());

        final int index = position;

        ((ListViewHolder) viewHolder).listName.setText(itemarray.get(position).getName());
        ((ListViewHolder) viewHolder).listAddress.setText(itemarray.get(position).getAddress() + " " + itemarray.get(position).getDetail_address());
        ((ListViewHolder) viewHolder).phone_num.setText(itemarray.get(position).getPhoneNumber());
        ((ListViewHolder) viewHolder).master.setText(itemarray.get(position).getMaster());


        holder.phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone_num = itemarray.get(position).getPhoneNumber();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone_num));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemarray.size();
    }

}