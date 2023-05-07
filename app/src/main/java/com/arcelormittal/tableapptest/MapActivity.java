package com.arcelormittal.tableapptest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import ovh.plrapps.mapview.MapView;
import ovh.plrapps.mapview.MapViewConfiguration;
import ovh.plrapps.mapview.core.TileStreamProvider;

public class MapActivity extends AppCompatActivity {

    private int fullSize = 8192;
    private int tileSize = 256;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        Intent intent = getIntent();
        String shaft = intent.getStringExtra("value");

        MapView map = new MapView(getApplicationContext());

        TileStreamProvider tileStreamProvider = (row, col, zoomLvl) -> {
            InputStream stream;
            try {
                stream = getResources().getAssets().open("shaft_kazakstanskaya/IMG-" + ((row * (fullSize / tileSize)) + col) + ".jpg");
//                int i3 = 0;
            } catch (IOException e) {
                System.out.println("Can't find file:" + e.getMessage());
                return null;
//                throw new RuntimeException(e);
            }
            return stream;
        };

        MapViewConfiguration config = new MapViewConfiguration(
                1, fullSize, fullSize, tileSize, tileStreamProvider
        ).setMinScale(0.3f).setMaxScale(2f).setStartScale(0.5f).setPadding(tileSize * 2).setWorkerCount(16);

        map.configure(config);

        ConstraintLayout layout = findViewById(R.id.MapLayout);

        layout.addView(map);

    }

}