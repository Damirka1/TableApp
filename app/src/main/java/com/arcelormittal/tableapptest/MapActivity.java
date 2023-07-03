package com.arcelormittal.tableapptest;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.arcelormittal.tableapptest.room.entities.Point;
import com.arcelormittal.tableapptest.services.PositionsListService;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.io.InputStream;

import ovh.plrapps.mapview.MapView;
import ovh.plrapps.mapview.MapViewConfiguration;
import ovh.plrapps.mapview.core.TileStreamProvider;
import ovh.plrapps.mapview.markers.MarkerLayout;

public class MapActivity extends AppCompatActivity {

    private ConstraintLayout mapLayout;
    private ConstraintLayout findLayout;
    private ConstraintLayout menuLayout;

    private ConstraintLayout searchLayout;

    private int menuWidth = 0;

    private int fullSize = 8192;
    private int tileSize = 256;

    private Button menuButton;

    // Services
    private PositionsListService positionsListService;


    private int convertDpToPx(int dps) {
        final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        int pixels = (int) (dps * scale + 0.5f);
        return pixels;
    }

    private void showMenu() {
        menuLayout.setVisibility(View.VISIBLE);

        ValueAnimator anim = ValueAnimator.ofInt(convertDpToPx(1), convertDpToPx(300));
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

        ValueAnimator anim = ValueAnimator.ofInt(convertDpToPx(300), convertDpToPx(1));
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

    private void hideSearchMenu() {
        searchLayout.setVisibility(View.GONE);
    }

    private void showSearchMenu() {
        searchLayout.setVisibility(View.VISIBLE);
    }

    private void setMarkers(MapView map) {
        MarkerLayout markerLayout = map.getMarkerLayout();

        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for(Point point : positionsListService.getPoints()) {
            View v = vi.inflate(R.layout.marker, null);

            ((TextView)v.findViewById(R.id.MarkerTest)).setText(point.getText());

            markerLayout.addMarker(v, point.getX(), point.getY(),
                    0f, 0f, 0f, 0f, point.getText());

            v.setOnClickListener(view -> {
                String position = ((TextView)v.findViewById(R.id.MarkerTest)).getText().toString();
                String positionWithoutLetters = position.replaceAll("([а-я])", "");

                if(Integer.parseInt(positionWithoutLetters) < 100)
                    position = "0" + position;

                try {
                    positionsListService.openFile(position);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    Toast.makeText(this, "Can't find file", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        mapLayout = findViewById(R.id.MapLayout);
        findLayout = findViewById(R.id.FindLayout);
        menuLayout = findViewById(R.id.MenuLayout);
        searchLayout = findViewById(R.id.SearchLayout);

        hideMenuStart();
        hideSearchMenu();

        Intent intent = getIntent();
        String shaft = intent.getStringExtra("value");

        ListView posList = findViewById(R.id.PositionList);
        ListView searchList = findViewById(R.id.SearchPositionList);

        positionsListService = new PositionsListService(shaft, getApplicationContext(), posList, searchList, this);

        MapView map = new MapView(getApplicationContext());

        TileStreamProvider tileStreamProvider = (row, col, zoomLvl) -> {
            InputStream stream;
            try {
                stream = getResources().getAssets().open(shaft + "/Карта/IMG-" + ((row * (fullSize / tileSize)) + col) + ".jpg");
            } catch (IOException e) {
//                System.out.println("Can't find file:" + e.getMessage());
                return null;
            }
            return stream;
        };

        MapViewConfiguration config = new MapViewConfiguration(
                1, fullSize, fullSize, tileSize, tileStreamProvider
        ).setMinScale(0.3f).setMaxScale(2f).setStartScale(0.5f).setPadding(tileSize * 2).setWorkerCount(16);

        map.configure(config);

        mapLayout.addView(map);

        setMarkers(map);

        menuButton = findViewById(R.id.MenuButton);

        menuButton.setOnClickListener(view -> {
            if(menuLayout.isShown()) {
                hideMenu();
            }
            else {
                showMenu();
            }

        });

        Button backButton = findViewById(R.id.BackButton);
        backButton.setOnClickListener(view -> {
            onBackPressed();
        });

        TextInputLayout searchInputLayout = findViewById(R.id.search_text_input);
        EditText searchEditText = searchInputLayout.getEditText();

        if (searchEditText != null) {
            searchEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    // Ничего не делаем перед изменением текста
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    // Вызываем метод для фильтрации списка на основе введенного текста

                    if(charSequence.length() > 0)
                        showSearchMenu();
                    else
                        hideSearchMenu();

                    positionsListService.filterList(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    // Ничего не делаем после изменения текста
                }
            });
        }

        positionsListService.filterList("");
    }



}

