package com.example.android_newsky.navigation;

import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.android_newsky.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class File_Write extends AppCompatActivity {

    final static String folderName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/friend";
    final static String fileName = "friendInfo.txt";

    TextView txtRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dsadsa7);

        txtRead = (TextView)findViewById(R.id.txtRead);
    }

    public void mOnFileWrite(View v) {
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String contents = "로그 생성 : " + now + "\n";

        WriteTextFile(folderName, fileName, contents);
    }

    //텍스트내용을 경로의 텍스트 파일에 쓰기
    public void WriteTextFile(String folder_name, String file_name, String contents) {

        try {
            File dir = new File(folder_name);
            //디렉토리 폴더가 없으면 생성함
            if(!dir.exists()) {
                dir.mkdir();
            }
            //파일 output stream 생성
            FileOutputStream fos = new FileOutputStream(folder_name + "/" + file_name, true);
            //파일쓰기
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));

            writer.write(contents);
            writer.flush();

            writer.close();
            fos.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void mOnFileRead(View v){
        String read = ReadTextFile(folderName + "/" + fileName);
        txtRead.setText(read);
    }
    //경로의 텍스트 파일읽기
    public String ReadTextFile(String path) {
        StringBuffer strBuffer = new StringBuffer();

        try{
            InputStream is = new FileInputStream(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line="";

            while((line=reader.readLine())!=null){
                strBuffer.append(line + "\n");
            }

            reader.close();
            is.close();
        }catch (IOException e){
            e.printStackTrace();
            return "";
        }
        return strBuffer.toString();
    }
}