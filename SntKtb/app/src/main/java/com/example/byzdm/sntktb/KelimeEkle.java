package com.example.byzdm.sntktb;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class KelimeEkle extends AppCompatActivity {

    ImageView imageView;
    EditText editText;
    EditText editText2;
    TextView resimText;
    Button ekle;
    static SQLiteDatabase database;
    Bitmap selectedImage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelime_ekle);



        imageView = (ImageView) findViewById(R.id.imageView);
        editText = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText2);
        resimText = (TextView) findViewById(R.id.resimEkleText);
        ekle = (Button) findViewById(R.id.button);
        Intent intent = getIntent();
        String kontrol = intent.getStringExtra("kontrol");
        if (kontrol.equalsIgnoreCase("yeni")) {
            Bitmap background = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.image);
            imageView.setImageBitmap(background);
            imageView.setVisibility(View.INVISIBLE);
            ekle.setVisibility(View.VISIBLE);
            editText2.setText("");
            editText.setText("");

        }else{


            String kelime = intent.getStringExtra("kelime");
            String anlam = intent.getStringExtra("anlam");
            int position = intent.getIntExtra("position", 0);

            editText.setText(kelime);
            editText2.setText(anlam);


            imageView.setImageBitmap(MainActivity.imageList.get(position));



            resimText.setVisibility(View.INVISIBLE);
            ekle.setVisibility(View.INVISIBLE);
        }


    }


    public void resimEkleKontrol(View view){
        imageView.setVisibility(View.VISIBLE);

        String resimMetin = resimText.getText().toString();

        if (resimMetin.equals("Resim Ekle")){
            resimText.setText("Resim Ekleme");
        }else{
            resimText.setText("Resim Ekle");
            imageView.setVisibility(View.INVISIBLE);
        }


    }






    public void resimekle (View view) {



        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 2);

        }
        else {

            Intent kontrolIntent = getIntent();
            String gelenKontrol = kontrolIntent.getStringExtra("kontrol");

            if (gelenKontrol.equalsIgnoreCase("eski")){

            }else{
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 2) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }

        }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri image = data.getData();

            try {
                selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),image);
                imageView.setImageBitmap(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    public void kaydet (View view) {

        String kelime = editText.getText().toString();
        String anlam = editText2.getText().toString();

        if (imageView.getVisibility() == View.VISIBLE) {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.PNG, 50, outputStream);
            byte[] byteArray = outputStream.toByteArray();

            try {

                database = this.openOrCreateDatabase("Sozlukdb", MODE_PRIVATE, null);

                database.execSQL("CREATE TABLE IF NOT EXISTS sozluktab(name VARCHAR, name2 VARCHAR, image BLOB)");

                String sqlString = "INSERT INTO sozluktab (name, name2, image) VALUES (?,?,?)";

                SQLiteStatement stament = database.compileStatement(sqlString);
                stament.bindString(1, kelime);
                stament.bindString(2, anlam);
                stament.bindBlob(3, byteArray);


                stament.execute();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(imageView.getVisibility() == View.INVISIBLE){

            try{

                database = this.openOrCreateDatabase("Sozlukdb", MODE_PRIVATE, null);

                database.execSQL("CREATE TABLE IF NOT EXISTS sozluktab(name VARCHAR, name2 VARCHAR, image BLOB)");

                String sqlString =  "INSERT INTO sozluktab (name, name2, image) VALUES (?,?,?)";

                SQLiteStatement stament = database.compileStatement(sqlString);
                stament.bindString(1,kelime);
                stament.bindString(2,anlam);
                stament.execute();


            }
            catch (Exception e){
                e.printStackTrace();
            }
        }




        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);



    }
}
