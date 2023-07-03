package com.arcelormittal.tableapptest.services;

import android.content.Intent;
import android.content.res.AssetManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.arcelormittal.tableapptest.DocumentActivity;
import com.arcelormittal.tableapptest.MapActivity;
import com.arcelormittal.tableapptest.R;
import com.arcelormittal.tableapptest.room.entities.Point;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PositionsListService {
    private final List<String> mapList;
    private final List<Point> points;
    private final ArrayAdapter<String> adapter;

    private final ListView posList;

    private final ListView searchList;

    private final String mapDocPath;

    private final android.content.Context context;

    private final MapActivity mapActivity;

    private List<Point> loadPoints(String path, AssetManager assets) {
        List<Point> pointList = new LinkedList<>();

        BufferedReader reader;

        try {
            reader = new BufferedReader(new InputStreamReader(assets.open(path)));
            String line = reader.readLine();

            while (line != null) {
                String[] res = line.split(" ");

                Point point = new Point(Integer.parseInt(res[0]), Integer.parseInt(res[1]), Integer.parseInt(res[2]), res[3].toLowerCase());
                pointList.add(point);

                // read next line
                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pointList;
    }

    private boolean listAssetFiles(String path, AssetManager assets) {
        String [] list;
        try {
            list = assets.list(path);
            if (list.length > 0) {
                // This is a folder
                for (String file : list) {
                    if (!listAssetFiles(path + "/" + file, assets))
                        return false;
                    else {
                        // This is a file
                        mapList.add(file.substring(0, file.indexOf('.')));
                    }
                }
            }
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public void openFile(String file) {
        Intent myIntent = new Intent(mapActivity, DocumentActivity.class);

        myIntent.putExtra("value", mapDocPath + file + ".pdf");

        mapActivity.startActivity(myIntent);
    }

    public PositionsListService(String path, android.content.Context context, ListView posList, ListView searchList, MapActivity mapActivity) {
        this.context = context;
        this.mapActivity = mapActivity;

        this.posList = posList;
        this.searchList = searchList;

        this.mapDocPath = path + "/Документы/";
        mapList = new LinkedList<>();
        listAssetFiles(mapDocPath, context.getAssets());

        this.points = loadPoints(path + "/Точки.txt", context.getAssets());

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
