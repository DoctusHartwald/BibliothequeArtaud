package org.biblio.dashboard;

import org.biblio.AppPreferences;
import org.biblio.HistoryActivicty;
import org.biblio.R;
import org.biblio.SearchActivicty;
import org.biblio.help.HelpActivity;
import org.biblio.ui.actionbarcompat.ActionBarActivity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AndroidDashboardDesignActivity extends ActionBarActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dashboard_layout);

		/**
		 * Creating all buttons instances
		 * */
		// Dashboard International feed button
		// Button btn_internationnal = (Button)
		// findViewById(R.id.btn_international_feed);

		// Dashboard Sudoc Button
		Button btn_sudoc = (Button) findViewById(R.id.btn_sudoc);

		// Dashboard History Button
		Button btn_history = (Button) findViewById(R.id.btn_history);

		// Dashboard Settings button
		Button btn_settings = (Button) findViewById(R.id.btn_settings);

		// Dashboard HELP button
		Button btn_help = (Button) findViewById(R.id.btn_help);

		// Dashboard Photos button
		// Button btn_photos = (Button) findViewById(R.id.btn_photos);

		/**
		 * Handling all button click events
		 * */

		// Listening to International Feed button click
		/*
		 * btn_internationnal.setOnClickListener(new View.OnClickListener() {
		 * 
		 * public void onClick(View view) { // Launching International Screen
		 * Intent i = new Intent(getApplicationContext(), Tab2.class);
		 * startActivity(i); } });
		 */

		// Listening sudoc button click
		btn_sudoc.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				// Launching Sudoc Screen
				Intent i = new Intent(getApplicationContext(),
						SearchActivicty.class);
				startActivity(i);
			}
		});

		// Listening History button click
		String applicationName = getApplicationContext().getResources()
				.getString(R.string.app_name);

		String messagePro = getApplicationContext().getResources().getString(
				R.string.app_pro_message);
		String messageProTitle = getApplicationContext().getResources()
				.getString(R.string.app_pro_message_title);

		btn_history.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						HistoryActivicty.class);
				startActivity(i);
			}
		});

		if (!"Doctus Premium".equalsIgnoreCase(applicationName)) {
			Log.i(getClass().getSimpleName(), "Want Doctus premium ??");


			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			String app_pro_message_title = getApplication().getResources().getString(R.string.app_pro_message_title);


			String app_buy_message = getApplication().getResources().getString(R.string.app_buy_more);
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
					Intent market =  new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:\"Artaud\""));
					Log.d("org.biblio.market", "Redirect to Google PLAY");
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

		// Listening settings button click
		btn_settings.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						AppPreferences.class);
				startActivity(i);
			}
		});

		// Listening Help button click
		btn_help.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				// Launching Help Screen
				Intent i = new Intent(getApplicationContext(),
						HelpActivity.class);
				startActivity(i);
			}
		});

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