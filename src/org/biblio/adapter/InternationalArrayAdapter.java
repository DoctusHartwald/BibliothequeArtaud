package org.biblio.adapter;

import java.util.List;

import org.biblio.R;
import org.biblio.mapping.viaf.Titre;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InternationalArrayAdapter extends ArrayAdapter<Titre> {

	private List<Titre> list;
	private final Activity context;
	private final String TAG = getClass().getName();
	int compteur = 0;

	public InternationalArrayAdapter(Activity context, List<Titre> list) {
		super(context, R.layout.affichageitem_internationnal, list);
		this.context = context;
		this.list = list;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = vi.inflate(R.layout.affichageitem_internationnal, null);
		ViewHolder viewHolder = new ViewHolder();

		LinearLayout body = (LinearLayout) view.findViewById(R.id.itemLayout);

		viewHolder.titre = (TextView) view.findViewById(R.id.titre);
		viewHolder.imgItem = (ImageView) view.findViewById(R.id.imgItem);
		// change d'image des sources de bibliotheques.

		Log.i(getClass().getSimpleName(), "Position " + position);
		List<String> sources = list.get(position).getSource();
		compteur = 0;

		for (String source : sources) {
			// Log.i(getClass().getSimpleName(), "Source " + source.toString());
			// Log.i(getClass().getSimpleName(), "compteur : " + compteur);

			if ("BNF".equalsIgnoreCase(source.trim())) {
				// fill in any details dynamically here
				TextView textView = (TextView) view
						.findViewById(R.id.description);
				textView.setText("BNF - Bibliothèque National de France");
				// insert into main view
				View insertPoint = view.findViewById(R.id.countries);
				body.addView(vi.inflate(R.layout.affichageitem_internationnal,
						null));
				((ViewGroup) insertPoint).addView(view, 0,
						new ViewGroup.LayoutParams(
								ViewGroup.LayoutParams.WRAP_CONTENT,
								ViewGroup.LayoutParams.WRAP_CONTENT));
			}

		}

		return view;
	}

	static class ViewHolder {
		ImageView imgItem;
		TextView titre;

		ImageView flag;
		TextView description;
		ImageView flag2;
		TextView description2;
		ImageView flag3;
		TextView description3;
		ImageView flag4;
		TextView description4;
		ImageView flag5;
		TextView description5;
		ImageView flag6;
		TextView description6;
		ImageView flag7;
		TextView description7;
		ImageView flag8;
		TextView description8;
		ImageView flag9;
		TextView description9;
		ImageView flag10;
		TextView description10;
		ImageView flag11;
		TextView description11;
		ImageView flag12;
		TextView description12;
		ImageView flag13;
		TextView description13;
		ImageView flag14;
		TextView description14;
		ImageView flag15;
		TextView description15;
		ImageView flag16;
		TextView description16;
		ImageView flag17;
		TextView description17;
		ImageView flag18;
		TextView description18;
		ImageView flag19;
		TextView description19;
		ImageView flag20;
		TextView description20;
		ImageView flag21;
		TextView description21;
		ImageView flag22;
		TextView description22;
	}
}
