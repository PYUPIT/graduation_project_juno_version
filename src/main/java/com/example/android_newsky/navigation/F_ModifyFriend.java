package com.example.android_newsky.navigation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

import com.example.android_newsky.R;


public class F_ModifyFriend extends Activity {

    TextView profile = null;
    String profId = null;

    final static String folderName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/friend";
    final static String fileName = "friendInfo05.txt";
    final static String filePath = folderName + "/" + fileName;

    private EditText editTextName;
    private EditText editTextEmail;

    FileOutputStream fileOutputStreamValue = null;
    BufferedWriter bufferedWriterValue = null;

    private FileInputStream fileInputStreamTemp = null;
    private BufferedReader bufferedReaderTemp = null;

    private FileOutputStream fileOutputStreamTemp = null;
    private BufferedWriter bufferedWriterTemp = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dsadsa6);

        Intent intent = getIntent();

        profile = (TextView) findViewById(R.id.textView100);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);

        profile.setText(intent.getStringExtra("profile"));
        editTextName.setText(intent.getStringExtra("name"));
        editTextEmail.setText(intent.getStringExtra("email"));

        profId = intent.getStringExtra("profId");
    }

    public void saveBtnClick(View view) {

        String editIndex = editTextName.getText().toString();

        if(editIndex.length() > 0)  {
            Modifying();
        }
        else {
            Toast.makeText(getApplicationContext(),"이름을 입력해주세요", Toast.LENGTH_LONG).show();
        }

        Intent intent = new Intent(F_ModifyFriend.this, Friend_View.class);

        startActivity(intent);
        finish();
    }

    public void Modifying() {

        String contents = profId + " " + editTextName.getText() + " " + editTextEmail.getText();

        try {

            fileInputStreamTemp = new FileInputStream(filePath);
            bufferedReaderTemp = new BufferedReader(new InputStreamReader(fileInputStreamTemp));

            String line = bufferedReaderTemp.readLine();
            StringTokenizer tempToken = null;

            String dummy = "";

            while(line != null) {
                tempToken = new StringTokenizer(line);

                if(profId.equals(tempToken.nextToken())) {
                    dummy = dummy + contents + "\n";
                }

                else {
                    dummy = dummy + line + "\n";
                }

                line = bufferedReaderTemp.readLine();
            }

            fileOutputStreamTemp = new FileOutputStream(filePath, false);
            bufferedWriterTemp = new BufferedWriter(new OutputStreamWriter(fileOutputStreamTemp));

            bufferedWriterTemp.write(dummy);
            bufferedWriterTemp.flush();
            bufferedWriterTemp.close();

        } catch (IOException e){ e.printStackTrace(); }
    }
}