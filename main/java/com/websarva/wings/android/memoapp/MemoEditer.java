package com.websarva.wings.android.memoapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.Toast;

public class MemoEditer extends AppCompatActivity {

    private DatabaseHelper helper;
    public int position=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_editer);

        Intent intent=getIntent();
        String _title=intent.getStringExtra("title");
        String _contents=intent.getStringExtra("contents");

        EditText etEditerTitle=findViewById(R.id.et_editertitle);
        EditText etEditerContents=findViewById(R.id.et_editercontents);

        etEditerTitle.setText(_title);
        etEditerContents.setText(_contents);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Button btSave=findViewById(R.id.bt_save);
        btSave.setOnClickListener(new SaveListener());

        helper=new DatabaseHelper(MemoEditer.this);

    }

    @Override
    public void onDestroy(){
        helper.close();
        super.onDestroy();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        boolean returnVal=true;
        finish();

        return returnVal;
    }

    class SaveListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            EditText etEditerTitle=findViewById(R.id.et_editertitle);
            String strTitle="";
            strTitle = etEditerTitle.getText().toString();

            EditText etEditerContents=findViewById(R.id.et_editercontents);
            String strContents="";
            strContents = etEditerContents.getText().toString();

            SQLiteDatabase db=helper.getWritableDatabase();
            String sqlDelete="DELETE FROM memodata WHERE _id = "+position;
            SQLiteStatement stmt=db.compileStatement(sqlDelete);
            stmt.executeUpdateDelete();

            String sqlInsert=
                    "INSERT INTO memodata(_id,title,contents) VALUES (?,?,?)";
            stmt=db.compileStatement(sqlInsert);
            stmt.bindLong(1,position);
            stmt.bindString(2,strTitle);
            stmt.bindString(3,strContents);
            stmt.executeInsert();

            Log.d("main",""+strTitle);
            Log.d("main",""+strContents);


            Toast.makeText
                (MemoEditer.this, "メモを保存しました", Toast.LENGTH_SHORT).show();



            finish();



        }
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);



    }


}