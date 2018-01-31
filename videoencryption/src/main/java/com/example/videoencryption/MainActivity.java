package com.example.videoencryption;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.VideoView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener {

    public static final String TAG = "liu-MainActivity";
    private VideoView videoMain;
    private FileDES fileDES;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videoMain = (VideoView) this.findViewById(R.id.video_main);
        try {
            fileDES = new FileDES("yuxinkeji.liu");
            //fileDES.doEncryptFile("/mnt/sdcard/1234.mkv", "/mnt/sdcard/4567.mkv");
            String path = CStorageManager.getInnerSDCardPath();
            List<String> extSDCardPath = CStorageManager.getExtSDCardPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        initVideo();
    }

    private void initVideo() {
        Uri uri = Uri.parse("/mnt/sdcard/1234.mkv");
        videoMain.setVideoURI(uri);
        videoMain.setOnPreparedListener(this);
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        videoMain.start();
    }
}
