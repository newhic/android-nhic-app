package apps4christ.android.nhicapp.audioplayer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import apps4christ.android.nhicapp.R;
import apps4christ.android.nhicapp.audioplayer.PodcastService.PodcastBinder;

/**
 * Created by mjmerin on 7/11/15.
 */
public class PodcastPlayerActivity extends Activity implements SeekBar.OnSeekBarChangeListener {
    private ImageButton btnPlay;
    private ImageButton btnForward;
    private ImageButton btnBackward;
    private SeekBar podcastProgressBar;
    private TextView songTitleLabel;
    private TextView songCurrentDurationLabel;
    private TextView songTotalDurationLabel;
    private PodcastService podcastSrv;
    private Intent playIntent;
    private boolean podcastBound = false;
    private String podcastTitle;
    private Intent intent;
    private String url;
    private Utilities utils;
    private int seekForwardTime = 5000; // 5000 milliseconds
    private int seekBackwardTime = 5000; // 5000 milliseconds
    private Handler mHandler = new Handler();
    ;
    private int position;

    static final String PODCAST_POS = "podcastPos";
    static final String PODCAST_URL = "podcastUrl";
    static final String PODCAST_TITLE = "podcastTitle";

    Tracker dbgTracker;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);

        // Find all media player buttons in the layout file and assign a variable to them.
        btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        btnForward = (ImageButton) findViewById(R.id.btnForward);
        btnBackward = (ImageButton) findViewById(R.id.btnBackward);
        podcastProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
        songTitleLabel = (TextView) findViewById(R.id.songTitle);
        songCurrentDurationLabel = (TextView) findViewById(R.id.songCurrentDurationLabel);
        songTotalDurationLabel = (TextView) findViewById(R.id.songTotalDurationLabel);


        utils = new Utilities();

        intent = getIntent();
        url = intent.getStringExtra("url");
        podcastTitle = intent.getStringExtra("title");

        podcastProgressBar.setOnSeekBarChangeListener(this);

        /**
         * Play button click event
         * plays a song and changes button to pause image
         * pauses a song and changes button to play image
         * */
        btnPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check for already playing
                if (podcastSrv.isPlaying()) {
                    if (podcastSrv != null) {
                        Log.d("CONTROL", "Pausing");
                        podcastSrv.setSeekPos(podcastSrv.getPodcastPosn());
                        podcastSrv.pause();
                        // Changing button image to play button
                        btnPlay.setImageResource(R.drawable.btn_play);
                    }
                } else {
                    // Resume song
                    if (podcastSrv != null) {
                   /*     if (podcastSrv.getSeekPosn() == 0) {
                            Log.d("MD", "Playing Podcast");
                            podcastSrv.playPodcast();
                        }
                        else {
                            Log.d("MD", "start playing");
                            podcastSrv.startPlaying();
                        }
                        */
                        podcastSrv.playPodcast();

                        // Changing button image to pause button
                        btnPlay.setImageResource(R.drawable.btn_pause);
                        updateProgressBar();
                    }
                }

            }
        });

        /**
         * Forward button click event
         * Forwards song specified seconds
         * */
        btnForward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // get current song position
                int currentPosition = podcastSrv.getPodcastPosn();
                // check if seekForward time is lesser than podcast duration
                if (currentPosition + seekForwardTime <= podcastSrv.getDur()) {
                    // forward podcast
                    podcastSrv.seek(currentPosition + seekForwardTime);
                } else {
                    // forward to end position
                    podcastSrv.seek(podcastSrv.getDur());
                }
            }
        });

        /**
         * Backward button click event
         * Backward song to specified seconds
         * */
        btnBackward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // get current song position
                int currentPosition = podcastSrv.getPodcastPosn();
                // check if seekBackward time is greater than 0 sec
                if (currentPosition - seekBackwardTime >= 0) {
                    // forward podcast
                    podcastSrv.seek(currentPosition - seekBackwardTime);
                } else {
                    // backward to starting position
                    podcastSrv.seek(0);
                }

            }
        });

        songTitleLabel.setText(podcastTitle);

    }

    @Override
    public void onResume() {
        super.onResume();

        dbgTracker.setScreenName("Podcast Audioplayer");
        dbgTracker.send(new HitBuilders.AppViewBuilder().build());

    }

    private ServiceConnection podcastConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PodcastBinder binder = (PodcastBinder) service;
            //get service
            podcastSrv = binder.getService();
            podcastSrv.setURL(url);
            //Setting seek position here
            Log.d("SEEKPOSN", "Seek position is" + position);
            podcastSrv.setSeekPos(position);
            podcastBound = true;

            /* This is mainly for updating the UI on rotation */
            if (podcastSrv.isPlaying()) {
                btnPlay.setImageResource(R.drawable.btn_pause);
                updateProgressBar();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            podcastBound = false;
        }
    };

    /* When the activity instance starts, we create the Intent object if it doesn't exist
    * and bind to it and start it.
     */
    @Override
    protected void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, PodcastService.class);
            bindService(playIntent, podcastConnection, Context.BIND_AUTO_CREATE);
            Log.d("Start Service", "starting the service");

            startService(playIntent);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current podcast position

        int currentPosition = podcastSrv.getPodcastPosn();
        savedInstanceState.putInt(PODCAST_POS, currentPosition);
        savedInstanceState.putString(PODCAST_TITLE, podcastTitle);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        // Restore state members from saved instance
        podcastTitle = savedInstanceState.getString(PODCAST_TITLE);
        position = savedInstanceState.getInt(PODCAST_POS);
    }

    /**
     * Update timer on seekbar
     */
    public void updateProgressBar() {


        long totalDuration = podcastSrv.getTotalDuration();
        long currentDuration = podcastSrv.getPodcastPosn();

        // Displaying Total Duration time
        songTotalDurationLabel.setText("" + utils.milliSecondsToTimer(totalDuration));
        // Displaying time completed playing
        songCurrentDurationLabel.setText("" + utils.milliSecondsToTimer(currentDuration));

        // Updating progress bar
        int progress = (utils.getProgressPercentage(currentDuration, totalDuration));

        //Log.d("Progress", "Setting Progress to "+progress);
        podcastProgressBar.setProgress(progress);

        mHandler.postDelayed(mUpdateTimeTask, 1000);

    }


    /**
     * Background Runnable thread
     */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {

            updateProgressBar();
        }
    };


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {

    }

    /**
     * When user starts moving the progress handler
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // remove message Handler from updating progress bar
        mHandler.removeCallbacks(mUpdateTimeTask);
    }


    /**
     * When user stops moving the progress handler
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = podcastSrv.getDur();
        int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        podcastSrv.seek(currentPosition);

        // update timer progress again
        updateProgressBar();
    }


    @Override
    public void onBackPressed() {
        Log.d("Back Pressed", "Pressed");
        super.onBackPressed();

        stopService(playIntent);

    }

    @Override
    public void onDestroy() {
        Log.d("DESTROY", "Destroying");
        //stopService(playIntent);

        /* Stop the seekbar from updating one last time */
        mHandler.removeCallbacks(mUpdateTimeTask);

        /* Unbind the service to prevent ServiceConnection leak */
        if (podcastConnection != null)
            unbindService(podcastConnection);

        podcastSrv = null;
        super.onDestroy();
    }

}