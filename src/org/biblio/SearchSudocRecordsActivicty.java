package org.biblio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import mapping.recorddetail.RecordDetailPaging;

import org.biblio.adapter.SudocRecordsAdapter;
import org.biblio.help.HelpActivity;
import org.biblio.record.Record;
import org.biblio.xml.SearchRecordItemHandler;
import org.xml.sax.SAXException;

import url.HttpUrlConnectionService;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class SearchSudocRecordsActivicty extends
		org.biblio.ui.actionbarcompat.ActionBarActivity implements
		OnClickListener {
	String TAG = getClass().getSimpleName();
	ListView listRecords;
	Button prev;
	Button next;
	SimpleAdapter mSchedule;

	// Creation de la ArrayList qui nous permettra de remplire la listView
	ArrayList<HashMap<String, String>> listItemData = new ArrayList<HashMap<String, String>>();
	HashMap<String, String> map;

	final static String SUDOCRECORDS = "http://www.sudoc.abes.fr/DB=2.1/SET=1/TTL=1/REL?PPN=";
	TextView textView;
	List<Record> records;
	RecordDetailPaging recordPaging;
	String ppn;

	String nextPage;
	String prevPage;
	String cookie;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sudoc_search_sudoc_records_title_layout);

		Log.d(TAG, " --> Beginning SearchSudocRecordsActivicty activity");
		// Layout
		textView = (TextView) findViewById(R.id.textView);
		prev = (Button) this.findViewById(R.id.buttonprev);
		next = (Button) this.findViewById(R.id.buttonnext);

		// listeners
		next.setOnClickListener(this);
		prev.setOnClickListener(this);

		// get param
		Bundle bundle = getIntent().getExtras();
		ppn = bundle.getString("ppn");
		String urlLoad = null;

		urlLoad = bundle.getString("urlLoad");
		if (urlLoad == null) {
			urlLoad = SUDOCRECORDS + ppn + "&Auto=false";
			ProgressDialog progress = new ProgressDialog(this);
			new SearchSudocRecordsInitialActivictyTask(getApplicationContext(),
					progress).execute(urlLoad);
		} else {
			ProgressDialog progress = new ProgressDialog(this);
			new SearchSudocRecordsInitialActivictyTask(getApplicationContext(),
					progress).execute(urlLoad);
		}

	}

	private class SearchSudocRecordsInitialActivictyTask extends
			AsyncTask<String, Integer, List<Record>> implements
			OnItemClickListener, OnItemLongClickListener {
		ProgressDialog dialog;
		Context context;

		SearchSudocRecordsInitialActivictyTask(Context context,
				ProgressDialog dialog) {
			this.dialog = dialog;
			this.context = context;
		}

		// TODO
		public void onPreExecute() {
			dialog.setMessage(" Recherche en cours. Attendez s'il vous plaît ... ");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			dialog.show();
		}

		@Override
		protected List<Record> doInBackground(String... params) {
			return parseRssInitiale(params[0]);
		}

		public void onPostExecute(final List<Record> records) {
			super.onPostExecute(records);
			if (records != null) {
				dialog.setMessage(" Nombre de resultats trouvés: "
						+ records.size());
			}

			listRecords = (ListView) findViewById(R.id.listRecords);
			// Listener Set
			listRecords.setOnItemClickListener(this);
			ArrayAdapter<Record> adapter = new SudocRecordsAdapter(
					SearchSudocRecordsActivicty.this, records);
			listRecords.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
		}

		public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {
			Intent searchSudocRecordsDetailsPlusActivicty = new Intent(context,
					SearchSudocRecordDetailsActivicty.class);
			Bundle param = new Bundle();
			param.putString("ppn", records.get(pos).getPpn());
			searchSudocRecordsDetailsPlusActivicty.putExtras(param);
			Log.i(TAG, "===> Starting SearchSudocRecordDetailsActivicty");
			startActivity(searchSudocRecordsDetailsPlusActivicty);
		}

		// TODO Enhance
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos,
				long id) {

			final Intent MessIntent = new Intent(Intent.ACTION_SEND);
			MessIntent.setType("text/plain");
			MessIntent.putExtra(Intent.EXTRA_TEXT,
					"Going to : " + records.get(pos).getLine());
			startActivity(Intent.createChooser(MessIntent, "Partager avec..."));

			return true;
		}
	}

	/**
	 * Parse Records Details Sudoc
	 * 
	 * @param url
	 * @return
	 */
	public List<Record> parseRssInitiale(String url) {
		try {

			InputStream is = HttpUrlConnectionService.connectToUrl(url);
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			SearchRecordItemHandler handler = new SearchRecordItemHandler();

			saxParser.parse(is, handler);
			cookie = handler.getCookie();
			records = handler.getRecords();
			recordPaging = handler.getPaging();

			Log.i(TAG, "NEXT : " + recordPaging.getNextPage());
			Log.i(TAG, "PREV : " + recordPaging.getPreviousPage());
			prevPage = recordPaging.getPreviousPage();
			nextPage = recordPaging.getNextPage();
			Log.i(TAG, "Size : " + records.size());

		} catch (MalformedURLException e1) {
			Toast.makeText(
					SearchSudocRecordsActivicty.this,
					"Technical Problem occur - Please try again your request.  \n Please send me the log for application improvement-  \n "
							+ "MalformedURLException : " + e1.getMessage(),
					Toast.LENGTH_LONG).show();

			e1.printStackTrace();
		} catch (IOException e) {
			Toast.makeText(
					SearchSudocRecordsActivicty.this,
					"Technical Problem occur - Please try again your request.  \n Please send me the log for application improvement-  \n "
							+ "IOException : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			Toast.makeText(
					SearchSudocRecordsActivicty.this,
					"Technical Problem occur - Please try again your request.  \n Please send me the log for application improvement-  \n "
							+ "ParserConfigurationException : "
							+ e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (SAXException e) {
			Toast.makeText(
					SearchSudocRecordsActivicty.this,
					"Technical Problem occur - Please try again your request.  \n Please send me the log for application improvement-  \n "
							+ "SAXException : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		return records;
	}

	/**
	 * <p>
	 * next and previous records editions register in sudoc application
	 * </p>
	 * 
	 * @author Artaud
	 */
	public void onClick(View view) {
		listRecords = (ListView) findViewById(R.id.listRecords);

		if (view == next) {
			Log.d(TAG, "[SUDOC-NEXT]Next pressed ");
			String url = "http://www.sudoc.abes.fr/DB=2.1/SET=1/TTL=1/"
					+ nextPage + ";PPN=" + ppn + "&COOKIE=" + cookie
					+ "&CharSet=UTF-8";

			Log.d(TAG, "NEXT URL load : " + url);
			records.clear();

			ProgressDialog progress = new ProgressDialog(this);
			new SearchSudocRecordsInitialActivictyTask(getApplicationContext(),
					progress).execute(url);

			ArrayAdapter<Record> adapter = new SudocRecordsAdapter(this,
					records);
			listRecords.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
		if (view == prev) {

			Log.i(TAG, "PREV " + prevPage);
			Log.d(TAG, "[SUDOC-PREV] Precedent pressed ");
			String url = "http://www.sudoc.abes.fr/DB=2.1/SET=1/TTL=1/"
					+ prevPage + ";PPN=" + ppn + "&COOKIE=" + cookie
					+ "&CharSet=UTF-8";
			records.clear();
			ProgressDialog progress = new ProgressDialog(this);
			new SearchSudocRecordsInitialActivictyTask(getApplicationContext(),
					progress).execute(url);

			Log.d(TAG, "PREV URL lod : " + url);

			ArrayAdapter<Record> adapter = new SudocRecordsAdapter(this,
					records);
			listRecords.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.main, menu);

		// Calling super after populating the menu is necessary here to ensure
		// that the
		// action bar helpers have a chance to handle this event.
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, SearchSudocAutorityActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
			startActivity(intent);
			return true;

		case R.id.menu_refresh:
			Toast.makeText(this, "Fake refreshing...", Toast.LENGTH_SHORT)
					.show();
			getActionBarHelper().setRefreshActionItemState(true);
			getWindow().getDecorView().postDelayed(new Runnable() {
				public void run() {
					getActionBarHelper().setRefreshActionItemState(false);
				}
			}, 1000);
			break;

		case R.id.menu_search:
			Log.d("<SearchSudocResult>", "==> Sharing content");
			Intent searchItent = new Intent(this, SearchActivicty.class);
			startActivity(searchItent);
			return true;

		case R.id.menu_help:
			Log.d("<SearchSudocResult>", "==> Sharing content");
			Intent helpItent = new Intent(this, HelpActivity.class);
			startActivity(helpItent);
			return true;

		case R.id.menu_settings:
			Log.d("<SearchSudocResult>", "==> Sharing content");
			Intent preferenceItent = new Intent(this, AppPreferences.class);
			startActivity(preferenceItent);
			return true;

		case R.id.menu_share:
			final Intent MessIntent = new Intent(Intent.ACTION_SEND);
			MessIntent.setType("text/plain");
			MessIntent.putExtra(Intent.EXTRA_TEXT, R.string.app_name
					+ " is a very good app check it on Google Play ! ");
			startActivity(Intent.createChooser(MessIntent, "Share with ...."));
			Toast.makeText(this, "Your Message has been shared ! ",
					Toast.LENGTH_SHORT).show();
			break;
		default:
			return false;
		}
		return super.onOptionsItemSelected(item);
	}

	@SuppressLint("NewApi")
	@TargetApi(4)
	public void initActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	/***************************************************************************/
	/******************** DEBUG CODE ********************************************/
	/***************************************************************************/

	/**
	 * Affiche dans text view la reponse XML
	 * 
	 * @param response
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	public void debugCode(InputStream is) throws IllegalStateException,
			IOException {
		// textView = (TextView) findViewById(R.id.textView);
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		String line = "";
		StringBuilder sb = new StringBuilder();
		while ((line = rd.readLine()) != null) {
			// textView.append(line);
			sb.append(line);
		}
		Log.d(TAG, sb.toString());
		String userAgent = System.getProperty("http.agent");
		Log.d(TAG, userAgent);
	}

	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		Log.d(TAG, "On Item CLick");

		Intent searchSudocRecordsDetailsPlusActivicty = new Intent(
				this.getApplicationContext(),
				SearchSudocRecordDetailsActivicty.class);
		Bundle param = new Bundle();
		param.putString("ppn", records.get(pos).getPpn());
		searchSudocRecordsDetailsPlusActivicty.putExtras(param);
		startActivity(searchSudocRecordsDetailsPlusActivicty);
	}

	/***
	 * Start an other activity
	 * 
	 * @param urlLoad
	 */
	public void startSearchSudoc(String urlLoad) {
		Intent searchSudocActivicty = new Intent(this.getApplicationContext(),
				SearchSudocRecordDetailsActivicty.class);
		Bundle param = new Bundle();
		param.putString("urlLoad", urlLoad);
		param.putString("ppn", ppn);
		Log.d("SearchSudocRecordsActivity", "--> ppn search :" + ppn);
		searchSudocActivicty.putExtras(param);

		startActivity(searchSudocActivicty);

	}

}
