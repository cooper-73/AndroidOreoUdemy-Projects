package com.example.multipleactivitydemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;

    public void showName() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);

        ArrayList<String> family = new ArrayList<>();

        family.add("Vicente");
        family.add("Faviola");
        family.add("Estefany");
        family.add("Cristhian");
        family.add("Yenfor");
        family.add("Rafael");
        family.add("Paola");

        ArrayAdapter<String> familyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, family);
        listView.setAdapter(familyAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                intent.putExtra("name", family.get(position));
                startActivity(intent);
            }
        });
    }
}