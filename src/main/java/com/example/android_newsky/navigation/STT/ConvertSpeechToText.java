package com.example.android_newsky.navigation.STT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android_newsky.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ConvertSpeechToText extends Fragment {

    SpeechRecognizer mRecognizer;
    Button sttBtn;
    TextView textView;
    Intent intent;

    final int PERMISSION = 1;

    private final static String directoryName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Nokmusae/ConvertedText";

    private String textFileName = null;

    private final static String TextFileName = "textInfo.txt";

    FileOutputStream fileOutputStreamText = null;
    BufferedWriter bufferedWriterText = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_menu_sst, container, false);

        File convertedTextDir = new File(directoryName);
        // 음성이 변환된 텍스트 파일을 저장할 디렉토리 생성
        if(!convertedTextDir.exists()) {
            convertedTextDir.mkdir();
        }

        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION);
        }

        textView = rootView.findViewById(R.id.sttResult);

        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getActivity().getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR");

        sttBtn = rootView.findViewById(R.id.sttStart);
        sttBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecognizer = SpeechRecognizer.createSpeechRecognizer(getContext());
                mRecognizer.setRecognitionListener(recognitionListener);
                mRecognizer.startListening(intent);
            }
        });

        return rootView;
    }

    private RecognitionListener recognitionListener = new RecognitionListener() {

        @Override
        public void onReadyForSpeech(Bundle params)
        {
            Toast.makeText(getActivity(), "음성인식을 시작합니다.", Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float v) {

        }

        @Override
        public void onBufferReceived(byte[] bytes) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int i) {
            String message;

            switch (i) {
                case SpeechRecognizer.ERROR_AUDIO:
                    message = "오디오 에러";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    message = "클라이언트 에러";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message = "퍼미션 없음";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message = "네트워크 에러";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message = "네트웍 타임아웃";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    message = "찾을 수 없음";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message = "RECOGNIZER가 바쁨";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message = "서버가 이상함";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message = "말하는 시간초과";
                    break;
                default:
                    message = "알 수 없는 오류임";
                    break;
            }
            Toast.makeText(getContext(), "에러가 발생하였습니다. : " + message,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResults(Bundle bundle) {
            // 말을 하면 ArrayList에 단어를 넣고 textView에 단어를 이어줍니다.
            ArrayList<String> matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

            String dummy = "";

            for(int i = 0; i < matches.size() ; i++)
            {
                textView.setText(matches.get(i));
            }

            dummy += textView.getText().toString();

            try
            {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd_HHmmss");

                Date time = new Date();

                String timeForNaming = simpleDateFormat.format(time);

                textFileName = "변환본 " + timeForNaming + ".txt";

                String filePath = directoryName + "/" + textFileName;

                fileOutputStreamText = new FileOutputStream(filePath, false);
                bufferedWriterText = new BufferedWriter(new OutputStreamWriter(fileOutputStreamText));

                bufferedWriterText.write(dummy);

                bufferedWriterText.flush();
                bufferedWriterText.close();

            } catch (IOException e) { e.printStackTrace(); }
        }

        @Override
        public void onPartialResults(Bundle partialResults) {}

        @Override
        public void onEvent(int eventType, Bundle params) {}
    };
}