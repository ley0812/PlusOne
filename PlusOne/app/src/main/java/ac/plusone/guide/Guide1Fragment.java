package ac.plusone.guide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ac.plusone.R;


public class Guide1Fragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<MyData> myDataset;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.g_activity_guide1_fragment, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        myDataset = new ArrayList<>();
        mAdapter = new GuideMyAdapter(myDataset, inflater, 1);
        mRecyclerView.setAdapter(mAdapter);


        myDataset.add(new MyData("길잡이 1", "7단계 준비절차", getString(R.string.st_guide1)));
        myDataset.add(new MyData("지원정책 1", "귀농창업 및 주택구입", getString(R.string.st_guide2)));
        myDataset.add(new MyData("지원정책 2", "현장실습지원사업", getString(R.string.st_guide3)));
        myDataset.add(new MyData("지원정책 3", "도시민농촌유치지원사업 ", getString(R.string.st_guide4)));
        //myDataset.add(new MyData("#text3", "dd","내용3"));

        return v;
    }

}

