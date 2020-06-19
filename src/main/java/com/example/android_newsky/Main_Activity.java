package com.example.android_newsky;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.android_newsky.navigation.STT.ConvertSpeechToText;
import com.example.android_newsky.navigation.inven.LoadRecordFile;
import com.example.android_newsky.navigation.inven.LoadTextFile;
import com.example.android_newsky.navigation.record.RecordVoice;
import com.example.android_newsky.navigation.social.ShowFriends;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;

public class Main_Activity extends AppCompatActivity {

    final static String directoryName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Nokmusae";

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentTransaction transaction = fragmentManager.beginTransaction();

    private LoadTextFile menu1Fragment = new LoadTextFile();
    private LoadRecordFile menu2Fragment = new LoadRecordFile();
    private ConvertSpeechToText menu3Fragment = new ConvertSpeechToText();
    private RecordVoice menu5Fragment = new RecordVoice();
    private ShowFriends menu4Fragment = new ShowFriends();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main02);

        getPermissionForNokmusae(); // 마이크, 인터넷, 외부저장소에 대한 권한 요청

        File nokmusaeDir = new File(directoryName);
        // 녹무새 앱에서 생성된 녹음파일, 텍스트 파일을 저장할 디렉토리 생성
        if(!nokmusaeDir.exists()) {
            nokmusaeDir.mkdirs();
            Toast.makeText(this, "저장완료", Toast.LENGTH_SHORT).show();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // 첫 화면 지정
        transaction.replace(R.id.main_content, menu4Fragment).commitAllowingStateLoss();

        // bottomNavigationView의 아이템이 선택될 때 호출될 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.action_home: {
                        transaction.replace(R.id.main_content, menu1Fragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.action_check: {
                        transaction.replace(R.id.main_content, menu2Fragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.action_chat: {
                        transaction.replace(R.id.main_content, menu3Fragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.action_account: {
                        transaction.replace(R.id.main_content, menu4Fragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.action_setting: {
                        transaction.replace(R.id.main_content, menu5Fragment).commitAllowingStateLoss();
                        break;
                    }
                }

                return true;
            }
        });
    }

    public void getPermissionForNokmusae()
    {
        if(ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(Main_Activity.this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
        }
    }
}