package apps4christ.android.nhicapp.audioplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.io.IOException;
import java.util.ArrayList;
import android.content.ContentUris;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.PowerManager;
import android.util.Log;
import java.util.Random;
import android.app.Notification;
import android.app.PendingIntent;

/**
 * Created by mjmerin on 7/11/15.
 */
public class PodcastService extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {

    private MediaPlayer player;
    private int seekPosn;
    private long totalDuration;
    private String podcastUrl;
    private final IBinder podcastBind = new PodcastBinder();

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return podcastBind;
    }

    @Override
    public boolean onUnbind(Intent intent){
        Log.d("UNBIND", "Unbinding Service");
        player.stop();
        player.release();
        return false;
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mp.reset();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    public class PodcastBinder extends Binder {
        PodcastService getService() {
            return PodcastService.this;
        }
    }

    public void onCreate(){
        //Create the service
        super.onCreate();

        seekPosn = 0;
        player = new MediaPlayer();

        initPodcastPlayer();
    }

    public void setURL(String url) {
        podcastUrl = url;
    }

    public void initPodcastPlayer(){
        //set player properties
        //player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void playPodcast() {
        //play podcast
        player.reset();

        try{
            Log.d("DATA", "Setting Data source to url " + podcastUrl);
            player.setDataSource(podcastUrl);
        }

        catch(Exception e){
            Log.e("PODCAST SERVICE", "Error setting data source", e);
        }

        player.prepareAsync();

    }

    @Override
    public void onPrepared(MediaPlayer mp) {

        Log.d("Seek", "Seeking to position " + seekPosn);
        player.seekTo(seekPosn);

        Log.d("Playback", "starting playback");
        player.start();

        totalDuration = player.getDuration();
    }

    // Media Player Controls

    // Get position of seekbar
    public int getPodcastPosn(){
        return player.getCurrentPosition();
    }

    public int getSeekPosn(){return seekPosn;}

    public void setSeekPos(int pos){seekPosn = pos;}

    public int getDur(){
        return player.getDuration();
    }

    public boolean isPlaying(){
        return player.isPlaying();
    }

    public void pause(){
        player.pause();
    }

    public void startPlaying() {
        player.start();
    }

    public void seek(int posn) {
        player.seekTo(posn);
    }

    public long getTotalDuration() {
        return totalDuration;
    }

}