package org.biblio.adapter;

import java.util.List;

import mapping.municipale.paris.EntryRow;

import org.biblio.R;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MunicipaleArrayLocationAdapter extends ArrayAdapter<EntryRow> {

	List<EntryRow> list;
	private final Activity context;
	private final String TAG = getClass().getSimpleName();

	public MunicipaleArrayLocationAdapter(Activity context, List<EntryRow> list) {
		super(context, R.layout.search_municipale_location_item, list);
		this.context = context;
		this.list = list;
		Log.i(TAG, "Beginning ArrayAdpter");
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null) {
			LayoutInflater inflator = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflator.inflate(
					R.layout.search_municipale_location_item, null);
			holder = new ViewHolder();
			holder.titre = (TextView) convertView.findViewById(R.id.titreMuni);
			holder.titrepublie = (TextView) convertView
					.findViewById(R.id.titrePublie);
			holder.auteur = (TextView) convertView.findViewById(R.id.auteur);
			holder.editeur = (TextView) convertView.findViewById(R.id.editeur);

			holder.format = (TextView) convertView.findViewById(R.id.format);
			holder.collection = (TextView) convertView
					.findViewById(R.id.collection);

			holder.contenu = (TextView) convertView.findViewById(R.id.contenu);
			holder.typeDeDocument = (TextView) convertView
					.findViewById(R.id.typededocument);

			holder.hiddenTitle = (TextView) convertView
					.findViewById(R.id.hiddenTitle);
			holder.hiddenCreator = (TextView) convertView
					.findViewById(R.id.hiddenCreator);

			holder.autreInformation = (TextView) convertView
					.findViewById(R.id.autreInformation);

			if (list.get(position).getTitle() != null) {
				holder.titre.setText("Titre : \n"
						+ list.get(position).getTitle());
			}
			if (list.get(position).getPublie() != null) {
				holder.titrepublie.setText("Publie : \n"
						+ list.get(position).getPublie());
			}
			if (list.get(position).getAuteur() != null) {
				holder.auteur.setText(list.get(position).getAuteur()
						.replaceAll("¤", " "));
			}
			if (list.get(position).getFormat() != null) {
				holder.format.setText(list.get(position).getFormat() + " \n");
			}
			if (list.get(position).getCollection() != null) {
				holder.collection.setText(list.get(position).getCollection()
						+ "\n");
			}
			if (list.get(position).getContenu() != null) {
				holder.contenu.setText(list.get(position).getContenu() + "\n");
			}
			if (list.get(position).getTypeDeDocument() != null) {
				holder.typeDeDocument.setText(list.get(position)
						.getTypeDeDocument() + "\n");
			}
			if (list.get(position).getHiddenTitle() != null) {
				holder.hiddenTitle.setText(list.get(position).getHiddenTitle()
						+ "\n");
			}
			if (list.get(position).getHiddenCreator() != null) {
				holder.hiddenCreator.setText(list.get(position)
						.getHiddenCreator() + "\n");
			}
			if (list.get(position).getAutreInformation() != null) {
				holder.autreInformation.setText("\n "
						+ list.get(position).getAutreInformation());
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	static class ViewHolder {
		TextView titre;
		TextView titrepublie;
		TextView auteur;
		TextView format;
		TextView collection;
		TextView contenu;
		TextView typeDeDocument;
		TextView hiddenTitle;
		TextView hiddenCreator;
		TextView editeur;
		TextView autreInformation;

	}
}
