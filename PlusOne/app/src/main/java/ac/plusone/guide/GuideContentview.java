package ac.plusone.guide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import ac.plusone.R;


public class GuideContentview extends AppCompatActivity {

    private TextView tv_content, tv_content_name;
    private Intent inIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.g_activity_guide_contentview);

        tv_content_name = (TextView) findViewById(R.id.tv_content_name);
        tv_content = (TextView) findViewById(R.id.tv_content);

        inIntent = getIntent();
        String mSubname = inIntent.getStringExtra("sub");
        String mContent = inIntent.getStringExtra("text");

        tv_content_name.setText(mSubname);
        tv_content.setText(mContent);
    }
}
