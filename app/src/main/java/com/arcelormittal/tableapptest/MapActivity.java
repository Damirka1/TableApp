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

import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.android.material.textfield.TextInputLayout;


import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private ArrayAdapter<String> adapter;


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

        Button backButton = findViewById(R.id.BackButton);
        backButton.setOnClickListener(view -> {
            onBackPressed();
        });



        TextInputLayout searchInputLayout = findViewById(R.id.search_text_input);
        EditText searchEditText = searchInputLayout.getEditText();


        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, positionsListService.getMapList());
        list.setAdapter(adapter);

        // Установка слушателя изменений текста в EditText
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Ничего не делаем перед изменением текста
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Вызываем метод для фильтрации списка на основе введенного текста
                filterList(charSequence.toString());
            }


            @Override
            public void afterTextChanged(Editable editable) {
                // Ничего не делаем после изменения текста
            }
        });

        filterList(""); // Фильтрация списка с пустой строкой или передайте нужное вам значение для фильтрации
    }

    private void filterList(String searchText) {
        // Создаем новый адаптер
        ArrayAdapter<String> newAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        List<String> mapList = positionsListService.getMapList();
        List<String> filteredList = new ArrayList<>(); // Отфильтрованный список

        // Проходим по каждому элементу в mapList
        for (String map : mapList) {
            // Если значение содержит введенный текст (игнорируя регистр), добавляем его в отфильтрованный список
            if (map.toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(map);
            }
        }

        // Добавляем все элементы в новый адаптер
        newAdapter.addAll(filteredList);

        // Устанавливаем новый адаптер в ListView
        ListView list = findViewById(R.id.PositionList);
        list.setAdapter(newAdapter);
    }

}

