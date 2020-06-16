package com.example.android_newsky.navigation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_newsky.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;

public class F_ShowProfile extends Activity {

    TextView profile = null;
    TextView profId = null;
    TextView name = null;
    TextView email = null;

    final static String folderName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/friend";
    final static String fileName = "friendInfo05.txt";
    final static String filePath = folderName + "/" + fileName;

    FileInputStream fileInputStreamTemp = null;
    BufferedReader bufferedReaderTemp = null;

    FileOutputStream fileOutputStreamTemp = null;
    BufferedWriter bufferedWriterTemp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dsadsa5);

        Intent intent = getIntent();

        profile = (TextView) findViewById(R.id.profile);
        profId = (TextView) findViewById(R.id.prof_id);
        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);

        profile.setText(intent.getStringExtra("profile"));
        profId.setText(intent.getStringExtra("profId"));
        name.setText(intent.getStringExtra("name"));
        email.setText(intent.getStringExtra("email"));
    }

    public void ModifyProfile(View view) {

        Intent intent = new Intent(F_ShowProfile.this, F_ModifyFriend.class);

        intent.putExtra("profile", profile.getText());
        intent.putExtra("profId", profId.getText());
        intent.putExtra("name", name.getText());
        intent.putExtra("email", email.getText());

        startActivity(intent);
    }

    public void SendEmail (View view) {

        Intent email2 = new Intent(Intent.ACTION_SEND);

        email2.setType("plain/text");

        String address = email.getText().toString();

        email2.putExtra(Intent.EXTRA_EMAIL, address);
        email2.putExtra(Intent.EXTRA_SUBJECT, "[녹무새]에서 발신");
        email2.putExtra(Intent.EXTRA_TEXT, "황호선 임베디드 0점 가즈아.");

        startActivity(email2);
    }

    public void DeleteProfile(View view) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        dialog.setTitle("알림창");
        dialog.setMessage("삭제하시겠습니까?");

        dialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Deleting();
            }});
        dialog.setNegativeButton("아니오", null);
        dialog.create().show();
    }

    public void Deleting() {

        try {
            fileInputStreamTemp = new FileInputStream(filePath);
            bufferedReaderTemp = new BufferedReader(new InputStreamReader(fileInputStreamTemp));

            String line = bufferedReaderTemp.readLine();
            StringTokenizer tempToken = null;

            String dummy = "";



            while(line != null) {

                tempToken = new StringTokenizer(line);

                if(!profId.getText().equals(tempToken.nextToken())){
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

        Intent intent = new Intent(F_ShowProfile.this, Friend_View.class);

        startActivity(intent);
        finish();
    }
}