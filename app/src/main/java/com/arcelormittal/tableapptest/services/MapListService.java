package com.arcelormittal.tableapptest.services;

import android.content.Intent;
import android.content.res.AssetManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.arcelormittal.tableapptest.MainActivity;
import com.arcelormittal.tableapptest.MapActivity;
import com.arcelormittal.tableapptest.R;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MapListService {
    private List<String> mapList;
    private ArrayAdapter<String> adapter;
    private ListView list;
    private android.content.Context context;

    private void listShafts(AssetManager assets) {
        String [] list;
        try {
            list = assets.list("");
            if (list.length > 0) {
                // This is a folders
                Collections.addAll(mapList, list);
            }
        } catch (IOException e) {
            return;
        }

        mapList.removeAll(List.of("images", "webkit"));

    }

    public MapListService(android.content.Context context, ListView list, MainActivity mainActivity) {
        this.context = context;
        this.list = list;
        mapList = new LinkedList<>();
        listShafts(context.getAssets());
        adapter = new ArrayAdapter<>(context, R.layout.map_list_element, mapList);
        list.setAdapter(adapter);
        list.setOnItemClickListener((adapterView, view, i, l) -> {
            TextView textView = (TextView) view;

            Intent myIntent = new Intent(mainActivity, MapActivity.class);

            myIntent.putExtra("value", textView.getText());

            mainActivity.startActivity(myIntent);
        });
    }
}
