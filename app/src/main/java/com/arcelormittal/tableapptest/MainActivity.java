package com.arcelormittal.tableapptest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.room.Room;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.arcelormittal.tableapptest.services.LiteDirectory;
import com.arcelormittal.tableapptest.services.MapListService;
import com.arcelormittal.tableapptest.services.MapUpdateService;
import com.arcelormittal.tableapptest.room.RoomDb;

public class MainActivity extends AppCompatActivity {

    // Inflater
    private LayoutInflater vi;

    // Buttons
    private Button generalBtn;
    private Button mapsBtn;
    private Button settingsBtn;

    // View layout
    private ConstraintLayout viewLayout;

    // Services
    private MapListService mapListService;
    private MapUpdateService mapUpdateService;

    private void setTabView(View v) {
        viewLayout.removeAllViews();
        viewLayout.addView(v);
    }

    private void createServices() {
        RoomDb room = Room.databaseBuilder(getApplicationContext(), RoomDb.class, "pla")
                .fallbackToDestructiveMigration().build();
        LiteDirectory.createInstance(room);

        mapListService = new MapListService();

        mapUpdateService = new MapUpdateService(mapListService);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createServices();

        vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        generalBtn = findViewById(R.id.GeneralButton);
        mapsBtn = findViewById(R.id.MapsButton);
        settingsBtn = findViewById(R.id.SettingsButton);

        viewLayout = findViewById(R.id.TabView);

        generalBtn.setOnClickListener(view -> {
            View v = vi.inflate(R.layout.general_view, null);
            setTabView(v);
        });

        generalBtn.performClick();

        mapsBtn.setOnClickListener(view -> {
            View v = vi.inflate(R.layout.maps_list, null);

            ListView list = v.findViewById(R.id.MapList);

            mapListService.setList(list, getApplicationContext(), this);

            setTabView(v);
        });

        settingsBtn.setOnClickListener(view -> {
            View v = vi.inflate(R.layout.settings_view, null);
            setTabView(v);
        });
    }
}