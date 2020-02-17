package com.example.meufortinite.VIEW;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.meufortinite.MODEL.Store;
import com.example.meufortinite.R;
import com.example.meufortinite.VIEW.ADAPTER.StoreAdapter;

import java.util.ArrayList;

public class StoreActivity extends AppCompatActivity {

    private ListView storeitem;
    private ArrayList<Store> stores = new ArrayList<Store>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        stores = getIntent().getParcelableArrayListExtra("stores");
        wireWidgets();
        populateListView();


    }


    private void populateListView() {

        StoreAdapter adapter = new StoreAdapter(StoreActivity.this, R.layout.storeitemdisplay, stores);
        storeitem.setAdapter(adapter);


    }

    private void wireWidgets() {
        storeitem = findViewById(R.id.listview_storeactivity);
    }






}
