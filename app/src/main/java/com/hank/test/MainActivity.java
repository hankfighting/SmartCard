package com.hank.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.hank.oma.SmartCard;
import com.hank.oma.entity.CardResult;
import com.hank.oma.utils.LogUtil;

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
                CardResult cardResult = SmartCard.getInstance().execute("00A4040008a000000151000000", "6A82");
                if(cardResult.getStatus() == 0) {
                    LogUtil.e(cardResult.getRapdu());
                    LogUtil.e(cardResult.getSw());
                } else {
                    LogUtil.e(cardResult.getMessage());
                }
            }
        }).start();
    }
}
