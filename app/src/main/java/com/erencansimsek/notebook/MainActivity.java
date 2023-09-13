package com.erencansimsek.notebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;

import com.erencansimsek.notebook.databinding.ActivityMainBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    ArrayList<dosya> dosyaArrayList;
    recycler recycler;
    ConstraintLayout constraintLayout;
    SharedPreferences preferences;
    Toolbar toolbar;
    ImageButton sutun,satir,backgrondColor;
    static String colorBlack,colorWhite;
    FloatingActionButton add;
    RecyclerView recyclerView;
    AdView banner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        dosyaArrayList = new ArrayList<>();
        recycler = new recycler(dosyaArrayList);
        recyclerView=binding.recyclerView;
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        binding.recyclerView.setAdapter(recycler);
        binding();

        preferences=this.getSharedPreferences("com.erencansimsek.notebook",MODE_PRIVATE);
        add();
        veriCek();
        backgroundColor();
        sutunSatir();
    }

    public void binding(){
        MobileAds.initialize(MainActivity.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {

            }
        });

        banner=binding.mainBanner;
        AdRequest adRequest=new AdRequest.Builder().build();
        banner.loadAd(adRequest);

        toolbar=binding.Tool.toolbar;
        sutun=binding.sutun;
        sutun.setBackgroundColor(Color.TRANSPARENT);
        satir=binding.satir;
        satir.setBackgroundColor(Color.TRANSPARENT);
        backgrondColor=binding.backgroundColor;
        backgrondColor.setBackgroundColor(Color.TRANSPARENT);
        constraintLayout=binding.main;
        setSupportActionBar(toolbar);
        add=binding.add;
        add.setBackgroundColor(Color.TRANSPARENT);
    }

    public void add(){
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,NotActivity.class);
                intent.putExtra("info","new");
                startActivity(intent);
            }
        });
    }

    public void backgroundColor(){
        backgrondColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(MainActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.pagecolor);

                final CardView Black=dialog.findViewById(R.id.black);
                final CardView White=dialog.findViewById(R.id.white);

                Black.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        constraintLayout.setBackgroundResource(R.color.black);
                        preferences.edit().putString("color","black").apply();
                        dialog.dismiss();
                    }
                });

                White.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        constraintLayout.setBackgroundResource(R.color.white);
                        preferences.edit().putString("color","white").apply();
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        colorBlack = preferences.getString("color",null);
        colorWhite = preferences.getString("color",null);

        if(colorBlack != null && colorBlack.equals("black"))
        {
            constraintLayout.setBackgroundResource(R.color.black);
        }
        if(colorWhite != null && colorWhite.equals("white"))
        {
            constraintLayout.setBackgroundResource(R.color.white);
        }
    }

    public void sutunSatir(){
        satir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                preferences.edit().putString("view","satir").apply();
            }
        });
        sutun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
                preferences.edit().putString("view","sutun").apply();
            }
        });
        String viewSatir=preferences.getString("view",null);
        String viewSutun=preferences.getString("view",null);
        if(viewSatir!=null&&viewSatir.equals("satir")){
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        else if(viewSutun!=null&&viewSutun.equals("sutun")){
            binding.recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        }
    }

    public void veriCek() {
        try {
            SQLiteDatabase database = this.openOrCreateDatabase("Notlar", MODE_PRIVATE, null);
            Cursor cursor = database.rawQuery("SELECT * FROM notlar", null);
            int baslikIx = cursor.getColumnIndex("baslik");
            int idIx = cursor.getColumnIndex("id");
            while (cursor.moveToNext()) {
                String name = cursor.getString(baslikIx);
                int Id = cursor.getInt(idIx);
                dosya dosya = new dosya(name, Id);
                dosyaArrayList.add(dosya);
            }
            recycler.notifyDataSetChanged();
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}