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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import mapping.municipale.paris.EntryRecord;
import mapping.municipale.paris.EntryRow;

import org.apache.http.HttpResponse;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.biblio.adapter.MunicipaleArrayAdapter;
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

public class BiblioMunicipaleParisActivicty extends Activity implements
		OnClickListener {

	String searchTerm;
	String category;
	final String TAG = getClass().getSimpleName();
	static CookieStore cookieStore;
	HttpContext localContext;

	Button prev;
	TextView prevTexte;
	Button next;
	TextView nextTexte;

	ListView listMunicipale;
	List<EntryRow> rows;
	int flagPagination = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.municipale_search_layout);

		// get param
		Bundle bundle = getIntent().getExtras();
		searchTerm = bundle.getString("searchTerm");
		category = bundle.getString("category");

		prev = (Button) this.findViewById(R.id.buttonprev);
		next = (Button) this.findViewById(R.id.buttonnext);
		prevTexte = (TextView) this.findViewById(R.id.textViewPrecedent);
		nextTexte = (TextView) this.findViewById(R.id.textViewSuivant);

		prevTexte.setOnClickListener(this);
		nextTexte.setOnClickListener(this);
		next.setOnClickListener(this);
		prev.setOnClickListener(this);

		// Algo searchTerm
		if (null != searchTerm) {
			String[] searchTerms = searchTerm.split(" ");
			String searchConcat = null;
			if (searchTerms.length > 1) {
				Log.i(TAG, " plusieurs term ont été tape par utilisateur ");

				int compteur = 0;
				for (String searchItem : searchTerms) {
					if (compteur == 0) {
						searchConcat = searchItem;
					} else {
						searchConcat = searchConcat + "+" + searchItem;
					}
					compteur++;
				}
				searchTerm = searchConcat;
				Log.i(TAG, "search Term Construction : " + searchTerm);
			}
		}

		String url = "http://b14-sigbermes.apps.paris.fr/ClientBookLine/recherche/executerRechercheprogress.asp?bNewSearch=true&strTypeRecherche=pr_multicritere&txtANY="
				+ searchTerm
				+ "&cboIndexFormatANY=touslesmots&cboIndexFormatLOCMULTI=phraseexacte&CodeDocBaseList=VPCO&chkSortKey=true&USEMULTIINDEXMODE=FALSE&BackUrl=http%3A%2F%2Fb14-sigbermes.apps.paris.fr%2Fmedias%2Fmedias.aspx%3FINSTANCE%3DEXPLOITATION&INSTANCE=EXPLOITATION&output=XML";

		ProgressDialog progress = new ProgressDialog(this);
		new SearchBibliInitialActivictyTask(getApplicationContext(), progress)
				.execute(url);

	}

	private class SearchBibliInitialActivictyTask extends
			AsyncTask<String, Integer, List<EntryRow>> implements
			OnItemClickListener, OnItemLongClickListener {
		ProgressDialog dialog;
		Context context;

		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			// TODO Auto-generated method stub
			return false;
		}

		SearchBibliInitialActivictyTask(Context context, ProgressDialog dialog) {
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
		protected List<EntryRow> doInBackground(String... params) {
			try {
				return requestURL(params[0]);
			} catch (IllegalStateException e) {
				Toast.makeText(
						BiblioMunicipaleParisActivicty.this,
						"Technical Problem occur - Please try again your request.  \n Please send me the log -  \n IllegalStateException : "
								+ e.getMessage(), Toast.LENGTH_LONG).show();
				e.printStackTrace();
			} catch (IOException e) {
				Toast.makeText(
						BiblioMunicipaleParisActivicty.this,
						"Technical Problem occur - Please try again your request.  \n Please send me the log -  \n IOException : "
								+ e.getMessage(), Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
			Log.e(TAG, "Error Occur");
			return null;
		}

		public void onPostExecute(final List<EntryRow> rows) {
			super.onPostExecute(rows);

			if (rows != null && rows.size() == 0) {
				AlertDialog alert = new AlertDialog.Builder(
						getApplicationContext()).create();
				alert.setTitle("Information");
				alert.setMessage("No data was found in Bibliotheque Municipale ");
			}
			if (rows == null) {
				Log.e(TAG, "No entry Rows is been retrieved ");
				AlertDialog alert = new AlertDialog.Builder(
						getApplicationContext()).create();
				alert.setTitle("Information");
				alert.setMessage("No data was found in Bibliotheque Municipale ");
			}
			// Autre cas
			if (rows != null) {
				dialog.setMessage(" Nombre de resultats trouvés: "
						+ rows.size());
			}

			// listview set
			listMunicipale = (ListView) findViewById(R.id.listMunicipale);
			listMunicipale.setOnItemClickListener(this);
			ArrayAdapter<EntryRow> adapter = new MunicipaleArrayAdapter(
					BiblioMunicipaleParisActivicty.this, rows);
			listMunicipale.setAdapter(adapter);

			adapter.notifyDataSetChanged();

			if (dialog.isShowing()) {
				dialog.dismiss();
			}
		}

		public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {
			Intent searchBiblioMunicipaleParisLocation = new Intent(context,
					BiblioMunicipaleParisLocation.class);

			Bundle param = new Bundle();
			param.putInt("searchPosition", rows.get(pos).getPosition());
			param.putString("searchTitle", rows.get(pos).getTitle());
			param.putString("searchAuteur", rows.get(pos).getAuteur());
			param.putString("searchHiddenTitle", rows.get(pos).getHiddenTitle());
			param.putString("searchTerm", searchTerm);
			Log.i(TAG, "Passing param position " + rows.get(pos).getPosition());
			Log.i(TAG, "Starting activicty BiblioMunicipaleParisLocation ");
			searchBiblioMunicipaleParisLocation.putExtras(param);
			startActivity(searchBiblioMunicipaleParisLocation);
		}
	}

	public List<EntryRow> requestURL(String url) throws IllegalStateException,
			IOException {

		Log.d(TAG, "URL Bibli Municipale" + url);

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

		// Recuperation du cookies de la premiere connection de recherche.
		if (flagPagination == 0) {
			localContext = new BasicHttpContext();
			cookieStore = new BasicCookieStore();
			localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		}

		Log.d(TAG, "===================================");
		List<Cookie> cookies = cookieStore.getCookies();
		for (int i = 0; i < cookies.size(); i++) {
			Log.i(TAG, "Local cookie: " + cookies.get(i));
		}

		HttpGet request = new HttpGet(url);

		HttpResponse response = null;
		try {
			response = client.execute(request, localContext);
			if (response != null) {
				Log.d(TAG, "///////////////////////////////////");

				for (int i = 0; i < cookies.size(); i++) {
					Log.i(TAG, "Local cookie: " + cookies.get(i));
				}
				setCookieStore(cookieStore);
				// parse bibli municipale de PARIS
				Log.d(TAG, "response HttpResponse not null ");
				InputStream is = response.getEntity().getContent();

				String dataInput = convertStreamToString(is);
				dataInput = dataInput.replaceAll("&", "et");
				InputStream con = new ByteArrayInputStream(dataInput.getBytes());

				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser saxParser;

				saxParser = factory.newSAXParser();
				SearchMunicipaleHandler handler = new SearchMunicipaleHandler();
				saxParser.parse(con, handler);

				List<EntryRecord> vItems = handler.getRecords();
				rows = new ArrayList<EntryRow>();

				if (vItems != null && vItems.size() == 0) {
					Toast.makeText(
							BiblioMunicipaleParisActivicty.this,
							"No EntryRecord has been returned for bibliotheque Municipale  . Please retry an other research .",
							Toast.LENGTH_SHORT).show();
				}

				Pattern p = Pattern.compile("^[0-9][0-9a-zA-z ]*");
				int flag = 0;
				EntryRow row = null;
				int flagNoAddValue = 0;
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
							System.out.println(entryRecord.getValueRecord());
							flag = 1;
						}
						if ("Auteur".equalsIgnoreCase(entryRecord.getTitle())) {
							row.setAuteur(entryRecord.getValueRecord());
							flag = 1;
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
							System.out.println(entryRecord.getValueRecord());
							row.setHiddenTitle(entryRecord.getValueRecord());
							flag = 1;
						}
						if ("HIDDEN_CREATOR".equalsIgnoreCase(entryRecord
								.getTitle())) {
							row.setHiddenCreator(entryRecord.getValueRecord());
							flag = 1;
						}
						if ("HIDDEN_STATUS".equalsIgnoreCase(entryRecord
								.getTitle())) {
							row.setHiddenNew(entryRecord.getValueRecord());
							flag = 1;
						}
						if ("HIDDEN_NEW".equalsIgnoreCase(entryRecord
								.getTitle())) {
							row.setTitle(entryRecord.getValueRecord());
							flag = 1;
						}
						if ("HIDDEN_STATUS".equalsIgnoreCase(entryRecord
								.getTitle())) {
							row.setHiddenStatus(entryRecord.getValueRecord());
							flag = 3;
							row.setPosition(positionIndex);
							positionIndex++;

						}
						if (flag == 3) {

							Matcher m = p.matcher(row.getTitle());
							if (row.getHiddenTitle() == null && m.find()) {
								flagNoAddValue = 1;
							}
							if (flagNoAddValue == 0) {
								Log.i(TAG,
										"Record position  : "
												+ row.getPosition());

								rows.add(row);
							}
							flagNoAddValue = 0;
							flag = 0;
						}

					}
					// debug entryRecords donnée sur N lignes
					// System.out.println(entryRecord.toString());
				}// end for entryRecord

				Log.d(TAG, "=====================");
				/*
				 * for (EntryRecord entryRecord2 : vItems) { Log.d(TAG,
				 * entryRecord2.toString()); }
				 */
				// debug entryRow

				return rows;

			}// end response

			if (response == null) {
				Toast.makeText(
						BiblioMunicipaleParisActivicty.this,
						"No reponse has been returned for bibliotheque Municipale  ",
						Toast.LENGTH_SHORT).show();
			}
		} catch (ClientProtocolException e) {
			Toast.makeText(
					BiblioMunicipaleParisActivicty.this,
					"Technical Problem occur - Please try again your request.  \n Please send me the log -  \n ClientProtocolExeption : "
							+ e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		} catch (ParserConfigurationException e1) {
			Toast.makeText(
					BiblioMunicipaleParisActivicty.this,
					"Technical Problem occur - Please try again your request.  \n Please send me the log -  \n "
							+ "ParserConfigurationException : "
							+ e1.getMessage(), Toast.LENGTH_LONG).show();
			e1.printStackTrace();
		} catch (SAXException e1) {
			Toast.makeText(
					BiblioMunicipaleParisActivicty.this,
					"Technical Problem occur - Please try again your request.  \n Please send me the log -  \n "
							+ "SAXException : " + e1.getMessage(),
					Toast.LENGTH_LONG).show();

			e1.printStackTrace();
		} catch (IOException e) {
			Toast.makeText(
					BiblioMunicipaleParisActivicty.this,
					"Technical Problem occur - Please try again your request.  \n Please send me the log -  \n "
							+ "IOException : " + e.getMessage(),
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		return rows;
	}

	public void onClick(View view) {

		listMunicipale = (ListView) findViewById(R.id.listMunicipale);

		if (view == next || view == nextTexte) {
			flagPagination = flagPagination + 10;
			String url = "http://b14-sigbermes.apps.paris.fr/ClientBookLine/recherche/executerRechercheProgress.asp?PORTAL_ID=&STAXON=&LTAXON=&IDCAT=&INSTANCE=EXPLOITATION&"
					+ "txtANY="
					+ searchTerm
					+ "&lDebut="
					+ flagPagination
					+ "&chkckbox23=off&chk0=off&chk1=off&chk2=off&chk3=off&chk4=off&chk5=off&chk6=off&chk7=off&chk8=off&chk9=off&DISPLAYMENU=&IDTEZO=&IDTEZOBASE=&IDTEZOFORM=";
			ProgressDialog progress = new ProgressDialog(this);
			new SearchBibliInitialActivictyTask(getApplicationContext(),
					progress).execute(url);
			ArrayAdapter<EntryRow> adapter = new MunicipaleArrayAdapter(
					BiblioMunicipaleParisActivicty.this, rows);
			listMunicipale.setAdapter(adapter);
			adapter.notifyDataSetChanged();

		}
		if (view == prev || view == prevTexte) {
			if (flagPagination != 0) {
				flagPagination = flagPagination - 10;
				String url = "http://b14-sigbermes.apps.paris.fr/ClientBookLine/recherche/executerRechercheProgress.asp?PORTAL_ID=&STAXON=&LTAXON=&IDCAT=&INSTANCE=EXPLOITATION&"
						+ "txtANY="
						+ searchTerm
						+ "&lDebut="
						+ flagPagination
						+ "&chkckbox23=off&chk0=off&chk1=off&chk2=off&chk3=off&chk4=off&chk5=off&chk6=off&chk7=off&chk8=off&chk9=off&DISPLAYMENU=&IDTEZO=&IDTEZOBASE=&IDTEZOFORM=";
				ProgressDialog progress = new ProgressDialog(this);
				new SearchBibliInitialActivictyTask(getApplicationContext(),
						progress).execute(url);
				ArrayAdapter<EntryRow> adapter = new MunicipaleArrayAdapter(
						BiblioMunicipaleParisActivicty.this, rows);
				listMunicipale.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			}
		}
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

	/*
	 * Affiche dans text view la reponse XML
	 * 
	 * @param response
	 * 
	 * @throws IllegalStateException
	 * 
	 * @throws IOException
	 */
	public void debugCode(InputStream is) throws IllegalStateException,
			IOException {
		// textView = (TextView) findViewById(R.id.textView);
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		String line = "";
		StringBuilder sb = new StringBuilder();
		while ((line = rd.readLine()) != null) {
			// textView.append(line);
			sb.append(line);
		}
		Log.d(TAG, sb.toString());
		String userAgent = System.getProperty("http.agent");
		Log.d(TAG, userAgent);
	}

	public static CookieStore getCookieStore() {
		return cookieStore;
	}

	public static void setCookieStore(CookieStore cookieStore) {
		BiblioMunicipaleParisActivicty.cookieStore = cookieStore;
	}

}
