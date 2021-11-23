package org.biblio.adapter;

import java.util.List;

import org.biblio.R;
import org.biblio.mapping.sudoc.search.SearchItem;
import org.biblio.sqli.Livre;
import org.biblio.sqli.dao.LivresBDD;

import android.app.Activity;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Adapter on SearchSudoc Result .
 * 
 * @author Artaud antoine
 * 
 */
public class InteractiveArrayAdapter extends ArrayAdapter<SearchItem> {

	private List<SearchItem> list;
	private final Activity context;
	private final String TAG = getClass().getName();

	public InteractiveArrayAdapter(Activity context, List<SearchItem> list) {
		super(context, R.layout.affichageitem, list);
		this.context = context;
		this.list = list;
		Log.d(TAG, " [Bdd] Conncetion ");

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.affichageitem, null);
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.titre = (TextView) view.findViewById(R.id.titre);
			viewHolder.description = (TextView) view
					.findViewById(R.id.description);
			viewHolder.imgItem = (ImageView) view.findViewById(R.id.imgItem);
			viewHolder.check = (CheckBox) view.findViewById(R.id.check);
			viewHolder.check.setChecked(false);
			view.setTag(viewHolder);
			viewHolder.check.setTag(list.get(position));

		} else {
			view = convertView;
			((ViewHolder) view.getTag()).check.setTag(list.get(position));
		}
		final ViewHolder holder = (ViewHolder) view.getTag();
		holder.titre.setText(list.get(position).getRecordType() + " - "
				+ list.get(position).getPpn());
		holder.description.setText(list.get(position).getAuteur());
		if ("personne".equalsIgnoreCase(list.get(position).getRecordType())) {
			holder.imgItem.setImageResource(R.drawable.info_user);
		} else {
			holder.imgItem.setImageResource(R.drawable.book2);
		}

		// holder.imgItem.setBackgroundResource(R.drawable.book2);
		holder.check.setChecked(list.get(position).isSelected());

		holder.check
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton checkView,
							boolean isChecked) {
						SearchItem element = (SearchItem) holder.check.getTag();
						element.setSelected(checkView.isChecked());
						// Création d'une instance de ma classe LivresBDD
						LivresBDD livreBdd = new LivresBDD(context);
						livreBdd.open();// On ouvre la base de données pour
						// écrire dedans
						Log.d(TAG + " Case 2 : ",
								" Data[2] : " + list.get(position).getPpn()
										+ " "
										+ list.get(position).getRecordType()
										+ " " + list.get(position).getAuteur());
						// Action on Checked Box
						Livre livre = new Livre(list.get(position).getPpn(),
								list.get(position).getRecordType(), list.get(
										position).getRecordType()
										+ " - " + list.get(position).getPpn(),
								list.get(position).getAuteur(), 1);
						try {
							livreBdd.insertLivre(livre);
						} catch (SQLiteConstraintException e) {
							Log.w(getClass().getName(),
									"Constraintes execption or records already exist");
						}
						Log.d(TAG,
								"[BDD-Searched Term] -> Insert record Livre in BDD");
						livreBdd.close();
					}
				});
		// Log.d(TAG, "[Bdd] Fermeture BDD");
		return view;
	}

	static class ViewHolder {
		TextView titre;
		TextView description;
		ImageView imgItem;
		ImageView arraow_right;
		CheckBox check;
	}
}