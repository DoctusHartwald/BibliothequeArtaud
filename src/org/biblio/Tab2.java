package org.biblio;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.client.ClientProtocolException;
import org.biblio.url.HttpUrlConnectionService;
import org.biblio.xml.SearchItemViafHandler;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class Tab2 extends Activity implements OnClickListener {

	String[] searchByCategory = { "All", "Nom de personne", "Nom collectivité",
			"Sujet", "Nom géographique", "Famille", "Titre", "Auteur Titre",
			"Nom de marque", "PPN", "RCR" };
	// view Component
	ListView listItem;
	SimpleAdapter mSchedule;
	Spinner searchBy;
	Button searchButtonUI;
	EditText searchItemUI;
	final String TAG = getClass().getName();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.onglet2);
		searchBy = (Spinner) findViewById(R.id.spinner2);
		searchButtonUI = (Button) findViewById(R.id.buttonSearch);

		searchButtonUI.setOnClickListener(this);
		searchItemUI = (EditText) findViewById(R.id.searchTerm);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, searchByCategory);
		searchBy.setAdapter(adapter);
	}

	public void onClick(View v) {
		if (v == searchButtonUI) {
			// Action search Viaf
			try {
				URI base = new URI("http", "viaf.org", "/viaf/search",
						"query=cql.any+all\""
								+ searchItemUI.getText().toString() + "\"",
						null);
				String suffixUrl = "&startRecord=1&sortKeys=holdingscount&httpAccept=text/xml";
				String urlRequest = base + "&maximumRecords=100" + suffixUrl;
				Log.d(TAG, "[VIAF] URL request : " + urlRequest);
				InputStream con = HttpUrlConnectionService
						.connectToUrl(urlRequest);

				SAXParserFactory factory = SAXParserFactory.newInstance();
				SAXParser saxParser = factory.newSAXParser();
				SearchItemViafHandler handler = new SearchItemViafHandler();
				saxParser.parse(con, handler);
				handler.getViaf();

			} catch (URISyntaxException e) {
				Toast.makeText(
						Tab2.this,
						"Technical Problem occur - Please try again your request.  \n Please send me the log for application improvement-  \n "
								+ "MalformedURLException : " + e.getMessage(),
						Toast.LENGTH_LONG).show();
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				Toast.makeText(
						Tab2.this,
						"Technical Problem occur - Please try again your request.  \n Please send me the log for application improvement-  \n "
								+ "ClientProtocolException : " + e.getMessage(),
						Toast.LENGTH_LONG).show();
				e.printStackTrace();
			} catch (IOException e) {
				Toast.makeText(
						Tab2.this,
						"Technical Problem occur - Please try again your request.  \n Please send me the log for application improvement-  \n "
								+ "IOException : " + e.getMessage(),
						Toast.LENGTH_LONG).show();
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				Toast.makeText(
						Tab2.this,
						"Technical Problem occur - Please try again your request.  \n Please send me the log for application improvement-  \n "
								+ "ParserConfigurationException : "
								+ e.getMessage(), Toast.LENGTH_LONG).show();
				e.printStackTrace();
			} catch (SAXException e) {
				Toast.makeText(
						Tab2.this,
						"Technical Problem occur - Please try again your request.  \n Please send me the log for application improvement-  \n "
								+ "SAXException : " + e.getMessage(),
						Toast.LENGTH_LONG).show();
				e.printStackTrace();
			}
		}

	}

}
