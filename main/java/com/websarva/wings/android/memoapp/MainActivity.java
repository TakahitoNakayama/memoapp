package com.websarva.wings.android.memoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    String[] from={"title","contents"};
    int[] to={android.R.id.text1,android.R.id.text2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView lvMemo=findViewById(R.id.lv_memo);

        List<Map<String,String>> memoList=new ArrayList<Map<String,String>>();
        Map<String,String> memoMap=new HashMap<>();
        memoMap.put("title","タイトル");
        memoMap.put("contents","メモ");
        memoList.add(memoMap);

        SimpleAdapter adapter=new SimpleAdapter
                (MainActivity.this,memoList, android.R.layout.simple_list_item_2,from,to);
        lvMemo.setAdapter(adapter);


    }
}