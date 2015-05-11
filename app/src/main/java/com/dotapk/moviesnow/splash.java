package com.dotapk.moviesnow;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;



public class splash extends Activity{

	MediaPlayer ourSong;
	@Override
	protected void onCreate(Bundle jitrox) {
		// TODO Auto-generated method stub
		super.onCreate(jitrox);
		setContentView(R.layout.splash);
		ourSong= MediaPlayer.create(splash.this,R.raw.splash);
		ourSong.start();
		Thread timer =new Thread(){
			public void run(){
				try{
					sleep(5000);
				}catch(InterruptedException e){
					e.printStackTrace();
					
				}finally{
					Intent open=new Intent("com.dotapk.moviesnow.MAINACTIVITY");
					startActivity(open);
					
				}
			}
		};
		timer.start();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		ourSong.release();
		finish();
	}
	

}
