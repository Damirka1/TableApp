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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arcelormittal.tableapptest.services.LiteDirectory;
import com.arcelormittal.tableapptest.services.MapListService;
import com.arcelormittal.tableapptest.services.MapUpdateService;
import com.arcelormittal.tableapptest.room.RoomDb;
import com.arcelormittal.tableapptest.services.UserService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

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

    // Threads
    private Thread downloadThread;

    private void setTabView(View v) {
        viewLayout.removeAllViews();
        viewLayout.addView(v);
    }

    private void createServices() {
        RoomDb room = Room.databaseBuilder(getApplicationContext(), RoomDb.class, "pla")
                .fallbackToDestructiveMigration().build();
        LiteDirectory.createInstance(room);

        mapListService = new MapListService();

        mapUpdateService = new MapUpdateService();
    }

    private void checkMapDownloading(View v)
    {
        TextView loadingText = v.findViewById(R.id.LoadingText);

        ProgressBar loadingProgressBar = v.findViewById(R.id.LoadingProgressBar);

        if(mapUpdateService.isDownloading()) {
            loadingText.setVisibility(View.VISIBLE);
            loadingProgressBar.setVisibility(View.VISIBLE);

            if(Objects.nonNull(downloadThread)) {
                downloadThread.interrupt();
                downloadThread = null;
            }

            downloadThread = new Thread(() -> {
                while(mapUpdateService.isDownloading()) {
                    loadingProgressBar.setProgress(mapUpdateService.getProgress());

                    if(Thread.currentThread().isInterrupted())
                        return;

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        return;
                    }
                }

                runOnUiThread(() -> {
                    loadingText.setVisibility(View.GONE);
                    loadingProgressBar.setVisibility(View.GONE);
                });

            });
            downloadThread.start();
        }
        else
        {
            loadingText.setVisibility(View.GONE);
            loadingProgressBar.setVisibility(View.GONE);
        }
    }

    private void openGeneral(View view) {
        View v = vi.inflate(R.layout.general_view, viewLayout, false);

        new Thread(() -> {
            TextView textInfo = v.findViewById(R.id.TextInfo);

            TextInputEditText codeInput = v.findViewById(R.id.CodeTextInput);
            TextInputLayout codeInputLayout = v.findViewById(R.id.CodeTextInputLayout);

            Button confirm = v.findViewById(R.id.CodeConfirm);

            if(UserService.getInstance().isFirstStartUp()) {
                codeInputLayout.setVisibility(View.VISIBLE);
                textInfo.setVisibility(View.VISIBLE);
                codeInput.setVisibility(View.VISIBLE);
                confirm.setVisibility(View.VISIBLE);

                confirm.setOnClickListener(v1 -> {
                    if(Objects.nonNull(codeInput.getText())) {
                        String code = codeInput.getText().toString();

                        code = "Казахстанская";

                        UserService.getInstance().saveCode(code);

                        codeInputLayout.setVisibility(View.GONE);
                        textInfo.setVisibility(View.GONE);
                        codeInput.setVisibility(View.GONE);
                        confirm.setVisibility(View.GONE);

                        mapUpdateService.forceDownload();

                        checkMapDownloading(v);
                    }
                });
            }
            else
            {
                TextView updateText = v.findViewById(R.id.UpdateText);
                Button updateButton = v.findViewById(R.id.DownloadUpdateButton);

                if(mapUpdateService.checkUpdate())
                {
                    updateText.setText("Доступно обновление");

                    runOnUiThread(() -> {
                        updateButton.setVisibility(View.VISIBLE);
                        updateText.setVisibility(View.VISIBLE);
                    });

                    updateButton.setOnClickListener((View v2) -> {
                        updateButton.setVisibility(View.GONE);
                        mapUpdateService.forceDownload();
                        checkMapDownloading(v);

                        updateText.setText("Обновлений нет");
                    });

                }
                else
                {
                    runOnUiThread(() -> {
                        updateText.setText("Обновлений нет");
                        updateText.setVisibility(View.VISIBLE);
                        checkMapDownloading(v);
                    });
                }
            }

        }).start();

        setTabView(v);
    }

    private void openMaps(View view) {
        View v = vi.inflate(R.layout.maps_list, viewLayout, false);
//        ListView list = v.findViewById(R.id.MapList);
//        mapListService.setList(list, getApplicationContext(), this);

        new Thread(() -> {

            if(!mapListService.openMap(this))
                runOnUiThread(() -> Toast.makeText(this, "У вас не установлена карта", Toast.LENGTH_LONG).show());

        }).start();

//        setTabView(v);
    }

    private void openSettings(View view) {
        View v = vi.inflate(R.layout.settings_view, viewLayout, false);

        // Setting buttons
        Button roomResetBtn = v.findViewById(R.id.RoomReset);
        Button roomDownloadBtn = v.findViewById(R.id.RoomDownload);

        roomResetBtn.setOnClickListener(resetView -> mapUpdateService.forceClear());

        roomDownloadBtn.setOnClickListener(resetView -> mapUpdateService.forceDownload());

        // Set info in settings
        if(!UserService.getInstance().isFirstStartUp()) {
            TextView textInfo = v.findViewById(R.id.UserCodeText);
            TextView codeInfo = v.findViewById(R.id.UserCodeTextToShow);

            codeInfo.setText(UserService.getInstance().getCode());

            textInfo.setVisibility(View.VISIBLE);
            codeInfo.setVisibility(View.VISIBLE);
        }

        setTabView(v);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create all services
        createServices();

        // Get Inflater
        vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Get all buttons
        generalBtn = findViewById(R.id.GeneralButton);
        mapsBtn = findViewById(R.id.MapsButton);
        settingsBtn = findViewById(R.id.SettingsButton);

        // Get tab layouts
        viewLayout = findViewById(R.id.TabView);

        // Set up listeners
        generalBtn.setOnClickListener(this::openGeneral);
        mapsBtn.setOnClickListener(this::openMaps);
        settingsBtn.setOnClickListener(this::openSettings);

        // Open general tab
        generalBtn.performClick();
    }
}