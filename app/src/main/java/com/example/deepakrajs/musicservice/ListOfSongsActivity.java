package com.example.deepakrajs.musicservice;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class ListOfSongsActivity extends AppCompatActivity {

    ListView listview;
    ImageButton btnPlayStop;
    TextView txtSongName;
    CardView cardView;
    ArrayList<SongObject> listOfContents;
    AdapterClass adapter;
    String path;
    private static final int UPDATE_FREQUENCY = 500;
    static String absolutePath, songName;
    public static boolean playing = false;

    private Intent intent;

    private SeekBar seekBar = null;
    private ImageButton prev = null;
    private ImageButton next = null;

    private final Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_songs);

        // If Android Marshmello or above, then check if permission is granted
        if (Build.VERSION.SDK_INT >= 23)
            checkPermission();
        else
            initViews();
    }

    public void layout(int duration) {
        seekBar.setMax(duration);
        btnPlayStop.setImageResource(android.R.drawable.ic_media_pause);
        updatePosition();
        playing = true;
    }

    private final Runnable updatePositinRunnable = new Runnable() {
        @Override
        public void run() {
            updatePosition();
        }
    };

    private void updatePosition() {
        handler.removeCallbacks(updatePositinRunnable);
        seekBar.setProgress(musicSrv.getCurrentDuration());
        handler.postDelayed(updatePositinRunnable, UPDATE_FREQUENCY);
    }

    void initViews() {
        //Play the selected song by starting the service
        Intent start = new Intent(ListOfSongsActivity.this, MusicService.class);
        startService(start);

        bindService(start,musicConnection,BIND_AUTO_CREATE);

        //initializing views
        btnPlayStop = (ImageButton) findViewById(R.id.btnPlayStop);
        txtSongName = (TextView) findViewById(R.id.txtSongName);
        cardView = (CardView) findViewById(R.id.cardView);
        listview = (ListView) findViewById(R.id.listView);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        prev = (ImageButton) findViewById(R.id.previous);
        next = (ImageButton) findViewById(R.id.next);
        listOfContents = new ArrayList<>();

        //If music is playing already on starting the app, player should be visible with Stop button
        if (playing) {
            txtSongName.setText(songName);
            cardView.setVisibility(View.VISIBLE);
            btnPlayStop.setImageResource(android.R.drawable.ic_media_pause);
        }

        //Gives you the full path of phone memory
        path = Environment.getExternalStorageDirectory().getAbsolutePath();

        //Calling the function which fetches the list of music files
        initList(path);

        //initializing the adapter and passing the context, list item and list of references of SongObject
        adapter = new AdapterClass(this, R.layout.list_item, listOfContents);
        listview.setAdapter(adapter);

        //handling events when user clicks on any music file in list view
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //player is visible
                cardView.setVisibility(View.VISIBLE);

                /*//If some other song is already playing, stop the service
                if (playing) {
                    Intent i = new Intent(ListOfSongsActivity.this, MusicService.class);
                    stopService(i);
                }

                playing = true;*/

                //getting absolute path of selected song from bean class 'SongObject'
                SongObject sdOb = listOfContents.get(position);
                absolutePath = sdOb.getAbsolutePath();

                //Play the selected song by starting the service
                Intent start = new Intent(ListOfSongsActivity.this, MusicService.class);
                startService(start);

                //Get and set the name of song in the player
                songName = listOfContents.get(position).getFileName();
                txtSongName.setText(songName);
                btnPlayStop.setImageResource(android.R.drawable.ic_media_play);
            }

        });

        //Handling events when button Play/Stop is clicked in the player
        btnPlayStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playing) {
                    //If song is playing and user clicks on Stop button
                    //Stop the song by calling stopService() and change boolean value
                    //text on button should be changed to 'Play'
                    playing = false;
                    btnPlayStop.setImageResource(android.R.drawable.ic_media_play);
//                    Intent i = new Intent(ListOfSongsActivity.this, MusicService.class);
//                    stopService(i);
                    musicSrv.stop();
                } else if (!playing) {
                    //If song is not playing and user clicks on Play button
                    //Start the song by calling startService() and change boolean value
                    //text on button should be changed to 'Stop'
                    playing = true;
                    btnPlayStop.setImageResource(android.R.drawable.ic_media_pause);
//                    Intent i = new Intent(ListOfSongsActivity.this, MusicService.class);
//                    startService(i);
                    txtSongName.setText(songName);
                    seekBar.setProgress(0);
                    musicSrv.startPlay(absolutePath);
                }
            }
        });

        // forward and backward implementation
        // fast forward by clicking on next button
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                musicSrv.next();
            }
        });

        // fast backword by clicking on prev button
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicSrv.prev();
            }
        });
    }

    private MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound=false;
    //private ArrayList<Song> songList;
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            musicSrv.list = ListOfSongsActivity.this;
            //pass list
            //musicSrv.setList(songList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    //Fetching .mp3 and .mp4 files from phone storage
    void initList(String path) {
        try {
            File file = new File(path);
            File[] filesArray = file.listFiles();
            String fileName;
            for (File file1 : filesArray) {
                if (file1.isDirectory()) {
                    initList(file1.getAbsolutePath());
                } else {
                    fileName = file1.getName();
                    if ((fileName.endsWith(".mp3")) /*|| (fileName.endsWith(".mp4"))*/) {
                        listOfContents.add(new SongObject(file1.getName(), file1.getAbsolutePath()));
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Handling permissions for Android Marshmallow and above
    void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            //if permission granted, initialize the views
            initViews();
        } else {
            //show the dialog requesting to grant permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    //Popups for the permissions request to allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initViews();
                } else {
                    //permission is denied (this is the first time, when "never ask again" is not checked)
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        finish();
                    }
                    //permission is denied (and never ask again is  checked)
                    else {
                        //shows the dialog describing the importance of permission, so that user should grant
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("You have forcefully denied Read storage permission.\n\nThis is necessary for the working of app." + "\n\n" + "Click on 'Grant' to grant permission")
                                //This will open app information where user can manually grant requested permission
                                .setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish();
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                Uri.fromParts("package", getPackageName(), null));
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                })
                                //close the app
                                .setNegativeButton("Don't", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish();
                                    }
                                });
                        builder.setCancelable(false);
                        builder.create().show();
                    }
                }
        }
    }
}
