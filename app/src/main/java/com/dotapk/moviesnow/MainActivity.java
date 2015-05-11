package com.dotapk.moviesnow;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import com.dotapk.moviesnow.imageutils.ImageLoader;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;

/*import com.devspark.sidenavigation.ISideNavigationCallback;
import com.devspark.sidenavigation.SideNavigationView;*/

public class MainActivity extends ActionBarActivity implements OnClickListener,
		LoaderCallbacks<Cursor>, OnTouchListener {
	
	//SideNavigationView sideNavigationView;
	public static final int LOADER = 0x01;
	private static final int news_PAGE_LIMIT = 20;
	TextToSpeech tts;
	Button tech, cricket, business, world, india, sports, tv, events, edu, env,
			sci, nri, beauty, breaking, local,live;
	LazyAdapter adapter;
	SQLiteDatabase db,fdb;
	ContentValues cv = new ContentValues();
    ContentValues cv1 = new ContentValues();
	SimpleCursorAdapter cad;
	DBHelper dbhelper = new DBHelper(this);

    LinearLayout searchlay;
	String net;
	ListView NewsList;
	Button[] ar = new Button[16];
	ImageView image;
    TextView maincap;
    LinearLayout lay;
    private Menu menu;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        ActionBar bar=getSupportActionBar();

       searchlay=(LinearLayout) findViewById(R.id.searchlay);
       lay=(LinearLayout) findViewById(R.id.lay);

		db = dbhelper.getWritableDatabase();

		// db.delete(DBHelper.DATABASE_TABLE, null, null);

		ContentValues cv = new ContentValues();
		new RequestTask()
				.execute("http://timesofindia.indiatimes.com/feeds/newsdefaultfeeds.cms?feedtype=sjson");
       // Toast.makeText(MainActivity.this," "+con,Toast.LENGTH_SHORT).show();
        if(con==1){
            builder = new AlertDialog.Builder(this,
                    AlertDialog.THEME_DEVICE_DEFAULT_DARK);


            builder.setMessage("NewZer will run in offline mode").setTitle(
                    "No Interner Connection");
            builder.setNeutralButton("Ok",null);
            // 3. Get the AlertDialog from create()
            dialog = builder.create();

            dialog.show();
        }
		cv.put(DBHelper.TYPE, "Breaking");
		NewsList = (ListView) findViewById(R.id.list_news);
        image=(ImageView)findViewById(R.id.IVmainImg);
        maincap=(TextView) findViewById(R.id.TVmainCap);
        live=(Button) findViewById(R.id.bLive);
        searchlay.setVisibility(View.GONE);
		tech = (Button) findViewById(R.id.bTech);
		breaking = (Button) findViewById(R.id.bBreakingNews);
		local = (Button) findViewById(R.id.bLocal);
		cricket = (Button) findViewById(R.id.bCricket);
		business = (Button) findViewById(R.id.bBusiness);
		india = (Button) findViewById(R.id.bIndia);
		world = (Button) findViewById(R.id.bWorld);
		env = (Button) findViewById(R.id.bEnvironment);
		edu = (Button) findViewById(R.id.bEducation);
		sports = (Button) findViewById(R.id.bSports);
		sci = (Button) findViewById(R.id.bScience);
		beauty = (Button) findViewById(R.id.bBeauty);
		nri = (Button) findViewById(R.id.bNRI);
		tv = (Button) findViewById(R.id.bTV);

		events = (Button) findViewById(R.id.bEvents);

		ar[1] = tech;
		ar[2] = cricket;
		ar[3] = business;
		ar[4] = india;
		ar[5] = world;
		ar[6] = edu;
		ar[7] = env;
		ar[8] = nri;
		ar[9] = sci;
		ar[11] = events;
		ar[12] = tv;
		ar[13] = beauty;
		ar[14] = breaking;
		ar[15] = local;
		ar[10] = sports;
        live.setVisibility(View.GONE);
		Typeface font = Typeface.createFromAsset(this.getAssets(),
				"24Janvier.otf");
		for (i = 1; i <= 15; i++)
			ar[i].setTypeface(font);
        image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(con==0){
                    Intent i = new Intent(MainActivity.this, SynActivity.class);
                    // i.putExtra("a", newsSyn[position]);

                    i.putExtra("syn", newsSyn[0]);
                    i.putExtra("pho", newsPhoto[0]);
                    i.putExtra("Tit", newsTitlea[0]);
                    i.putExtra("cap", newsPhotoCap[0]); // i.putExtra("tit",newsTitlea[position]);



                    startActivity(i);
                }
            }
        });
		tech.setOnClickListener(this);
		env.setOnClickListener(this);
        live.setOnClickListener(this);
		edu.setOnClickListener(this);
		breaking.setOnClickListener(this);
		local.setOnClickListener(this);
		beauty.setOnClickListener(this);
		nri.setOnClickListener(this);
		sci.setOnClickListener(this);
		tv.setOnClickListener(this);
		sports.setOnClickListener(this);
		events.setOnClickListener(this);
		cricket.setOnClickListener(this);
		business.setOnClickListener(this);
		india.setOnClickListener(this);
		world.setOnClickListener(this);
		String[] a = new String[] { DBHelper.TITLE };
		int[] na = new int[] { R.id.textView1 };
		Cursor c = db.rawQuery("select * from " + DBHelper.DATABASE_TABLE
				+ " where " + DBHelper.TYPE + "=?", new String[] { "India" });
		// Cursor cursor =db.query(DBHelper.DATABASE_TABLE, null, null, null,
		// null, null, null);
		startManagingCursor(c);
		String[] b = { "a", "a" };
		cad = new SimpleCursorAdapter(getApplicationContext(), R.layout.single,
				c, a, na);
		adapter = new LazyAdapter(MainActivity.this, b, a);
		NewsList.setAdapter(cad);
		NewsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

				if (con == 0) {
                    if(isTablet(MainActivity.this)){
                        LinearLayout ll=(LinearLayout) findViewById(R.id.TabDet);
                        ll.setVisibility(View.VISIBLE);
                        Typeface fonts = Typeface.createFromAsset(MainActivity.this.getAssets(),
                                "Nesia.ttf");
                        TextView TabTitle=(TextView) findViewById(R.id.tvTabTitle);
                        TabTitle.setText(newsTitlea[position]);
                        TabTitle.setTypeface(fonts);
                        WebView TabDet=(WebView)findViewById(R.id.tvTabSyn);
                        String texts=newsSyn[position];
                        texts = texts.replaceAll("<strong>", "<br><strong>");
                        texts = texts.replaceAll("\n", "\n<br>");
                        String m ="<font color='white'>"+ texts + "<br>" +"<font color='cyan'>"+"<font size='2'>"+""+"</font>";
                        TabDet.loadData(m,
                                "text/html", "utf-8");
                        TabDet.getSettings()
                                .setSupportZoom(true);

                        TabDet.getSettings()
                                .setJavaScriptEnabled(true);
                        TabDet.setBackgroundColor(0x00000000);
                    }
                    else {
                        Intent i = new Intent(MainActivity.this, SynActivity.class);
                        // i.putExtra("a", newsSyn[position]);

                        i.putExtra("syn", newsSyn[position]);
                        i.putExtra("pho", newsPhoto[position]);
                        i.putExtra("Tit", newsTitlea[position]);
                        i.putExtra("cap", newsPhotoCap[position]); // i.putExtra("tit",newsTitlea[position]);


                        startActivity(i);
                    }
				} else {
					Cursor c = db.rawQuery("select * from "
							+ DBHelper.DATABASE_TABLE + " where "
							+ DBHelper.TYPE + "=?", new String[] { select });
					c.moveToPosition(position);
					
					String str = " ";
					String det = " ";
					if (c.moveToPosition(position)) {
						det = c.getString(c.getColumnIndex(DBHelper.TITLE));
						str = c.getString(c.getColumnIndex(DBHelper.DETAILS));
					}
					c.close();
                    if(isTablet(MainActivity.this)){
                        LinearLayout ll=(LinearLayout) findViewById(R.id.TabDet);
                        ll.setVisibility(View.VISIBLE);
                        Typeface fonts = Typeface.createFromAsset(MainActivity.this.getAssets(),
                                "Nesia.ttf");
                        TextView TabTitle=(TextView) findViewById(R.id.tvTabTitle);
                        TabTitle.setText(det);
                        TabTitle.setTypeface(fonts);
                        WebView TabDet=(WebView)findViewById(R.id.tvTabSyn);
                        String texts=str;
                        texts = texts.replaceAll("<strong>", "<br><strong>");
                        texts = texts.replaceAll("\n", "\n<br>");
                        String m ="<font color='white'>"+ texts + "<br>" +"<font color='cyan'>"+"<font size='2'>"+""+"</font>";
                        TabDet.loadData(m,
                                "text/html", "utf-8");
                        TabDet.getSettings()
                                .setSupportZoom(true);

                        TabDet.getSettings()
                                .setJavaScriptEnabled(true);
                        TabDet.setBackgroundColor(0x00000000);
                    }
                    else {
                        Intent i = new Intent(MainActivity.this, SynActivity.class);

                        i.putExtra("Tit", det);
                        i.putExtra("syn", str);
                        i.putExtra("pho", "a");
                        i.putExtra("cap", " ");

                        startActivity(i);
                    }
				}
			}
		});
       if(isTablet(this)){
           NewsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
               @Override
               public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                   if (con == 0) {
                           Intent i = new Intent(MainActivity.this, SynActivity.class);
                           // i.putExtra("a", newsSyn[position]);

                           i.putExtra("syn", newsSyn[position]);
                           i.putExtra("pho", newsPhoto[position]);
                           i.putExtra("Tit", newsTitlea[position]);
                           i.putExtra("cap", newsPhotoCap[position]); // i.putExtra("tit",newsTitlea[position]);


                           startActivity(i);

                   } else {
                       Cursor c = db.rawQuery("select * from "
                               + DBHelper.DATABASE_TABLE + " where "
                               + DBHelper.TYPE + "=?", new String[] { select });
                       c.moveToPosition(position);

                       String str = " ";
                       String det = " ";
                       if (c.moveToPosition(position)) {
                           det = c.getString(c.getColumnIndex(DBHelper.TITLE));
                           str = c.getString(c.getColumnIndex(DBHelper.DETAILS));
                       }
                       c.close();
                       Intent i = new Intent(MainActivity.this, SynActivity.class);

                       i.putExtra("Tit", det);
                       i.putExtra("syn", str);
                       i.putExtra("pho", "a");
                       i.putExtra("cap", " ");

                       startActivity(i);
                   }

                   return true;
               }
           });
       }


		tts = new TextToSpeech(MainActivity.this,
				new TextToSpeech.OnInitListener() {

					@Override
					public void onInit(int status) {
						// TODO Auto-generated method stub
						if (status != TextToSpeech.ERROR) {
							tts.setLanguage(Locale.US);
						}
					}
				});
        if(con==1){
            builder = new AlertDialog.Builder(this,
                    AlertDialog.THEME_DEVICE_DEFAULT_DARK);


            builder.setMessage("NewZer will run in offline mode").setTitle(
                    "No Internet Connection");
            builder.setNeutralButton("Ok",null);
            // 3. Get the AlertDialog from create()
            dialog = builder.create();

            dialog.show();
        }

	}

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;

    }

        MenuItem speak1;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mainmen, menu);
        this.menu = menu;
        speak1=menu.findItem(R.id.speak1);
		return true;
	}
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            image.setVisibility(View.GONE);
            maincap.setVisibility(View.GONE);

        } else {
            if(con==0) {
                image.setVisibility(View.VISIBLE);
                maincap.setVisibility(View.VISIBLE);
            }

        }
    }
	int i, con = 0;
    EditText searchnews;
	String translatedText, tt2, tt3,sss;
       int m=1;
        int q=1;
    AlertDialog.Builder builder = null;
    AlertDialog dialog = null;
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int id = item.getItemId();


        //if (id == android.R.id.home)
			//sideNavigationView.toggleMenu();
		if (id == R.id.speak1) {

                if (con == 0) {
                    tts.speak(read + "Thank you", TextToSpeech.QUEUE_FLUSH, null);

                } else
                    tts.speak(read1 + "Thank you", TextToSpeech.QUEUE_FLUSH, null);

           }

		if (id == R.id.Location1) {
			startActivity(new Intent(this, SettingActivitymain.class));

		}
        if(id==R.id.About){
            builder = new AlertDialog.Builder(this,
                    AlertDialog.THEME_DEVICE_DEFAULT_DARK);


            builder.setMessage("Created By Jit Sarkar").setTitle(
                    "About");
            builder.setNeutralButton("Ok",null);
            // 3. Get the AlertDialog from create()
            dialog = builder.create();

            dialog.show();

        }
        if (id == R.id.Search) {
            if (q%2==1) {
                lay.setVisibility(View.GONE);
                searchlay.setVisibility(View.VISIBLE);
                searchnews=(EditText) findViewById(R.id.etNews);
                // sss=searchnews.getText().toString();
                ImageButton ib=(ImageButton) findViewById(R.id.imageButton);
                ib.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i=new Intent(MainActivity.this,Simplebrowser.class);
                       // Toast.makeText(MainActivity.this,searchnews.getText().toString(),Toast.LENGTH_SHORT).show();
                        i.putExtra("url","http://timesofindia.indiatimes.com/topic/"+searchnews.getText().toString());
                        startActivity(i);
                    }
                });
            }
            else
            {
                lay.setVisibility(View.VISIBLE);
                searchlay.setVisibility(View.GONE);
            }
            q++;
        }

		if (id == R.id.Refresh) {
			new RequestTask()
					.execute("http://timesofindia.indiatimes.com/feeds/newsdefaultfeeds.cms?feedtype=sjson");
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDestroy() {
		NewsList.setAdapter(null);
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if (tts != null) {
			tts.stop();
		//	tts.shutdown();

		}
		super.onPause();
	}

	int n = 25;
	int img[];
	String select = "India";
	String read = "Todays Breaking news are.";
	String read1 = "Todays Breaking news are.";
	String[] newsTitlea;// = new String[n];
	String[] newsPhoto;// = new String[n];
	String[] newsReleasedvd = new String[n];
	String[] newsPhotoCap;// = new String[n];
	String[] newsThumb;// = new String[n];
	String[] newsSyn;// = new String[n];
	String[] newsPos = new String[n];
	String[] newsRun = new String[n];
	String[] newsRuna = new String[n];

	private void refreshnewssList(String[] newsTitles, String[] imgs) {


		adapter = new LazyAdapter(this, imgs, newsTitles);
		NewsList.setAdapter(adapter);
	}

	private class RequestTask extends AsyncTask<String, String, String> {
		// make a request to the specified url
		@Override
		protected String doInBackground(String... uri) {

			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			String responseString = null;
			try {
				// make a HTTP request
				response = httpclient.execute(new HttpGet(uri[0]));
				StatusLine statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {

					// request successful - read the response and close the
					// connection
					con = 0;
					db.delete(DBHelper.DATABASE_TABLE, DBHelper.TYPE + "='"
							+ select + "'", null);
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					responseString = out.toString();
				} else {
					// request failed - close the connection
					response.getEntity().getContent().close();
                    con=1;
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (Exception e) {


				con = 1;

				Log.d("Test",
						"Couldn't make a successful request!" + e.toString());
			}
			return responseString;
		}

		int n;

		// if the request above completed successfully, this method will
		// automatically run so you can do something with the response
		@Override
		protected void onPostExecute(String response) {
			super.onPostExecute(response);

			if (response != null) {
				try {

					// convert the String response to a JSON object,
					// because JSON is the response format Rotten Tomatoes uses
					JSONObject jsonResponse = new JSONObject(response);

					// fetch the array of newss in the response
					JSONArray newss = jsonResponse.getJSONArray("NewsItem");
					// JSONArray newssrun = jsonResponse.getJSONArray("poster");
					// add each news's title to an array

					// String[] newsSyn = new String[newss.length()];
					String[] newsTitles = new String[newss.length()];
					String[] newsDetail = new String[newss.length()];
					String[] newsYear = new String[newss.length()];
					newsTitlea = new String[newss.length()];
					newsPhoto = new String[newss.length()];

					newsPhotoCap = new String[newss.length()];
					newsThumb = new String[newss.length()];
					newsSyn = new String[newss.length()];
					n = newss.length();
					for (int i = 0; i < newss.length(); i++) {
						// JSONObject newsr = newssrun.getJSONObject(i);
						JSONObject news = newss.getJSONObject(i);
						if (news.has("HeadLine")) {
							newsTitles[i] = news.getString("HeadLine");
							cv.put(DBHelper.TITLE, newsTitles[i]);
							newsTitlea[i] = news.getString("HeadLine");
						} else
							newsTitles[i] = "";
						if (news.has("DateLine"))
							newsYear[i] = news.getString("DateLine");
						else
							newsYear[i] = "";
						if (news.has("Story")) {
							newsSyn[i] = news.getString("Story");
							cv.put(DBHelper.DETAILS, newsSyn[i]);

						} else
							newsSyn[i] = "";

						try {
							JSONObject ab = news.getJSONObject("Image");

							if (ab.has("PhotoCaption")) {

								newsPhotoCap[i] = ab.getString("PhotoCaption");
								cv.put(DBHelper.CAPTION, newsPhotoCap[i]);
							} else
								newsPhotoCap[i] = "";
						} catch (JSONException e) {
							newsPhotoCap[i] = "";
						}
						try {
							JSONObject ab = news.getJSONObject("Image");

							if (ab.has("Photo")) {
								newsPhoto[i] = ab.getString("Photo");

							} else
								newsPhoto[i] = "https://cdn2.iconfinder.com/data/icons/picol-vector/32/news-128.png";
						} catch (JSONException e) {
							newsPhoto[i] = "https://cdn2.iconfinder.com/data/icons/picol-vector/32/news-128.png";
						}
						try {
							JSONObject ab = news.getJSONObject("Image");

							if (ab.has("Thumb")) {
								newsThumb[i] = ab.getString("Thumb");

							} else
								newsThumb[i] = "https://cdn2.iconfinder.com/data/icons/picol-vector/32/news-128.png";
						} catch (JSONException e) {
							newsThumb[i] = "https://cdn2.iconfinder.com/data/icons/picol-vector/32/news-128.png";
						}

						/*
						 * JSONObject newsR = news.getJSONObject("posters");
						 * newsPos[i]=newsR.getString("original");
						 */
						/*
						 * ContentResolver content =
						 * MainActivity.this.getContentResolver();
						 * content.insert(NewsProvider.CONTENT_URI, cv);
						 */
						db.insert(DBHelper.DATABASE_TABLE, null, cv);
						read = read + "." + " " + (i + 1) + "." + newsTitles[i]
								+ ".";
						newsDetail[i] = (newsTitles[i]);
                        if(i==0)
                            dispimg(newsTitles[i],newsPhoto[i]);
					}

					// db.close();
					refreshnewssList(newsDetail, newsThumb);

				} catch (JSONException e) {
					Log.d("Test",
							"Failed to parse the JSON response!" + e.toString());
				}
			}
		}
	}

    private void dispimg(String newsTitle, String s) {


            maincap.setText(newsTitle);
            ImageLoader imageLoader=new ImageLoader(getApplicationContext());
            imageLoader.DisplayImage(s, image);
        if(con==1){
            maincap.setText("No Internet Connection!");

        }

    }

    @SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {

		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bTech:
			tts.stop();
            live.setVisibility(View.GONE);
			cv.put(DBHelper.TYPE, "Tech");
			select = "Tech";
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                image.setVisibility(View.VISIBLE);
                maincap.setVisibility(View.VISIBLE);
            }
			read = read1 = "Todays tech news are.";
			butcol(1);
			if (con == 0)
				new RequestTask()
						.execute("http://timesofindia.indiatimes.com/feeds/newsfeed/5880659.cms?feedtype=sjson");
			else {
                image.setVisibility(View.GONE);
                maincap.setVisibility(View.GONE);
				String[] a = new String[] { DBHelper.TITLE };
				int[] na = new int[] { R.id.textView1 };
				// db.q
				Cursor c = db.rawQuery("select * from "
						+ DBHelper.DATABASE_TABLE + " where " + DBHelper.TYPE
						+ "=?", new String[] { "Tech" });
				getread(c);
				// Cursor cursor =db.query(DBHelper.DATABASE_TABLE, null, null,
				// null, null, null, null);
				startManagingCursor(c);
				String[] b = { "a", "a" };
				cad = new SimpleCursorAdapter(getApplicationContext(),
						R.layout.single, c, a, na);
				// adapter = new LazyAdapter(MainActivity.this, b, a);
				NewsList.setAdapter(cad);
				// getLoaderManager().restartLoader(LOADER, null, null);
			}

			break;
		case R.id.bCricket:
			tts.stop();
			cv.put(DBHelper.TYPE, "Cricket");
			select = "Cricket";
            live.setVisibility(View.VISIBLE);
            live=(Button) findViewById(R.id.bLive);
			setTitle("Cricket News");
			butcol(2);
			read = read1 = "Todays Cricket news are.";
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                image.setVisibility(View.VISIBLE);
                maincap.setVisibility(View.VISIBLE);
            }
			if (con == 0)
				new RequestTask()
						.execute("http://timesofindia.indiatimes.com/feeds/newsfeed/4719161.cms?feedtype=sjson");
			else {
                image.setVisibility(View.GONE);
                maincap.setVisibility(View.GONE);
				String[] a = new String[] { DBHelper.TITLE };
				int[] na = new int[] { R.id.textView1 };
				// db.q
				Cursor c = db.rawQuery("select * from "
						+ DBHelper.DATABASE_TABLE + " where " + DBHelper.TYPE
						+ "=?", new String[] { select });
				getread(c);
				// Cursor cursor =db.query(DBHelper.DATABASE_TABLE, null, null,
				// null, null, null, null);
				startManagingCursor(c);
				String[] b = { "a", "a" };
				cad = new SimpleCursorAdapter(getApplicationContext(),
						R.layout.single, c, a, na);
				adapter = new LazyAdapter(MainActivity.this, b, a);
				NewsList.setAdapter(cad);
			}
			break;
		case R.id.bBusiness:
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                image.setVisibility(View.VISIBLE);
                maincap.setVisibility(View.VISIBLE);
            }
			cv.put(DBHelper.TYPE, "Business");
			select = "Business";
			butcol(3);
            live.setVisibility(View.GONE);
			read = read1 = "Todays Business News are.";
			tts.stop();
			if (con == 0)
				new RequestTask()
						.execute("http://timesofindia.indiatimes.com/feeds/newsdefaultfeeds.cms?feedtype=sjson");
			else {
                image.setVisibility(View.GONE);
                maincap.setVisibility(View.GONE);
				String[] a = new String[] { DBHelper.TITLE };
				int[] na = new int[] { R.id.textView1 };
				// db.q
				Cursor c = db.rawQuery("select * from "
						+ DBHelper.DATABASE_TABLE + " where " + DBHelper.TYPE
						+ "=?", new String[] { select });
				// Cursor cursor =db.query(DBHelper.DATABASE_TABLE, null, null,
				// null, null, null, null);
				getread(c);
				startManagingCursor(c);
				String[] b = { "a", "a" };
				cad = new SimpleCursorAdapter(getApplicationContext(),
						R.layout.single, c, a, na);
				adapter = new LazyAdapter(MainActivity.this, b, a);
				NewsList.setAdapter(cad);
			}
			break;
		case R.id.bIndia:
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                image.setVisibility(View.VISIBLE);
                maincap.setVisibility(View.VISIBLE);
            }
			setTitle("India News");
			cv.put(DBHelper.TYPE, "India");
            live.setVisibility(View.GONE);
			select = "India";
			butcol(4);
			// Toast.makeText(getApplicationContext(), " "+con,
			// Toast.LENGTH_SHORT).show();
			read = read1 = "Todays India News are.";
			tts.stop();

			new RequestTask()
					.execute("http://timesofindia.indiatimes.com/feeds/newsfeed/1898055.cms?feedtype=sjson");
			if (con == 1) {
                image.setVisibility(View.GONE);
                maincap.setVisibility(View.GONE);
				String[] a = new String[] { DBHelper.TITLE };
				int[] na = new int[] { R.id.textView1 };
				// db.q
				Cursor c = db.rawQuery("select * from "
						+ DBHelper.DATABASE_TABLE + " where " + DBHelper.TYPE
						+ "=?", new String[] { select });
				getread(c);
				// Cursor cursor =db.query(DBHelper.DATABASE_TABLE, null, null,
				// null, null, null, null);
				startManagingCursor(c);
				String[] b = { "a", "a" };
				cad = new SimpleCursorAdapter(getApplicationContext(),
						R.layout.single, c, a, na);
				adapter = new LazyAdapter(MainActivity.this, b, a);
				NewsList.setAdapter(cad);
			}
			break;
		case R.id.bWorld:
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                image.setVisibility(View.VISIBLE);
                maincap.setVisibility(View.VISIBLE);
            }
			cv.put(DBHelper.TYPE, "World");
			select = "World";
			butcol(5);
            live.setVisibility(View.GONE);
			read = read1 = "Todays World News are.";
			tts.stop();
			if (con == 0)
				new RequestTask()
						.execute("http://timesofindia.indiatimes.com/feeds/newsfeed/296589292.cms?feedtype=sjson");
			else {
                image.setVisibility(View.GONE);
                maincap.setVisibility(View.GONE);
				String[] a = new String[] { DBHelper.TITLE };
				int[] na = new int[] { R.id.textView1 };
				// db.q
				Cursor c = db.rawQuery("select * from "
						+ DBHelper.DATABASE_TABLE + " where " + DBHelper.TYPE
						+ "=?", new String[] { select });
				getread(c);
				// Cursor cursor =db.query(DBHelper.DATABASE_TABLE, null, null,
				// null, null, null, null);
				startManagingCursor(c);
				String[] b = { "a", "a" };
				cad = new SimpleCursorAdapter(getApplicationContext(),
						R.layout.single, c, a, na);
				adapter = new LazyAdapter(MainActivity.this, b, a);
				NewsList.setAdapter(cad);
			}
			break;
		case R.id.bEducation:
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                image.setVisibility(View.VISIBLE);
                maincap.setVisibility(View.VISIBLE);
            }
            live.setVisibility(View.GONE);
			select = "Education";
			butcol(6);
			cv.put(DBHelper.TYPE, "Education");
			read = read1 = "Todays Education News are.";
			tts.stop();
			if (con == 0)
				new RequestTask()
						.execute("http://timesofindia.indiatimes.com/feeds/newsfeed/913168846.cms?feedtype=sjson");
			else {
                image.setVisibility(View.GONE);
                maincap.setVisibility(View.GONE);
				String[] a = new String[] { DBHelper.TITLE };
				int[] na = new int[] { R.id.textView1 };
				// db.q
				Cursor c = db.rawQuery("select * from "
						+ DBHelper.DATABASE_TABLE + " where " + DBHelper.TYPE
						+ "=?", new String[] { select });
				getread(c);
				// Cursor cursor =db.query(DBHelper.DATABASE_TABLE, null, null,
				// null, null, null, null);
				startManagingCursor(c);
				String[] b = { "a", "a" };
				cad = new SimpleCursorAdapter(getApplicationContext(),
						R.layout.single, c, a, na);
				adapter = new LazyAdapter(MainActivity.this, b, a);
				NewsList.setAdapter(cad);
			}
			break;
		case R.id.bEnvironment:
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                image.setVisibility(View.VISIBLE);
                maincap.setVisibility(View.VISIBLE);
            }
            live.setVisibility(View.GONE);
			select = "Environment";
			butcol(7);
			cv.put(DBHelper.TYPE, "Environment");
			read = read1 = "Todays Environment news are.";
			tts.stop();
			if (con == 0)
				new RequestTask()
						.execute("http://timesofindia.indiatimes.com/feeds/newsfeed/2647163.cms?feedtype=sjson");
			else {
                image.setVisibility(View.GONE);
                maincap.setVisibility(View.GONE);
				String[] a = new String[] { DBHelper.TITLE };
				int[] na = new int[] { R.id.textView1 };
				// db.q
				Cursor c = db.rawQuery("select * from "
						+ DBHelper.DATABASE_TABLE + " where " + DBHelper.TYPE
						+ "=?", new String[] { select });
				getread(c);
				// Cursor cursor =db.query(DBHelper.DATABASE_TABLE, null, null,
				// null, null, null, null);
				startManagingCursor(c);
				String[] b = { "a", "a" };
				cad = new SimpleCursorAdapter(getApplicationContext(),
						R.layout.single, c, a, na);
				adapter = new LazyAdapter(MainActivity.this, b, a);
				NewsList.setAdapter(cad);
			}
			break;
		case R.id.bNRI:
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                image.setVisibility(View.VISIBLE);
                maincap.setVisibility(View.VISIBLE);
            }
			select = "NRI";
			butcol(8);
            live.setVisibility(View.GONE);
			cv.put(DBHelper.TYPE, "NRI");
			read = read1 = "Todays NRI news are.";
			tts.stop();
			if (con == 0)
				new RequestTask()
						.execute("http://timesofindia.indiatimes.com/feeds/newsfeed/7098563.cms?feedtype=sjson");
			else {
                image.setVisibility(View.GONE);
                maincap.setVisibility(View.GONE);
				String[] a = new String[] { DBHelper.TITLE };
				int[] na = new int[] { R.id.textView1 };
				// db.q
				Cursor c = db.rawQuery("select * from "
						+ DBHelper.DATABASE_TABLE + " where " + DBHelper.TYPE
						+ "=?", new String[] { select });
				getread(c);
				// Cursor cursor =db.query(DBHelper.DATABASE_TABLE, null, null,
				// null, null, null, null);
				startManagingCursor(c);
				String[] b = { "a", "a" };
				cad = new SimpleCursorAdapter(getApplicationContext(),
						R.layout.single, c, a, na);
				adapter = new LazyAdapter(MainActivity.this, b, a);
				NewsList.setAdapter(cad);
			}
			break;
		case R.id.bScience:
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                image.setVisibility(View.VISIBLE);
                maincap.setVisibility(View.VISIBLE);
            }
			select = "Science";
			butcol(9);
            live.setVisibility(View.GONE);
			cv.put(DBHelper.TYPE, "Science");
			read = read1 = "Todays Science News are.";
			tts.stop();
			if (con == 0)
				new RequestTask()
						.execute("http://timesofindia.indiatimes.com/feeds/newsfeed/-2128672765.cms?feedtype=sjson");
			else {
                image.setVisibility(View.GONE);
                maincap.setVisibility(View.GONE);
				// String[] a= new String [] {DBHelper.TITLE};
				String[] a = new String[] { DBHelper.TITLE };
				int[] na = new int[] { R.id.textView1 };
				// db.q
				Cursor c = db.rawQuery("select * from "
						+ DBHelper.DATABASE_TABLE + " where " + DBHelper.TYPE
						+ "=?", new String[] { select });
				getread(c);
				// Cursor cursor =db.query(DBHelper.DATABASE_TABLE, null, null,
				// null, null, null, null);
				startManagingCursor(c);
				String[] b = { "a", "a" };
				cad = new SimpleCursorAdapter(getApplicationContext(),
						R.layout.single, c, a, na);
				adapter = new LazyAdapter(MainActivity.this, b, a);
				NewsList.setAdapter(cad);
			}
			break;
		case R.id.bSports:
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                image.setVisibility(View.VISIBLE);
                maincap.setVisibility(View.VISIBLE);
            }
			select = "Sports";
			butcol(10);
            live.setVisibility(View.GONE);
			cv.put(DBHelper.TYPE, "Sports");
			read = read1 = "Todays sports news are.";
			tts.stop();
			if (con == 0)
				new RequestTask()
						.execute("http://timesofindia.indiatimes.com/feeds/newsfeed/4719148.cms?feedtype=sjson");
			else {
                image.setVisibility(View.GONE);
                maincap.setVisibility(View.GONE);
				// String[] a= new String [] {DBHelper.TITLE};
				String[] a = new String[] { DBHelper.TITLE };
				int[] na = new int[] { R.id.textView1 };
				// db.q
				Cursor c = db.rawQuery("select * from "
						+ DBHelper.DATABASE_TABLE + " where " + DBHelper.TYPE
						+ "=?", new String[] { select });
				getread(c);
				// Cursor cursor =db.query(DBHelper.DATABASE_TABLE, null, null,
				// null, null, null, null);
				startManagingCursor(c);
				String[] b = { "a", "a" };
				cad = new SimpleCursorAdapter(getApplicationContext(),
						R.layout.single, c, a, na);
				adapter = new LazyAdapter(MainActivity.this, b, a);
				NewsList.setAdapter(cad);
			}
			break;
		case R.id.bEvents:
			select = "Events";
			butcol(11);
            live.setVisibility(View.GONE);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                image.setVisibility(View.VISIBLE);
                maincap.setVisibility(View.VISIBLE);
            }
			cv.put(DBHelper.TYPE, "Events");
			read = read1 = " todays Events News are.";
			tts.stop();
			if (con == 0)
				new RequestTask()
						.execute("http://timesofindia.indiatimes.com/feeds/newsfeed/2277129.cms?feedtype=sjson");
			else {
                image.setVisibility(View.GONE);
                maincap.setVisibility(View.GONE);
				// String[] a= new String [] {DBHelper.TITLE};
				String[] a = new String[] { DBHelper.TITLE };
				int[] na = new int[] { R.id.textView1 };
				// db.q
				Cursor c = db.rawQuery("select * from "
						+ DBHelper.DATABASE_TABLE + " where " + DBHelper.TYPE
						+ "=?", new String[] { select });
				getread(c);
				// Cursor cursor =db.query(DBHelper.DATABASE_TABLE, null, null,
				// null, null, null, null);
				startManagingCursor(c);
				String[] b = { "a", "a" };
				cad = new SimpleCursorAdapter(getApplicationContext(),
						R.layout.single, c, a, na);
				adapter = new LazyAdapter(MainActivity.this, b, a);
				NewsList.setAdapter(cad);
			}
			break;
		case R.id.bTV:
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                image.setVisibility(View.VISIBLE);
                maincap.setVisibility(View.VISIBLE);
            }
			butcol(12);
            live.setVisibility(View.GONE);
			select = "TV";
			cv.put(DBHelper.TYPE, "TV");
			read = read1 = "todays tv news are.";
			tts.stop();
			if (con == 0)
				new RequestTask()
						.execute("http://timesofindia.indiatimes.com/feeds/newsfeed/17781976.cms?feedtype=sjson");
			else {

                image.setVisibility(View.GONE);
                maincap.setVisibility(View.GONE);
				// String[] a= new String [] {DBHelper.TITLE};
				String[] a = new String[] { DBHelper.TITLE };
				int[] na = new int[] { R.id.textView1 };
				// db.q
				Cursor c = db.rawQuery("select * from "
						+ DBHelper.DATABASE_TABLE + " where " + DBHelper.TYPE
						+ "=?", new String[] { select });
				getread(c);
				// Cursor cursor =db.query(DBHelper.DATABASE_TABLE, null, null,
				// null, null, null, null);
				startManagingCursor(c);
				String[] b = { "a", "a" };
				cad = new SimpleCursorAdapter(getApplicationContext(),
						R.layout.single, c, a, na);
				adapter = new LazyAdapter(MainActivity.this, b, a);
				NewsList.setAdapter(cad);
			}
			break;
		case R.id.bBeauty:
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                image.setVisibility(View.VISIBLE);
                maincap.setVisibility(View.VISIBLE);
            }
			select = "Beauty";
			butcol(13);
			cv.put(DBHelper.TYPE, "Beauty");
            live.setVisibility(View.GONE);
			read = read1 = "Todays Beauty pegaent news are.";
			tts.stop();
			if (con == 0)
				new RequestTask()
						.execute("http://beautypageants.indiatimes.com/feeds/newsfeedtoi/4866542.cms?feedtype=sjson");
			else {

                image.setVisibility(View.GONE);
                maincap.setVisibility(View.GONE);
				String[] a = new String[] { DBHelper.TITLE };
				int[] na = new int[] { R.id.textView1 };

				Cursor c = db.rawQuery("select * from "
						+ DBHelper.DATABASE_TABLE + " where " + DBHelper.TYPE
						+ "=?", new String[] { select });
				getread(c);
				// Cursor cursor =db.query(DBHelper.DATABASE_TABLE, null, null,
				// null, null, null, null);
				startManagingCursor(c);
				String[] b = { "a", "a" };
				cad = new SimpleCursorAdapter(getApplicationContext(),
						R.layout.single, c, a, na);
				adapter = new LazyAdapter(MainActivity.this, b, a);
				NewsList.setAdapter(cad);
			}
			break;
		case R.id.bBreakingNews:
			setTitle("Breaking News");
            live.setVisibility(View.GONE);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                image.setVisibility(View.VISIBLE);
                maincap.setVisibility(View.VISIBLE);
            }
			select = "Breaking";
			cv.put(DBHelper.TYPE, "Breaking");
			read = read1 = "Todays Breaking news are.";
			tts.stop();
			butcol(14);
			if (con == 0)
				new RequestTask()
						.execute("http://timesofindia.indiatimes.com/feeds/newsdefaultfeeds.cms?feedtype=sjson");
			else {


                image.setVisibility(View.GONE);
                maincap.setVisibility(View.GONE);
				// String[] a= new String [] {DBHelper.TITLE};
				String[] a = new String[] { DBHelper.TITLE };
				int[] na = new int[] { R.id.textView1 };
				// db.q
				Cursor c = db.rawQuery("select * from "
						+ DBHelper.DATABASE_TABLE + " where " + DBHelper.TYPE
						+ "=?", new String[] { select });
				getread(c);
				// Cursor cursor =db.query(DBHelper.DATABASE_TABLE, null, null,
				// null, null, null, null);
				startManagingCursor(c);
				String[] b = { "a", "a" };
				cad = new SimpleCursorAdapter(getApplicationContext(),
						R.layout.single, c, a, na);
				adapter = new LazyAdapter(MainActivity.this, b, a);
				NewsList.setAdapter(cad);
			}
			break;
            case R.id.bLive:
                tts.stop();
                Intent j=new Intent(MainActivity.this,Simplebrowser.class);
                j.putExtra("url","http://m.cricbuzz.com/cricket-match/live-scores");
                startActivity(j);
                break;
		case R.id.bLocal:
			read = read1 = "";
            live.setVisibility(View.GONE);
			butcol(15);
			tts.stop();
			SharedPreferences sharedPrefs = PreferenceManager
					.getDefaultSharedPreferences(this);
			String location = sharedPrefs.getString(
					getString(R.string.pref_location_key),
					getString(R.string.pref_location_default));
			if (location.length()<3) {
				Toast.makeText(getApplicationContext(),
						"Enter the Nearest CIty", Toast.LENGTH_SHORT).show();
			} else {

                Intent i=new Intent(MainActivity.this,Simplebrowser.class);
                i.putExtra("url","http://timesofindia.indiatimes.com/topic/"+location);
                startActivity(i);
				break;
			}
		}
	}

	private void getread(Cursor c) {
		// TODO Auto-generated method stub
		int i = 1;
		if (c.moveToFirst()) {
			do {
				read1 = read1 + i + "."
						+ (c.getString(c.getColumnIndex(DBHelper.TITLE)) + ".");
				i++;
			} while (c.moveToNext());
		} else
			read1 = "Sorry. No saved News On this topic yet .";

		startManagingCursor(c);
	}

	private void butcol(int j) {
		// TODO Auto-generated method stub
		int i;
		for (i = 1; i <= 15; i++) {
			if (i == j)
				ar[i].setTextColor(Color.parseColor("#EE7621"));
			else
				ar[i].setTextColor(Color.parseColor("#F0FAFF"));
		}

	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		String[] columns = { DBHelper.KEY_ID, DBHelper.TITLE, DBHelper.TYPE,
				DBHelper.CAPTION, DBHelper.DETAILS };
		CursorLoader loader = new CursorLoader(this, NewsProvider.CONTENT_URI,
				columns, null, null, null);

		return loader;

	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		// TODO Auto-generated method stub
		cad.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		cad.swapCursor(null);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub

		return false;
	}

}
