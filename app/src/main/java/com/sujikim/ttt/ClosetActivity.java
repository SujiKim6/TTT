package com.sujikim.ttt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ClosetActivity extends AppCompatActivity {

    String[] Clothes =new String[] {"긴 팔","반 팔","긴바지","반바지","두꺼운 외투", "얇은 외투"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closet);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, Clothes);

        ListView listView = (ListView) findViewById(R.id.MyList);
        listView.setAdapter(adapter);

        //목록을눌렀을때 전환되는 것
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectString = (String)parent.getItemAtPosition(position);

                Intent longT = new Intent(ClosetActivity.this, LongTActivity.class);
                ClosetActivity.this.startActivity(longT);

            }
        });

    }
}
