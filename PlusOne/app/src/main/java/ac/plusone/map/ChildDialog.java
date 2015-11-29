package ac.plusone.map;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import ac.plusone.R;

public class ChildDialog extends Dialog{
    public ChildDialog(Context context, final Address addresses){
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.map_child_dialog);
        Button calcul = (Button)findViewById(R.id.calculate);
        Button cancel = (Button)findViewById(R.id.exit);
        final EditText editText = (EditText)findViewById(R.id.pyeng);
        TextView per = (TextView)findViewById(R.id.per);
        final TextView result = (TextView)findViewById(R.id.result);
        TextView addr = (TextView)findViewById(R.id.addr);
        addr.setText(addresses.getLocation());
        per.setText("지가 : " + addresses.getPrice());
        calcul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String str = editText.getText().toString();
                    int result_val = Integer.parseInt(str) * addresses.getPrice();
                    result.setText("총 지가 "+result_val+"");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
}