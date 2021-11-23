package org.biblio.adapter.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.biblio.R;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class SpinnerAdapter extends SimpleAdapter {
	private final Activity context;

	public SpinnerAdapter(Activity context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();

			convertView = inflator.inflate(R.layout.list_layout, null);
		}
		HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);

		((TextView) convertView.findViewById(R.id.name)).setText((String) data
				.get("Name"));
		TextView texte = (TextView) convertView.findViewById(R.id.name);
		texte.setTextColor(Color.BLACK);
		texte.setPadding(20, 0, 0, 0);
		((ImageView) convertView.findViewById(R.id.icon)).setImageResource(R.drawable.spinner_default_holo_light);
		
		return convertView;
	}

}
