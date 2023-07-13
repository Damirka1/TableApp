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
    private List<Map> maps;
    private ArrayAdapter<String> adapter;

    private void listShafts() {
        LiteDirectory ld = LiteDirectory.getInstance();
        maps = ld.getShafts();
        mapList.clear();
        maps.forEach((Map m) -> mapList.add(m.getName()));
    }

    public MapListService() {
        mapList = new LinkedList<>();
    }

    public boolean openMap(MainActivity mainActivity) {
        LiteDirectory ld = LiteDirectory.getInstance();
        maps = ld.getShafts();

        if(maps.size() == 0)
            return false;

        Map map = maps.get(0);

        Intent myIntent = new Intent(mainActivity, MapActivity.class);

        myIntent.putExtra("value", map.getName());

        myIntent.putExtra("id", map.getId());

        mainActivity.startActivity(myIntent);
        return true;
    }

    public void setList(ListView list, android.content.Context context, MainActivity mainActivity) {

        Thread t = new Thread(this::listShafts);

        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        adapter = new ArrayAdapter<>(context, R.layout.map_list_element, mapList);
        list.setAdapter(adapter);
        list.setOnItemClickListener((adapterView, view, i, l) -> {
            TextView textView = (TextView) view;

            Intent myIntent = new Intent(mainActivity, MapActivity.class);

            myIntent.putExtra("value", textView.getText());

            long id = 0;

            for(Map m : maps) {
                if(m.getName() == textView.getText())
                    id = m.getId();
            }

            myIntent.putExtra("id", id);

            mainActivity.startActivity(myIntent);
        });
    }

    public List<String> getMapList() {
        return mapList;
    }
}
