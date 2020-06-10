package com.example.android_newsky.navigation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.example.android_newsky.R;

public class F_ShowProfile extends Activity {

    private int img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dsadsa5);

        Intent intent = getIntent();
        ImageView profile = (ImageView) findViewById(R.id.profile);
        TextView name = (TextView) findViewById(R.id.name);
        TextView email = (TextView) findViewById(R.id.email);

        img = Integer.parseInt(intent.getStringExtra("profile"));
        profile.setImageResource(img);
        name.setText(intent.getStringExtra("name"));
        email.setText(intent.getStringExtra("email"));
    }
}
