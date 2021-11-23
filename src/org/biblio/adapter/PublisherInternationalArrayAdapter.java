package org.biblio.adapter;

import java.util.List;

import org.biblio.R;
import org.biblio.mapping.viaf.CoAuthors;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PublisherInternationalArrayAdapter extends ArrayAdapter<CoAuthors> {

	private List<CoAuthors> list;
	private final Activity context;
	private final String TAG = getClass().getName();

	public PublisherInternationalArrayAdapter(Activity context,
			List<CoAuthors> list) {
		super(context, R.layout.affichageitem_internationnal, list);
		this.context = context;
		this.list = list;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		return null;
	}

	static class ViewHolder {
		ImageView imgItem;
		TextView titre;

		ImageView flag;
		TextView description;
		ImageView flag2;
		TextView description2;
	}
}
