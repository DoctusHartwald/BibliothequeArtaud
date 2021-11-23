package org.biblio;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.client.ClientProtocolException;
import org.biblio.adapter.BibliographieArrayAdapter;
import org.biblio.help.HelpActivity;
import org.biblio.mapping.sudoc.record.Bibliographie;
import org.biblio.mapping.sudoc.record.Document;
import org.biblio.mapping.sudoc.record.Role;
import org.biblio.xml.SearchBiblioItemHandler;
import org.xml.sax.SAXException;

import url.HttpUrlConnectionService;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Bibliographie Activicty Describe the bibliographie Auteur Contribution .
 * 
 * @author artaud antoine
 * 
 */
public class BiblioActivity extends
		org.biblio.ui.actionbarcompat.ActionBarActivity implements
		OnItemClickListener, OnItemSelectedListener {
	ArrayList<HashMap<String, String>> listItemData = new ArrayList<HashMap<String, String>>();
	HashMap<String, String> map;
	List<Document> documents;
	BibliographieArrayAdapter mSchedule;

	TextView auteurValue;
	TextView countDataValueRole;
	TextView countDataResult;
	ListView listItemBiblio;
	final String urlBiblio = "http://www.idref.fr/services/biblio/";
	final String formatExchange = ".xml";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sudoc_biblio_layout);

		// get param
		Bundle bundle = getIntent().getExtras();
		String ppn = bundle.getString("ppn");
		Log.d("Biblio", "Request PPN : " + ppn);

		// Add callback
		ProgressDialog progress = new ProgressDialog(this);
		new BiblioActivityTask(getApplicationContext(), progress).execute(ppn);
	}

	private class BiblioActivityTask extends
			AsyncTask<String, Void, List<Document>> implements
			OnItemClickListener {
		private ProgressDialog dialog;
		Context context;

		BiblioActivityTask(Context context, ProgressDialog dialog) {
			this.dialog = dialog;
			this.context = context;
		}

		public void onPreExecute() {
			dialog.setMessage("Recherche en cours. Attendez s'il vous plaît ... ");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			dialog.show();
		}

		@Override
		protected List<Document> doInBackground(String... params) {
			return parseRss(params[0]);
		}

		public void onPostExecute(final List<Document> documents) {
			super.onPostExecute(documents);
			Document documentZero = null;
			if (documents != null) {
				dialog.setMessage(" Nombre de resultats trouvés: "
						+ documents.size());
				documentZero = documents.get(0);
			}

			// layout
			auteurValue = (TextView) findViewById(R.id.textNameAuteurData);
			countDataValueRole = (TextView) findViewById(R.id.textCountData);
			countDataResult = (TextView) findViewById(R.id.itemCountDataResult);
			listItemBiblio = (ListView) findViewById(R.id.listBiblioItems);

			if (null != documentZero) {
				auteurValue.setText(documentZero.getmAuteur());
				countDataValueRole.setText(documentZero.getCountRoles());
				if (documents != null)
					countDataResult.setText(new Integer(documents.size())
							.toString());
			}

			// Listener Set
			listItemBiblio.setOnItemClickListener(this);

			ArrayAdapter<Document> adapter = new BibliographieArrayAdapter(
					BiblioActivity.this, documents);
			listItemBiblio.setAdapter(adapter);
			adapter.notifyDataSetChanged();

			if (dialog.isShowing()) {
				dialog.dismiss();
			}
		}

		public void onItemClick(AdapterView<?> parent, View view, int pos,
				long id) {
			Log.d("<Biblio>", "Calling activity where , ppn : "
					+ documents.get(pos).getmPpn());
			Bundle param = new Bundle();
			param.putString("ppn", documents.get(pos).getmPpn());

			Intent newIntent = new Intent(context, WhereActivity.class);
			newIntent.putExtras(param);
			startActivity(newIntent);
		}
	}

	public List<Document> parseRss(String ppn) {
		String urlRequest = urlBiblio + ppn + formatExchange;
		// urlRequest = "http://www.idref.fr/services/biblio/029405882.xml";

		InputStream con;
		try {
			con = HttpUrlConnectionService.connectToUrl(urlRequest);
			// SAX Parser XML response
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			SearchBiblioItemHandler handler = new SearchBiblioItemHandler();
			saxParser.parse(con, handler);
			Bibliographie bibliographies = handler.getmBibliographie();

			documents = new ArrayList<Document>();
			// recopie Name role dans lobjet Document car pour le rendre
			// compatible ArrayAdapter
			for (Role role : bibliographies.getmRole()) {
				for (Document doc : role.getmDocs()) {
					doc.setCountRoles(bibliographies.getmCountRoles());
					doc.setmAuteur(bibliographies.getmName());// Auteur Name
					doc.setmRoleName(role.getmRoleName());
					documents.add(doc);
				}
			}
		} catch (ClientProtocolException e) {
			Toast.makeText(
					BiblioActivity.this,
					"Technical Problem occur -  Please send me the log - \n ClientProtocolException : "
							+ e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (IOException e) {
			Toast.makeText(
					BiblioActivity.this,
					"Technical Problem occur - Please send me the log - \n IOException : "
							+ e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			Toast.makeText(
					BiblioActivity.this,
					"Technical Problem occur - Please send me the log -  Exception : \n ParserConfiguration : "
							+ e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (SAXException e) {
			Toast.makeText(
					BiblioActivity.this,
					"Technical Problem occur - Please send me the log -  \n SaxException : "
							+ e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		return documents;
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub

	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

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
			Intent intent = new Intent(this, SearchSudocAutorityActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
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
			actionBar.setDisplayHomeAsUpEnabled(false);
			actionBar.setDisplayUseLogoEnabled(true);
		}
	}
}
