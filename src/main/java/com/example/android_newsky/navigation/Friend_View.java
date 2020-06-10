package com.example.android_newsky.navigation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.example.android_newsky.R;

public class Friend_View extends AppCompatActivity {

    private ListView listOfFriends;
    private ListViewAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dsadsa3);

        ListView listview ;

        // Adapter 생성
        adapter = new ListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter 달기
        listview = (ListView) findViewById(R.id.list);
        listview.setAdapter(adapter);

        adapter.addItem(R.drawable.ic_account, "Box", "Account Box Black 36dp");
        adapter.addItem(R.drawable.ic_account, "Circle", "Account Circle Black 36dp");
        adapter.addItem(R.drawable.ic_account, "Ind", "Assignment Ind Black 36dp");
        adapter.addItem(R.drawable.ic_account, "Ind", "Assignment Ind Black 36dp");
        adapter.addItem(R.drawable.ic_account, "Ind", "Assignment Ind Black 36dp");
        adapter.addItem(R.drawable.ic_account, "Ind", "Assignment Ind Black 36dp");
        adapter.addItem(R.drawable.ic_account, "Ind", "Assignment Ind Black 36dp");
        adapter.addItem(R.drawable.ic_account, "Ind", "Assignment Ind Black 36dp");
        adapter.addItem(R.drawable.ic_account, "Ind", "Assignment Ind Black 36dp");
        adapter.addItem(R.drawable.ic_account, "Ind", "Assignment Ind Black 36dp");

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), F_ShowProfile.class);

                ListViewItem item = (ListViewItem) parent.getItemAtPosition(position) ;

                intent.putExtra("profile", Integer.toString(item.getProfile()));
                intent.putExtra("name", item.getTitle());
                intent.putExtra("email", item.getDesc());

                startActivity(intent);

                // Toast.makeText(getApplicationContext(),"황호선바보", Toast.LENGTH_LONG).show();
            }
        }) ;
    }
}