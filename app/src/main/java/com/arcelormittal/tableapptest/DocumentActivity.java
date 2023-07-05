package com.arcelormittal.tableapptest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.arcelormittal.tableapptest.room.entities.Document;
import com.arcelormittal.tableapptest.services.LiteDirectory;
import com.github.barteksc.pdfviewer.PDFView;

import java.util.Objects;

public class DocumentActivity extends AppCompatActivity {

    private PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);

        pdfView = findViewById(R.id.pdfView);

        Intent intent = getIntent();
        String filename = intent.getStringExtra("value");
        long shaftId = intent.getLongExtra("id", 0);

        Context context = getApplicationContext();

        new Thread(() -> {
            LiteDirectory ld = LiteDirectory.getInstance();

            Document d = ld.getDocumentByShaftIdAndName(shaftId, filename);

            if(Objects.nonNull(d)) {
                pdfView.fromBytes(d.getFile())
                        .password(null) // if password protected, then write password
                        .defaultPage(0) // set the default page to open
                        .onPageError((page, t) -> {
                            Toast.makeText(
                                    context,
                                    String.format("Ошибка при загрузке страницы -  {%s}", page), Toast.LENGTH_LONG
                            ).show();
                        })
                        .load();
            }
            else {
                Toast.makeText(
                        context,
                        String.format("Файла для данной точки не существует -  {%s}", filename), Toast.LENGTH_LONG
                ).show();
            }
        }).start();
    }
}