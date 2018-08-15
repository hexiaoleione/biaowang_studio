package com.hex.express.iwant.service;

import java.io.IOException;

import com.hex.express.iwant.R;

import android.app.Service;  
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;  
  
public class MediaplayService extends Service{

	    private MediaPlayer mp;  
	    
	    @Override  
	    public void onCreate() {  
	        // TODO Auto-generated method stub  
	        // 初始化音乐资源  
	        try {  
	            // 创建MediaPlayer对象  
	            mp = new MediaPlayer();  
	            // 将音乐保存在res/raw/xingshu.mp3,R.java中自动生成{public static final int xingshu=0x7f040000;}  
	            mp = MediaPlayer.create(MediaplayService.this, R.raw.nbwse);// nbw.mp3  biaow 
	            //http://pc1.gamedog.cn/ring/1412/xiaohuangrenchaojigaoxiaodedaxiaosheng.mp3
//	            Uri uri = Uri.parse("http://boscdn.bpc.baidu.com/v1/developer/b361af63-53bf- 46d1-ad90-03308bf9bd49.mp3");  
//	            mp.setDataSource(this, uri);  
//	            Uri setDataSourceuri = Uri.parse("android.resource://com.android.sim/"+R.raw.beep);
//	            mp.setDataSource(this, setDataSourceuri);
	            // 在MediaPlayer取得播放资源与stop()之后要准备PlayBack的状态前一定要使用MediaPlayer.prepeare()  
	            mp.prepare();  
	            mp.setLooping(false);
//	            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
	            mp.setVolume(1, 1);
	        } catch (IllegalStateException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        } catch (IOException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        }  
	  
	        super.onCreate();  
	    }  
	  
	    @Override  
	    public void onStart(Intent intent, int startId) {  
	        // TODO Auto-generated method stub  
	    	
	    	try {
	    		  // 开始播放音乐  
		        mp.start();  
			} catch (Exception e) {
				// TODO: handle exception
			}
	      
	        // 音乐播放完毕的事件处理  
	        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {  
	  
	            public void onCompletion(MediaPlayer mp) {  
	                // TODO Auto-generated method stub  
	                // 循环播放  
	                try {  
//	                    mp.start();  
	                	   mp.stop();  
//	                	   mp.release();  
	                	   
	                } catch (IllegalStateException e) {  
	                    // TODO Auto-generated catch block  
	                    e.printStackTrace();  
	                }  
	            }  
	        });  
	        // 播放音乐时发生错误的事件处理  
	        mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {  
	  
	            public boolean onError(MediaPlayer mp, int what, int extra) {  
	                // TODO Auto-generated method stub  
	                // 释放资源  
	                try {  
	                    mp.release();  
	                } catch (Exception e) {  
	                    e.printStackTrace();  
	                }  
	  
	                return false;  
	            }  
	        });  
	  
	        super.onStart(intent, startId);  
	    }  
	  
	    @Override  
	    public void onDestroy() {  
	        // TODO Auto-generated method stub  
	        // 服务停止时停止播放音乐并释放资源  
	        mp.stop();  
	        mp.release();  
	  
	        super.onDestroy();  
	    }  
	  
	    @Override  
	    public IBinder onBind(Intent intent) {  
	        // TODO Auto-generated method stub  
	        return null;  
	    }  
	  
	
}
