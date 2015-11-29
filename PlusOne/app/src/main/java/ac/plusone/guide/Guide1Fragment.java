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


        myDataset.add(new MyData("1. 귀농길잡이", "7단계 준비절차", getString(R.string.st_guide1)));
        myDataset.add(new MyData("2. 귀농길잡이", "결심, 하셨나요 빨리 가서 시골서 고생하는 게 나아요", getString(R.string.st_guide5)));
        myDataset.add(new MyData("3. 귀농길잡이", "무턱대고 땅부터 산다고요 빌려 쓸 땅 많으니 ‘천천히’", getString(R.string.st_guide6)));
        myDataset.add(new MyData("4. 귀농길잡이", "귀농, 인생을 거는 일…생각보다 어려울 수 있습니다", getString(R.string.st_guide7)));
        myDataset.add(new MyData("5. 귀농길잡이", "돈 아닌 행복을 선택한 ‘자발적 가난’의 즐거움", getString(R.string.st_guide8)));
        myDataset.add(new MyData("6. 귀농길잡이", "‘농사는 자연이 짓는다’고 생각을 바꿔보세요", getString(R.string.st_guide9)));
        myDataset.add(new MyData("7. 귀농길잡이", "비용 아끼려고만 선택한 공동체라면…실패하기 십상", getString(R.string.st_guide10)));
        myDataset.add(new MyData("1. 지원정책", "귀농창업 및 주택구입", getString(R.string.st_guide2)));
        myDataset.add(new MyData("2. 지원정책", "현장실습지원사업", getString(R.string.st_guide3)));
        myDataset.add(new MyData("3. 지원정책", "도시민농촌유치지원사업 ", getString(R.string.st_guide4)));
        //myDataset.add(new MyData("#text3", "dd","내용3"));

        return v;
    }

}

