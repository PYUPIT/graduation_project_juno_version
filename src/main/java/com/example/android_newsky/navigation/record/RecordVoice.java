package com.example.android_newsky.navigation.record;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android_newsky.R;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;

import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import com.example.android_newsky.navigation.RecorderPlayerActivity;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;

import me.itangqi.waveloadingview.WaveLoadingView;

@RequiresApi(api = Build.VERSION_CODES.O)

public class RecordVoice extends Fragment {

    private MediaPlayer player;
    private MediaRecorder recorder;

    private final File file = Environment.getExternalStorageDirectory();

    String dataTimeNow = LocalDateTime.now().toString();

    private final String PATH = file.getAbsolutePath() + "/" + dataTimeNow + "_recorder.mp3";

    private String saveFilePath = null;
    private int position;
    String dirPath = file.getAbsoluteFile() + "/Recorder";

    File fileFolder;


    private Timer t = null;
    private Timer t2 = null;

    TextView textView;

    private int seconds = 0;
    private int minute = 0;
    private int hour = 0;

    private int temp1 = 0;
    private int temp3 = 0;
    private int temp2 = 0;

    WaveLoadingView waveLoadingView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_menu_record, container, false);

        fileFolder = new File(dirPath);

        if(!fileFolder.exists()) {
            fileFolder.mkdirs();
            Toast.makeText(getContext(), "재생을 시작합니다!", Toast.LENGTH_LONG).show();
        }

        textView = (TextView)rootView.findViewById(R.id.text_time);

        t = new Timer("hello", true);

        waveLoadingView = (WaveLoadingView)rootView.findViewById(R.id.waveLoadingView);

        Button btnForRecord = rootView.findViewById(R.id.record);
        btnForRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButton5Clicked();
            }
        });

        Button btnForPlay = rootView.findViewById(R.id.play);
        btnForPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickPlayBtn();
            }
        });

        Button btnForStop = rootView.findViewById(R.id.stop);
        btnForStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickStopBtn();
            }
        });

        return rootView;
    }

    public void ClickPlayBtn() {
        Log.d("RecordActivity", "시작 버튼 클릭됨!");

        FileInputStream fis = null;
        try {
            try {
                KillPlayer();

                player = new MediaPlayer();

                fis = new FileInputStream(saveFilePath);

                FileDescriptor fd = fis.getFD();

                player.setDataSource(fd);
                player.prepare();
                player.start();

                seconds = 0;
                minute = 0;
                hour = 0;

                t2 = new Timer("second", true);

                temp2 = 0;
                temp3 = (int) player.getDuration();
                temp3 /= 1000;

                t2.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        textView.post(new Runnable() {

                            public void run() {
                                seconds++;
                                if (seconds == 60) {
                                    seconds = 0;
                                    minute++;
                                }
                                if (minute == 60) {
                                    minute = 0;
                                    hour++;
                                }
                                textView.setText(""
                                        + (hour > 9 ? hour : ("0" + hour)) + " : "
                                        + (minute > 9 ? minute : ("0" + minute))
                                        + " : "
                                        + (seconds > 9 ? seconds : "0" + seconds));

                                temp2 ++;

                                if (temp2 >= temp3) {
                                    t2.cancel();
                                }
                            }
                        });
                    }
                }, 1000, 1000);

                if(temp2 > temp3) {
                    t2.cancel();
                }



            } catch (IOException e) {
                e.printStackTrace();
            }

            Toast.makeText(getContext(), "재생을 시작합니다!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), "재생할 파일이 존재하지 않습니다!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void ClickStopBtn() {
        Log.d("RecordActivity", "일시정지 버튼 클릭됨!");

        if (player != null && player.isPlaying()) {
            position = player.getCurrentPosition();
            player.pause();
            ;
        }

        t2.cancel();

        Toast.makeText(getContext(), "재생을 일시정지 합니다!", Toast.LENGTH_LONG).show();
    }

    public void onButton4Clicked(View v) {
        Log.d("RecordActivity", "중지 버튼 클릭됨!");

        if (player != null && player.isPlaying()) {
            player.stop();
        }
        Toast.makeText(getContext(), "재생을 중지합니다!", Toast.LENGTH_LONG).show();
    }

    public void onButton3Clicked(View v) {
        Log.d("RecordActivity", "목록 버튼 클릭됨!");

//        if (player != null && !player.isPlaying()) {
//            player.start();
//            player.seekTo(position);
//        }

        Intent intent = new Intent(getActivity(), RecorderPlayerActivity.class);
        startActivity(intent);
    }

    private int index_rdc = 0;

    public void onButton5Clicked() {

        if(index_rdc == 0) {
            RunTime();
            recordAudio();
            index_rdc = 1;
        }
        else {
            if (recorder != null) {


                recorder.stop();

                System.out.println("111111111111111111111111111111111");

                recorder.release();
                recorder = null;

                Toast.makeText(getContext(), "녹음을 중지합니다!", Toast.LENGTH_LONG).show();
            }
            index_rdc = 0;
            t.cancel();
        }
    }


    public void RunTime() {

        temp1 = 0;

        t.schedule(new TimerTask() {

            @Override
            public void run() {
                textView.post(new Runnable() {

                    public void run() {
                        seconds++;
                        if (seconds == 60) {
                            seconds = 0;
                            minute++;
                        }
                        if (minute == 60) {
                            minute = 0;
                            hour++;
                        }
                        textView.setText(""
                                + (hour > 9 ? hour : ("0" + hour)) + " : "
                                + (minute > 9 ? minute : ("0" + minute))
                                + " : "
                                + (seconds > 9 ? seconds : "0" + seconds));

                        waveLoadingView.setProgressValue(temp1);
                        temp1++;
                    }
                });

            }
        }, 1000, 1000);
    }

    private void recordAudio() {
        try {
            System.out.println(recorder);

            if (recorder != null) {
                recorder.stop();
                recorder.release();
                recorder = null;
            }

            saveFilePath = fileFolder + "/Recorder" + LocalDateTime.now().toString()+ "_recorder.mp3";

            recorder = new MediaRecorder();

            try {
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                recorder.setOutputFile(saveFilePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //System.out.println(PATH);
            recorder.prepare();
            recorder.start();

            Toast.makeText(getContext(), "녹음을 시작합니다!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 앱이 종료가 되었을 때도
    @Override
    public void onDestroy() {
        super.onDestroy();

        KillPlayer();
    }

    private void KillPlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }
}
