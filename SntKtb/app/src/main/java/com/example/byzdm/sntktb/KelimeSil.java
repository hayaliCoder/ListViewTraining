package com.example.byzdm.sntktb;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class KelimeSil extends AppCompatActivity {

    EditText silinecekMetin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelime_sil);
        silinecekMetin = (EditText) findViewById(R.id.editText3);


    }


    public void kelimeSil(View view){

        /*Cursor cursor = KelimeEkle.database.rawQuery("SELECT name,name2,image FROM sozluktab", null);

        int nameIx = cursor.getColumnIndex("name");
        int name2Ix = cursor.getColumnIndex("name2");
        int imageIx = cursor.getColumnIndex("image");*/








        String silMetin = silinecekMetin.getText().toString();

        String sqlString =  "DELETE FROM sozluktab WHERE name=?";

        SQLiteStatement stament = KelimeEkle.database.compileStatement(sqlString);



        stament.bindString(1,silMetin);
        stament.execute();


        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);




    }



}
