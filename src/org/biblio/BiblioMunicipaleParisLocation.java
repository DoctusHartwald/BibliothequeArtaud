package org.biblio;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import mapping.municipale.paris.EntryRecord;
import mapping.municipale.paris.EntryRow;
import mapping.municipale.paris.EntryRowLocation;
import mapping.municipale.paris.Holding;
import mapping.municipale.paris.HoldingRow;

import org.apache.http.HttpResponse;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.biblio.adapter.MunicipaleArrayLocationAdapter;
import org.biblio.xml.SearchMunicipaleHandler;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BiblioMunicipaleParisLocation extends Activity implements
		OnClickListener {
	ListView listMunicipaleLocation;

	String searchPosition;
	String searchTitle;
	String searchAuteur;
	String searchHiddenTitle;
	HttpContext localContext;
	String searchTerm;
	List<EntryRow> rows;
	static List<HoldingRow> holdingRows;

	final String TAG = getClass().getSimpleName();
	BibliMunicipaleSession appState;
	Button buttonLocation;
	TextView textLocation;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.municipale_location);

		buttonLocation = (Button) findViewById(R.id.buttonLocation);
		buttonLocation.setOnClickListener(this);
		textLocation = (TextView) findViewById(R.id.textView11);
		textLocation.setOnClickListener(this);

		// get param
		Bundle bundle = getIntent().getExtras();
		searchTitle = bundle.getString("searchTitle");
		searchAuteur = bundle.getString("searchAuteur");
		searchHiddenTitle = bundle.getString("searchHiddenTitle");
		searchTerm = bundle.getString("searchTerm");
		Integer position = bundle.getInt("searchPosition");

		searchPosition = position.toString();
		Log.i(TAG, "searchTitle : " + searchTitle);
		Log.i(TAG, "searchAuteur :" + searchAuteur);
		Log.i(TAG, "searchHiddenTitle :" + searchHiddenTitle);

		Log.i(TAG, "Position item click :" + position);

		String[] arrays = new String[2];
		arrays[0] = searchPosition;
		ProgressDialog progress = new ProgressDialog(this);
		new SearchBiblioLocationTask(getApplicationContext(), progress)
				.execute(arrays);
	}

	private class SearchBiblioLocationTask extends
			AsyncTask<String, Integer, EntryRowLocation> implements
			OnItemClickListener, OnItemLongClickListener {
		ProgressDialog dialog;
		Context context;

		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			// TODO Auto-generated method stub
			return false;
		}

		SearchBiblioLocationTask(Context context, ProgressDialog dialog) {
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
		protected EntryRowLocation doInBackground(String... params) {
			try {
				return requestURL(params[0]);

			} catch (IllegalStateException e) {
				Toast.makeText(
						BiblioMunicipaleParisLocation.this,
						"Technical Problem occur - Please try again your request.  \n Please send me the log -  \n "
								+ "IllegalStateException : " + e.getMessage(),
						Toast.LENGTH_LONG).show();
				e.printStackTrace();
			} catch (IOException e) {
				Toast.makeText(
						BiblioMunicipaleParisLocation.this,
						"Technical Problem occur - Please try again your request.  \n Please send me the log -  \n "
								+ "IOException : " + e.getMessage(),
						Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
			Log.e(TAG, "Error Occur");
			return null;
		}

		public void onPostExecute(final EntryRowLocation entryRowLocation) {
			super.onPostExecute(entryRowLocation);

			List<EntryRow> rows = entryRowLocation.getEntryRows();
			List<HoldingRow> holdingRows = entryRowLocation.getHoldingRow();
			setHoldingRows(holdingRows);

			if (rows != null && rows.size() == 0) {
				AlertDialog alert = new AlertDialog.Builder(
						getApplicationContext()).create();
				alert.setTitle("Information");
				alert.setMessage("No data was found in Municipale Library");

				Toast.makeText(BiblioMunicipaleParisLocation.this,
						"No data was found in the Municipale Library.",
						Toast.LENGTH_SHORT).show();
			}
			if (rows == null) {
				Log.e(TAG, "No entry Rows is been retrieved ");
				AlertDialog alert = new AlertDialog.Builder(
						getApplicationContext()).create();
				alert.setTitle("Information");
				alert.setMessage("No data was found in Bibliotheque Municipale ");
				Toast.makeText(BiblioMunicipaleParisLocation.this,
						"No data was found in the Municipale Library.",
						Toast.LENGTH_SHORT).show();
			}
			// Autre cas
			if (rows != null) {
				dialog.setMessage(" Nombre de resultats trouvés: "
						+ rows.size());
			}
			if (rows != null) {
				for (EntryRow entryRow : rows) {
					Log.d(TAG, entryRow.toString());
				}
				// listview set
				listMunicipaleLocation = (ListView) findViewById(R.id.listMunicipaleLocationDetails);
				ArrayAdapter<EntryRow> adapter = new MunicipaleArrayLocationAdapter(
						BiblioMunicipaleParisLocation.this, rows);
				listMunicipaleLocation.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				listMunicipaleLocation.setOnItemClickListener(this);
			}

			if (dialog.isShowing()) {
				dialog.dismiss();
			}
		}

		public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {

		}
	}

	public EntryRowLocation requestURL(String position)
			throws IllegalStateException, IOException {
		EntryRowLocation entryRowLocation = new EntryRowLocation();

		Log.i(TAG, "Position notice : " + position);
		String url = "http://b14-sigbermes.apps.paris.fr/ClientBookLine/recherche/NoticesDetaillees.asp?INSTANCE=EXPLOITATION&iNotice="
				+ position + "&ldebut=&output=XML";

		Log.d(TAG, "URL Bibli Municipale Location :" + url);
		Log.d(TAG, "==========================================");

		CookieStore cookieStore = BiblioMunicipaleParisActivicty
				.getCookieStore();
		List<Cookie> cookies = cookieStore.getCookies();

		for (int i = 0; i < cookies.size(); i++) {
			Log.i(TAG, "Local cookie: " + cookies.get(i));
		}
		DefaultHttpClient client = new DefaultHttpClient();
		HttpProtocolParams.setUseExpectContinue(client.getParams(), false);
		HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {

			public boolean retryRequest(IOException exception,
					int executionCount, HttpContext context) {
				// retry a max of 5 times
				if (executionCount >= 5) {
					return false;
				}
				if (exception instanceof NoHttpResponseException) {
					return true;
				} else if (exception instanceof ClientProtocolException) {
					return true;
				}
				return false;
			}
		};
		client.setHttpRequestRetryHandler(retryHandler);
		HttpContext localContext = new BasicHttpContext();
		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);

		HttpGet request = new HttpGet(url);

		HttpResponse response = null;
		try {
			response = client.execute(request, localContext);
			if (response != null) {

				// parse bibli municipale de PARIS
				Log.d(TAG, "response HttpResponse not null ");
				InputStream is = response.getEntity().getContent();

				Log.d(TAG, "============================================");
				StringBuilder resultatStream = new StringBuilder();
				String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
				String dataInput = convertStreamToString(is);

				dataInput = dataInput.replaceAll("&", "et");
				resultatStream.append(header);
				resultatStream.append(dataInput);
				Log.d(TAG, resultatStream.toString());
				InputStream con = new ByteArrayInputStream(resultatStream
						.toString().getBytes());

				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser saxParser;

				saxParser = factory.newSAXParser();
				SearchMunicipaleHandler handler = new SearchMunicipaleHandler();
				saxParser.parse(con, handler);

				List<EntryRecord> vItems = handler.getRecords();

				rows = new ArrayList<EntryRow>();
				List<Holding> holdings = handler.getHoldings();

				if (holdings != null && holdings.size() == 0) {
					Toast.makeText(BiblioMunicipaleParisLocation.this,
							" No Library available has your item . ",
							Toast.LENGTH_LONG).show();
				}

				if (vItems != null && vItems.size() == 0) {
					Toast.makeText(
							BiblioMunicipaleParisLocation.this,
							"No EntryRecord has been returned for bibliotheque Municipale  . Please retry an other research .",
							Toast.LENGTH_LONG).show();
				}
				int flag = 0;
				EntryRow row = null;
				int positionIndex = 0;

				for (EntryRecord entryRecord : vItems) {
					if (entryRecord.getTitle() != null) {

						if (flag == 0) {
							row = new EntryRow();
							flag = 2; // autre valeur que 1 on est passe dessus
							// 3 flag = fin de EntryRow
						}

						if ("Titre".equalsIgnoreCase(entryRecord.getTitle())) {
							row = new EntryRow();
							row.setTitle(entryRecord.getValueRecord());
							// System.out.println(entryRecord.getValueRecord());
							flag = 1;
						}
						if ("Auteur".equalsIgnoreCase(entryRecord.getTitle())) {
							if (entryRecord.getRawValue() != null) {
								row.setAuteur(entryRecord.getRawValue()
										.replaceAll("¤", " "));
							} else {
								row.setAuteur(entryRecord.getValueRecord());
							}

							flag = 1;
						}
						if ("Format".equalsIgnoreCase(entryRecord.getTitle())) {
							row.setFormat(entryRecord.getValueRecord());
						}
						if ("Collection".equalsIgnoreCase(entryRecord
								.getTitle())) {
							row.setCollection(entryRecord.getValueRecord()
									+ entryRecord.getRawValue());
						}
						if ("Contenu".equalsIgnoreCase(entryRecord.getTitle())) {
							row.setContenu(entryRecord.getValueRecord());
						}
						if ("Type de document".equalsIgnoreCase(entryRecord
								.getTitle())) {
							row.setTypeDeDocument(entryRecord.getValueRecord());
						}

						if ("editeur".equalsIgnoreCase(entryRecord.getTitle())) {
							row.setEditeur(entryRecord.getValueRecord());
							flag = 1;
						}
						if ("HIDDEN_ISBN".equalsIgnoreCase(entryRecord
								.getTitle())) {
							row.setHiddenIsbn(entryRecord.getValueRecord());
							flag = 1;
						}
						if ("HIDDEN_TITLE".equalsIgnoreCase(entryRecord
								.getTitle())) {

							row.setHiddenTitle(entryRecord.getValueRecord());
							flag = 1;
						}
						if ("HIDDEN_ISBN".equalsIgnoreCase(entryRecord
								.getTitle())) {
							row.setHiddenIsbn(entryRecord.getValueRecord()
									.replace("-", ""));
						}
						if ("HIDDEN_CREATOR".equalsIgnoreCase(entryRecord
								.getTitle())) {
							row.setHiddenCreator(entryRecord.getValueRecord());
							flag = 3;
							positionIndex++;
						} else {
							if (row.getAutreInformation() != null) {
								row.setAutreInformation(row
										.getAutreInformation()
										+ " \n"
										+ entryRecord.getValueRecord());
							} else {
								row.setAutreInformation(entryRecord
										.getValueRecord());
							}
						}
						if (flag == 3) {
							rows.add(row);
							flag = 0;
						}

					}
					// debug entryRecords donnée sur N lignes
					// System.out.println(entryRecord.toString());
				}// end for entryRecord

				int flagHolding = 0;
				HoldingRow holdingRow = null;
				List<HoldingRow> holdingRows = new ArrayList<HoldingRow>();
				for (Holding holding : holdings) {
					// System.out.println(holding.toString());
					if (flagHolding == 0) {
						holdingRow = new HoldingRow();
					}
					if ("SITE".equalsIgnoreCase(holding.getCode())) {
						holdingRow.setSection("Site : " + holding.getValue());
						flagHolding = 1;
					}
					if ("CODE_SECTION".equalsIgnoreCase(holding.getCode())) {
						holdingRow.setCodeSection("Section : "
								+ holding.getValue());
						flagHolding = 1;
					}
					if ("COTE".equalsIgnoreCase(holding.getCode())) {
						holdingRow.setCote("Cote : " + holding.getValue());
						flagHolding = 1;

					}
					if ("STATUT".equalsIgnoreCase(holding.getCode())) {
						holdingRow.setCodeStatut("Statut : "
								+ holding.getValue());
						holdingRows.add(holdingRow);
						flagHolding = 0;
					}
				}

				entryRowLocation.setEntryRows(rows);
				entryRowLocation.setHoldingRow(holdingRows);
				return entryRowLocation;

			}// end response

			if (response == null) {
				Toast.makeText(
						BiblioMunicipaleParisLocation.this,
						"No reponse has been returned for bibliotheque Municipale  ",
						Toast.LENGTH_SHORT).show();
			}
		} catch (ClientProtocolException e) {
			Toast.makeText(
					BiblioMunicipaleParisLocation.this,
					"Technical Problem occur - Please try again your request.  \n Please send me the log -  \n "
							+ "ClientProtocolException : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (ParserConfigurationException e1) {
			Toast.makeText(
					BiblioMunicipaleParisLocation.this,
					"Technical Problem occur - Please try again your request.  \n Please send me the log -  \n "
							+ "ParserConfigurationException : "
							+ e1.getMessage(), Toast.LENGTH_LONG).show();
			e1.printStackTrace();
		} catch (SAXException e1) {
			Toast.makeText(
					BiblioMunicipaleParisLocation.this,
					"Technical Problem occur - Please try again your request.  \n Please send me the log -  \n "
							+ " SAXException : " + e1.getMessage(),
					Toast.LENGTH_LONG).show();
			e1.printStackTrace();
		} catch (IOException e) {
			Toast.makeText(
					BiblioMunicipaleParisLocation.this,
					"Technical Problem occur - Please try again your request.  \n Please send me the log -  \n "
							+ " IOException : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		return entryRowLocation;
	}

	public static String convertStreamToString(InputStream is)
			throws IOException {
		/*
		 * To convert the InputStream to String we use the Reader.read(char[]
		 * buffer) method. We iterate until the Reader return -1 which means
		 * there's no more data to read. We use the StringWriter class to
		 * produce the string.
		 */
		if (is != null) {
			Writer writer = new StringWriter();

			char[] buffer = new char[1024];
			try {
				// Reader reader = new BufferedReader(new
				// InputStreamReader(is,Charset.forName("UTF-8")));
				Reader reader = new BufferedReader(new InputStreamReader(is));

				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			return writer.toString();
		}
		return "";
	}

	public static List<HoldingRow> getHoldingRows() {
		return holdingRows;
	}

	public static void setHoldingRows(List<HoldingRow> holdingRows) {
		BiblioMunicipaleParisLocation.holdingRows = holdingRows;
	}

	public void onClick(View view) {
		if (view == buttonLocation || view == textLocation) {
			Intent biblioMunicipalParisWhere = new Intent(
					getApplicationContext(),
					BiblioMunicipaleWhereActivity.class);
			Log.d(TAG, "Starting BiblioMunicipaleWhereActivity activicty ");

			startActivity(biblioMunicipalParisWhere);
		}

	}

}
