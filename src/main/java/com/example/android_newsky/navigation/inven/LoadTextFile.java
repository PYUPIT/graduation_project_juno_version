package com.example.android_newsky.navigation.inven;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.android_newsky.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class LoadTextFile extends Fragment {

    private final String directoryName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Nokmusae/ConvertedText";

    private String textFileName = null;

    EditText memo;
    TextView tv1;
    ListView lv1;

    Button btnForRegister;
    Button btnForSave;
    Button btnForCancel;

    ArrayList<String> name = new ArrayList<>();

    ArrayAdapter<String> adapter;
    DatePicker dp1;
    LinearLayout linear1, linear2;

    private int count = 0;
    private int countForText = 0;
    int positions = 0;

    String temp;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_menu_inven_text, container, false);

        memo = rootView.findViewById(R.id.et1);
        lv1 = rootView.findViewById(R.id.listViewForText);
        dp1 = rootView.findViewById(R.id.datePicker);
        tv1 = rootView.findViewById(R.id.tvCount);

        linear1 = rootView.findViewById(R.id.Lay1);
        linear2 = rootView.findViewById(R.id.Lay2);


        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, name);

        lv1.setAdapter(adapter);

        File convertedTextDir = new File(directoryName);
        // 음성이 변환된 텍스트 파일을 저장할 디렉토리 생성
        if(!convertedTextDir.exists())
        {
            convertedTextDir.mkdir();
        }

        btnForRegister = rootView.findViewById(R.id.btn_register_text);
        btnForRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickRegisterBtn();
            }
        });

        btnForSave = rootView.findViewById(R.id.btn_save);
        btnForSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickSaveBtn();
            }
        });

        btnForCancel = rootView.findViewById(R.id.btn_cancel);
        btnForCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCancelBtn();
            }
        });

        showFileList();
        listViewAction();

        return rootView;
    }

    void showFileList()
    {
        countForText = 0;

        name.clear();

        File dir = new File(directoryName);
        File[] fileList = dir.listFiles();

        for(File f : fileList)
        {
            name.add(f.getName());
            countForText++;
        }

        Collections.sort(name, Collections.reverseOrder());

        adapter.notifyDataSetChanged();

        tv1.setText("등록된 메모 개수: " + String.valueOf(countForText));
    }

    public void clickRegisterBtn()
    {
        linear1.setVisibility(View.INVISIBLE);
        linear2.setVisibility(View.VISIBLE);

        memo.setText("");
    }

    public void clickSaveBtn()
    {
        linear1.setVisibility(View.VISIBLE);
        linear2.setVisibility(View.INVISIBLE);

        Date date = new Date(dp1.getYear(), dp1.getMonth(), dp1.getDayOfMonth());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd_HHmmss");

        Date time = new Date();

        String timeForNaming = simpleDateFormat.format(time);

        textFileName = "텍스트 " + timeForNaming;

        Toast.makeText(getContext(), "저장완료", Toast.LENGTH_SHORT).show();

        if (btnForSave.getText().toString().equals("저장")) {
            count = 0;
            if (name.contains(textFileName)) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(getContext());
                dlg.setMessage("이미 파일이 존재합니다 수정하시겠습니까?")
                        .setNegativeButton("아니오", null)
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                readFile(directoryName + "/" + textFileName + ".txt");
                                btnForSave.setText("수정");
                            }
                        }).show();
                linear1.setVisibility(View.INVISIBLE);
                linear2.setVisibility(View.VISIBLE);
                count++;
                return;
            }

            writeFile(directoryName + "/" + textFileName+ ".txt");

            Toast.makeText(getContext(), "저장완료", Toast.LENGTH_SHORT).show();
        }
        else
        {
            deleteExternalFile(directoryName + "/" + temp + ".txt");
            writeFile(directoryName+ "/" + textFileName + ".txt");
            Toast.makeText(getContext(), "수정완료", Toast.LENGTH_SHORT).show();
            btnForSave.setText("저장");
        }

        showFileList();
    }

    public void clickCancelBtn()
    {
        linear1.setVisibility(View.VISIBLE);
        linear2.setVisibility(View.INVISIBLE);

        btnForSave.setText("저장");
    }
    void listViewAction()
    {
        lv1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id)
            {
                AlertDialog.Builder dlg = new AlertDialog.Builder(getContext());
                dlg.setMessage("삭제하시겠습니까?")
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {

                                String item = name.get(position);

                                deleteExternalFile(directoryName + "/" + item);
                                showFileList();
                            }
                        })
                        .setNegativeButton("취소", null)
                        .show();

                return true;
            }
        });

        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                readFile(directoryName + "/" + name.get(position));
                btnForSave.setText("수정");

                linear1.setVisibility(View.INVISIBLE);
                linear2.setVisibility(View.VISIBLE);

                temp = name.get(position);
            }
        });
    }
    void readFile(String path)
    {
        try
        {
            FileInputStream fileInputStreamTemp = new FileInputStream(path);
            BufferedReader bufferedReaderTemp = new BufferedReader(new InputStreamReader(fileInputStreamTemp));

            String dummy = "";
            String line = bufferedReaderTemp.readLine();

            while (line != null) {
                dummy += line;
                line = bufferedReaderTemp.readLine();
            }

            memo.setText(dummy);

            bufferedReaderTemp.close();
        }
        catch (FileNotFoundException e) { e.printStackTrace(); }
        catch (IOException e) { e.printStackTrace(); }
    }

    void writeFile(String path)
    {
        try
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter(path, false));
            bw.write(memo.getText().toString());
            bw.newLine();
            bw.close();
        } catch (IOException e) { e.printStackTrace(); }
    }

    void deleteExternalFile(String path)
    {
        File file = new File(path);
        file.delete();
    }
}
