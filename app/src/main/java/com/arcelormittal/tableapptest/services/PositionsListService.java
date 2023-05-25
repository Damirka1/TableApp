package com.arcelormittal.tableapptest.services;

import android.content.Intent;
import android.content.res.AssetManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.arcelormittal.tableapptest.DocumentActivity;
import com.arcelormittal.tableapptest.MainActivity;
import com.arcelormittal.tableapptest.MapActivity;
import com.arcelormittal.tableapptest.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PositionsListService {
    private List<String> mapList;
    private ArrayAdapter<String> adapter;

    private ArrayList<String> originalList;

    private ListView list;

    private String mapPath;

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

    public PositionsListService(String path, android.content.Context context, ListView list, MapActivity mapActivity) {
        this.list = list;
        this.mapPath = path;
        mapList = new LinkedList<>();
        listAssetFiles(path, context.getAssets());
        adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, mapList);
        list.setAdapter(adapter);

        list.setOnItemClickListener((adapterView, view, i, l) -> {
            TextView textView = (TextView) view;

            Toast.makeText(context, textView.getText(), Toast.LENGTH_SHORT).show();

            Intent myIntent = new Intent(mapActivity, DocumentActivity.class);

            myIntent.putExtra("value", path + textView.getText() + ".pdf");

            mapActivity.startActivity(myIntent);

            // Создание копии исходного списка
            originalList = new ArrayList<>(mapList);

        });

    }
    public List<String> getMapList() {
        return mapList;
    }
}
