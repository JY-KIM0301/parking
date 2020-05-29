//주변정보 화면
package com.example.parking;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;

public class FragInfo extends Fragment {
    public FragInfo() {
    }

    private TextView selected_item_textview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_info);

        ListView listview = (ListView) findViewById(R.id.listview);
        selected_item_textview = (TextView) findViewById(R.id.selected_item_textview);

        //데이터를 저장하게 되는 리스트
        List<String> list = new ArrayList<>();

        //리스트뷰와 리스트를 연결하기 위해 사용되는 어댑터
        // ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, list);

        //리스트뷰의 어댑터를 지정해준다.
        // listview.setAdapter(adapter);


        //리스트뷰의 아이템을 클릭시 해당 아이템의 문자열을 가져오기 위한 처리
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            //리스트 클릭이벤트
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view, int position, long id) {
            }
        });


        //리스트뷰에 보여질 아이템
        list.add("역곡역");
        list.add("역곡 CGV");
        list.add("역곡 상상시장");
        list.add("역곡 남부시장");

        list.add("가톨릭대학교");
        list.add("역곡공원");

        list.add("소사종합시장");
        list.add("소사역");

        list.add("송내시장");
        list.add("송내역");

        list.add("부천자유시장");
        list.add("부천역");
        list.add("부천 버스터미널");
        list.add("부천 롯데시네마");

    }

    private Object findViewById(int selected_item_textview) {
        return null;
    }

    private void setContentView(int frag_info) {

    }
}
