package com.dotapk.moviesnow;

import java.io.File;
import java.util.Locale;

import android.app.DownloadManager;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.webkit.URLUtil;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import com.dotapk.moviesnow.R;
import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

public class SynActivity extends ActionBarActivity{
    private Menu menu,menutran;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_syn);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();//this
			tts = new TextToSpeech(SynActivity.this,
					new TextToSpeech.OnInitListener() {

						@Override
						public void onInit(int status) {
							// TODO Auto-generated method stub
							if (status != TextToSpeech.ERROR) {
								tts.setLanguage(Locale.US);
							}
						}
					});
		}
	}

	@Override
	protected void onPostResume() {
		// TODO Auto-generated method stub
		super.onPostResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if (tts != null) {
			tts.stop();
			tts.shutdown();
			
		}
		super.onPause();
	}
    MenuItem speak1,trans;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        this.menutran=menu;
        trans=menu.findItem(R.id.Translate);
        speak1=menu.findItem(R.id.speak);
		return true;
	}

	private static final String LOG_TAG = PlaceholderFragment.class
			.getSimpleName();
    int m=1,p=1;
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.speak) {
            if(m%2==1) {
                speak1.setTitle("Stop Reading");
                m++;
                tts.speak(syn.replaceAll("\\<[^>]*>", ""),
                        TextToSpeech.QUEUE_FLUSH, null);
            }
            else
            {
                m++;
                speak1.setTitle("Read");
                tts.stop();
            }
		}
		if (id == R.id.action_settings) {
			startActivity(new Intent(SynActivity.this, SettingActivity.class));
			return true;
		}
		
		if (id == R.id.Translate) {
			
			if(i==0)
			{

			class bgStuff extends AsyncTask<Void, Void, Void> {

				

				@Override
				protected Void doInBackground(Void... params) {
					// TODO Auto-generated method stub
					try {

						translatedText = translate(syn.replaceAll("\\<[^>]*>",
								"").toString());
						tt2 = translate(Title).toString();
						tt3 = translate(Caption);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						translatedText = e.toString();
					}

					return null;
				}

				@Override
				protected void onPostExecute(Void result) {
					// TODO Auto-generated method stub
					web.setVisibility(View.GONE);
					Trans1.setVisibility(View.VISIBLE);
					Trans1.setText(translatedText);
					Trans2.setText(tt2);
					Trans3.setText(tt3);
                    trans.setTitle("English");
					super.onPostExecute(result);
				}

			}
			new bgStuff().execute();
			i++;
		}
			else if(i%2==1)
			{
                trans.setTitle("Hindi");
				web.setVisibility(View.VISIBLE);
				Trans1.setVisibility(View.GONE);
				Trans2.setText(Title);
				Trans3.setText(Caption);
				i++;
			}
			else
			{

                trans.setTitle("English");
				web.setVisibility(View.GONE);
				Trans1.setVisibility(View.VISIBLE);
				Trans1.setText(translatedText);
				Trans2.setText(tt2);
				Trans3.setText(tt3);
				i++;
			}
		}
		return super.onOptionsItemSelected(item);
	}
	String translatedText, tt2, tt3;
	static int i = 0;
	private static int con;
	static MenuItem menuItem;
	private static String Trans;
	private static TextView Trans1;
	private static TextView Trans2;
	private static TextView Trans3;
	private static String Title;
	private static String Photo;
	private static String Year;
	private static String Caption;
	private String Pos;
	private String dvd;
	private static String syn;
	static ImageView Image;
	TextToSpeech tts;
	static WebView web;

	public String translate(String text) throws Exception {

		// Set the Client ID / Client Secret once per JVM. It is set statically
		// and applies to all services
		Translate.setClientId("NewZerTrans"); // Change this
		Translate
				.setClientSecret("FXwcgOIg03a4qIACOmwyFRxoWZC08E0V1rt21iuG1MM="); // change

		String translatedText = "";

		translatedText = Translate.execute(text, Language.HINDI);

		return translatedText.toString();
	}
	//static Context context;
	/**
	 * 
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {


		public PlaceholderFragment() {

			setHasOptionsMenu(true);
		}

		
		  @Override 
		  public void onCreateOptionsMenu(Menu menu, MenuInflater
		  inflater) { // TODO Auto-generated method stub
		  inflater.inflate(R.menu.synmen, menu);
		  
		  menuItem = menu.findItem(R.id.action_share);
		  
		  ShareActionProvider mShareActionProvider = (ShareActionProvider)
		  MenuItemCompat .getActionProvider(menuItem);
		  
		  if (mShareActionProvider != null) { mShareActionProvider
		  .setShareIntent(createShareForecastIntent());
		  
		  } else { Log.d(LOG_TAG, "Share action provider is null ? "); } }
		 
		private static final String LOG_TAG = PlaceholderFragment.class
				.getSimpleName();

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			Intent intent = getActivity().getIntent();

			View rootView = inflater.inflate(R.layout.fragment_syn, container,
					false);
			 
			
			Title = intent.getStringExtra("Tit");
			// Year = intent.getStringExtra("Year");
			Photo = intent.getStringExtra("pho");
			Caption = intent.getStringExtra("cap");
			/*
			 * Pos= intent.getStringExtra("Pos"); dvd=
			 * intent.getStringExtra("dvd");
			 */
			syn = intent.getStringExtra("syn");
			Image = (ImageView) rootView.findViewById(R.id.ivPhoto);
            Image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(getActivity(), Image);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater()
                            .inflate(R.menu.popup, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                           int id=item.getItemId();
                            if(id==R.id.Save)
                            {

                                // TODO Auto-generated method stub

                                DownloadManager.Request request = new DownloadManager.Request(
                                        Uri.parse(Photo));
                                request.setTitle("File Download.");
                                request.setDescription("File is being downloaded....");

                                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                                String name = URLUtil.guessFileName(Photo, null,
                                        MimeTypeMap.getFileExtensionFromUrl(Photo));
                                File rootdirectory = new File(
                                        Environment
                                                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                                        "NewZer");
                                if(!rootdirectory.exists())
                                {
                                    rootdirectory.mkdirs();
                                }

                                //request.setDestinationInExternalPublicDir(
                                //Environment.DIRECTORY_DOWNLOADS, name);
                                request.setDestinationInExternalPublicDir(rootdirectory.toString(), name);
                                DownloadManager manager = (DownloadManager) getActivity()
                                        .getSystemService(Context.DOWNLOAD_SERVICE);
                                request.allowScanningByMediaScanner();
                                manager.enqueue(request);


                            }
                            return true;
                        }
                    });

                    popup.show();
                }
            });
			new DownloadImageTask(
					(ImageView) rootView.findViewById(R.id.ivPhoto))
					.execute(Photo);
			Trans1 = (TextView) rootView.findViewById(R.id.textView);
			Trans2 = (TextView) rootView.findViewById(R.id.tvTitle);
			Trans3 = (TextView) rootView.findViewById(R.id.tvPhotocom);
			((TextView) rootView.findViewById(R.id.tvTitle)).setText(Title);
            Typeface fonts = Typeface.createFromAsset(getActivity().getAssets(),
                    "Nesia.ttf");
            Trans2.setTypeface(fonts);
			((TextView) rootView.findViewById(R.id.textView))
					.setVisibility(View.GONE);
			syn = syn.replaceAll("<strong>", "<br><strong>");
			syn = syn.replaceAll("\n", "\n<br>");
			/*
			 * ((TextView) rootView.findViewById(R.id.tvDuration) )
			 * .setText(Run);
			 */
			((TextView) rootView.findViewById(R.id.tvPhotocom))
					.setText(Caption);
			// ((TextView) rootView.findViewById(R.id.tvDvd) )
			// .setText("DVD:"+dvd);*/
			/*
			 * ((TextView) rootView.findViewById(R.id.tvRun) ) .setText(Run +
			 * "mins");
			 */
			/*
			 * ((WebView) rootView.findViewById(R.id.tvSyn) )
			 * .loadData((syn.toString
			 * ()),"text/html; charset=UTF-8",null.replaceAll("\\<[^>]*>","");
			 */
			web = (WebView) rootView.findViewById(R.id.tvSyn);
			((WebView) rootView.findViewById(R.id.tvSyn)).loadData(syn,
					"text/html", "utf-8");
            String m ="<font color='white'>"+ syn + "<br>" +"<font color='cyan'>"+"<font size='2'>"+""+"</font>";
            ((WebView) rootView.findViewById(R.id.tvSyn)).loadData(m,
                    "text/html", "utf-8");
            ((WebView) rootView.findViewById(R.id.tvSyn)).getSettings()
					.setSupportZoom(true);
			// ((WebView)
			// rootView.findViewById(R.id.tvSyn)).getSettings().setUseWideViewPort(true);
			// ((WebView)
			// rootView.findViewById(R.id.tvSyn)).getSettings().setLoadWithOverviewMode(true);
		/*	((WebView) rootView.findViewById(R.id.tvSyn)).getSettings()
					.setBuiltInZoomControls(true);*/
			((WebView) rootView.findViewById(R.id.tvSyn)).getSettings()
					.setJavaScriptEnabled(true);
            ((WebView) rootView.findViewById(R.id.tvSyn)).setBackgroundColor(0x00000000);
			final WebSettings webSettings = ((WebView) rootView
					.findViewById(R.id.tvSyn)).getSettings();
			Resources res = getResources();

        	SharedPreferences sharedPrefs=
        			PreferenceManager.getDefaultSharedPreferences(getActivity());
        	String font=sharedPrefs.getString(
        			getActivity().getString(R.string.pref_units_key),
        			getActivity().getString(R.string.pref_units_metric));
        	if (font.equals(getActivity().getString(R.string.pref_font_small))){
        		float fontSize = res.getDimension(R.dimen.txtSizesmall);
    			webSettings.setDefaultFontSize((int) fontSize);
    		
    			
			}
        	else if (font.equals(getActivity().getString(R.string.pref_font_normal))){
        		float fontSize = res.getDimension(R.dimen.txtSizenormal);
    			webSettings.setDefaultFontSize((int) fontSize);
    		
    			
			}
        	else if (font.equals(getActivity().getString(R.string.pref_font_large))){
        		float fontSize = res.getDimension(R.dimen.txtSizelarge);
    			webSettings.setDefaultFontSize((int) fontSize);
    			
    			
			}
			
			/*
			 * ((TextView) rootView.findViewById(R.id.tvSyn) ) .setText(syn);
			 */
			return rootView;

		}

		@Override
		public void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
			web.onPause();
		}

		@SuppressWarnings("deprecation")
		private Intent createShareForecastIntent() {
			Intent shareIntent = new Intent(Intent.ACTION_SEND);
			shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			shareIntent.setType("text/plain");
			shareIntent.putExtra(Intent.EXTRA_TEXT, syn.replaceAll("\\<[^>]*>",
					"").toString() + "#jits");
			return shareIntent;
		}
	}
}
