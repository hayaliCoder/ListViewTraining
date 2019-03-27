package com.example.byzdm.sntktb;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.concurrent.RejectedExecutionException;

public class MainActivity extends AppCompatActivity {

    static ArrayList<Bitmap> imageList;
    static ArrayList<String> wordList;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater sisirmek = getMenuInflater();
        sisirmek.inflate(R.menu.add_word, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.add) {
            Intent ekle = new Intent(getApplicationContext(),KelimeEkle.class);
            ekle.putExtra("kontrol","yeni");
            startActivity(ekle);
        }

        if (item.getItemId() == R.id.delete) {
            Intent sil = new Intent(getApplicationContext(), KelimeSil.class);
            startActivity(sil);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ListView sozluk = (ListView) findViewById(R.id.listv);



        wordList = new ArrayList<String>();
        imageList = new ArrayList<Bitmap>();

        /*ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, wordList);

        sozluk.setAdapter(arrayAdapter);*/

        CustomAdaptor deneme = new CustomAdaptor(getApplicationContext(), wordList);
        sozluk.setAdapter(deneme);


        try {

            KelimeEkle.database = this.openOrCreateDatabase("Sozlukdb", MODE_PRIVATE, null);
            KelimeEkle.database.execSQL("CREATE TABLE IF NOT EXISTS sozluktab(name VARCHAR, name2 VARCHAR, image BLOB)");

            Cursor cursor = KelimeEkle.database.rawQuery("SELECT * FROM sozluktab", null);

            int nameIx = cursor.getColumnIndex("name");
            int name2Ix = cursor.getColumnIndex("name2");
            int imageIx = cursor.getColumnIndex("image");

            cursor.moveToFirst();

            /////While'in içini kurcalayacağız..   Ayrıca Veritabanına resim olmadan eklememeyer başladı////


            while (cursor.getString(nameIx) != null){

                wordList.add(cursor.getString(nameIx) + " - " + cursor.getString(name2Ix));

                if (cursor.getBlob(imageIx)!= null){
                    byte[] byteArray = cursor.getBlob(imageIx);
                    Bitmap image = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
                    imageList.add(image);
                }
                else {

                    imageList.add(null);
                }


                cursor.moveToNext();


                deneme.notifyDataSetChanged();
            }



        }catch(Exception e){
            e.printStackTrace();
        }



        sozluk.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), KelimeEkle.class);
                intent.putExtra("kontrol", "eski");

                String listItem = wordList.get(position);
                String[] ListSplit = ((String) listItem).split(" - ");
                String kelimeSplit = ListSplit[0];
                String anlamSplit = ListSplit[1];

                intent.putExtra("kelime", kelimeSplit);
                intent.putExtra("anlam", anlamSplit);


                intent.putExtra("position" , position);

                startActivity(intent);

            }
        });
    }




    private class CustomAdaptor extends BaseAdapter {

        Context context;
        ArrayList<String> kelimeler;


        public CustomAdaptor(Context context, ArrayList<String> kelimeler){
            this.context = context;
            this.kelimeler = kelimeler;


        }

        @Override
        public int getCount() {

            return kelimeler.size();
        }

        @Override
        public Object getItem(int position) {

            return position;
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View benimView = convertView;

            if (benimView == null){

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                benimView = inflater.inflate(R.layout.list_row, parent, false);

            }

            TextView kelimeT = (TextView) benimView.findViewById(R.id.kelimeText);
            TextView anlamT = (TextView) benimView.findViewById(R.id.anlamText);




            String listItem = wordList.get(position);
            String[] ListSplit = ((String) listItem).split(" - ");
            String kelimeSplit = ListSplit[0];
            String anlamSplit = ListSplit[1];



            kelimeT.setText(kelimeSplit);
            anlamT.setText(anlamSplit);




            return benimView;
        }
    }






}





