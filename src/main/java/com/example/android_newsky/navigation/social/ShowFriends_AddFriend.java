package com.example.android_newsky.navigation.social;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.example.android_newsky.Main_Activity;
import com.example.android_newsky.R;

public class ShowFriends_AddFriend extends AppCompatActivity {

    final static String folderName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/friend";
    final static String fileName = "friendInfo05.txt";
    final static String filePath = folderName + "/" + fileName;

    final static String idFilePath = folderName + "/" + "id.txt";

    private EditText editTextName;
    private EditText editTextEmail;

    FileOutputStream fileOutputStreamNumber = null;
    BufferedWriter  bufferedWriterNumber = null;

    FileInputStream fileInputStreamNumber = null;
    BufferedReader bufferedReaderNumber = null;

    FileOutputStream fileOutputStreamValue = null;
    BufferedWriter bufferedWriterValue = null;

    String temp;
    int temp2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_add);
    }

    public void saveBtnClick(View view) {

        editTextName = (EditText)findViewById(R.id.editTextName);
        editTextEmail = (EditText)findViewById(R.id.editTextEmail);

        String editIndex = editTextName.getText().toString();

        try {
            File dir = new File(folderName);

            if (!dir.exists()) { dir.mkdir(); } // 폴더가 존재하지 않는다면 폴더 생성

            fileInputStreamNumber = new FileInputStream(idFilePath);
            bufferedReaderNumber = new BufferedReader(new InputStreamReader(fileInputStreamNumber));

            if(bufferedReaderNumber.readLine() == null) {
                fileOutputStreamNumber = new FileOutputStream(idFilePath, false);
                bufferedWriterNumber = new BufferedWriter(new OutputStreamWriter(fileOutputStreamNumber));

                bufferedWriterNumber.write("0");
                bufferedWriterNumber.flush();

                bufferedWriterNumber.close();
                bufferedWriterNumber.close();
            }
        } catch (IOException e) { e.printStackTrace(); }

        try {
            FileInputStream fileInputStreamNumber = new FileInputStream(idFilePath);
            BufferedReader bufferedReaderNumber = new BufferedReader(new InputStreamReader(fileInputStreamNumber));

            temp = bufferedReaderNumber.readLine();
            temp2 = Integer.parseInt(temp);
            temp2++;
            temp = String.valueOf(temp2);
        } catch (IOException e){ e.printStackTrace(); }

        try {
            File dir = new File(folderName);

            if (!dir.exists()) { dir.mkdir(); } // 폴더가 존재하지 않는다면 폴더 생성

            fileOutputStreamNumber = new FileOutputStream(idFilePath, false);
            bufferedWriterNumber = new BufferedWriter(new OutputStreamWriter(fileOutputStreamNumber));

            bufferedWriterNumber.write(temp);
            bufferedWriterNumber.flush();

            bufferedWriterNumber.close();
            bufferedWriterNumber.close();
        } catch (IOException e) { e.printStackTrace(); }

        String contents = temp + " " + editTextName.getText() + " " + editTextEmail.getText() + "\n";

        if(editIndex.length() > 0)  {

            try {
                File dir = new File(folderName);

                if (!dir.exists()) { dir.mkdir(); } // 폴더가 존재하지 않는다면 폴더 생성

                fileOutputStreamValue = new FileOutputStream(filePath, true);
                bufferedWriterValue = new BufferedWriter(new OutputStreamWriter(fileOutputStreamValue));

                bufferedWriterValue.write(contents);
                bufferedWriterValue.flush();

                bufferedWriterValue.close();
                fileOutputStreamValue.close();

                Intent intent = new Intent(com.example.android_newsky.navigation.social.ShowFriends_AddFriend.this, Main_Activity.class);

                startActivity(intent);
                finish();
            } catch (IOException e) { e.printStackTrace(); }
        }
        else {
            Toast.makeText(getApplicationContext(), "이름을 입력해주세요", Toast.LENGTH_LONG).show();
        }
    }
}