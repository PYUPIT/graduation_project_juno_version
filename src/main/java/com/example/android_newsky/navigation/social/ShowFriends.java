package com.example.android_newsky.navigation.social;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.android_newsky.Main_Activity;
import com.example.android_newsky.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class ShowFriends extends Fragment {

    private ListView listview = null;
    private ListViewAdapter adapter = null;
    private String[] colorArray = {"#D1DDDB", "#85B8CB", "#1D6A96", "#283B42", "#BD8E62", "#A46843", "#370D00", "#7FB174", "#689C97", "#072A24"};

    String[] list_line;
    String[] list_name;
    String[] list_email;
    String[] list_id;

    private final String directoryName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Nokmusae";
    private final String fileName = "61895144.txt";
    private final String filePath = directoryName + "/" + fileName;

    FileInputStream fileInputStreamCounter = null;
    BufferedReader bufferedReaderCounter = null;

    FileInputStream fileInputStreamLoader = null;
    BufferedReader bufferedReaderLoader = null;

    Main_Activity mainActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (Main_Activity)getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_menu_social, container, false);

        adapter = new ListViewAdapter();

        listview = rootView.findViewById(R.id.list);
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

                Intent intent = new Intent(getContext(), ShowFriends_ShowProfile.class);

                ListViewItem item = (ListViewItem) parent.getItemAtPosition(position) ;

                intent.putExtra("profile", item.getProfile());
                intent.putExtra("profId", item.getProfId());
                intent.putExtra("name", item.getTitle());
                intent.putExtra("email", item.getDesc());

                startActivity(intent);
            }
        });

        Button addItem = (Button)rootView.findViewById(R.id.addItem);

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ShowFriends_AddFriend.class);
                startActivity(intent);
            }
        });

        return rootView;
    }
}