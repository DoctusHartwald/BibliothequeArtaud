package org.biblio;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.client.ClientProtocolException;
import org.biblio.adapter.InteractiveArrayAdapter;
import org.biblio.help.HelpActivity;
import org.biblio.mapping.sudoc.search.SearchItem;
import org.biblio.url.HttpUrlConnectionService;
import org.biblio.url.UrlBuilder;
import org.biblio.xml.SearchItemHandler;
import org.xml.sax.SAXException;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class SearchSudocAutorityActivity extends Activity implements
		OnItemClickListener, ISearchActivicty {

	final String TAG = getClass().getSimpleName();

	ListView listItem;
	ListView listPreviousSearch;

	InteractiveArrayAdapter mSchedule;
	SimpleAdapter mScheduleSearch;

	ArrayList<SearchItem> searchItems;
	String searchTerm;
	UrlBuilder urlBuidler;

	List<SearchItem> itemsResults;
	String category;

	// String[] searchByCategory = { "Tous les mots", "Auteur Titre",
	// "Auteur(s)","Sujet", "Titre", "PPN", "Nom collectivité",
	// "Nom géographique","Nom de marque", "RCR", "Famille" };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		// Possibility to use a static field on previous Activity
		// List<SearchItem> getvItems()
		// searchItems = (ArrayList<SearchItem>) Tab1.vItems;

		super.onCreate(savedInstanceState);
		setContentView(R.layout.sudoc_search_sudoc_layout);
		itemsResults = new ArrayList<SearchItem>();
		itemsResults.clear();

		// get param from previous activity
		Bundle bundle = getIntent().getExtras();
		searchTerm = bundle.getString("searchTerm");
		category = bundle.getString("category");

		Log.d(TAG, "search Term : " + searchTerm + " / Category : " + category);

		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		boolean histoRecherche = preferences
				.getBoolean("histoRecherche", false);
		listPreviousSearch = (ListView) findViewById(R.id.listPreviousSearch);
		if (histoRecherche) {
			// Build Search history top view
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("search", "Search Term : " + searchTerm);
			map.put("category", "Category : " + category);
			map.put("img", String.valueOf(R.drawable.search));
			ArrayList<HashMap<String, String>> searchItemsTerms = new ArrayList<HashMap<String, String>>();
			searchItemsTerms.add(map);

			// layout

			// Search Layout previously checked item =====================
			mScheduleSearch = new SimpleAdapter(this.getBaseContext(),
					searchItemsTerms, R.layout.affichageitem_simple,
					new String[] { "img", "search", "category" }, new int[] {
							R.id.imgItem, R.id.titre, R.id.description });
			this.mScheduleSearch.notifyDataSetChanged();
			listPreviousSearch.setAdapter(mScheduleSearch);
		} else {
			listPreviousSearch.setVisibility(LinearLayout.INVISIBLE);
		}

		try {
			urlBuidler = new UrlBuilder();
			spinnerUrlBuilderMapping(searchTerm);
		} catch (UnsupportedEncodingException e) {
			Toast.makeText(
					SearchSudocAutorityActivity.this,
					"Technical Problem occur - Please try again your request.  \n Please send me the log for application improvement-  \n "
							+ "Unsupported Encoding : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}

		ProgressDialog progress = new ProgressDialog(this);
		new Tab1SearchTask(progress).execute();
	}

	private class Tab1SearchTask extends
			AsyncTask<Void, Integer, List<SearchItem>> implements
			OnItemClickListener {
		private ProgressDialog dialog;

		Tab1SearchTask(ProgressDialog dialog) {
			this.dialog = dialog;
		}

		public void onPreExecute() {
			dialog.setMessage("Recherche en cours. Attendez s'il vous plaît ... ");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			dialog.show();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			// Mise à jour de la ProgressBar
			dialog.setProgress(values[0]);
		}

		@Override
		protected List<SearchItem> doInBackground(Void... params) {
			List<SearchItem> items = parseRss();
			publishProgress(items.size());
			return items;
		}

		public void onPostExecute(List<SearchItem> items) {
			listItem = (ListView) findViewById(R.id.listItems);

			listItem.setOnItemClickListener(this);
			listItem.setFocusable(false);

			ArrayAdapter<SearchItem> adapter = new InteractiveArrayAdapter(
					SearchSudocAutorityActivity.this, items);

			dialog.setMessage(" Nombre de resultats trouvés: " + items.size());
			listItem.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			dialog.dismiss();
		}

		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long id) {
			int flag = 0;
			// cas redirection Bibliographie
			if ("personne".equalsIgnoreCase(itemsResults.get(position)
					.getRecordType())) {
				Bundle param = new Bundle();
				param.putString("ppn", itemsResults.get(position).getPpn());

				Intent newIntent = new Intent(SearchSudocAutorityActivity.this,
						BiblioActivity.class);
				newIntent.putExtras(param);
				startActivity(newIntent);
				flag = 1;
			}
			// Realiser les ouvrages
			// Auteur Titre
			if ("Auteur Titre".equalsIgnoreCase(itemsResults.get(position)
					.getRecordType())) {
				Bundle param = new Bundle();

				param.putString("auteur", searchTerm);
				param.putString("ppn", itemsResults.get(position).getPpn());
				Intent newIntent = new Intent(SearchSudocAutorityActivity.this,
						SearchSudocRecordsActivicty.class);
				newIntent.putExtras(param);
				startActivity(newIntent);
				Log.d("SearchSudocActivity",
						"Passing ppn" + itemsResults.get(position).getPpn());
				flag = 1;
			}
			if (flag == 0) {
				Log.d("SearchSudocActivity", "Not Person nor Title");
				Bundle param = new Bundle();
				param.putString("ppn", itemsResults.get(position).getPpn());
				Intent newIntent = new Intent(SearchSudocAutorityActivity.this,
						SearchSudocRecordsActivicty.class);
				newIntent.putExtras(param);
				startActivity(newIntent);
			}
		}
	}

	/**
	 * parse Item Search
	 * 
	 * @return
	 */
	public List<SearchItem> parseRss() {
		InputStream con;
		try {

			// get parameter define for sudoc authority
			SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(getBaseContext());
			String orderBy = preferences.getString("sortBy",
					"sort by result search");
			Log.i(TAG, "[Preferences] Order by : " + orderBy);
			if ("a".equals(orderBy)) {
				orderBy = "affcourt_z asc";
			}
			if ("abdesc".equals(orderBy)) {
				orderBy = "affcourt_z desc";
			} else {
				orderBy = "score desc";
			}
			Log.i(TAG, "Order by transcoded: " + orderBy);

			String numberResult = preferences.getString("numberResults",
					"Nombre resultat a retourner ");
			Log.i(TAG, "Nombre de resultats : " + numberResult);

			// Construction URL Autorite
			con = HttpUrlConnectionService.connectToUrl(urlBuidler
					.contructUrl2(orderBy, numberResult));

			// SAX Parser XML response
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			SearchItemHandler handler = new SearchItemHandler();
			saxParser.parse(con, handler);
			itemsResults = handler.getvItems();
			// Tab1.this.setvItems(vItems);
			if (itemsResults != null && itemsResults.size() > 0)
				Log.i("Tab1", "==> number items :  " + itemsResults.size());
		} catch (ClientProtocolException e) {
			Toast.makeText(
					SearchSudocAutorityActivity.this,
					"Technical Problem occur - Please try again your request.  \n Please send me the log for application improvement-  \n "
							+ "ClientProtocolException : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			Toast.makeText(
					SearchSudocAutorityActivity.this,
					"Technical Problem occur - Please try again your request.  \n Please send me the log for application improvement-  \n "
							+ "UnsupportedEncodingException : "
							+ e.getMessage(), Toast.LENGTH_LONG).show();

			e.printStackTrace();
		} catch (IOException e) {
			Toast.makeText(
					SearchSudocAutorityActivity.this,
					"Technical Problem occur - Please try again your request.  \n Please send me the log for application improvement-  \n "
							+ "IOException : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (URISyntaxException e) {
			Toast.makeText(
					SearchSudocAutorityActivity.this,
					"Technical Problem occur - Please try again your request.  \n Please send me the log for application improvement-  \n "
							+ "URISyntaxException : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			Toast.makeText(
					SearchSudocAutorityActivity.this,
					"Technical Problem occur - Please try again your request.  \n Please send me the log for application improvement-  \n "
							+ "ParserConfigurationException : "
							+ e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (SAXException e) {
			Toast.makeText(
					SearchSudocAutorityActivity.this,
					"Technical Problem occur - Please try again your request.  \n Please send me the log for application improvement-  \n "
							+ "SAXException : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}

		return itemsResults;
	}

	/**
	 * 
	 * <p>
	 * category set after the button is clicked on
	 * </p>
	 * 
	 * @param searchTerm
	 * @throws UnsupportedEncodingException
	 */
	public void spinnerUrlBuilderMapping(String searchTerm)
			throws UnsupportedEncodingException {
		Log.i(TAG, "Construction of searchTerm Pattern");
		// "All", "Auteur Titre", "Nom de personne","Sujet", "Titre", "PPN",
		// "Nom collectivité", "Nom géographique","Nom de marque", "RCR",
		// "Famille"
		if (null != category) {
			if (category.length() == 0) {
				Toast.makeText(getBaseContext(),
						"Please choose a category  for your research",
						Toast.LENGTH_LONG).show();
			}
		}
		// searchTerm
		// ((Boulnois olivier)or(Boulnois\ olivier*) or (Boulnois Olivier.*) or
		// (Boulnois ,Olivier*) or (Boulnois*Olivier*))
		String resultat = null;
		int flag = 0;
		resultat = searchTerm.trim();
		if (searchTerm.contains(" ")) {
			String[] searchTerms = searchTerm.split(" ");
			if (searchTerms.length == 1) {
				resultat = searchTerm;
				flag = 1;
			}
			if (searchTerms.length < 3) {
				resultat = "(" + searchTerms[0] + " " + searchTerms[1]
						+ ") or " + "(" + searchTerms[0] + "\\ "
						+ searchTerms[1] + "*) or " + "(" + searchTerms[0]
						+ " " + searchTerms[1] + ".*) or" + "("
						+ searchTerms[0] + " ," + searchTerms[1] + "*)";
			} else {
				if (flag == 0) {
					int compteur = 0;
					for (String searchTermItem : searchTerms) {
						if (compteur == 0) {
							searchTermItem = "(" + searchTermItem + "*";
						}
						if (compteur == searchTerms.length) {
							searchTermItem = searchTermItem + "*)";
							resultat = searchTermItem;
						}
						searchTermItem += searchTermItem + "*";
						compteur++;
					}
				}
			}
			Log.i(getClass().getName(), " >>>   " + resultat);
		}

		// searchTerm = new String(resultat.getBytes(), "ISO-8859-1");
		searchTerm = "(" + resultat + ")";
		Log.i(getClass().getName(), "******#######********");
		Log.i(getClass().getName(), searchTerm);

		// all
		if (category.equals(searchByCategory[0])) {
			urlBuidler.setMall(searchTerm);
		}

		// Nom de personne
		if (category.equals(searchByCategory[1])) {
			urlBuidler.setMpersnameSearch(searchTerm);
		}
		// Nom collectivite
		if (category.equals(searchByCategory[14])) {
			urlBuidler.setMcorpnameSearch(searchTerm);
		}
		// Sujet
		if (category.equals(searchByCategory[4])) {
			urlBuidler.setMsubjectheadingSearch(searchTerm);
		}
		// Nom geographique
		if (category.equals(searchByCategory[15])) {
			urlBuidler.setMgeognameSearch(searchTerm);
		}
		// Famille
		if (category.equals(searchByCategory[18])) {
			urlBuidler.setMfamnameSearch(searchTerm);
		}
		// Titre
		if (category.equals(searchByCategory[2])) {
			urlBuidler.setmNametitleSearch(searchTerm);
		}
		// Auteur Titre
		if (category.equals(searchByCategory[3])) {
			urlBuidler.setmNametitleSearch(searchTerm);
		}
		// Nom de marque
		if (category.equals(searchByCategory[16])) {
			urlBuidler.setMtrademarkSearch(searchTerm);
		}
		// PPN
		if (category.equals(searchByCategory[13])) {
			urlBuidler.setMppnSearch(searchTerm);
		}
		// RCR
		if (category.equals(searchByCategory[17])) {
			urlBuidler.setMrcr(searchTerm);
		}
	}

	@SuppressLint("NewApi")
	@TargetApi(4)
	public void initActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
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
			Intent intent = new Intent(this, SearchActivicty.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
			startActivity(intent);
			return true;

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

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

	// Fonction appelée au clic d'une des checkbox
	public void MyHandler(View v) {

		CheckBox cb = (CheckBox) v;
		// CheckBox cb = (CheckBox) v.findViewById(R.id.check);
		cb.setOnCheckedChangeListener(null);
		mSchedule.notifyDataSetChanged();
		// on récupère la position à l'aide du tag défini dans la classe
		// MyListAdapter
		int position = Integer.parseInt(v.getTag().toString());

		Log.i("SEARCH", "position detected: " + position);

		Log.i("Searhc", "checked: " + listItem.getCheckedItemPosition());
		Log.i("Searhc", "item AT :" + listItem.getItemAtPosition(position));
		Log.i("Searhc", "child count :" + listItem.getChildCount());
		Log.i("Search", "Count " + listItem.getCount());

		// On récupère l'élément sur lequel on va changer la couleur
		View o = listItem.getChildAt(position).findViewById(R.id.arraow_right);
		cb.setChecked(listItem.getChildAt(position).isSelected());
		// On change la couleur
		if (cb.isChecked()) {
			o.setBackgroundResource(R.drawable.bk_grey);

		} else {
			o.setBackgroundResource(R.drawable.btn_sky_bmp);
		}
	}

}
