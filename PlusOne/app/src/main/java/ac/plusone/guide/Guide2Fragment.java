package ac.plusone.guide;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ac.plusone.R;


public class Guide2Fragment extends Fragment {

    private Spinner mSpinner;
    private TextView mText;
    private ArrayList<String> mList;
    private View v;
    private BufferedReader reader;
    private ArrayList<CounselData> mData = new ArrayList<>();

    private ListView mListView1 = null;
    private ListViewAdapter mAdapter1 = null;
    private ListView mListView2 = null;
    private ListViewAdapter2 mAdapter2 = null;
    private ListView mListView3 = null;
    private ListViewAdapter3 mAdapter3 = null;
    private ScrollView scrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.g_activity_guide2_fragment, container, false);
        //View header = inflater.inflate(R.layout.g_guide2_list_header, null, false);


        mListView1 = (ListView) v.findViewById(R.id.Listview_sub1);
        mAdapter1 = new ListViewAdapter(getActivity());
        mListView2 = (ListView) v.findViewById(R.id.Listview_sub2);
        mAdapter2 = new ListViewAdapter2(getActivity());
        mListView3 = (ListView) v.findViewById(R.id.Listview_sub3);
        mAdapter3 = new ListViewAdapter3(getActivity());

        initSpinner();
        initcsvfile();

        AdapterView.OnItemSelectedListener mItemSelectedListener = new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String strItem = mList.get(position);

                mAdapter1.getData().clear();
                mAdapter2.getData().clear();
                mAdapter3.getData().clear();

                for(int m = 0; m<mData.size(); m++){
                    if(mData.get(m).LOCAL.equals(strItem)){
                        if(mData.get(m).CTGRY_NM.equals("기술상담/농기계임대")){
                            mAdapter1.addItem(mData.get(m).getLOCAL(), mData.get(m).getGOV_NM(), mData.get(m).getADDR(), mData.get(m).getCALL_NUL());
                        }else if(mData.get(m).CTGRY_NM.equals("농지/주택")){
                            mAdapter2.addItem(mData.get(m).getLOCAL(), mData.get(m).getGOV_NM(), mData.get(m).getDIV(), mData.get(m).getCALL_NUL());
                        }else if(mData.get(m).CTGRY_NM.equals("금융")){
                            mAdapter3.addItem(mData.get(m).getLOCAL(), mData.get(m).getGOV_NM(), mData.get(m).getADDR(), mData.get(m).getCALL_NUL());
                        }
                    }
                }
                mAdapter1.notifyDataSetChanged();
                mAdapter2.notifyDataSetChanged();
                mAdapter3.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //mText.setText("NoItem Selected.");
            }
        };

        mSpinner.setOnItemSelectedListener(mItemSelectedListener);

        mListView1.setAdapter(mAdapter1);
        mListView2.setAdapter(mAdapter2);
        mListView3.setAdapter(mAdapter3);



        //scrollView = (ScrollView)v.findViewById(R.id.scrollView2);
//        mListView1.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                scrollView.requestDisallowInterceptTouchEvent(true);
//                return false;
//            }
//        });
//        mListView2.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                scrollView.requestDisallowInterceptTouchEvent(true);
//                return false;
//            }
//        });
//        mListView3.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                scrollView.requestDisallowInterceptTouchEvent(true);
//                return false;
//            }
//        });

        listViewHeightSet(mAdapter1,mListView1);
        listViewHeightSet(mAdapter2,mListView2);
        listViewHeightSet(mAdapter3,mListView3);

//        setListViewHeightBasedOnChildren(mListView1);
//        setListViewHeightBasedOnChildren(mListView2);
//        setListViewHeightBasedOnChildren(mListView3);

        return v;
    }
    private void listViewHeightSet(BaseAdapter listAdapter, ListView listView)
    {
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++)
        {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public void initcsvfile(){
        StringBuffer str = new StringBuffer();
        try {
            InputStream fin = getContext().getResources().openRawResource(R.raw.gangwon_counsel);
            InputStreamReader in = new InputStreamReader(fin, "euc-kr");
            reader = new BufferedReader(in);
            String line = "";
            int row = 0, i;

            try {
                while ((line = reader.readLine()) != null) {
                    String[] token = line.split(",",-1);

                    for (i = 0; i < 6; i++){
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
            mText.setText("File error");
        }

        try {
            //mText.setText();
        } catch (Exception e) {
            mText.setText("setText error");
        }
    }

    public void initSpinner() {
        String[] strTextList = {"강원도 강릉시", "강원도 강원도원", "강원도 고성군", "강원도 동해시", "강원도 삼척시",
                "강원도 속초시", "강원도 양구군", "강원도 양양군","강원도 영월군", "강원도 원주시", "강원도 인제군",
                "강원도 정선군", "강원도 철원군", "강원도 춘천시", "강원도 태백시", "강원도 평창군",
                "강원도 홍천군", "강원도 화천군", "강원도 횡성군"};

        mList = new ArrayList<String>();
        for (int index = 0; index < 19; index++) {
            mList.add(strTextList[index]);
        }

        ArrayAdapter<String> adapter;

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinner = (Spinner) v.findViewById(R.id.spinner1);
        mSpinner.setAdapter(adapter);
    }

    private class ViewHolder {
        public TextView tv_sub1_local;
        public TextView tv_sub1_gov;
        public TextView tv_sub1_addr;
        public TextView tv_sub1_call;

    }

    private class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<MyListData1> mListData = new ArrayList<MyListData1>();

        public ListViewAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }

        public List<MyListData1> getData(){
            return mListData;
        }

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.g_guide2_list_item, null);

                holder.tv_sub1_local = (TextView)convertView.findViewById(R.id.tv_local1);
                holder.tv_sub1_gov = (TextView)convertView.findViewById(R.id.tv_gov1);
                holder.tv_sub1_addr = (TextView)convertView.findViewById(R.id.tv_addr1);
                holder.tv_sub1_call = (TextView)convertView.findViewById(R.id.tv_call1);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            MyListData1 mData = mListData.get(position);

            holder.tv_sub1_local.setText(mData.mLocal);
            holder.tv_sub1_gov.setText(mData.mGov);
            holder.tv_sub1_addr.setText(mData.mAddr);
            holder.tv_sub1_call.setText(mData.mCall);


            return convertView;
        }

        public void addItem(String local, String gov, String addr, String call){
            MyListData1 addinfo = null;
            addinfo = new MyListData1();
            addinfo.mLocal = local;
            addinfo.mGov = gov;
            addinfo.mAddr = addr;
            addinfo.mCall = call;

            mListData.add(addinfo);
        }
        public void remove1(int postion){
            mListData.remove(postion);
            dataChage();
        }
        public void dataChage(){
            mAdapter1.notifyDataSetChanged();
        }
        public void dataclear(ArrayList cleardata){
            cleardata.clear();
        }
    }
    private class ViewHolder2 {
        public TextView tv_sub2_local;
        public TextView tv_sub2_gov;
        public TextView tv_sub2_div;
        public TextView tv_sub2_call;

    }

    private class ListViewAdapter2 extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<MyListData2> mListData = new ArrayList<MyListData2>();

        public ListViewAdapter2(Context mContext) {
            super();
            this.mContext = mContext;
        }

        public List<MyListData2> getData(){
            return mListData;
        }

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder2 holder;
            if(convertView == null){
                holder = new ViewHolder2();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.g_guide2_list_item2, null);

                holder.tv_sub2_local = (TextView)convertView.findViewById(R.id.tv_local2);
                holder.tv_sub2_gov = (TextView)convertView.findViewById(R.id.tv_gov2);
                holder.tv_sub2_div = (TextView)convertView.findViewById(R.id.tv_div2);
                holder.tv_sub2_call = (TextView)convertView.findViewById(R.id.tv_call2);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder2) convertView.getTag();
            }

            MyListData2 mData = mListData.get(position);

            holder.tv_sub2_local.setText(mData.mLocal);
            holder.tv_sub2_gov.setText(mData.mGov);
            holder.tv_sub2_div.setText(mData.mDiv);
            holder.tv_sub2_call.setText(mData.mCall);


            return convertView;
        }

        public void addItem(String local, String gov, String div, String call){
            MyListData2 addinfo = null;
            addinfo = new MyListData2();
            addinfo.mLocal = local;
            addinfo.mGov = gov;
            addinfo.mDiv = div;
            addinfo.mCall = call;

            mListData.add(addinfo);
        }
        public void remove2(int postion){
            mListData.remove(postion);
            dataChage();
        }
        public void dataChage(){
            mAdapter2.notifyDataSetChanged();
        }
        public void dataclear(ArrayList cleardata){
            cleardata.clear();
        }
    }
    private class ViewHolder3 {
        public TextView tv_sub3_local;
        public TextView tv_sub3_gov;
        public TextView tv_sub3_addr;
        public TextView tv_sub3_call;

    }

    private class ListViewAdapter3 extends BaseAdapter {
        private Context mContext = null;
        private ArrayList<MyListData3> mListData = new ArrayList<MyListData3>();

        public ListViewAdapter3(Context mContext) {
            super();
            this.mContext = mContext;
        }

        public List<MyListData3> getData(){
            return mListData;
        }

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder3 holder;
            if(convertView == null){
                holder = new ViewHolder3();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.g_guide2_list_item, null);

                holder.tv_sub3_local = (TextView)convertView.findViewById(R.id.tv_local1);
                holder.tv_sub3_gov = (TextView)convertView.findViewById(R.id.tv_gov1);
                holder.tv_sub3_addr = (TextView)convertView.findViewById(R.id.tv_addr1);
                holder.tv_sub3_call = (TextView)convertView.findViewById(R.id.tv_call1);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder3) convertView.getTag();
            }

            MyListData3 mData = mListData.get(position);

            holder.tv_sub3_local.setText(mData.mLocal);
            holder.tv_sub3_gov.setText(mData.mGov);
            holder.tv_sub3_addr.setText(mData.mAddr);
            holder.tv_sub3_call.setText(mData.mCall);


            return convertView;
        }

        public void addItem(String local, String gov, String addr, String call){
            MyListData3 addinfo = null;
            addinfo = new MyListData3();
            addinfo.mLocal = local;
            addinfo.mGov = gov;
            addinfo.mAddr = addr;
            addinfo.mCall = call;

            mListData.add(addinfo);
        }
        public void remove3(int postion){
            mListData.remove(postion);
            dataChage();
        }
        public void dataChage(){
            mAdapter1.notifyDataSetChanged();
        }
        public void dataclear(ArrayList cleardata){
            cleardata.clear();
        }
    }

}
