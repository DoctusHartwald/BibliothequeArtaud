package org.biblio;

import java.io.IOException;
import java.io.InputStream;
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
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(4)
public class SearchSudocActivity extends Activity implements OnClickListener,
		ISearchActivicty {
	String TAG = getClass().getSimpleName();
	ListView listRecords;
	ListView listPreviousSearch;
	SimpleAdapter mScheduleSearch;
	TextView paginationNumber;
	TextView textViewHit;

	Button prev;
	TextView txtPrev;
	Button next;
	TextView txtNext;
	SimpleAdapter mSchedule;

	// Creation de la ArrayList qui nous permettra de remplire la listView
	ArrayList<HashMap<String, String>> listItemData = new ArrayList<HashMap<String, String>>();
	HashMap<String, String> map;

	String SUDOCRECORDS;
	TextView textView;

	List<Record> records;
	RecordDetailPaging recordPaging;
	String searchTerm;
	String category;

	String nextPage;
	String prevPage;
	String cookie;
	String ppnHeader;
	String hitNumberResult;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.sudoc_search_sudoc_records_title_layout);
		records = new ArrayList<Record>();

		Log.d(TAG, " --> Beginning SearchSudocRecordsActivicty activity");
		// Layout navigation
		textView = (TextView) findViewById(R.id.textView);
		prev = (Button) this.findViewById(R.id.buttonprev);
		next = (Button) this.findViewById(R.id.buttonnext);

		txtNext = (TextView) findViewById(R.id.textViewSuivant);
		txtPrev = (TextView) findViewById(R.id.textViewPrecedent);
		// listeners navigation
		txtNext.setOnClickListener(this);
		txtPrev.setOnClickListener(this);
		next.setOnClickListener(this);
		prev.setOnClickListener(this);

		// get param
		Bundle bundle = getIntent().getExtras();
		searchTerm = bundle.getString("searchTerm");
		category = bundle.getString("category");

		// Historique Recherche
		// Build Search history top view
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		boolean histoRecherche = preferences
				.getBoolean("histoRecherche", false);
		listPreviousSearch = (ListView) findViewById(R.id.listPreviousSearchSudoc);

		if (histoRecherche) {
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

		String urlLoad = null;
		SUDOCRECORDS = customUrlSudoc(category);
		// Algo searchTerm
		if (null != searchTerm) {
			String[] searchTerms = searchTerm.split(" ");
			String searchConcat = null;
			if (searchTerms.length > 1) {
				Log.i(TAG, " plusieurs term ont été tape par utilisateur ");

				int compteur = 0;
				for (String searchItem : searchTerms) {
					if (compteur == 0) {
						searchConcat = searchItem;
					} else {
						searchConcat = searchConcat + "+" + searchItem;
					}
					compteur++;
				}
				searchTerm = searchConcat;
				Log.i(TAG, "search Term Construction : " + searchTerm);
			}
		}

		urlLoad = bundle.getString("urlLoad");
		if (urlLoad == null) {
			urlLoad = SUDOCRECORDS + searchTerm + "&Auto=false";
			ProgressDialog progress = new ProgressDialog(this);
			new SearchSudocRecordsInitialActivictyTask(getApplicationContext(),
					progress).execute(urlLoad);
		} else {
			ProgressDialog progress = new ProgressDialog(this);
			new SearchSudocRecordsInitialActivictyTask(getApplicationContext(),
					progress).execute(urlLoad);
		}
	}

	private String customUrlSudoc(String category) {

		// Tous les mots
		if (category.equals(searchByCategory[0])) {
			return "http://www.sudoc.abes.fr/DB=2.1/SET=1/TTL=1/CMD?ACT=SRCHA&IKT=1016&SRT=RLV&TRM=";
		}

		// Auteurs
		if (category.equals(searchByCategory[1])) {
			return "http://www.sudoc.abes.fr/DB=2.1/CMD?ACT=SRCHA&IKT=1004&SRT=RLV&TRM=";
		}

		// Titre
		if (category.equals(searchByCategory[2])) {
			return "http://www.sudoc.abes.fr/DB=2.1/CMD?ACT=SRCHA&IKT=4&SRT=RLV&TRM=";
		}

		// Auteur Titre
		if (category.equals(searchByCategory[3])) {
			return "http://www.sudoc.abes.fr/DB=2.1/CMD?ACT=SRCHA&IKT=1004&SRT=RLV&TRM=";
		}

		// Sujet
		if (category.equals(searchByCategory[4])) {
			return "http://www.sudoc.abes.fr/DB=2.1/CMD?ACT=SRCHA&IKT=21&SRT=RLV&TRM=";
		}

		// Collection
		if (category.equals(searchByCategory[5])) {
			return "http://www.sudoc.abes.fr/DB=2.1/CMD?ACT=SRCHA&IKT=8109&SRT=RLV&TRM=";
		}

		// Editeur
		if (category.equals(searchByCategory[6])) {
			return "http://www.sudoc.abes.fr/DB=2.1/CMD?ACT=SRCHA&IKT=1018&SRT=RLV&TRM=";
		}

		// Note de these
		if (category.equals(searchByCategory[6])) {
			return "http://www.sudoc.abes.fr/DB=2.1/CMD?ACT=SRCHA&IKT=63&SRT=RLV&TRM=";
		}

		// ISBN Livres
		if (category.equals(searchByCategory[7])) {
			return "http://www.sudoc.abes.fr/DB=2.1/CMD?ACT=SRCHA&IKT=7&SRT=RLV&TRM=";
		}

		// Titre Abrégé (périodiques)
		if (category.equals(searchByCategory[8])) {
			return "http://www.sudoc.abes.fr/DB=2.1/CMD?ACT=SRCHA&IKT=8062&SRT=RLV&TRM=";
		}

		// Note de livre ancien
		if (category.equals(searchByCategory[9])) {
			return "http://www.sudoc.abes.fr/DB=2.1/CMD?ACT=SRCHA&IKT=8865&SRT=RLV&TRM=";
		}

		// ISBN Periodiques
		if (category.equals(searchByCategory[10])) {
			return "http://www.sudoc.abes.fr/DB=2.1/CMD?ACT=SRCHA&IKT=8&SRT=RLV&TRM=";
		}

		// Mot sujet Anglais
		if (category.equals(searchByCategory[11])) {
			return "http://www.sudoc.abes.fr/DB=2.1/CMD?ACT=SRCHA&IKT=8141&SRT=RLV&TRM=";
		}

		return category;
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
			records.clear();
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

			// Cas aucune donnee
			if (records != null && records.size() == 0) {
				AlertDialog alert = new AlertDialog.Builder(
						getApplicationContext()).create();
				alert.setTitle("Information");
				alert.setMessage("No data was found in Sudoc Catalog ");
			}

			if (records == null) {
				AlertDialog alert = new AlertDialog.Builder(
						getApplicationContext()).create();
				alert.setTitle("Information");
				alert.setMessage("No data was found in Sudoc Catalog. % SYS ");
			}

			// Autre cas
			if (records != null) {
				dialog.setMessage(" Nombre de resultats trouvés: "
						+ records.size());
			}

			// cas particulier un seul resultat.
			if (records != null && records.size() == 1) {
				Log.d(TAG, "Only one record found  /PPN : " + ppnHeader);
				Bundle param = new Bundle();
				param.putString("ppn", ppnHeader);
				Intent newIntent = new Intent(SearchSudocActivity.this,
						SearchSudocRecordDetailsActivicty.class);
				newIntent.putExtras(param);
				Log.d(TAG, "Starting RecordsDetails ");
				startActivity(newIntent);
			}

			else {
				if (records != null) {
					Log.i(TAG, " ** hit : " + hitNumberResult);
					if (hitNumberResult != null && !"".equals(hitNumberResult)) {
						textViewHit = (TextView) findViewById(R.id.textNumberRecord);
						textViewHit.setText(hitNumberResult);
					}

					paginationNumber = (TextView) findViewById(R.id.textNumberRecordPage);
					if (nextPage != null) {
						Log.d(TAG, "Number Page " + nextPage);
						String numPageTempo = nextPage.substring(9,
								nextPage.length());
						Log.d(TAG, "Number Page parse : " + numPageTempo);

						if (!nextPage.startsWith("CBRWS?IKT=")) {
							if (nextPage.startsWith("NXT?")) {
								Integer num = new Integer(numPageTempo);
								Integer numR = num / 10 % 10;
								Log.d(TAG, "number " + numR);
								paginationNumber
										.setText("P/" + numR.toString());
							} else {
								paginationNumber.setText("");
							}
						}

					}

					listRecords = (ListView) findViewById(R.id.listRecords);
					// Listener Set
					listRecords.setOnItemClickListener(this);
					ArrayAdapter<Record> adapter = new SudocRecordsAdapter(
							SearchSudocActivity.this, records);
					listRecords.setAdapter(adapter);
					adapter.notifyDataSetChanged();
					if (dialog.isShowing()) {
						dialog.dismiss();
					}
				}

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
			ppnHeader = handler.getPpnHeaderContext();
			records = handler.getRecords();
			recordPaging = handler.getPaging();
			hitNumberResult = handler.getHitsResult();

			Log.i(TAG, "NEXT : " + recordPaging.getNextPage());
			Log.i(TAG, "PREV : " + recordPaging.getPreviousPage());
			prevPage = recordPaging.getPreviousPage();
			nextPage = recordPaging.getNextPage();
			Log.i(TAG, "Size : " + records.size());

		} catch (MalformedURLException e1) {
			Toast.makeText(
					SearchSudocActivity.this,
					"Technical Problem occur - Please try again your request.  \n Please send me the log for application improvement-  \n "
							+ "MalFormedURLException : " + e1.getMessage(),
					Toast.LENGTH_LONG).show();
			e1.printStackTrace();
		} catch (IOException e) {
			Toast.makeText(
					SearchSudocActivity.this,
					"Technical Problem occur - Please try again your request.  \n Please send me the log for application improvement-  \n "
							+ "IOException : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			Toast.makeText(
					SearchSudocActivity.this,
					"Technical Problem occur - Please try again your request.  \n Please send me the log for application improvement-  \n "
							+ "ParserConfigurationException : "
							+ e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (SAXException e) {
			Toast.makeText(
					SearchSudocActivity.this,
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

		if (view == next || txtNext == view) {
			Log.d(TAG, "[SUDOC-NEXT]Next pressed ");
			// String url = "http://www.sudoc.abes.fr/DB=2.1/SET=1/TTL=1/"+
			// nextPage + ";TRM=" + searchTerm + "&COOKIE="+
			// cookie+"&CharSet=UTF-8";
			// String url = SUDOCRECORDS+searchTerm+ nextPage + "&COOKIE="+
			// cookie+"&CharSet=UTF-8";
			String url = SUDOCRECORDS + searchTerm + "&" + nextPage
					+ "&COOKIE=" + cookie + "&CharSet=UTF-8";

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

		if (view == prev || txtPrev == view) {
			Log.i(TAG, "PREV " + prevPage);
			Log.d(TAG, "[SUDOC-PREV] Precedent pressed ");
			// String url = "http://www.sudoc.abes.fr/DB=2.1/SET=1/TTL=1/"+
			// prevPage + ";TRM=" + searchTerm + "&COOKIE="+
			// cookie+"&CharSet=UTF-8";
			String url = SUDOCRECORDS + searchTerm + "&" + prevPage
					+ "&COOKIE=" + cookie + "&CharSet=UTF-8";

			records.clear();
			ProgressDialog progress = new ProgressDialog(this);
			new SearchSudocRecordsInitialActivictyTask(getApplicationContext(),
					progress).execute(url);

			Log.d(TAG, "PREV URL load : " + url);

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

}
