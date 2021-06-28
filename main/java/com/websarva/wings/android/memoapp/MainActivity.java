package com.websarva.wings.android.memoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
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
    private DatabaseHelper helper;
    private String strTitle="タイトル";
    private String strContents="メモ";
    public int position=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    @Override
    protected void onStart(){
        super.onStart();

        helper=new DatabaseHelper(MainActivity.this);
        SQLiteDatabase db=helper.getWritableDatabase();
        String sqlSelect="SELECT * FROM memodata where _id = "+position;
        Cursor cursor=db.rawQuery(sqlSelect,null);
        while (cursor.moveToNext()){
            int idxTitle=cursor.getColumnIndex("title");
            strTitle=cursor.getString(idxTitle);
            int idxContents=cursor.getColumnIndex("contents");
            strContents=cursor.getString(idxContents);
        }
        
        ListView lvMemo=findViewById(R.id.lv_memo);

        List<Map<String,String>> memoList=new ArrayList<Map<String,String>>();
        Map<String,String> memoMap=new HashMap<>();
        memoMap.put("title",strTitle);
        memoMap.put("contents",strContents);
        memoList.add(memoMap);

        SimpleAdapter adapter=new SimpleAdapter
                (MainActivity.this,memoList, android.R.layout.simple_list_item_2,from,to);
        lvMemo.setAdapter(adapter);
        lvMemo.setOnItemClickListener(new ListListener());


    }

    @Override
    public void onDestroy(){
        helper.close();
        super.onDestroy();

    }

    class ListListener implements AdapterView.OnItemClickListener{


        @Override
        public void onItemClick
                (AdapterView<?> parent, View view, int position, long id) {
            Intent intent=new Intent(MainActivity.this,MemoEditer.class);
            intent.putExtra("title",strTitle);
            intent.putExtra("contents",strContents);
            startActivity(intent);

        }
    }


}

