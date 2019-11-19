package com.hank.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.hank.oma.SmartCard;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onBtnClick(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SmartCard.getInstance().execute("00A4040008A000000151000000");
            }
        }).start();
    }
}
