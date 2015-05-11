package com.dotapk.moviesnow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

public class Simplebrowser extends Activity implements OnClickListener{

    String loc;
	WebView ourbrow;
	EditText url;
	Button go,back,refresh,forward,clear;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simplebrowser);
		ourbrow = (WebView) findViewById (R.id.wvBrowser);
		ourbrow.getSettings().setJavaScriptEnabled(true);
		ourbrow.getSettings().setLoadWithOverviewMode(true);
		ourbrow.getSettings().setUseWideViewPort(true);
		ourbrow.getSettings().setBuiltInZoomControls(true);
		ourbrow.getSettings().setSupportZoom(true);
        ourbrow.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		ourbrow.setWebViewClient(new ourViewClient());
        Intent intent=getIntent();
        String url=intent.getStringExtra("url");
		try{
		ourbrow.loadUrl(url);
		} catch (Exception e){
			e.printStackTrace();
		}

		back=(Button) findViewById (R.id.bBack);
		refresh=(Button) findViewById (R.id.bRefresh);
		forward=(Button) findViewById (R.id.bForward);


		back.setOnClickListener(this);
		refresh.setOnClickListener(this);
		forward.setOnClickListener(this);

		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){

		
		case R.id.bBack:
			if(ourbrow.canGoBack())
				ourbrow.goBack();
			break;
		case R.id.bForward:
			if(ourbrow.canGoForward())
				ourbrow.goForward();
			break;

		case R.id.bRefresh:
			ourbrow.reload();
			break;
		}
			
		
	}
	

}
