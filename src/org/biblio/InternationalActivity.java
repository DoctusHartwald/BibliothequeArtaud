package org.biblio;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.biblio.adapter.InternationalArrayAdapter;
import org.biblio.mapping.viaf.Titre;
import org.biblio.mapping.viaf.Viaf;
import org.biblio.xml.SearchItemViafHandler;
import org.xml.sax.SAXException;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class InternationalActivity extends Activity implements
		OnItemClickListener, OnItemLongClickListener {
	ListView listInternationnalTitles;
	ListView listInternationalCoAuthors;

	SimpleAdapter mSchedule;
	// Creation de la ArrayList qui nous permettra de remplire la listView
	ArrayList<HashMap<String, String>> listItemData = new ArrayList<HashMap<String, String>>();
	HashMap<String, String> map;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.international);

		// get param
		Bundle bundle = getIntent().getExtras();
		String urlRequest = bundle.getString("urlRequest");

		// Layout
		listInternationnalTitles = (ListView) this
				.findViewById(R.id.listInternationnalTitles);
		listInternationalCoAuthors = (ListView) this
				.findViewById(R.id.listInternationalCoAuthors);

		// listeners
		listInternationnalTitles.setOnItemClickListener(this);
		listInternationnalTitles.setOnItemLongClickListener(this);
		listInternationalCoAuthors.setOnItemClickListener(this);
		listInternationalCoAuthors.setOnItemLongClickListener(this);

		// SAX Parser XML response
		try {

			// InputStream con =
			// HttpUrlConnectionService.connectToUrl(urlRequest);

			HttpClient client = new DefaultHttpClient();

			// Parametrage Requete HTTP.
			// agent Client connection
			client.getParams()
					.setParameter(
							CoreProtocolPNames.USER_AGENT,
							"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21");

			// encoding UTF-8
			client.getParams().setParameter(
					CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");

			HttpGet request = new HttpGet(urlRequest);

			HttpResponse response = client.execute(request);

			if (response != null)
				Log.d("HttpUrlConnectionService",
						"response HttpResponse not null ");

			String stringDatosEntrada = new Scanner(
					convertStreamToString(response.getEntity().getContent()))
					.useDelimiter("\\A").next().replaceAll("&#39;", "'")
					.replaceAll("&#x9C;", "").replaceAll("&#x98;", "");
			//stringDatosEntrada = StringEscapeUtils.unescapeHtml4(stringDatosEntrada);

			InputStream con = new ByteArrayInputStream(
					stringDatosEntrada.getBytes());

			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			SearchItemViafHandler handler = new SearchItemViafHandler();

			saxParser.parse(con, handler);
			Viaf viaf = handler.getViaf();
			List<Titre> titres = viaf.getTitre();
			for (Titre titre : titres) {
				Log.d(getClass().getSimpleName(), "" + titre.toString());
			}

			// List<CoAuthors> coauthors = viaf.getCoauthors();
			// List<Publishers> publishers = viaf.getPublishers();

			ArrayAdapter<Titre> adapter = new InternationalArrayAdapter(this,
					titres);

			listInternationnalTitles.setAdapter(adapter);
			adapter.notifyDataSetChanged();

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String convertStreamToString(InputStream is) throws IOException {
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
				Reader reader = new BufferedReader(new InputStreamReader(is,
						Charset.forName("UTF-8")));

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

	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		Log.d("<WHERE>", "On Item CLick");
		HashMap<String, String> map = (HashMap<String, String>) parent
				.getItemAtPosition(pos);

		Uri uri = Uri.parse("geo:0,0?q=" + map.get("shortname"));
		Intent searchAddress = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(searchAddress);
	}

	public boolean onItemLongClick(AdapterView<?> parent, View view, int pos,
			long id) {
		HashMap<String, String> map = (HashMap<String, String>) parent
				.getItemAtPosition(pos);
		Log.d("<Where>", "==> Sharing content");
		final Intent MessIntent = new Intent(Intent.ACTION_SEND);
		MessIntent.setType("text/plain");
		MessIntent.putExtra(Intent.EXTRA_TEXT,
				"Going to : " + map.get("shortname"));
		startActivity(Intent.createChooser(MessIntent, "Partager avec..."));

		return true;
	}
}
