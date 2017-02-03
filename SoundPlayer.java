package uk.ac.reading.sis05kol.mooc;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * Created by VIPIN on 21-11-2016.
 */

public class SoundPlayer {
    private static SoundPool soundPool;
    private static int hitsound;
    public SoundPlayer(Context context){
        soundPool=new SoundPool(2, AudioManager.STREAM_MUSIC,0);
        hitsound=soundPool.load(context,R.raw.gravity,1);
    }
    public void playhitsound(){
        soundPool.play(hitsound,1.0f,1.0f,1,0,1.0f);
    }
}