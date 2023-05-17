package com.arcelormittal.tableapptest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;

public class DocumentActivity extends AppCompatActivity {

    private PDFView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);

        pdfView = findViewById(R.id.pdfView);

        Intent intent = getIntent();
        String shaft = intent.getStringExtra("value");

        pdfView.fromAsset(shaft)
                .password(null) // if password protected, then write password
                .defaultPage(0) // set the default page to open
                .onPageError((page, t) -> {
                    Toast.makeText(
                            this,
                            String.format("Ошибка при загрузке страницы -  {%s}", page), Toast.LENGTH_LONG
                    ).show();
                })
                .load();
    }
}