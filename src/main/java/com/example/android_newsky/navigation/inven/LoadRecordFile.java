package com.example.android_newsky.navigation.inven;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Environment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.android_newsky.R;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Timer;

import me.itangqi.waveloadingview.WaveLoadingView;

public class LoadRecordFile extends Fragment {

    private final String directoryName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Nokmusae/Recording";

    ListView lvFileList;
    Button btnPlay;
    Button btnNext;

    ArrayList<String> searchFileList = new ArrayList<String>();//검색한 파일 목록
    ArrayList<String> playFileList = new ArrayList<String>();//재생 선택한 파일 목록
    ArrayList<String> searchFileName = new ArrayList<String>();//파일 이름만 저장할 목록

    MediaPlayer mp = new MediaPlayer();

    int playPosition=0;//재생할 곳의 위치

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_menu_inven_record, container, false);

        lvFileList = rootView.findViewById(R.id.lvFileList);
        btnPlay = rootView.findViewById(R.id.btnPlay);
        btnNext = rootView.findViewById(R.id.btnNext);

        playFileList.clear();
        searchFileList.clear();
        searchFileName.clear();

        File recordingDir = new File(directoryName);
        // 녹음된 음성 파일을 저장할 디렉토리 생성
        if(!recordingDir.exists())
        {
            recordingDir.mkdir();
        }
        else
        {
            SearchFile(directoryName);   //파일을 검색한다
        }

        if(searchFileList.size() <= 0)
        {
            ToastMessage("재생할 오디오 파일이 존재하지 않습니다.", Toast.LENGTH_LONG);
        }
        else
        {
            ArrayAdapter<String> adapter;

            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_multiple_choice, searchFileName);

            lvFileList.setAdapter(adapter);
        }

        btnPlay.setOnClickListener(reButtonClickListener);
        btnNext.setOnClickListener(reButtonClickListener);

        return rootView;
    }

    private void ToastMessage(String msg, int time)
    {
        Toast.makeText(getContext(), msg, time).show();
    }

    private void SearchFile(String str)
    {
        File root = new File(str);
        String[] file = root.list();
        if(file != null)
        {
            for(int idx=0; idx<file.length; ++idx)
            {
                File f = new File(str+"/"+file[idx]);
                if(f.isFile())   //파일이면
                {
                    if(f.getName().endsWith(".mp3"))
                    {
                        searchFileList.add(str + "/" + file[idx]);
                        searchFileName.add(file[idx]);
                        continue;
                    }
                }
                //전체 디렉토리만 검색할 경우 아래 주석해제
                if(f.isDirectory())   //폴더면..
                    SearchFile(str + "/" + file[idx]);//폴더 안의 내용을 재귀 검색한다.
            }
        }

       /*
        * str 디렉토리에서 mp3 파일많을 읽어 오는 또다른 방법
        File root = new File(str);
       if(root.listFiles(new Mp3Filter()).length>0){
          for(File file : root.listFiles(new Mp3Filter())){
             searchFileList.add(file.getName());
          }
       }
        */
    }
    /*str 디렉토리에서 mp3 파일많을 읽어 오는 또다른 방법을 사용할 경우 주석을 해제 한다.
    class Mp3Filter implements FilenameFilter{
       public boolean accept(File dir, String name){
          return(name.endsWith("mp3"));
       }
    }
    */
    private void playAudio(String filePath)
    {
        FileInputStream fis = null;
        try
        {
            mp.reset();
            fis = new FileInputStream(filePath);
            FileDescriptor fd = fis.getFD();
            mp.setDataSource(fd);
            mp.prepare();
            mp.start();
            btnPlay.setText("중지");
            mp.setOnCompletionListener(new OnCompletionListener(){
                public void onCompletion(MediaPlayer mp1)
                {
                    btnPlay.setText("재생");
                    playPosition++; //재생 위치를 1 증가 시킨다.
                    nextAudio();
                }
            });
        }
        catch(Exception err)
        {
            ToastMessage(err.getMessage(), Toast.LENGTH_LONG);
        }

    }

    private void nextAudio()
    {
        if(playPosition<playFileList.size())
        {
            playAudio(playFileList.get(playPosition));
        }
        else
        {
            ToastMessage("마지막 곡입니다.", Toast.LENGTH_LONG);
        }
    }

    private OnClickListener reButtonClickListener = new OnClickListener(){
        public void onClick(View v)
        {
            switch(v.getId())
            {
                case R.id.btnPlay:
                    if(mp.isPlaying())
                    {
                        mp.stop();//재생 정지
                        btnPlay.setText("재생");
                    }
                    else
                    {
                        //searchFileList에서 선택한 곡만을 모아서 playFileList를 만든다.
                        playFileList.clear();//재생목록 초기화
                        SparseBooleanArray sba = lvFileList.getCheckedItemPositions();

                        int max = sba.size();
                        for(int i=0;i<max;i++)
                        {
                            if(sba.valueAt(i))
                            {
                                playFileList.add(searchFileList.get(sba.keyAt(i)));
                            }
                        }
                        //playFileList의 음악을 하나씩 재생한다.
                        if(playFileList.size()>0)
                        {

                            playPosition=0;//처음 시작은 처음 부터 한다.
                            // 에러 부분
                            System.out.println(playFileList.get(playPosition));
                            playAudio(playFileList.get(playPosition));
                        }
                    }
                    break;
                case R.id.btnNext:
                    playPosition++; //재생 위치를 1 증가 시킨다.
                    nextAudio();
                    break;
            }
        }
    };
}