package org.biblio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mapping.municipale.paris.HoldingRow;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class BiblioMunicipaleWhereActivity extends
		org.biblio.ui.actionbarcompat.ActionBarActivity implements
		OnItemClickListener {
	final String TAG = getClass().getSimpleName();
	HashMap<String, String> map;

	ListView listWheres;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sudoc_where_layout);
		Log.d(TAG, "Beginning Where activity municipale");
		List<HoldingRow> holdings = BiblioMunicipaleParisLocation
				.getHoldingRows();

		listWheres = (ListView) findViewById(R.id.listwhere);

		if (holdings != null && holdings.size() > 0) {
			Toast.makeText(BiblioMunicipaleWhereActivity.this,
					" Nombre de Bibliothèque : " + holdings.size(),
					Toast.LENGTH_SHORT).show();
		}

		ArrayList<HashMap<String, String>> listItemData = new ArrayList<HashMap<String, String>>();
		int flag = 0;
		for (HoldingRow holding : holdings) {
			Log.d(TAG, holding.toString());
			map = new HashMap<String, String>();
			map.put("titre", holding.getSection());
			map.put("site", holding.getSite());
			map.put("section", holding.getCodeSection());
			map.put("cote", holding.getCote());
			map.put("disponibilite", holding.getCodeStatut());

			Log.d(TAG, " Statut >> " + holding.getCodeStatut());

			if ("Statut : En rayon".equalsIgnoreCase(holding.getCodeStatut())
					&& flag == 0) {
				map.put("right_toggle", String.valueOf(R.drawable.circle_green));
				flag = 1;
			}
			if ("Statut : Indisponible".equalsIgnoreCase(holding
					.getCodeStatut()) && flag == 0) {
				map.put("right_toggle", String.valueOf(R.drawable.circle_red));
				flag = 1;
			}
			if (flag == 0) {
				map.put("right_toggle", String.valueOf(R.drawable.circle_blue));
				flag = 1;
			}
			flag = 0;
			listItemData.add(map);
		}
		SimpleAdapter mSchedule = new SimpleAdapter(
				BiblioMunicipaleWhereActivity.this, listItemData,
				R.layout.affichageitem_where_municipale_paris, new String[] {
						"titre", "site", "section", "cote", "disponibilite",
						"right_toggle" }, new int[] { R.id.titre, R.id.site,
						R.id.section, R.id.cote, R.id.disponibilite,
						R.id.right_toggle });
		listWheres.setAdapter(mSchedule);
		mSchedule.notifyDataSetChanged();
	}

	public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
		HashMap<String, String> map = (HashMap<String, String>) parent
				.getItemAtPosition(pos);
		String location = map.get("titre").replace("Site : ", "");
		Uri uri = Uri.parse("geo:0,0?q=" + location);
		Intent searchAddress = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(searchAddress);

	}
}
