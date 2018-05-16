package io.berrycorp.noti.utilities;

import android.content.Context;
import android.media.MediaPlayer;

public class MusicSingleton {
    private static MusicSingleton sInstance;
    private Context mContext;
    private MediaPlayer mMediaPlayer;
    public MusicSingleton(Context context) {
        mContext = context;
    }

    public static MusicSingleton getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new MusicSingleton(context);
        }
        return sInstance;
    }

    public void playMusic(int bell) {
        mMediaPlayer = MediaPlayer.create(mContext, bell);
        mMediaPlayer.start();
    }

    public void stopMusic() {
        if(mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.seekTo(0);
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
