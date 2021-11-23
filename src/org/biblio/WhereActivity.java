package org.biblio;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.biblio.dashboard.AndroidDashboardDesignActivity;
import org.biblio.help.HelpActivity;
import org.biblio.mapping.sudoc.record.Library;
import org.biblio.url.HttpUrlConnectionService;
import org.biblio.xml.SearchWhereItemHandler;
import org.xml.sax.SAXException;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/**
 * 
 * webservice where Sudoc http://www.sudoc.fr/services/where/107167980
 * 
 * @author Artaud Antoine
 * 
 */
public class WhereActivity extends
		org.biblio.ui.actionbarcompat.ActionBarActivity {
	String URL_WHERE = "http://www.sudoc.fr/services/where/";
	ListView listWheres;
	List<Library> libraries;
	// Creation de la ArrayList qui nous permettra de remplire la listView
	HashMap<String, String> map;
	final String TAG = getClass().getSimpleName();
	String ppn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sudoc_where_layout);
		// initActionBar();

		Log.d("<WHERE>", "Beginning Where activity");

		// get param
		Bundle bundle = getIntent().getExtras();
		ppn = bundle.getString("ppn");

		// HttpURLConnection con = HttpUrlConnectionService.connect(urlWhere +
		// ppn+ ".xml");
		String urlLocation = URL_WHERE + ppn + ".xml";
		ProgressDialog progress = new ProgressDialog(this);
		Log.i(TAG, " Starting multhreading SearchLibraryActivictyTask ");
		new SearchLibraryActivictyTask(getApplicationContext(), progress)
				.execute(urlLocation);

	}

	private class SearchLibraryActivictyTask extends
			AsyncTask<String, Integer, List<Library>> implements
			OnItemClickListener {

		ProgressDialog dialog;
		Context context;

		SearchLibraryActivictyTask(Context context, ProgressDialog dialog) {
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
		protected List<Library> doInBackground(String... params) {
			return parseLibraries(params[0]);
		}

		public void onPostExecute(final List<Library> libaries) {
			super.onPostExecute(libaries);
			if (libaries != null) {
				dialog.setMessage(" Nombre de resultats trouvés: "
						+ libaries.size());
			}
			listWheres = (ListView) findViewById(R.id.listwhere);

			// listeners
			listWheres.setOnItemClickListener(this);
			ArrayList<HashMap<String, String>> listItemData = new ArrayList<HashMap<String, String>>();

			// adding list to map for the adapter
			for (Library library : libraries) {
				map = new HashMap<String, String>();
				map.put("img", String.valueOf(R.drawable.home));
				map.put("rcr", "Numéro Rcr : " + library.getRcr());
				map.put("shortname", library.getShortname());
				listItemData.add(map);
			}
			// CrŽation d'un SimpleAdapter qui se chargera de mettre les items
			// prŽsent dans notre list (listItem) dans la vue affichageitem
			SimpleAdapter mSchedule = new SimpleAdapter(WhereActivity.this,
					listItemData, R.layout.affichageitem_simple_where,
					new String[] { "img", "rcr", "shortname" }, new int[] {
							R.id.imgItem, R.id.titre, R.id.description });

			listWheres.setAdapter(mSchedule);
			mSchedule.notifyDataSetChanged();

			if (dialog.isShowing()) {
				dialog.dismiss();
			}
		}

		public void onItemClick(AdapterView<?> libaries, View arg1, int pos,
				long id) {

			Bundle param = new Bundle();
			param.putString("rcr", libraries.get(pos).getRcr());
			param.putString("shortname", libraries.get(pos).getShortname());
			param.putString("ppn", ppn);
			Intent newIntent = new Intent(WhereActivity.this,
					WhereActivityDetails.class);
			newIntent.putExtras(param);
			Log.d(TAG, "Starting RecordsDetails ");
			startActivity(newIntent);

			// /TODO Preference to be added
			/*
			 * String location = libraries.get(pos).getShortname(); Uri uri =
			 * Uri.parse("geo:0,0?q=" + location); Intent searchAddress = new
			 * Intent(Intent.ACTION_VIEW, uri); startActivity(searchAddress);
			 */
		}
	}

	/**
	 * Parse list library
	 * 
	 * @param url
	 * @return
	 */
	public List<Library> parseLibraries(String url) {
		libraries = null;
		try {
			HttpURLConnection con = HttpUrlConnectionService.connect(url);
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			SearchWhereItemHandler handler = new SearchWhereItemHandler();
			saxParser.parse(con.getInputStream(), handler);

			libraries = handler.getmLibraries();
			if (libraries.isEmpty()) {
				Toast.makeText(getBaseContext(),
						"No library information available", Toast.LENGTH_LONG)
						.show();
			}
		} catch (ParserConfigurationException e) {
			Toast.makeText(
					WhereActivity.this,
					"Technical Problem occur - Please try again your request.  \n Please send me the log for application improvement-  \n "
							+ "ParserConfigurationException : "
							+ e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (SAXException e) {
			Toast.makeText(
					WhereActivity.this,
					"Technical Problem occur - Please try again your request.  \n Please send me the log for application improvement-  \n "
							+ "SAXException : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (IOException e) {
			Toast.makeText(
					WhereActivity.this,
					"Technical Problem occur - Please try again your request.  \n Please send me the log for application improvement-  \n "
							+ "IOException : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		return libraries;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// TODO
		case android.R.id.home:
			Intent intent = new Intent(this,
					AndroidDashboardDesignActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.main, menu);

		// Calling super after populating the menu is necessary here to ensure
		// that the
		// action bar helpers have a chance to handle this event.
		return super.onCreateOptionsMenu(menu);
	}

	// TODO
	@SuppressLint("NewApi")
	@TargetApi(4)
	public void initActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

}
