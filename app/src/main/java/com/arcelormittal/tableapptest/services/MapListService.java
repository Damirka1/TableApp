package com.arcelormittal.tableapptest.services;

import android.content.Intent;
import android.content.res.AssetManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.arcelormittal.tableapptest.MainActivity;
import com.arcelormittal.tableapptest.MapActivity;
import com.arcelormittal.tableapptest.R;
import com.arcelormittal.tableapptest.room.entities.Map;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MapListService {
    private List<String> mapList;
    private ArrayAdapter<String> adapter;

    private void listShafts() {
        LiteDirectory ld = LiteDirectory.getInstance();

        ld.getShafts().forEach((Map m) -> mapList.add(m.getName()));
    }

    public MapListService() {
        mapList = new LinkedList<>();
        new Thread(this::listShafts).start();
    }

    public void setList(ListView list, android.content.Context context, MainActivity mainActivity) {
        adapter = new ArrayAdapter<>(context, R.layout.map_list_element, mapList);
        list.setAdapter(adapter);
        list.setOnItemClickListener((adapterView, view, i, l) -> {
            TextView textView = (TextView) view;

            Intent myIntent = new Intent(mainActivity, MapActivity.class);

            myIntent.putExtra("value", textView.getText());

            mainActivity.startActivity(myIntent);
        });
    }

    public List<String> getMapList() {
        return mapList;
    }
}
