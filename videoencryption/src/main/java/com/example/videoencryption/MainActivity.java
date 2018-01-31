package com.example.videoencryption;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener {

    public static final String TAG = "liu-MainActivity";
    private VideoView videoMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videoMain = (VideoView) this.findViewById(R.id.video_main);
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
