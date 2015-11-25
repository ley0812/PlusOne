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


public class Guide3Fragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<MyData3> myDataset;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.g_activity_guide3_fragment, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        myDataset = new ArrayList<>();
        mAdapter = new GuideMyAdapter3(myDataset, inflater);
        mRecyclerView.setAdapter(mAdapter);

        myDataset.add(new MyData3("교육1", "아는 것이 힘이다!", R.mipmap.edu01, "https://www.youtube.com/watch?v=bByrHo_UZZU"));
        myDataset.add(new MyData3("교육2", "정보수입은 이렇게!", R.mipmap.edu02, "https://www.youtube.com/watch?v=Bqp8cBLfaaI"));
        myDataset.add(new MyData3("교육3", "블루베리", R.mipmap.edu04, "https://www.youtube.com/watch?v=VK_7SZdrNZ0"));
        myDataset.add(new MyData3("교육4", "과일의 여왕 사과", R.mipmap.edu03, "https://www.youtube.com/watch?v=zjInuhYJ450"));
        myDataset.add(new MyData3("교육5", "귀농귀촌 이것부터 시작하자", R.mipmap.edu07, "https://www.youtube.com/watch?v=RmzgcP4Bpd8"));
        myDataset.add(new MyData3("행복한 귀농 노하우", "(농촌생활_임경수)", R.mipmap.edu05, "https://www.youtube.com/watch?v=sW-ZvtHECZg"));
        myDataset.add(new MyData3("부농의 꿈", "부농의 꿈", R.mipmap.edu06, "https://www.youtube.com/watch?v=jR76JckhBzs"));
        myDataset.add(new MyData3("귀농 건축학개론", "겨울이 따뜻한 집", R.mipmap.edu08, "https://www.youtube.com/watch?v=H5RGJrAcyPg"));
        myDataset.add(new MyData3("귀농귀촌을 위한 생태주택", "어떤 집에서 살 것인가?", R.mipmap.edu09, "https://www.youtube.com/watch?v=QbLQ2iU3ZV4"));
        myDataset.add(new MyData3("귀농의 기쁨", "귀농에서 기쁨을 찾다", R.mipmap.edu10, "https://www.youtube.com/watch?v=ChQcxt509dw"));
        myDataset.add(new MyData3("빌려쓰는 농기계", "농기계 임대은행", R.mipmap.edu11, "https://www.youtube.com/watch?v=sSf2KWvAvZk"));

        return v;
    }


}
