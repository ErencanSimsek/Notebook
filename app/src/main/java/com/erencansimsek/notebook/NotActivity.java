package com.erencansimsek.notebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.erencansimsek.notebook.databinding.ActivityNotBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class NotActivity extends AppCompatActivity {

    static int notId;
    ActivityNotBinding binding;
    SQLiteDatabase database;
    SharedPreferences preferences;
    ConstraintLayout constraintLayout;
    String baslik,note;
    static String info;
    ImageButton Delete;
    AdView banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        preferences=this.getSharedPreferences("com.erencansimsek.notebook",MODE_PRIVATE);
        database=this.openOrCreateDatabase("Notlar",MODE_PRIVATE,null);
        binding();
        Delete();
        backgroundColor();
        Intent intent=getIntent();
        info= intent.getStringExtra("info");

        if(info.matches("new"))
        {
            binding.baslik.setText("");
            binding.note.setText("");
        }

        else if(info.matches("old")){
            notId= intent.getIntExtra("notId",1);
            try {
                Cursor cursor=database.rawQuery("SELECT * FROM notlar WHERE id=?",new String[]{String.valueOf(notId)});
                int baslikIx=cursor.getColumnIndex("baslik");
                int notIx=cursor.getColumnIndex("note");
                while(cursor.moveToNext()){
                    binding.baslik.setText(cursor.getString(baslikIx));
                    binding.note.setText(cursor.getString(notIx));
                }
                cursor.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void binding(){
        Delete=binding.Delete;
        Delete.setBackgroundColor(Color.TRANSPARENT);

        MobileAds.initialize(NotActivity.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });

        banner=binding.mainBanner;
        AdRequest adRequest=new AdRequest.Builder().build();
        banner.loadAd(adRequest);
    }

    public void backgroundColor(){
        MainActivity.colorBlack = preferences.getString("color",null);
        MainActivity.colorWhite = preferences.getString("color",null);
        constraintLayout=binding.Not;
        if(MainActivity.colorBlack != null && MainActivity.colorBlack.equals("black"))
        {
            constraintLayout.setBackgroundResource(R.color.black);
        }
        if(MainActivity.colorWhite != null && MainActivity.colorWhite.equals("white"))
        {
            constraintLayout.setBackgroundResource(R.color.white);
        }
    }

    @Override
    public void onBackPressed() {
        if(info.matches("new"))
        {
            try {
                baslik=binding.baslik.getText().toString();
                note=binding.note.getText().toString();
                if(!baslik.equals("")||!note.equals("")){
                    database.execSQL("CREATE TABLE IF NOT EXISTS notlar(id INTEGER PRIMARY KEY AUTOINCREMENT,baslik VARCHAR,note VARCHAR)");
                    String sqlString = "INSERT INTO notlar(baslik,note)VALUES(?,?)";
                    SQLiteStatement sqLiteStatement = database.compileStatement(sqlString);
                    sqLiteStatement.bindString(1, baslik);
                    sqLiteStatement.bindString(2, note);
                    sqLiteStatement.execute();
                }


            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else if(info.matches("old")){
            try {
                baslik = binding.baslik.getText().toString();
                note = binding.note.getText().toString();
                database.execSQL("CREATE TABLE IF NOT EXISTS notlar(id INTEGER PRIMARY KEY AUTOINCREMENT,baslik VARCHAR,note VARCHAR)");
                String update = "UPDATE notlar SET baslik='" + baslik + "', note='" + note + "' WHERE id=" + notId;
                database.execSQL(update);
                Cursor cursor = database.rawQuery("SELECT * FROM notlar",null);
                int baslikIx = cursor.getColumnIndex("baslik");
                int notIx = cursor.getColumnIndex("note");
                while(cursor.moveToNext()) {
                    cursor.getString(baslikIx);
                    cursor.getString(notIx);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        Intent intent=new Intent(NotActivity.this,MainActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
    }

    public void Delete(){
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    baslik = binding.baslik.getText().toString();
                    note = binding.note.getText().toString();
                    database.execSQL("CREATE TABLE IF NOT EXISTS notlar(id INTEGER PRIMARY KEY AUTOINCREMENT,baslik VARCHAR,note VARCHAR)");
                    String delete = "DELETE FROM notlar WHERE id=" + notId;
                    database.execSQL(delete);
                    Cursor cursor = database.rawQuery("SELECT * FROM notlar",null);
                    int baslikIx = cursor.getColumnIndex("baslik");
                    int notIx = cursor.getColumnIndex("note");
                    while(cursor.moveToNext()) {
                        cursor.getString(baslikIx);
                        cursor.getString(notIx);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                Intent intent=new Intent(NotActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}