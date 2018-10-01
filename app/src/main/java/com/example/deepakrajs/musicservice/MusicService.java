package com.example.deepakrajs.musicservice;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

public class MusicService extends Service {
    public MusicService() {
    }

    ListOfSongsActivity list;
    MediaPlayer mediaPlayer;
    private static final int STEP_VALUE = 5000;

    private IBinder iBinder = new MusicBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        //create media player object
        mediaPlayer = new MediaPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        /*try {

            mediaPlayer = new MediaPlayer();
            //sets the data source of audio file
            mediaPlayer.setDataSource(ListOfSongsActivity.absolutePath);
            //prepares the player for playback synchronously
            mediaPlayer.prepare();
            //sets the player for looping
            mediaPlayer.setLooping(true);
            //starts or resumes the playback
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
            Log.i("show","Error: "+e.toString());
        }*/

        return START_STICKY;
    }

    // seekbar code
    public void startPlay(String file) {
        Log.i("Selected", file);


        try {
            mediaPlayer.setDataSource(file);
            mediaPlayer.prepare();
            mediaPlayer.start();
            list.layout(mediaPlayer.getDuration());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void stop() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            Log.d("Media", "stopped");
            mediaPlayer.reset();
        }
    }

    public void next() {
        if (mediaPlayer.isPlaying()) {
            // get current song position
            int currentPosition = mediaPlayer.getCurrentPosition();
            // check if seekForward time is lesser than song duration
            if (currentPosition + STEP_VALUE <= mediaPlayer.getDuration()) {
                // forward song
                mediaPlayer.pause();
                mediaPlayer.seekTo(currentPosition + STEP_VALUE);
                mediaPlayer.start();
            } else {
                // forward to end position
                mediaPlayer.seekTo(mediaPlayer.getDuration());
            }
        }
    }

    public void prev() {
        if (mediaPlayer.isPlaying()) {
            // get current song position
            int currentPosition = mediaPlayer.getCurrentPosition();
            // check if seekForward time is lesser than song duration
            if (currentPosition - STEP_VALUE >= 0) {
                // forward song
                mediaPlayer.pause();
                mediaPlayer.seekTo(currentPosition - STEP_VALUE);
                mediaPlayer.start();
            } else {
                // forward to end position
                mediaPlayer.seekTo(0);
            }
        }
    }

    public int getCurrentDuration() {
        //int time = mediaPlayer.getCurrentPosition();
        //int duration = time/
        return mediaPlayer.getCurrentPosition();
    }

    public void onDestroy(){
        //stops the playback
        mediaPlayer.stop();
        //releases any resource attached with MediaPlayer object
        mediaPlayer.release();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }
}