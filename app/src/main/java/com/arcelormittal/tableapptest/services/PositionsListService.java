package com.arcelormittal.tableapptest.services;

import android.content.Intent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.arcelormittal.tableapptest.DocumentActivity;
import com.arcelormittal.tableapptest.MapActivity;
import com.arcelormittal.tableapptest.R;
import com.arcelormittal.tableapptest.room.entities.Document;
import com.arcelormittal.tableapptest.room.entities.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class PositionsListService {
    private final long shaftId;
    private final List<String> mapList = new LinkedList<>();;
    private List<Point> points;
    private final ArrayAdapter<String> adapter;

    private final ListView posList;

    private final ListView searchList;

//    private final String mapDocPath;

    private final android.content.Context context;

    private final MapActivity mapActivity;

    private void loadPoints() {
        LiteDirectory ld = LiteDirectory.getInstance();
        points = ld.getPointsByShaft(shaftId);
        mapList.addAll(ld.getDocumentsByShaftId(shaftId).stream().map(Document::getName).collect(Collectors.toList()));
        Collections.sort(mapList);
    }

    public void openFile(String file) {
        Intent myIntent = new Intent(mapActivity, DocumentActivity.class);

        myIntent.putExtra("value", file);
        myIntent.putExtra("id", shaftId);

        mapActivity.startActivity(myIntent);
    }

    public PositionsListService(long shaftId, android.content.Context context, ListView posList, ListView searchList, MapActivity mapActivity) {
        this.shaftId = shaftId;

        this.context = context;
        this.mapActivity = mapActivity;

        this.posList = posList;
        this.searchList = searchList;

        Thread t = new Thread(this::loadPoints);
        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, mapList);
        posList.setAdapter(adapter);

        AdapterView.OnItemClickListener listener = (adapterView, view, i, l) -> {
            TextView textView = (TextView) view;

            Toast.makeText(context, textView.getText(), Toast.LENGTH_SHORT).show();

            openFile(textView.getText().toString());
        };

        posList.setOnItemClickListener(listener);
        searchList.setOnItemClickListener(listener);
    }
    public List<String> getMapList() {
        return mapList;
    }

    public void filterList(String searchText) {
        ArrayAdapter<String> newAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1);
        ArrayAdapter<String> newSearchAdapter = new ArrayAdapter<>(context, R.layout.map_list_element);
        List<String> mapList = getMapList();
        List<String> filteredList = new ArrayList<>();

        for (String map : mapList) {
            if (map.toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(map);
            }
        }

        newAdapter.addAll(filteredList);
        newSearchAdapter.addAll(filteredList);

        posList.setAdapter(newAdapter);
        searchList.setAdapter(newSearchAdapter);
    }

    public List<Point> getPoints() {
        return points;
    }
}
