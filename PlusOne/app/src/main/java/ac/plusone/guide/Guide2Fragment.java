package ac.plusone.guide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ac.plusone.R;


public class Guide2Fragment extends Fragment {

    private View v;

    private ExpandableListAdapter listAdapter = null;
    private ExpandableListView expListView = null;
    private List<String> listDataHeader = new ArrayList<String>();
    private HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();

    private Spinner mSpinner;
    private ArrayList<String> mList;
    private BufferedReader reader;
    private ArrayList<CounselData> mData = new ArrayList<>();

    private List<String> e_list1 = null;
    private List<String> e_list2 = null;
    private List<String> e_list3 = null;
    private String local;
    private String gov;
    private String div;
    private String addr;
    private String call;
    int refresh_num = 0;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.g_activity_guide2_fragment, container, false);

        initSpinner(); //스피너 초기화
        initcsvfile(); //csv 초기화

        expListView = (ExpandableListView) v.findViewById(R.id.elv_list);

        setData();

        AdapterView.OnItemSelectedListener mItemSelectedListener = new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String strItem = mList.get(position);

                if (refresh_num != 0) {
                    listAdapter.get_listDataChild().clear();
                    //listAdapter.get_listDataChild().clear();
//                    listAdapter = new ExpandableListAdapter(v.getContext(), listDataHeader, listDataChild);
//                    expListView.setAdapter(listAdapter);
                    e_list1 = new ArrayList<String>();
                    e_list2 = new ArrayList<String>();
                    e_list3 = new ArrayList<String>();
                }

                for (int m = 0; m < mData.size(); m++) {
                    if (mData.get(m).LOCAL.equals(strItem)) {
                        local = mData.get(m).getLOCAL();
                        gov = mData.get(m).getGOV_NM();
                        div = mData.get(m).getDIV();
                        addr = mData.get(m).getADDR();
                        call = mData.get(m).getCALL_NUL();
                        if (mData.get(m).CTGRY_NM.equals("기술상담/농기계임대")) {
                            e_list1.add("지역 : " + local + "\n" + "기관명 : " + gov + "\n" + "주소 : " + addr + "\n" + "전화 : " + call);
                        } else if (mData.get(m).CTGRY_NM.equals("농지/주택")) {
                            e_list2.add("지역 : " + local + "\n" + "기관명 : " + gov + "\n" + "구분 : " + div + "\n" + "전화 : " + call);
                        } else if (mData.get(m).CTGRY_NM.equals("금융")) {
                            e_list3.add("지역 : " + local + "\n" + "기관명 : " + gov + "\n" + "주소 : " + addr + "\n" + "전화 : " + call);
                        }
                    }
                    listAdapter.notifyDataSetChanged();
                }

                listDataChild.put(listDataHeader.get(0), e_list1);
                listDataChild.put(listDataHeader.get(1), e_list2);
                listDataChild.put(listDataHeader.get(2), e_list3);

                listAdapter.notifyDataSetChanged();
                refresh_num++;


                expListView.setAdapter(listAdapter);
                listAdapter = new ExpandableListAdapter(v.getContext(), listDataHeader, listDataChild);

                //모든항목 열기
                int groupCount = (int) listAdapter.getGroupCount();
                for (int i = 0; i < groupCount; i++) {
                    expListView.expandGroup(i);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        mSpinner.setOnItemSelectedListener(mItemSelectedListener);

        listAdapter = new ExpandableListAdapter(v.getContext(), listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);


        // 그룹 Indiacator 삭제 (그룹 왼쪽에 기본제공되는 화살표 아이콘 삭제)
        expListView.setGroupIndicator(null);

        // 처음 시작시 그룹 모두 열기 (expandGroup)
        int groupCount = (int) listAdapter.getGroupCount();
        for (int i = 0; i < groupCount; i++) {
            expListView.expandGroup(i);
        }

        return v;
    }

    private void setData(){
        listDataHeader.add("기술상담/농기계임대");
        listDataHeader.add("농지/주택");
        listDataHeader.add("금융");

        e_list1 = new ArrayList<String>();
        e_list2 = new ArrayList<String>();
        e_list3 = new ArrayList<String>();

        listDataChild.put(listDataHeader.get(0), e_list1);
        listDataChild.put(listDataHeader.get(1), e_list2);
        listDataChild.put(listDataHeader.get(2), e_list3);

    }
    private ExpandableListView getExpandableListView() {
        return expListView;
    }

    public void initcsvfile() {
        StringBuffer str = new StringBuffer();
        try {
            //InputStream fin = getContext().getResources().openRawResource(R.raw.gangwon_counsel);
            InputStream fin = this.getResources().openRawResource(R.raw.gangwon_counsel);
            InputStreamReader in = new InputStreamReader(fin, "euc-kr");
            reader = new BufferedReader(in);
            String line = "";
            int row = 0, i;

            try {
                while ((line = reader.readLine()) != null) {
                    String[] token = line.split(",", -1);

                    for (i = 0; i < 6; i++) {
                        System.out.printf(token[i]);
                    }
                    mData.add(row, new CounselData());
                    mData.get(row).setCTGRY_NM(token[0]);
                    mData.get(row).setLOCAL(token[1]);
                    mData.get(row).setGOV_NM(token[2]);
                    mData.get(row).setDIV(token[3]);
                    mData.get(row).setADDR(token[4]);
                    mData.get(row).setCALL_NUL(token[5]);
                    System.out.println();
                    row++;

                    str.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            in.close();
            //mText.setText(str.toString());
        } catch (IOException e) {
            e.printStackTrace();
            //mText.setText("File error");
        }

        try {
            //mText.setText();
        } catch (Exception e) {
            //mText.setText("setText error");
        }
    }

    public void initSpinner() {
        String[] strTextList = {"강원도 강릉시", "강원도 강원도원", "강원도 고성군", "강원도 동해시", "강원도 삼척시",
                "강원도 속초시", "강원도 양구군", "강원도 양양군", "강원도 영월군", "강원도 원주시", "강원도 인제군",
                "강원도 정선군", "강원도 철원군", "강원도 춘천시", "강원도 태백시", "강원도 평창군",
                "강원도 홍천군", "강원도 화천군", "강원도 횡성군"};

        mList = new ArrayList<String>();
        for (int index = 0; index < 19; index++) {
            mList.add(strTextList[index]);
        }

        ArrayAdapter<String> adapter;

        //adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mList);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //mSpinner = (Spinner) v.findViewById(R.id.spinner1);
        mSpinner = (Spinner) v.findViewById(R.id.spinner1);
        mSpinner.setAdapter(adapter);
    }
}
