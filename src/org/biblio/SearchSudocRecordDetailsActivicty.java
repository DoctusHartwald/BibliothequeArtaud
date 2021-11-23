package org.biblio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import mapping.recorddetail.RecordDetail;

import org.apache.http.client.ClientProtocolException;
import org.biblio.dashboard.AndroidDashboardDesignActivity;
import org.biblio.help.HelpActivity;
import org.biblio.url.HttpUrlConnectionService;
import org.biblio.xml.RecordItemDetailsPlusHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.xml.sax.SAXException;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class SearchSudocRecordDetailsActivicty extends
		org.biblio.ui.actionbarcompat.ActionBarActivity implements
		OnClickListener {

	final String TAG = "SearchSudocRecordDetailsActivicty";

	// SUDOC - details de edition recherches
	final String urlSudocRecordDetail = "http://www.sudoc.abes.fr/DB=2.1/SRCH?IKT=12&TRM=";

	// GOOGLE BOOK--------------------
	// url Google Isbn
	final String googleIsbnUrl = "https://www.googleapis.com/books/v1/volumes?q=isbn:";
	// url Google Book Preview
	final String googleBookPreviewUrl = "http://bks3.books.google.fr/books?id=";
	final String googleBookPreviewCover = "&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api";

	String volumeId;// volume Google Id
	final String googleBookWebUrl = "http://books.google.fr/books/reader?id=";
	final String googleBookWebUrlOption = "&ie=ISO-8859-1&printsec=frontcover&output=reader&source=gbs_api";

	// WorldCat
	final String worldCatUrl = "http://www.worldcat.org/search?q=no%3A";
	// ppn
	String ppn;
	String isbnNumberGoogle;
	String worldCatId;
	ListView listRecordsDetails;
	TextView textview;
	ArrayList<HashMap<String, String>> listItemData = new ArrayList<HashMap<String, String>>();

	Button btnGoogle;
	Button btnLocation;
	Button btnWorldCat;

	TextView txtBiblio;
	TextView txtGoogle;
	TextView txtWorldCat;

	public Object fetch(String address) throws MalformedURLException,
			IOException {
		URL url = new URL(address);
		Object content = url.getContent();
		return content;
	}

	public Bitmap loadBitmap(String url) {
		Bitmap bitmap = null;
		try {
			InputStream is = (InputStream) this.fetch(url);
			bitmap = BitmapFactory.decodeStream(is);
		} catch (IOException e) {
			Toast.makeText(
					SearchSudocRecordDetailsActivicty.this,
					"Technical Problem occur - Please try again your request.  \n Please send me the log for application improvement-  \n "
							+ "IOException : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
			Log.e(TAG, "Could not load Bitmap from: " + url);
		}
		return bitmap;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sudoc_records_details_layout);

		// initActionBar();
		Log.d(TAG, "Starting search Sudo Details Activity ");

		// get param
		Bundle bundle = getIntent().getExtras();
		ppn = bundle.getString("ppn");
		Log.d(TAG, "ppn : " + ppn);
		// layout

		btnGoogle = (Button) this.findViewById(R.id.buttonGoogle);
		btnLocation = (Button) this.findViewById(R.id.buttonLocation);
		btnWorldCat = (Button) this.findViewById(R.id.buttonWorldCat);
		txtGoogle = (TextView) this.findViewById(R.id.textViewGoogle);
		txtBiblio = (TextView) this.findViewById(R.id.textViewLocation);
		txtWorldCat = (TextView) this.findViewById(R.id.textViewWorldCat);

		txtGoogle.setOnClickListener(this);
		txtBiblio.setOnClickListener(this);
		txtWorldCat.setOnClickListener(this);
		btnGoogle.setOnClickListener(this);
		btnLocation.setOnClickListener(this);
		btnWorldCat.setOnClickListener(this);

		textview = (TextView) this.findViewById(R.id.textViewRecordDetails);

		Log.i(TAG,
				"Starting multiThreading SearchSudocRecordsDetailsActivictyTask");
		ProgressDialog progress = new ProgressDialog(this);
		new SearchSudocRecordsDetailsActivictyTask(getApplicationContext(),
				progress).execute(ppn);

	}

	private class SearchSudocRecordsDetailsActivictyTask extends
			AsyncTask<String, Integer, List<RecordDetail>> implements
			OnItemClickListener, OnItemLongClickListener {
		ProgressDialog dialog;
		Context context;

		SearchSudocRecordsDetailsActivictyTask(Context context,
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
		protected List<RecordDetail> doInBackground(String... params) {
			return parseSudocRecordDetail(params[0]);
		}

		public void onPostExecute(final List<RecordDetail> recordsDetails) {
			super.onPostExecute(recordsDetails);
			if (recordsDetails != null) {
				dialog.setMessage(" Nombre de resultats trouvés: "
						+ recordsDetails.size());
			}

			listRecordsDetails = (ListView) findViewById(R.id.listRecordsDetails); // Listener
																					// Set
			// listRecords.setOnItemClickListener(this);
			HashMap<String, String> map;
			// fill data records
			for (RecordDetail recordDetailPlus : recordsDetails) {
				map = new HashMap<String, String>();
				map.put("img", String.valueOf(R.drawable.double_right_arrow));
				map.put("label", recordDetailPlus.getLabel().getText());

				String resultData = "";
				for (String data : recordDetailPlus.getData().getLine()) {
					resultData += data;
				}

				Log.d(TAG, recordDetailPlus.getLabel().getText());
				Log.d(TAG, "Resultat Data :" + resultData);

				if ("ISBN".equalsIgnoreCase(recordDetailPlus.getLabel()
						.getText())) {
					// iSBNTreatement(resultData);
					Log.i(TAG,
							"Starting multiThreading SearchSudocRecordDetailsGoogleActivictyTask");

					ProgressDialog progress = new ProgressDialog(
							SearchSudocRecordDetailsActivicty.this);
					new SearchSudocRecordDetailsGoogleActivictyTask(
							getApplicationContext(), progress)
							.execute(resultData);
				}
				if ("WorldCat".equalsIgnoreCase(recordDetailPlus.getLabel()
						.getText().trim())) {
					worldCatId = resultData;
				}
				map.put("data", resultData);
				listItemData.add(map);
			}
			// CrŽation d'un SimpleAdapter qui se chargera de mettre les items
			// prŽsent dans notre list (listItem) dans la vue affichageitem
			SimpleAdapter mSchedule = new SimpleAdapter(
					SearchSudocRecordDetailsActivicty.this, listItemData,
					R.layout.affichage_sudoc_records_details, new String[] {
							"img", "label", "data" }, new int[] { R.id.icon,
							R.id.firstLine, R.id.secondLine });

			listRecordsDetails.setAdapter(mSchedule);
			mSchedule.notifyDataSetChanged();

			if (dialog.isShowing()) {
				dialog.dismiss();
			}
		}

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub

		}

		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			// TODO Auto-generated method stub
			return false;
		}

	}

	public List<RecordDetail> parseSudocRecordDetail(String ppn) {
		List<RecordDetail> recordsDetails = null;
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			RecordItemDetailsPlusHandler handler = new RecordItemDetailsPlusHandler();
			InputStream is = HttpUrlConnectionService
					.connectToUrl(urlSudocRecordDetail + ppn);
			saxParser.parse(is, handler);
			recordsDetails = handler.getRecorddetails();
		} catch (ClientProtocolException e) {
			Toast.makeText(
					SearchSudocRecordDetailsActivicty.this,
					"Technical Problem occur - Please try again your request.  \n Please send me the log for application improvement-  \n "
							+ "ClientProtocolException : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (IOException e) {
			Toast.makeText(
					SearchSudocRecordDetailsActivicty.this,
					"Technical Problem occur - Please try again your request.  \n Please send me the log for application improvement-  \n "
							+ "IOException : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			Toast.makeText(
					SearchSudocRecordDetailsActivicty.this,
					"Technical Problem occur - Please try again your request.  \n Please send me the log for application improvement-  \n "
							+ "ParserConfigurationException : "
							+ e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (SAXException e) {
			Toast.makeText(
					SearchSudocRecordDetailsActivicty.this,
					"Technical Problem occur - Please try again your request.  \n Please send me the log for application improvement-  \n "
							+ "SAXException : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		return recordsDetails;
	}

	/**
	 * Loading bitmap Cover from google book
	 * 
	 * @author artaud
	 * 
	 */
	private class SearchSudocRecordDetailsGoogleActivictyTask extends
			AsyncTask<String, Integer, Bitmap> {
		ProgressDialog dialog;
		Context context;

		SearchSudocRecordDetailsGoogleActivictyTask(Context context,
				ProgressDialog dialog) {
			this.dialog = dialog;
			this.context = context;
		}

		// TODO
		public void onPreExecute() {
			dialog.setMessage(" Recherche de la pochette en cours. \n Attendez s'il vous plaît ... ");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			dialog.show();
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			return iSBNTreatement(params[0]);
		}

		public void onPostExecute(final Bitmap image) {

			if (image == null) {
				dialog.setMessage("Warning: "
						+ " La pochette n'a pu être trouvé sur le site de Google Books ");
			} else {
				dialog.setMessage("La pochette a pu être trouvé sur Google Books ");
			}

			ImageView pochette;
			pochette = (ImageView) findViewById(R.id.pochette);
			LinearLayout pochetteLayout = (LinearLayout) findViewById(R.id.top_linear_layout_id_pochette);
			if (image == null) {
				pochetteLayout.setVisibility(LinearLayout.INVISIBLE);
			}
			if (image != null) {
				pochette.setImageBitmap(image);

			} else {

				pochette.setImageResource(R.drawable.item_book);
			}
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
		}

	}

	public Bitmap iSBNTreatement(String isbnNumber) {
		Log.d(TAG, "====> ISBN Treatement  ============= ");
		try {
			isbnNumber = isbnNumber.trim();
			String[] results = isbnNumber.split("-");
			StringBuilder isbn = new StringBuilder();
			for (String resu : results) {
				isbn.append(resu);
			}
			Pattern p = Pattern.compile("^([0-9]*).*");
			Matcher m = p.matcher(isbn.toString());

			if (m.find()) {
				String isbnTreated = m.group(1);
				Log.i(TAG, "ISBN : " + isbnTreated);

				InputStream googleBookJson = HttpUrlConnectionService
						.connectToUrl("https://www.googleapis.com/books/v1/volumes?q=isbn:"
								+ isbnTreated);

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(googleBookJson));

				String line;
				StringBuilder builder = new StringBuilder();

				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}

				JSONObject object = (JSONObject) new JSONTokener(
						builder.toString()).nextValue();
				Log.d(TAG, "Total Items : " + object.get("totalItems") + "");

				JSONObject json = new JSONObject(builder.toString());
				JSONArray array = json.getJSONArray("items");
				System.out.println(array.length());
				for (int i = 0; i < array.length(); i++) {
					JSONObject volume = array.getJSONObject(i);
					volumeId = volume.getString("id");
					Log.i("[GOOGLE Books]", " Volume Id  " + volumeId);
				}
				if (volumeId != null) {
					Bitmap img = loadBitmap(googleBookPreviewUrl + volumeId
							+ googleBookPreviewCover);
					return img;
				}
			}
		} catch (IOException e) {
			Toast.makeText(
					SearchSudocRecordDetailsActivicty.this,
					"Technical Problem occur - Please try again your request.  \n Please send me the log for application improvement-  \n "
							+ "IOException : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (JSONException e) {
			Toast.makeText(
					SearchSudocRecordDetailsActivicty.this,
					"Technical Problem occur - Please try again your request.  \n Please send me the log for application improvement-  \n "
							+ "JSONException : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Bottom List Action Release
	 */
	public void onClick(View view) {
		if (view == btnGoogle || view == txtGoogle) {
			String urlWeb = googleBookWebUrl + volumeId
					+ googleBookWebUrlOption;
			Log.d(TAG, "Url Google " + urlWeb);
			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse(urlWeb));
			startActivity(browserIntent);
		}
		if (view == btnLocation || view == txtBiblio) {
			Bundle param = new Bundle();
			param.putString("ppn", ppn);
			Intent newIntent = new Intent(this.getApplicationContext(),
					WhereActivity.class);
			newIntent.putExtras(param);
			startActivity(newIntent);
		}
		if (view == btnWorldCat || view == txtWorldCat) {
			String worldCatUrlTreat = worldCatUrl + worldCatId;
			Log.d(TAG, "World Cat URL : " + worldCatUrlTreat);
			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse(worldCatUrlTreat));
			startActivity(browserIntent);
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
