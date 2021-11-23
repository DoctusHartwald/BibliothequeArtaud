package org.biblio;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.biblio.adapter.ui.SpinnerAdapter;
import org.biblio.dashboard.AndroidDashboardDesignActivity;
import org.biblio.help.HelpActivity;
import org.biblio.mapping.sudoc.search.SearchItem;
import org.biblio.sqli.Compteur;
import org.biblio.sqli.dao.CompteurBdd;
import org.biblio.url.UrlBuilder;
import org.biblio.xml.SearchItemHandler;
import org.xml.sax.SAXException;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class SearchActivicty extends
		org.biblio.ui.actionbarcompat.ActionBarActivity implements
		OnItemSelectedListener, OnClickListener, OnDrawerOpenListener,
		OnDrawerCloseListener, OnEditorActionListener, ISearchActivicty {
	final String TAG = getClass().getSimpleName();

	// String[] searchByCategory = { "Tous les mots", "Auteur Titre",
	// "Auteur(s)", "Sujet", "Titre", "PPN", "Nom collectivité",
	// "Nom géographique","Nom de marque", "RCR", "Famille" };

	public static List<SearchItem> vItems;
	// CrŽation de la ArrayList qui nous permettra de remplire la listView
	ArrayList<HashMap<String, String>> listItemData = new ArrayList<HashMap<String, String>>();

	// On dŽclare la HashMap qui contiendra les informations pour un item
	HashMap<String, String> map;

	String category;

	Spinner searchBy;
	EditText searchItemUI;
	Button searchButtonUI;
	SlidingDrawer slidingDrawer;
	LinearLayout linearLayout1;
	LinearLayout linearLayout2;
	LinearLayout linearLayout3;
	SeekBar mSeekbar;
	TextView numberResults;

	String searchTerm;
	private ArrayList<String> proposals;
	ArrayAdapter<String> adapterProposals;
	int compteurSearch = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sudoc_search_layout);
		initActionBar();
		vItems = new ArrayList<SearchItem>();
		vItems.clear();

		// layout
		linearLayout1 = (LinearLayout) this.findViewById(R.id.linearLayout1);
		linearLayout2 = (LinearLayout) this.findViewById(R.id.linearLayout2);

		searchBy = (Spinner) findViewById(R.id.spinner2);
		searchItemUI = (EditText) findViewById(R.id.searchTerm);

		searchButtonUI = (Button) findViewById(R.id.button1);

		// Get reference to SlidingDrawer
		slidingDrawer = (SlidingDrawer) this.findViewById(R.id.slidingDrawer);

		searchBy = (Spinner) findViewById(R.id.spinner2);

		ArrayList<HashMap<String, Object>> list = constructSpinnerCusto();
		SpinnerAdapter adapter = new SpinnerAdapter(this, list,
				R.layout.list_layout, new String[] { "Name", "Icon" },
				new int[] { R.id.name, R.id.icon });

		searchBy.setAdapter(adapter);
		searchBy.setBackgroundColor(android.graphics.Color.GRAY);

		// Listener Set
		searchItemUI.setOnEditorActionListener(this);

		searchBy.setOnItemSelectedListener(this);
		searchButtonUI.setOnClickListener(this);
		searchItemUI.setOnClickListener(this);
		// searchItemUI.addTextChangedListener(this);

		// Listen for open event
		slidingDrawer.setOnDrawerOpenListener(this);
		slidingDrawer.setOnDrawerCloseListener(this);
	}

	// String[] searchByCategory = { "Tous les mots", "Auteur Titre",
	// "Auteur(s)", "Sujet", "Titre", "PPN", "Nom collectivité",
	// "Nom géographique","Nom de marque", "RCR", "Famille" };

	public ArrayList<HashMap<String, Object>> constructSpinnerCusto() {
		// simple spinner
		// ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_spinner_item, searchByCategory);
		// searchBy.setAdapter(adapter);

		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

		// preferences.
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		boolean categoryExpand = preferences
				.getBoolean("categorySearch", false);
		Log.i(TAG, "category Search expanded ?" + categoryExpand);

		if (categoryExpand) {

			Log.i(TAG, "==> Full list category ");
			HashMap<String, Object> map = new HashMap<String, Object>();

			// Tous les mots
			map.put("Name", searchByCategory[0]);
			map.put("Icon", R.drawable.search);
			list.add(map);

			// Auteurs
			map = new HashMap<String, Object>();
			map.put("Name", searchByCategory[1]);
			map.put("Icon", R.drawable.personneinfo);
			list.add(map);

			// Titre
			map = new HashMap<String, Object>();
			map.put("Name", searchByCategory[2]);
			map.put("Icon", R.drawable.titre);
			list.add(map);

			// Auteur Titre
			// map = new HashMap<String, Object>();
			// map.put("Name", searchByCategory[3]);
			// map.put("Icon", R.drawable.auteurtitre);
			// list.add(map);

			// Mots Sujet
			map = new HashMap<String, Object>();
			map.put("Name", searchByCategory[4]);
			map.put("Icon", R.drawable.sujet);
			list.add(map);

			// ////////////////////////////////
			// Biblio par ville
			// map = new HashMap<String, Object>();
			// map.put("Name", searchByCategory[19]);
			// map.put("Icon", R.drawable.location_pin);
			// list.add(map);
			//
			// Biblio par departement
			// map = new HashMap<String, Object>();
			// map.put("Name", searchByCategory[20]);
			// map.put("Icon", R.drawable.location);
			// list.add(map);

			// Collection
			map = new HashMap<String, Object>();
			map.put("Name", searchByCategory[5]);
			map.put("Icon", R.drawable.book2);
			list.add(map);

			// Editeur
			map = new HashMap<String, Object>();
			map.put("Name", searchByCategory[6]);
			map.put("Icon", R.drawable.ppnhelp);
			list.add(map);

			// Note de thèse
			map = new HashMap<String, Object>();
			map.put("Name", searchByCategory[7]);
			map.put("Icon", R.drawable.graduation2);
			list.add(map);

			// ISBN Livres
			map = new HashMap<String, Object>();
			map.put("Name", searchByCategory[8]);
			map.put("Icon", R.drawable.search_by);
			list.add(map);

			// Titre Abrégé (périodiques)
			map = new HashMap<String, Object>();
			map.put("Name", searchByCategory[9]);
			map.put("Icon", R.drawable.periodique);
			list.add(map);

			// Note de livres ancien
			map = new HashMap<String, Object>();
			map.put("Name", searchByCategory[10]);
			map.put("Icon", R.drawable.sujet);
			list.add(map);

			// ISBN périodiques
			map = new HashMap<String, Object>();
			map.put("Name", searchByCategory[11]);
			map.put("Icon", R.drawable.search_by);
			list.add(map);

			// Mots sujet Anglais
			map = new HashMap<String, Object>();
			map.put("Name", searchByCategory[12]);
			map.put("Icon", R.drawable.navbar_videoselected);
			list.add(map);

			// ppn
			map = new HashMap<String, Object>();
			map.put("Name", searchByCategory[13]);
			map.put("Icon", R.drawable.infoplus);
			list.add(map);

		} else {
			HashMap<String, Object> map = new HashMap<String, Object>();

			// Tous les mots
			map.put("Name", searchByCategory[0]);
			map.put("Icon", R.drawable.search);
			list.add(map);

			// Auteurs
			map = new HashMap<String, Object>();
			map.put("Name", searchByCategory[1]);
			map.put("Icon", R.drawable.personneinfo);
			list.add(map);

			// Titre
			map = new HashMap<String, Object>();
			map.put("Name", searchByCategory[2]);
			map.put("Icon", R.drawable.titre);
			list.add(map);

			// Auteur Titre
			// map = new HashMap<String, Object>();
			// map.put("Name", searchByCategory[3]);
			// map.put("Icon", R.drawable.auteurtitre);
			// list.add(map);

			// Mots Sujet
			map = new HashMap<String, Object>();
			map.put("Name", searchByCategory[4]);
			map.put("Icon", R.drawable.sujet);
			list.add(map);
		}
		return list;
	}

	/**
	 * Spinner selector custom .
	 */
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		// category = parent.getItemAtPosition(pos).toString();
		HashMap<String, String> map = (HashMap<String, String>) parent
				.getItemAtPosition(pos);

		category = map.get("Name");
		Log.i(getClass().getName(), "Category search " + category);
	}

	public void onNothingSelected(AdapterView<?> arg0) {
	}

	/**
	 * Start next screen after tab1
	 * 
	 * @author Artaud antoine
	 * @param vItems
	 */
	public void startSearchSudoc(List<SearchItem> vItems) {
		if (vItems != null)
			Log.i(getClass().getSimpleName(), "Size Items : " + vItems.size());
		Intent searchSudocActivicty = new Intent(this.getApplicationContext(),
				SearchSudocAutorityActivity.class);

		// searchSudocActivicty.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Bundle param = new Bundle();
		param.putString("searchTerm", searchTerm);
		param.putString("category", category);
		searchSudocActivicty.putExtras(param);
		startActivity(searchSudocActivicty);
	}

	public void startSearch() {
		Intent searchSudocActivicty = new Intent(this.getApplicationContext(),
				MyBookActivity.class);
		Bundle param = new Bundle();
		param.putString("searchTerm", searchTerm);
		param.putString("category", category);
		searchSudocActivicty.putExtras(param);
		startActivity(searchSudocActivicty);
	}

	/**
	 * Action launching after clicking Searhc button.
	 * 
	 * @author Artaud antoine.
	 */
	public void onClick(View view) {
		
		String appname = getApplicationContext().getResources().getString(R.string.app_name);
		
		if(!"Doctus Premium".equalsIgnoreCase(appname)){
			Log.i(TAG, "==> Doctus Lite ");
			CompteurBdd compteurBdd = new CompteurBdd(this);
			compteurBdd.open();
			Compteur compteur = compteurBdd.getCompteur();
			
			if (compteur==null){
				compteurBdd.insertCompteur(new Compteur(new Integer(1)));
				compteur = compteurBdd.getCompteur();
				Log.d(TAG, " compteur : "+compteur.getCompteur());
			}
			
			if (view == searchItemUI && compteurSearch == 0) {
				compteur.setCompteur(compteur.getCompteur()+1);
				compteurBdd.insertCompteur(compteur);
				
				searchItemUI.setText("");
				compteurSearch++;

			}
			if (view == searchButtonUI) {
				compteur.setCompteur(compteur.getCompteur()+1);
				compteurBdd.insertCompteur(compteur);
				int compteurUsed= compteur.getCompteur();
				searchTerm = searchItemUI.getText().toString();
				setSearchTerm(searchTerm);
				Log.d(TAG, "Search term : " + searchTerm);
				Log.d(TAG, "Category : " + category);
				
				if(compteurUsed>12){

					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
					String app_pro_message_title = getApplication().getResources().getString(R.string.app_pro_message_title);

					
					String app_buy_message = getApplication().getResources().getString(R.string.app_buy);
					String app_buy_yes = getApplication().getResources().getString(R.string.app_buy_yes);
					String app_buy_non = getApplication().getResources().getString(R.string.app_buy_non);
					
					alertDialogBuilder.setTitle(app_pro_message_title);

					// set dialog message
					alertDialogBuilder
						.setMessage(app_buy_message)
						.setCancelable(false)
						.setPositiveButton(app_buy_yes,new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								// if this button is clicked, close
								// current activity
								//SearchActivicty.this.finish();
								Intent market = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:\"Artaud\""));
								Log.d("org.biblio.market", " => Market Redirection (app expire)");
								startActivity(market);
							}
						  })
						.setNegativeButton(app_buy_non,new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								
								// if this button is clicked, just close
								// the dialog box and do nothing
									dialog.cancel();
								}
							});
						
							// create alert dialog
							AlertDialog alertDialog = alertDialogBuilder.create();
			 
							// show it
							alertDialog.show();
				}
				if(compteurUsed<12){
					startSearch();
				}
				
			}
			compteurBdd.close();
		}
		else{
			Log.i(TAG, " ==> Doctus Premium Version used ");
			if (view == searchItemUI && compteurSearch == 0) {
				searchItemUI.setText("");
				compteurSearch++;
			}
			if (view == searchButtonUI) {
				
				searchTerm = searchItemUI.getText().toString();
				setSearchTerm(searchTerm);
				Log.d(TAG, "Search term : " + searchTerm);
				Log.d(TAG, "Category : " + category);
				startSearch();
			}
		}
	

	}

	public void onDrawerClosed() {
		linearLayout2.setVisibility(LinearLayout.VISIBLE);
		searchButtonUI.setVisibility(Button.VISIBLE);
	}

	public void onDrawerOpened() {
		linearLayout2.setVisibility(LinearLayout.GONE);
		searchButtonUI.setVisibility(Button.GONE);
	}

	public void onTextChanged(CharSequence query, int start, int before,
			int count) {

		// TODO Auto-generated method stub
		if (query.length() > 3) {
			UrlBuilder urlBuidler = new UrlBuilder();
			HttpURLConnection con;
			URL url;
			try {
				url = new URL(urlBuidler.constructUrlSuggest(query.toString()));
				con = (HttpURLConnection) url.openConnection();
				con.setReadTimeout(10000);
				con.setConnectTimeout(15000);
				con.setRequestMethod("GET");
				con.connect();

				// SAX Parser XML response
				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser saxParser = factory.newSAXParser();

				SearchItemHandler handler = new SearchItemHandler();
				saxParser.parse(con.getInputStream(), handler);

				List<SearchItem> vItems = handler.getvItems();
				if (vItems.isEmpty()) {
					Toast.makeText(getBaseContext(),
							"No data returned .Please search again . ",
							Toast.LENGTH_LONG).show();
				} else {
					for (SearchItem searchItem : vItems) {
						proposals.add(searchItem.getAuteur());
					}
				}
			} catch (MalformedURLException e) {
				Toast.makeText(
						SearchActivicty.this,
						"Technical Problem occur - Please try again your request.  \n Please send me the log for application improvement-  \n "
								+ "MalFormedURLException : " + e.getMessage(),
						Toast.LENGTH_LONG).show();
				e.printStackTrace();
			} catch (IOException e) {
				Toast.makeText(
						SearchActivicty.this,
						"Technical Problem occur - Please try again your request.  \n Please send me the log for application improvement-  \n "
								+ " IOException : " + e.getMessage(),
						Toast.LENGTH_LONG).show();
				e.printStackTrace();
			} catch (SAXException e) {
				Toast.makeText(
						SearchActivicty.this,
						"Technical Problem occur - Please try again your request.  \n Please send me the log for application improvement-  \n "
								+ " SAXException: " + e.getMessage(),
						Toast.LENGTH_LONG).show();
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				Toast.makeText(
						SearchActivicty.this,
						"Technical Problem occur - Please try again your request.  \n Please send me the log for application improvement-  \n "
								+ " ParserConfigurationException : "
								+ e.getMessage(), Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
		}
		adapterProposals.getFilter().filter(query.toString());
	}

	public static Intent createIntent(Context context) {
		Intent i = new Intent(context, AndroidDashboardDesignActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		return i;
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

	public String getSearchTerm() {
		return searchTerm;
	}

	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}

	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (EditorInfo.IME_ACTION_SEARCH == actionId) {
			Log.d(TAG, "===>EnterKey pressed");

			searchButtonUI.performClick();
			InputMethodManager inputManager = (InputMethodManager) getApplicationContext()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(this.getCurrentFocus()
					.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			return true;
		}
		return false;
	}

}