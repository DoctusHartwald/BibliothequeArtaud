package org.biblio;

import org.biblio.help.HelpActivity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.Toast;

@TargetApi(4)
public class MyBookActivity extends TabActivity {
	final String TAG = getClass().getSimpleName();
	boolean prod = true;
	String searchTerm;
	String category;

	/** Called when the activity is first created. */
	@TargetApi(4)
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// get param from previous activity
		Bundle bundleFrom = getIntent().getExtras();
		searchTerm = bundleFrom.getString("searchTerm");
		if (searchTerm != null) {
			searchTerm = searchTerm.trim();
		}
		category = bundleFrom.getString("category");

		Bundle bundle = new Bundle();
		bundle.putString("searchTerm", searchTerm);
		bundle.putString("category", category);
		Intent intent;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			setContentView(R.layout.main);

			Resources res = getResources(); // Resource object to get Drawables
			TabHost tabHost = getTabHost(); // The activity TabHost

			tabHost.setOnTabChangedListener(new OnTabChangeListener() {
				public void onTabChanged(String tabId) {
					Log.i("Tab id", "" + tabId);
					setTitle(tabId);
				}
			});

			TabHost.TabSpec spec;
			initActionBar();
			Log.i(TAG, "ICS or superior version ");
			intent = new Intent().setClass(this.getApplicationContext(),
					SearchSudocActivityICS.class);
			intent.putExtras(bundle);

			spec = tabHost.newTabSpec("Catalogue").setIndicator("Sudoc")
					.setContent(intent);
			tabHost.addTab(spec);
			otherTabLoading(spec, bundle, tabHost);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			setContentView(R.layout.main);

			Resources res = getResources(); // Resource object to get Drawables
			TabHost tabHost = getTabHost(); // The activity TabHost

			tabHost.setOnTabChangedListener(new OnTabChangeListener() {
				public void onTabChanged(String tabId) {
					Log.i("Tab id", "" + tabId);
					setTitle(tabId);
				}
			});

			TabHost.TabSpec spec;
			initActionBar();
			Log.i(TAG, "HoneyComb  version ");
			intent = new Intent().setClass(this.getApplicationContext(),
					SearchSudocActivityICS.class);
			intent.putExtras(bundle);

			spec = tabHost.newTabSpec("Catalogue").setIndicator("Sudoc")
					.setContent(intent);
			tabHost.addTab(spec);
			otherTabLoading(spec, bundle, tabHost);
		}// v2x
		else {
			setContentView(R.layout.mybookactivity);

			Resources res = getResources(); // Resource object to get Drawables
			TabHost tabHost = getTabHost(); // The activity TabHost

			tabHost.setOnTabChangedListener(new OnTabChangeListener() {
				public void onTabChanged(String tabId) {
					Log.i("Tab id", "" + tabId);
					setTitle(tabId);
				}
			});

			TabHost.TabSpec spec;
			Log.i(TAG, "Android version 2 or bellow used  ");
			intent = new Intent().setClass(this.getApplicationContext(),
					SearchSudocActivity.class);
			intent.putExtras(bundle);

			spec = tabHost.newTabSpec("Catalogue").setIndicator("Sudoc")
					.setContent(intent);
			tabHost.addTab(spec);
			otherTabLoading(spec, bundle, tabHost);
		}
	}

	void otherTabLoading(TabHost.TabSpec spec, Bundle bundle, TabHost tabHost) {

		Intent intent;
		// TODO
		// Bibli municipale

		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		boolean categoryExpand = preferences
				.getBoolean("categoryOnglet", false);

		boolean municipale = preferences.getBoolean("municipaleView", false);

		if (municipale) {
			intent = new Intent().setClass(this.getApplicationContext(),
					BiblioMunicipaleParisActivicty.class);
			intent.putExtras(bundle);

			spec = tabHost.newTabSpec("Bibliothèque Paris")
					.setIndicator("Municipale").setContent(intent);
			tabHost.addTab(spec);
		}

		int flag = 0;
		Log.i(getClass().getSimpleName(), "Categroy " + category);
		if ("Auteur(s)".equalsIgnoreCase(category)) {
			intent = new Intent(this.getApplicationContext(),
					SearchSudocAutorityActivity.class);
			intent.putExtras(bundle);

			spec = tabHost.newTabSpec("Bibliographie").setIndicator("Auteur")
					.setContent(intent);
			tabHost.addTab(spec);
			flag = 1;
		}

		// cas ou utilisateur veut toujours afficher
		if (categoryExpand) {
			intent = new Intent(this.getApplicationContext(),
					SearchSudocAutorityActivity.class);
			intent.putExtras(bundle);
			if ("Auteur(s)".equalsIgnoreCase(category) && flag == 0) {
				spec = tabHost.newTabSpec("Bibliographie")
						.setIndicator("Bibliographie").setContent(intent);
			} else {
				spec = tabHost.newTabSpec("Autorité Sudoc")
						.setIndicator("Autorité").setContent(intent);
			}
			tabHost.addTab(spec);
		}

		tabHost.setCurrentTab(0);
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

		case R.id.menu_refresh:

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

}