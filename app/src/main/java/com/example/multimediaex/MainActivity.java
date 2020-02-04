package com.example.multimediaex;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {
    Switch swPlayer;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swPlayer=(Switch)findViewById(R.id.swPlayer);
        mp=MediaPlayer.create(this,R.raw.da);
        swPlayer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    mp=MediaPlayer.create(MainActivity.this,R.raw.da);
                    mp.start();
                }else{
                    mp.stop();
                }
            }
        });

    }
}
