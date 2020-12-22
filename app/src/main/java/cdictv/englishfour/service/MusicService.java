package cdictv.englishfour.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import cdictv.englishfour.R;

public class MusicService extends Service {
    private MediaPlayer mediaPlayer;

    //但会定义的中间人对象
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }
    //服务已开启 就执行这个方法
    @Override
    public void onCreate() {
        //[1]初始化mediaplayer
        mediaPlayer =new MediaPlayer();

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    //播放音乐方法
    public void playMusic(){
        //[2]设置要播放的资源位置  path 可以是网络 路径 也可是本地路径
        try {
            mediaPlayer.reset();
//            Uri uri=Uri.parse("android:resource://cdictv.englishfour/"+ R.raw.syst);
            mediaPlayer=MediaPlayer.create(this, R.raw.syst);
//            mediaPlayer.setDataSource(MusicService.this,uri);

            //[4]开始播放
            mediaPlayer.start();
            Log.i("===","放歌咯");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //暂停音乐方法
    public void pauseMusic(){
        mediaPlayer.pause();

    }
    //继续播放音乐方法
    public void replayMusic(){
        mediaPlayer.start();
        Log.i("===","继续放歌咯");
    }
    //[1]在服务内部定义一个中间人对象binder
    private  class MyBinder extends Binder implements Iservice{

        @Override
        public void callplayMusic() {
            playMusic();
        }

        @Override
        public void callpauseMusic() {
            pauseMusic();
        }

        @Override
        public void callreplayMusic() {
            replayMusic();
        }
    }
}
