package ac.plusone.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.lang.reflect.Array;
import java.net.BindException;
import java.util.ArrayList;

/**
 * Created by MinJeong on 2015-11-11.
 */
public class BoardFragmentAdapter extends FragmentStatePagerAdapter {
    Intent intent;

    private String[] tabTitles = {"멘토", "멘티", "Q&A"};
    public BoardFragmentAdapter(FragmentManager fm, Intent intent) {
        super(fm);
        this.intent = intent;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        Bundle args = new Bundle();

        switch (position) {
            case 0:
                fragment = MentorFragment.newInstance();
                args.putParcelableArrayList("MentorList", intent.getParcelableArrayListExtra("MentorList"));
                fragment.setArguments(args);
                return fragment;
            case 1:
                fragment = MenteeFragment.newInstance();
                args.putParcelableArrayList("MenteeList", intent.getParcelableArrayListExtra("MenteeList"));
                fragment.setArguments(args);
                return fragment;
            case 2:
                fragment = QnAFragment.newInstance();
                args.putParcelableArrayList("QnAList", intent.getParcelableArrayListExtra("QnAList"));
                fragment.setArguments(args);
                return fragment;
            default:
                fragment = QnAFragment.newInstance();
                return fragment;
        }

    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }


}
