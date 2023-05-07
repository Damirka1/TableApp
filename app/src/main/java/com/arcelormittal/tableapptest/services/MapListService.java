package com.arcelormittal.tableapptest.services;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.arcelormittal.tableapptest.MainActivity;
import com.arcelormittal.tableapptest.MapActivity;
import com.arcelormittal.tableapptest.R;

import org.w3c.dom.Text;

import java.util.List;

public class MapListService {
    private List<String> mapList;
    private ArrayAdapter<String> adapter;

    private ListView list;

    public MapListService(android.content.Context context, ListView list, MainActivity mainActivity) {
        this.list = list;
        mapList = List.of("Казахстанская", "Тест");
        adapter = new ArrayAdapter<>(context, R.layout.map_list_element, mapList);
        list.setAdapter(adapter);
        list.setOnItemClickListener((adapterView, view, i, l) -> {
            TextView textView = (TextView) view;
//            Toast.makeText(context, textView.getText(), Toast.LENGTH_SHORT).show();

            Intent myIntent = new Intent(mainActivity, MapActivity.class);

            myIntent.putExtra("value", textView.getText());

            mainActivity.startActivity(myIntent);
        });
    }
}
