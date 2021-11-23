package org.biblio.help;

import org.biblio.AppPreferences;
import org.biblio.R;
import org.biblio.SearchActivicty;
import org.biblio.dashboard.AndroidDashboardDesignActivity;
import org.biblio.viewpagertabs.ViewPagerTabs;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class HelpActivity extends
		org.biblio.ui.actionbarcompat.ActionBarActivity {
	private ViewPager mPager;
	private ViewPagerTabs mTabs;
	private HelpAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sudoc_help_layout);
		initActionBar();
		mAdapter = new HelpAdapter(this);

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		mTabs = (ViewPagerTabs) findViewById(R.id.tabs);
		mTabs.setViewPager(mPager);
		/*
		 * ((Button) findViewById(R.id.style)) .setOnClickListener(new
		 * OnClickListener() {
		 * 
		 * public void onClick(View v) { mTabs.setBackgroundColor(0x00FFFFFF);
		 * mTabs.setBackgroundColorPressed(0x33333333);
		 * mTabs.setTextColor(0x44A80000); mTabs.setTextColorCenter(0xFFA80000);
		 * mTabs.setLineColorCenter(0xFFA80000); mTabs.setLineHeight(5);
		 * mTabs.setTextSize(22); mTabs.setTabPadding(5, 1, 5, 10);
		 * mTabs.setOutsideOffset(200);
		 * 
		 * // mTabs.refreshTitles();
		 * 
		 * mAdapter.changeData(); mTabs.notifyDatasetChanged();
		 * 
		 * findViewById(R.id.colorline).setBackgroundColor( 0xFFA80000);
		 * 
		 * v.setVisibility(View.GONE); } });
		 */
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
