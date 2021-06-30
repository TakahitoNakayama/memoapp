package com.websarva.wings.android.memoapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import static java.nio.file.Paths.get;

public class MainActivity extends AppCompatActivity {

    String[] from={"title","contentsshort"};
    int[] to={android.R.id.text1,android.R.id.text2};

    private DatabaseHelper helper;

    private String strTitle="タイトル";
    private String strContents="メモ";
    private String strContentsShort="メモ";

    List<Map<String,String>> memoList;
    Map<String,String> memoMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView lvMemo=findViewById(R.id.lv_memo);

        memoList=new ArrayList<Map<String,String>>();

        helper=new DatabaseHelper(MainActivity.this);
        SQLiteDatabase db=helper.getWritableDatabase();
        String sqlAll="SELECT * FROM memodata";
        Cursor cursor=db.rawQuery(sqlAll,null);
        while (cursor.moveToNext()){
            int idxTitle=cursor.getColumnIndex("title");
            strTitle=cursor.getString(idxTitle);
            int idxContents=cursor.getColumnIndex("contents");
            strContents=cursor.getString(idxContents);
            shortContentsCreate();

            memoMap=new HashMap<>();
            memoMap.put("title",strTitle);
            memoMap.put("contents",strContents);
            memoMap.put("contentsshort",strContentsShort);
            memoList.add(memoMap);
        }

        SimpleAdapter adapter=new SimpleAdapter
                (MainActivity.this,memoList, android.R.layout.simple_list_item_2,from,to);
        lvMemo.setAdapter(adapter);


        registerForContextMenu(lvMemo);



    }

    private void shortContentsCreate(){
        if(strContents.length()<20){
            strContentsShort=strContents.substring(0,strContents.length());
        }
        else{
            strContentsShort=strContents.substring(0,20);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onStart(){
        super.onStart();

        helper=new DatabaseHelper(MainActivity.this);
        SQLiteDatabase db=helper.getWritableDatabase();
        String sqlSelect="SELECT * FROM memodata where _id = "+MemoEditer.position;
        Cursor cursor=db.rawQuery(sqlSelect,null);
        while (cursor.moveToNext()){
            int idxTitle=cursor.getColumnIndex("title");
            strTitle=cursor.getString(idxTitle);
            int idxContents=cursor.getColumnIndex("contents");
            strContents=cursor.getString(idxContents);
        }
        ListView lvMemo=findViewById(R.id.lv_memo);
        shortContentsCreate();

        Map<String,String> map=memoList.get(MemoEditer.position);
        String v=map.replace("title",strTitle);
        v=map.replace("contents",strContents);
        v=map.replace("contentsshort",strContentsShort);

        SimpleAdapter adapter=new SimpleAdapter
                (MainActivity.this,memoList, android.R.layout.simple_list_item_2,from,to);
        lvMemo.setAdapter(adapter);

        lvMemo.setOnItemClickListener(new ListListener());
    }

    public void memoCreate(){
        ListView lvMemo=findViewById(R.id.lv_memo);
        memoMap=new HashMap<>();
        memoMap.put("title",strTitle);
        memoMap.put("contents",strContents);
        memoList.add(memoMap);

        SimpleAdapter adapter=new SimpleAdapter
                (MainActivity.this,memoList, android.R.layout.simple_list_item_2,from,to);
        lvMemo.setAdapter(adapter);

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
            Map<String,String> map=memoList.get(position);
            strTitle=map.get("title");
            strContents=map.get("contents");
            intent.putExtra("id",position);
            intent.putExtra("title",strTitle);
            intent.putExtra("contents",strContents);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_options_list,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        boolean returnVal=true;
        strTitle="";
        strContents="";

        memoCreate();

        int index=memoList.lastIndexOf(memoMap);
        Log.d("main",""+index);

        Intent intent=new Intent(MainActivity.this,MemoEditer.class);
        intent.putExtra("id",index);
        intent.putExtra("title",strTitle);
        intent.putExtra("contents",strContents);
        startActivity(intent);

        return returnVal;
    }

    @Override
    public void onCreateContextMenu
            (ContextMenu menu,View view,ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu,view,menuInfo);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_context_menu_list,menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        boolean returnVal=true;
        AdapterView.AdapterContextMenuInfo info=
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int listPosition=info.position;

        helper=new DatabaseHelper(MainActivity.this);
        SQLiteDatabase db=helper.getWritableDatabase();
        String sqlDelete="DELETE FROM memodata WHERE _id = "+listPosition;
        SQLiteStatement stmt=db.compileStatement(sqlDelete);
        stmt.executeUpdateDelete();

        memoList.remove(listPosition);
        ListView lvMemo=findViewById(R.id.lv_memo);
        SimpleAdapter adapter=new SimpleAdapter
                (MainActivity.this,memoList, android.R.layout.simple_list_item_2,from,to);
        lvMemo.setAdapter(adapter);


        return returnVal;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}

