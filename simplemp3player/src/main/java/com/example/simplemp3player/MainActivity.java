package com.example.simplemp3player;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listViewMp3;
    Button btnPlay, btnPause, btnStop;
    TextView tvPlayName, tvTime;
    SeekBar sbMp3;   //ProgressBar pbPlayStatus;
    ArrayList<String> mp3List;
    String selectedMp3;
    String mp3Path= Environment.getExternalStorageDirectory().getPath()+"/Music/";
    MediaPlayer mp;
    static boolean PAUSED=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listViewMp3=(ListView)findViewById(R.id.listViewMp3);
        btnPlay=(Button)findViewById(R.id.btnPlay);
        btnPause=(Button)findViewById(R.id.btnPause);
        btnStop=(Button)findViewById(R.id.btnStop);
        tvPlayName=(TextView)findViewById(R.id.tvPlayName);
        tvTime=(TextView)findViewById(R.id.tvTime);
        sbMp3=(SeekBar)findViewById(R.id.sbMp3);
        //pbPlayStatus=(ProgressBar)findViewById(R.id.pbPlayStatus);
        int permissionCheck=ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissionCheck== PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE},MODE_PRIVATE);
        }else {
            sdcardProcess();
        }

        listViewMp3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedMp3=mp3List.get(position);
            }
        });
        sbMp3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {    // fromuser 시크바를 움직이면 값을 반환하는 곳 시크바를 움직이면 트루가됨
                if(fromUser) {
                    mp.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mp=new MediaPlayer();
                    mp.setDataSource(mp3Path+selectedMp3);
                    mp.prepare();
                    mp.start();
                    btnStop.setEnabled(true);
                    btnPause.setEnabled(true);
                    btnPlay.setEnabled(false);
                    tvPlayName.setText("실행중인 음악 : " + selectedMp3);
                    makeThread();
                    //pbPlayStatus.setVisibility(View.VISIBLE);
                }catch (IOException e) {
                    Toast.makeText(getApplicationContext(),"노래를 재생할 수 없습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PAUSED==false) {
                    mp.pause();
                    btnPause.setText("이어듣기");
                    PAUSED=true;
                    // pbPlayStatus.setVisibility(View.INVISIBLE);
                }else {
                    mp.start();
                    btnPause.setText("일시정지");
                    PAUSED=false;
                    makeThread();
                    // pbPlayStatus.setVisibility(View.VISIBLE);
                }
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                mp.reset();
                btnStop.setEnabled(false);
                btnPause.setEnabled(false);
                btnPlay.setEnabled(true);
                btnPause.setText("일시정지");
                PAUSED=false;
                new Thread(){
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sbMp3.setProgress(0);
                                tvTime.setText("실행중인 음악 : ");
                                tvTime.setText("진행시간 : ");
                            }
                        });
                    }
                }.start();
                // pbPlayStatus.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        sdcardProcess();
    }
    void sdcardProcess() {
        mp3List=new ArrayList<String>();
        File listFiles[]=new File(mp3Path).listFiles();
        String fileName, extName;
        for(File file:listFiles) {
            fileName=file.getName();
            extName=fileName.substring(fileName.length()-3);
            if(extName.equals("mp3")) {
                mp3List.add(fileName);
            }
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice,mp3List);
        listViewMp3.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listViewMp3.setAdapter(adapter);
        listViewMp3.setItemChecked(0,true);
        selectedMp3=mp3List.get(0);
    }
    private void makeThread() {
        new Thread() {
            SimpleDateFormat timeFormat=new SimpleDateFormat("mm:ss");
            @Override
            public void run() {
                sbMp3.setMax(mp.getDuration());
                while (mp.isPlaying()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sbMp3.setProgress(mp.getCurrentPosition());
                            tvTime.setText("진행시간 : "+ timeFormat.format(mp.getCurrentPosition()));
                        }
                    });
                }
            }
        }.start();
    }
}
