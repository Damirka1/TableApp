package com.arcelormittal.tableapptest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.arcelormittal.tableapptest.services.MapListService;
import com.arcelormittal.tableapptest.services.PositionsListService;

import java.io.IOException;
import java.io.InputStream;

import ovh.plrapps.mapview.MapView;
import ovh.plrapps.mapview.MapViewConfiguration;
import ovh.plrapps.mapview.core.TileStreamProvider;

public class MapActivity extends AppCompatActivity {

    private ConstraintLayout mapLayout;
    private ConstraintLayout findLayout;
    private ConstraintLayout menuLayout;

    private int menuWidth = 0;

    private int fullSize = 8192;
    private int tileSize = 256;

    private Button menuButton;

    // Services
    private PositionsListService positionsListService;

    private int convertPxToDp(int dps) {
        final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        int pixels = (int) (dps * scale + 0.5f);
        return pixels;
    }

    private void showMenu() {
        menuLayout.setVisibility(View.VISIBLE);

        ValueAnimator anim = ValueAnimator.ofInt(convertPxToDp(1), convertPxToDp(300));
        anim.addUpdateListener(valueAnimator -> {
            int val = (Integer) valueAnimator.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = menuLayout.getLayoutParams();
            layoutParams.width = val;
            menuLayout.setLayoutParams(layoutParams);

        });

        anim.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                for (int i = 0; i < menuLayout.getChildCount(); i++) {
                    View child = menuLayout.getChildAt(i);
                    child.setVisibility(View.VISIBLE);
                }
            }
        });
        anim.setDuration(250);
        anim.start();
    }

    private void hideMenuStart() {
        menuLayout.setVisibility(View.GONE);
        for (int i = 0; i < menuLayout.getChildCount(); i++) {
            View child = menuLayout.getChildAt(i);
            child.setVisibility(View.GONE);
        }
    }

    private void hideMenu() {
        for (int i = 0; i < menuLayout.getChildCount(); i++) {
            View child = menuLayout.getChildAt(i);
            child.setVisibility(View.GONE);
        }

        ValueAnimator anim = ValueAnimator.ofInt(convertPxToDp(300), convertPxToDp(1));
        anim.addUpdateListener(valueAnimator -> {
            int val = (Integer) valueAnimator.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = menuLayout.getLayoutParams();
            layoutParams.width = val;
            menuLayout.setLayoutParams(layoutParams);
        });

        anim.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                menuLayout.setVisibility(View.GONE);
            }
        });
        anim.setDuration(250);
        anim.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        mapLayout = findViewById(R.id.MapLayout);
        findLayout = findViewById(R.id.FindLayout);
        menuLayout = findViewById(R.id.MenuLayout);

        hideMenuStart();

        Intent intent = getIntent();
        String shaft = intent.getStringExtra("value");

        ListView list = findViewById(R.id.PositionList);

        positionsListService = new PositionsListService(shaft + "/Документы/", getApplicationContext(), list, this);

        MapView map = new MapView(getApplicationContext());

        TileStreamProvider tileStreamProvider = (row, col, zoomLvl) -> {
            InputStream stream;
            try {
                stream = getResources().getAssets().open(shaft + "/Карта/IMG-" + ((row * (fullSize / tileSize)) + col) + ".jpg");
            } catch (IOException e) {
                System.out.println("Can't find file:" + e.getMessage());
                return null;
            }
            return stream;
        };

        MapViewConfiguration config = new MapViewConfiguration(
                1, fullSize, fullSize, tileSize, tileStreamProvider
        ).setMinScale(0.3f).setMaxScale(2f).setStartScale(0.5f).setPadding(tileSize * 2).setWorkerCount(16);

        map.configure(config);

        mapLayout.addView(map);

        menuButton = findViewById(R.id.MenuButton);

        menuButton.setOnClickListener(view -> {
            if(menuLayout.isShown()) {
                hideMenu();
            }
            else {
                showMenu();
            }

        });
    }

}