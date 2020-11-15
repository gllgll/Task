package com.example.task;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class WeiBo extends AppCompatActivity {

    private String[] core = {"Python", "Java", "C", "数据库", "操作系统", "大道朝天", "a", "b", "c", "Python", "Java", "C", "数据库", "操作系统", "大道朝天", "a", "b", "c"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wei_bo);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(WeiBo.this, android.R.layout.simple_list_item_1, core);
        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                finish();
            }
        });
    }
}