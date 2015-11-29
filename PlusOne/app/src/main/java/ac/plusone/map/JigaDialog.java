package ac.plusone.map;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import java.util.ArrayList;

import ac.plusone.R;

public class JigaDialog extends Dialog{
    RecyclerView recyclerView;
    public JigaDialog(Context context, ArrayList<Address> addresses){
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.map_dialog);
        recyclerView = (RecyclerView) findViewById(R.id.dialogRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        ac.plusone.map.JigaRecyclerAdapter adapter = new ac.plusone.map.JigaRecyclerAdapter(addresses, context);
        recyclerView.setAdapter(adapter);
        Button cancel = (Button)findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
}
