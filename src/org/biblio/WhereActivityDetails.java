package org.biblio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.xml.sax.SAXException;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class WhereActivityDetails extends
		org.biblio.ui.actionbarcompat.ActionBarActivity implements
		OnClickListener {

	ListView listRecordsDetails;
	Button disponibilite;
	Button googleMap;
	Button telBibli;
	String linkAccess;
	Boolean isClickable = false;
	TextView titleScreen;

	String numeroRCR;
	String SUDOCRECORDS = "http://www.sudoc.abes.fr/DB=2.2/CMD?ACT=SRCHA&IKT=8888&SRT=RLV&TRM=";
	final String TAG = getClass().getSimpleName();
	ArrayList<HashMap<String, String>> listItemData = new ArrayList<HashMap<String, String>>();
	String ppn;
	String shortName;

	String gpsLatitude;
	Boolean isGpsLatitudeFound = false;
	String gpsLongitude;
	Boolean isGpsLongitudeFound = false;
	String telephoneNum;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.where_details);

		Bundle bundle = getIntent().getExtras();
		numeroRCR = bundle.getString("rcr");
		ppn = bundle.getString("ppn");
		shortName = bundle.getString("shortName");

		Log.d(TAG, "Numero RCR recherche " + numeroRCR);

		titleScreen = (TextView) findViewById(R.id.textViewWhereDetailsTitle);
		titleScreen.setText(getResources()
				.getString(R.string.WhereDetailsTitle));

		disponibilite = (Button) findViewById(R.id.wherebuttonDispo);
		disponibilite.setOnClickListener(this);

		telBibli = (Button) findViewById(R.id.wherebuttonTel);
		telBibli.setOnClickListener(this);

		googleMap = (Button) findViewById(R.id.wherebuttonLocation);
		googleMap.setOnClickListener(this);

		ProgressDialog progress = new ProgressDialog(this);
		new SearchSudocRecordsDetailsWhereActivictyTask(
				getApplicationContext(), progress).execute(SUDOCRECORDS
				+ numeroRCR);
	}

	private class SearchSudocRecordsDetailsWhereActivictyTask extends
			AsyncTask<String, Integer, List<RecordDetail>> implements
			OnItemClickListener, OnItemLongClickListener {
		ProgressDialog dialog;
		Context context;

		SearchSudocRecordsDetailsWhereActivictyTask(Context context,
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

			listRecordsDetails = (ListView) findViewById(R.id.listwhereDetails); // Listener
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
				Log.i(TAG, "=======================");

				Log.i(TAG, recordDetailPlus.getLabel().getText());
				Log.i(TAG, "Resultat Data :" + resultData);

				if ("Géolocalisation :".equalsIgnoreCase(recordDetailPlus
						.getLabel().getText().trim())) {
					Log.i(TAG, "=======================");
					Log.i(TAG, " Géolocalisation ");
					Log.d(TAG, "" + recordDetailPlus.getData());
					String gpsDataTemp = "";
					for (String data : recordDetailPlus.getData().getLine()) {
						gpsDataTemp += data;
					}
					exactGps(gpsDataTemp);
				}

				if ("Tél. renseignements :".equalsIgnoreCase(recordDetailPlus
						.getLabel().getText().trim())) {
					String telDataTemp = "";
					for (String data : recordDetailPlus.getData().getLine()) {
						telDataTemp += data;
					}
					if (telDataTemp != null) {
						telDataTemp = telDataTemp.replaceAll(" ", "");
						if (telDataTemp.contains(" ")) {
							String[] telNum = telDataTemp.split(" ");
							String telBibliotheque = telNum[0].replace("-", "");
							telBibliotheque.replaceAll(".", "");
							telephoneNum = telBibliotheque;
						} else {
							String telBibliotheque = telDataTemp.replace("-",
									"");
							telBibliotheque.replaceAll(".", "");
							telephoneNum = telBibliotheque;
						}
					}
				}

				map.put("data", resultData);
				listItemData.add(map);
			}
			ProgressDialog progress = new ProgressDialog(
					WhereActivityDetails.this);
			new SearchSudocDisponibiliteActivictyTask(getApplicationContext(),
					progress).execute(numeroRCR);

			// Creation d'un SimpleAdapter qui se chargera de mettre les items
			// present dans notre list (listItem) dans la vue affichageitem
			SimpleAdapter mSchedule = new SimpleAdapter(
					WhereActivityDetails.this, listItemData,
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

	/**
	 * <p>
	 * parse sudoc records details
	 * </p>
	 * 
	 * @param urlRcr
	 * @return
	 */
	public List<RecordDetail> parseSudocRecordDetail(String urlRcr) {
		List<RecordDetail> recordsDetails = null;
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			RecordItemDetailsPlusHandler handler = new RecordItemDetailsPlusHandler();
			InputStream is = HttpUrlConnectionService.connectToUrl(urlRcr);
			saxParser.parse(is, handler);
			recordsDetails = handler.getRecorddetails();
		} catch (ClientProtocolException e) {
			Toast.makeText(
					WhereActivityDetails.this,
					"Technical Problem occur - Please try again your request.  \n Please send me the log for application improvement-  \n "
							+ "ClientProtocolException : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (IOException e) {
			Toast.makeText(
					WhereActivityDetails.this,
					"Technical Problem occur - Please try again your request.  \n Please send me the log for application improvement-  \n "
							+ "IOException : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			Toast.makeText(
					WhereActivityDetails.this,
					"Technical Problem occur - Please try again your request.  \n Please send me the log for application improvement-  \n "
							+ "ParserConfigurationException : "
							+ e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (SAXException e) {
			Toast.makeText(
					WhereActivityDetails.this,
					"Technical Problem occur - Please try again your request.  \n Please send me the log for application improvement-  \n "
							+ "SAXException : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		return recordsDetails;
	}

	/**
	 * 
	 * verifie la disponibilite de louvrage
	 * 
	 */
	private class SearchSudocDisponibiliteActivictyTask extends
			AsyncTask<String, Integer, Boolean> {
		ProgressDialog dialog;
		Context context;

		SearchSudocDisponibiliteActivictyTask(Context context,
				ProgressDialog dialog) {
			this.dialog = dialog;
			this.context = context;
		}

		// TODO
		public void onPreExecute() {
			dialog.setMessage(" Vérification de la disponiblité de l'ouvrage . Attendez s'il vous plaît ... ");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			dialog.show();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			Log.d(TAG, "Vérification de la disponibilité de ouvrage ");
			linkAccess = dispoOuvrage(params[0]);
			Log.i(TAG, "linkAcess: " + linkAccess);
			if (null != linkAccess) {

				// filtrage des cas null
				if ("null".equals(linkAccess)) {
					return false;
				}

				String temp = linkAccess.trim();
				if ("null".equalsIgnoreCase(temp)) {
					return false;
				}

				// Ok ouvrage trouve .
				else {
					Log.i(TAG, "[LinkAcess FOUND] " + linkAccess);
					return true;
				}
			}
			return false;
		}

		public void onPostExecute(final Boolean dispo) {
			disponibilite = (Button) findViewById(R.id.wherebuttonDispo);

			if (dispo) {
				isClickable = true;
				Log.d(TAG, "[OK] Ouvrage  disponible  !!");
				disponibilite
						.setBackgroundResource(R.drawable.navbar_allnormal);
			} else {
				isClickable = false;
				Log.d(TAG, "==> Ouvrage non disponible");
				disponibilite
						.setBackgroundResource(R.drawable.navigationcancel);
			}
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
		}
	}

	/**
	 * Traitement verification JSon
	 * 
	 * @param numRcr
	 * @return
	 */
	public String dispoOuvrage(String numRcr) {

		String deeplink = null;
		try {
			InputStream bibliJson = HttpUrlConnectionService
					.connectToUrl("http://www.sudoc.fr/services/deeplinkpsi/"
							+ numRcr + "&format=text/json");
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					bibliJson));

			String line;
			StringBuilder builder = new StringBuilder();

			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			Log.d(TAG, " ====  JSON====");
			Log.d(TAG, "" + builder.toString());

			JSONObject object = (JSONObject) new JSONTokener(builder.toString())
					.nextValue();
			JSONObject sudocObject = object.getJSONObject("sudoc");
			Log.d(TAG, sudocObject.toString(3));

			JSONObject library = sudocObject.getJSONObject("library");
			deeplink = library.getString("deeplink");
			Log.i(TAG, "Deeplink :" + deeplink);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return deeplink;
	}

	public void exactGps(String gpsCoordonnées) {
		Log.i(TAG, "GPS: " + gpsCoordonnées);
		String[] gpsTempCorrdo = gpsCoordonnées.split(":");
		if (gpsTempCorrdo != null) {
			String[] gpsElement = gpsTempCorrdo[1].split("/");
			gpsLatitude = gpsElement[0].trim();
			gpsLongitude = gpsElement[1].trim();
			Log.i(TAG, "Latitude :" + gpsLatitude);
			Log.i(TAG, "Longitude :" + gpsLongitude);
			isGpsLatitudeFound = true;
			isGpsLongitudeFound = true;
		}
	}

	public void onClick(View view) {
		// Check dispo ou pas
		String messagePro = getApplicationContext().getResources().getString(
				R.string.app_pro_message);
		String messageProTitle = getApplicationContext().getResources()
				.getString(R.string.app_pro_message_title);

		String applicationName = getApplicationContext().getResources()
				.getString(R.string.app_name);
		String message_dispo = getApplicationContext().getResources()
				.getString(R.string.message_disponibilite);
		String message_dispo_pro = getApplicationContext().getResources()
				.getString(R.string.message_pro_dispo);

		int flagOK = 0;
		if (isClickable) {
			flagOK = 1;
		}
		if (view == disponibilite) {
//			if ("Doctus Premium".equalsIgnoreCase(applicationName)) {
//				flagOK = 0;
				if (isClickable) {
					Log.d(TAG, "Processing redirection");
					Log.i(TAG, "ppn :Searching : " + ppn);
					linkAccess = linkAccess.replaceAll("#Ppn#", ppn);
					linkAccess = linkAccess.replaceAll("&amp;", "&");

					Log.i(TAG, "Link : " + linkAccess);
					Intent browserIntent = new Intent(Intent.ACTION_VIEW,
							Uri.parse(linkAccess));
					startActivity(browserIntent);
				}
				
				if (!isClickable) {
					Toast.makeText(getBaseContext(), " " + message_dispo,
							Toast.LENGTH_LONG).show();
				}
				
			/*}
			if (flagOK == 1) {
				Log.i(getClass().getSimpleName(), "Want Doctus premium ??");
				AlertDialog alert = new AlertDialog.Builder(
						getApplicationContext()).create();
				alert.setTitle(messageProTitle);
				alert.setMessage(messagePro);

				Toast.makeText(getBaseContext(), message_dispo_pro,
						Toast.LENGTH_LONG).show();
			}
			 */
			
		}

		// Google MAP Location
		if (view == googleMap) {
			if (isGpsLatitudeFound && isGpsLongitudeFound) {
				Log.i(TAG, "Coordonée GPS Bibliotheque Trouve !!!");
				Uri uri = Uri.parse("geo:" + gpsLatitude.trim() + ","
						+ gpsLongitude.trim());
				Intent searchAddress = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(searchAddress);
			} else {
				Log.i(TAG, "Coordonée GPS par Nom " + shortName);
				Uri uri = Uri.parse("geo:0,0?q=" + shortName);
				Intent searchAddress = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(searchAddress);
			}
		}
		if (view == telBibli) {
			Log.i(TAG, "Tel Bibliotheque :" + telephoneNum);
			Uri uri = Uri.parse("tel:" + telephoneNum);
			Intent searchTel = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(searchTel);
		}
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

	// String gps = "Latitude / Longitude : 48.8478098 / 2.3424414";
	public void exactGpsRegex(String gpsCoordonnées) {
		Pattern p = Pattern
				.compile("^(Latitude / Longitude :)([0-9. ]*)/([ 0-9.]*).*");
		Matcher m = p.matcher(gpsCoordonnées);

		if (m.find()) {
			String r2 = m.group(2);
			if (r2 != null) {
				Log.d(TAG, "Latitude Founded");
				gpsLatitude = r2;
				isGpsLatitudeFound = true;
			}

			String r3 = m.group(3);
			if (r3 != null) {
				Log.d(TAG, "Longitude  Founded");
				gpsLongitude = r3;
				isGpsLongitudeFound = true;
			}
		}
		Log.i(TAG, "Aucune correspondance trouve avec :" + gpsCoordonnées);
	}

}
