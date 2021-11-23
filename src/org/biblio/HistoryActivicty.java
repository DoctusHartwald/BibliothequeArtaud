package org.biblio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.biblio.dashboard.AndroidDashboardDesignActivity;
import org.biblio.help.HelpActivity;
import org.biblio.sqli.Livre;
import org.biblio.sqli.dao.LivresBDD;
import org.biblio.sqli.db.MaBaseSQLite;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class HistoryActivicty extends
org.biblio.ui.actionbarcompat.ActionBarActivity implements
OnItemClickListener, OnItemLongClickListener {
	final String TAG = getClass().getSimpleName();
	ListView listHistory;
	SimpleAdapter mSchedule;

	// private String[] allColumns = { MaBaseSQLite.COL_ID,
	// MaBaseSQLite.COL_PPN,
	// MaBaseSQLite.COL_TITRE, MaBaseSQLite.COL_AUTEUR,
	// MaBaseSQLite.COL_DATE, MaBaseSQLite.COL_DESCRIPTION };
	private String[] allColumns = { MaBaseSQLite.COL_ID, MaBaseSQLite.COL_PPN,
			MaBaseSQLite.COL_TITRE, MaBaseSQLite.COL_AUTEUR,
			MaBaseSQLite.COL_DESCRIPTION, MaBaseSQLite.COL_DATE };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sudoc_history_layout);

		initActionBar();
		// Layout
		listHistory = (ListView) this.findViewById(R.id.listHistory);

		// listeners
		listHistory.setOnItemClickListener(this);
		listHistory.setOnItemLongClickListener(this);

		// Création d'une instance de ma classe LivresBDD
		LivresBDD livreBdd = new LivresBDD(getApplicationContext());
		livreBdd.open();

		// Recupere tout les livres
		List<Livre> livres = livreBdd.getAllLivres();

		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		String[] from = new String[] { "imgItemHistory", "ppnHistory",
				"auteurHistory", "titreHistory", "descriptionHistory",
		"dateRightHistory" };
		int[] to = new int[] { R.id.imgItemHistory, R.id.ppnHistory,
				R.id.auteurHistory, R.id.titreHistory, R.id.descriptionHistory,
				R.id.date_rightHistory };
		if (livres != null && livres.size() > 0) {
			for (Livre livre : livres) {
				HashMap<String, String> map = new HashMap<String, String>();
				Log.i(TAG, livre.toString());
				map.put("ppnHistory", livre.getPpn());
				map.put("auteurHistory", livre.getAuteur());

				map.put("titreHistory", livre.getTitre());
				map.put("descriptionHistory", livre.getDescription());
				map.put("dateRightHistory", livre.getDate());

				
				
				Log.d(TAG, "Auteur "+livre.getAuteur());
				Log.d(TAG, "Titre History  "+livre.getTitre());
				
				Log.d(TAG, "TDescription   "+livre.getDescription());
				
				int flag =0;
				// Traitement Custo Image
				if ("Livre".equalsIgnoreCase("Livre")) {
					map.put("imgItemHistory", String.valueOf(R.drawable.book2));
					flag=1;
				}
				if ("Article".equalsIgnoreCase(livre.getAuteur())) {
					map.put("imgItemHistory", String.valueOf(R.drawable.pen));
					flag=1; 
				}
				if ((livre.getAuteur().contains("Thèse"))) {
					map.put("imgItemHistory",
							String.valueOf(R.drawable.graduation));
					flag=1; 
				}
				if ("Electronique".equalsIgnoreCase(livre.getAuteur())) {
					map.put("imgItemHistory",
							String.valueOf(R.drawable.electronic_network));
					flag=1;
				}
				if ("Periodique".equalsIgnoreCase(livre.getAuteur())) {
					map.put("imgItemHistory",
							String.valueOf(R.drawable.periodique));
					flag=1; 
				}
				if ("Partitions".equalsIgnoreCase(livre.getAuteur())) {
					map.put("imgItemHistory",
							String.valueOf(R.drawable.musique_bleu));
					flag=1; 
				}
				if ("Personne".equalsIgnoreCase(livre.getTitre())) {
					map.put("imgItemHistory",
							String.valueOf(R.drawable.personneinfo));
					flag=1; 
				} if(flag==0) {
					map.put("imgItemHistory",
							String.valueOf(R.drawable.book_others));
				}
				list.add(map);
			}
			SimpleAdapter adapter = new SimpleAdapter(this, list,
					R.layout.search_sudoc_history, from, to);
			adapter.notifyDataSetChanged();
			// Bind to our new adapter.
			listHistory.setAdapter(adapter);
			listHistory.setOnItemClickListener(this);
		}
		livreBdd.close();
	}

	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onItemClick(AdapterView<?>parent, View arg1, int pos,
			long id) {
		HashMap<String, String> map = (HashMap<String, String>) parent
				.getItemAtPosition(pos);
		String ppn = map.get("ppnHistory");
		Log.d(TAG, "PPN "+ppn);
		
		Bundle param = new Bundle();
		param.putString("ppn", ppn);
		
		String typeRecherche = map.get("auteurHistory");
		if("Personne".equalsIgnoreCase(typeRecherche)){
			Log.i(TAG, "Calling iBilio");
			Intent newIntent = new Intent(this.getApplicationContext(),
					BiblioActivity.class);
			newIntent.putExtras(param);
			startActivity(newIntent);

		}
		else{
			Log.i(TAG, "Calling where Service ");
			Intent newIntent = new Intent(this.getApplicationContext(),
					WhereActivity.class);
			newIntent.putExtras(param);
			startActivity(newIntent);
		}
		
	
		
		///param.putString("ppn", histories.get(pos).);
		
		
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

	@SuppressLint("NewApi")
	@TargetApi(4)
	public void initActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

}