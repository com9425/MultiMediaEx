package com.example.thred;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    SeekBar sb1, sb2;
    TextView tv1, tv2;
    Button btnStart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sb1=(SeekBar)findViewById(R.id.sb1);
        sb2=(SeekBar)findViewById(R.id.sb2);
        tv1=(TextView)findViewById(R.id.tv1);
        tv2=(TextView)findViewById(R.id.tv2);
        btnStart=(Button)findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    public void run() {
                        for(int i=0; i<100; i=i+2) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    sb1.setProgress(sb1.getProgress()+2);
                                    tv1.setText("1번 진행률 : " + sb1.getProgress() + "%");
                                }
                            });
                            SystemClock.sleep(100);
                        }
                    }
                }.start();

                new Thread() {
                    public void run() {
                        for(int i=0; i<100; i++) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    sb2.setProgress(sb2.getProgress()+1);
                                    tv2.setText("2번 진행률 : " + sb2.getProgress() + "%");
                                }
                            });
                            SystemClock.sleep(100);
                        }
                    }
                }.start();
            }
        });
    }
}
