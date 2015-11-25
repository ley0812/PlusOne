package ac.plusone.guide;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by MinJeong on 2015-11-11.
 */
public class GuideFragmentAdapter extends FragmentStatePagerAdapter {

    Fragment[] fragments = new Fragment[3];

    public GuideFragmentAdapter(FragmentManager fm) {
        super(fm);
        fragments[0] = new Guide1Fragment();
        fragments[1] = new Guide2Fragment();
        fragments[2] = new Guide3Fragment();
    }

    public Fragment getItem(int arg0) {
        return fragments[arg0];
    }

    public int getCount() {
        return fragments.length;
    }

}

