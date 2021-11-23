package org.biblio;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.biblio.mapping.sudoc.search.SearchItem;
import org.biblio.url.UrlBuilder;
import org.biblio.xml.SearchItemHandler;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


public class SearchSudocSuggestActivity extends Activity{
	HashMap<String, String> map;
	ArrayList<HashMap<String, String>> listItemData = new ArrayList<HashMap<String, String>>();
	ListView listItem;
	
	SimpleAdapter mSchedule;
	   @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.onglet2);
	        
	    	// layout
			//listItem = (ListView) findViewById(R.id.listItemsSuggest);
			
			// CrŽation d'un SimpleAdapter qui se chargera de mettre les items
			// prŽsent dans notre list (listItem) dans la vue affichageitem
			mSchedule = new SimpleAdapter(this.getBaseContext(), listItemData,
					R.layout.affichageitem, new String[] { "img", "titre",
							"description" }, new int[] { R.id.imgItem, R.id.titre,
							R.id.description });
			
			listItem.setAdapter(mSchedule);
			this.mSchedule.notifyDataSetChanged();

	    	// get param
			Bundle bundle = getIntent().getExtras();
			String query = bundle.getString("queryWord");
			Toast.makeText(getBaseContext(),
					"Passing"+query,
					Toast.LENGTH_LONG).show();
			Log.i("SearchSudocSuggestActivity", "==> query :"+query);
			try {
				doMySearch(query);
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	   }
	   private void doMySearch(String query) throws SAXException, IOException, ParserConfigurationException {
		  
			// TODO Auto-generated method stub
			UrlBuilder urlBuidler = new UrlBuilder();
			HttpURLConnection con;
			URL url = new URL(urlBuidler.constructUrlSuggest(query));
			con = (HttpURLConnection) url.openConnection();
			con.setReadTimeout(10000);
			con.setConnectTimeout(15000);
			con.setRequestMethod("GET");
			con.connect();

			// SAX Parser XML response
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			SearchItemHandler handler = new SearchItemHandler();
			saxParser.parse(con.getInputStream(), handler);

			List<SearchItem> vItems = handler.getvItems();
			if (vItems.isEmpty()) {
				Toast.makeText(getBaseContext(),
						"No data returned .Please search again . ",
						Toast.LENGTH_LONG).show();
			}
			for (SearchItem searchItem : vItems) {
				// Log.d("<TAB1DATA>", searchItem.toString());
				map = new HashMap<String, String>();
				map.put("titre", searchItem.getRecordType() + " - "
						+ searchItem.getPpn());
				map.put("description", searchItem.getAuteur());
				map.put("img", String.valueOf(R.drawable.book2));
				map.put("ppn", searchItem.getPpn());
				listItemData.add(map);
			}
			this.mSchedule.notifyDataSetChanged();

			con.disconnect();
		}
}
