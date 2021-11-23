package org.biblio;

import org.biblio.dashboard.AndroidDashboardDesignActivity;
import org.biblio.help.HelpActivity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class AppPreferences extends PreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initActionBar();

		addPreferencesFromResource(R.xml.preferences);
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