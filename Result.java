package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.appjam22.MainActivity;
import com.example.appjam22.R;

public class Result extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        ListView listview = findViewById(R.id.listview);

        final Intent intent = getIntent();

        String result = intent.getStringExtra("");
        String[] data= result.split(",");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.items, data);
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        Button btn = findViewById(R.id.main);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
    }
}
