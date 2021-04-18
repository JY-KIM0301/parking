//피드백 화면
package com.example.parking.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parking.activity.AddActivity;
import com.example.parking.adapter.MemoAdapter;
import com.example.parking.R;
import com.example.parking.database.SQLiteHelper;
import com.example.parking.model.Memo;

import java.util.ArrayList;
import java.util.List;

public class FragFeedback extends Fragment {
    SQLiteHelper dbHelper;
    RecyclerView recyclerView;
    MemoAdapter recyclerAdapter;
    Button btnAdd;
    List<Memo> memoList;
    public FragFeedback(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.frag_feedback, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        memoList = new ArrayList<>();

        dbHelper = new SQLiteHelper(getContext());
        memoList = dbHelper.selectAll();

        recyclerView = view.findViewById(R.id.recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerAdapter = new MemoAdapter(memoList);
        recyclerView.setAdapter(recyclerAdapter);
        btnAdd = view.findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //새로운 메모 작성
                Intent intent = new Intent(getActivity(), AddActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        dbHelper = new SQLiteHelper(getContext());
        memoList = dbHelper.selectAll();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && data != null) {
            String strMain = data.getStringExtra("main");
            String strSub = data.getStringExtra("sub");

            Memo memo = new Memo(strMain, strSub, 0);
            recyclerAdapter.addItem(memo);
            recyclerAdapter.notifyDataSetChanged();

            dbHelper.insertMemo(memo);
        }
    }
}
