package com.example.dtcbuspass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class PassHistory extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_history);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        loadRecyclerViewData();
    }

    private void loadRecyclerViewData() {


        adapter = new PassHistoryAdapter(getApplicationContext());
        recyclerView.setAdapter(adapter);
    }
}
