package com.example.android_newsky.navigation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_newsky.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Friend_View extends AppCompatActivity {

    private ListView listview = null;
    private ListViewAdapter adapter = null;
    private String[] colorArray = {"#D1DDDB", "#85B8CB", "#1D6A96", "#283B42", "#BD8E62", "#A46843", "#370D00", "#7FB174", "#689C97", "#072A24"};

    String[] list_line;
    String[] list_name;
    String[] list_email;
    String[] list_id;

    final static String folderName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/friend";
    final static String fileName = "friendInfo05.txt";
    final static String filePath = folderName + "/" + fileName;

    FileInputStream fileInputStreamCounter = null;
    BufferedReader bufferedReaderCounter = null;

    FileInputStream fileInputStreamLoader = null;
    BufferedReader bufferedReaderLoader = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dsadsa3);

        adapter = new ListViewAdapter();

        // 리스트뷰 참조 및 Adapter 달기

        listview = (ListView) findViewById(R.id.list);
        listview.setAdapter(adapter);

        int countLine = 0;

        try {
            fileInputStreamCounter = new FileInputStream(filePath);
            bufferedReaderCounter = new BufferedReader(new InputStreamReader(fileInputStreamCounter));

            fileInputStreamLoader = new FileInputStream(filePath);
            bufferedReaderLoader = new BufferedReader(new InputStreamReader(fileInputStreamLoader));

            while(bufferedReaderCounter.readLine() != null) {
                countLine ++;
            }
        } catch (IOException e){ e.printStackTrace(); }

        try {
            list_line = new String[countLine];

            for(int i = 0; i < countLine; i++) {
                list_line[i] = bufferedReaderLoader.readLine();
            }
        } catch (IOException e){ e.printStackTrace(); }

        GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(this, R.drawable.circleforprofile);
        TextView ivShape = (TextView) findViewById(R.id.textView0);

        // drawable.setColor(Color.parseColor(colorArray[5]));

        list_id = new String[countLine];
        list_name = new String[countLine];
        list_email = new String[countLine];

        StringTokenizer st = null;

        for(int i = 0; i < countLine; i++) {
            st = new StringTokenizer(list_line[i]);

            list_id[i] = st.nextToken();
            list_name[i] = st.nextToken();
            list_email[i] = st.nextToken();

            adapter.addItem(String.valueOf(list_name[i].charAt(0)), list_id[i], list_name[i], list_email[i]);
        }


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), F_ShowProfile.class);

                ListViewItem item = (ListViewItem) parent.getItemAtPosition(position) ;

                intent.putExtra("profile", item.getProfile());
                intent.putExtra("profId", item.getProfId());
                intent.putExtra("name", item.getTitle());
                intent.putExtra("email", item.getDesc());

                startActivity(intent);
            }
        });

        Button addItem = (Button)findViewById(R.id.addItem);

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Friend_View.this, F_AddFriend_Activity.class);

                startActivity(intent);
            }
        });
    }
}