package ac.plusone.guide;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import ac.plusone.R;


public class GuideActivity extends ActionBarActivity implements OnClickListener {

    Button btn[] = new Button[3];
    ViewPager viewPager = null;

    int p = 0;    //페이지번호
    int v = 1;    //화면 전환 뱡향


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_activity_guide);

        viewPager = (ViewPager) findViewById(R.id.g_viewPager);
        GuideFragmentAdapter adapter = new GuideFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        btn[0] = (Button) findViewById(R.id.btn_a);
        btn[1] = (Button) findViewById(R.id.btn_b);
        btn[2] = (Button) findViewById(R.id.btn_c);


        for (int i = 0; i < btn.length; i++) {
            btn[i].setOnClickListener(this);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_a:
                viewPager.setCurrentItem(0);
                break;

            case R.id.btn_b:
                viewPager.setCurrentItem(1);
                break;

            case R.id.btn_c:
                viewPager.setCurrentItem(2);
                break;

            default:
                break;
        }
    }
}



