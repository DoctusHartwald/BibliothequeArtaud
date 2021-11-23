package org.biblio.adapter;

import java.util.List;

import org.biblio.R;
import org.biblio.mapping.sudoc.record.Document;
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
 * 
 * @author artaud
 * 
 */
public class BibliographieArrayAdapter extends ArrayAdapter<Document> {

	private List<Document> list;
	private final Activity context;
	private final String TAG = getClass().getName();

	public BibliographieArrayAdapter(Activity context, List<Document> list) {
		super(context, R.layout.affichageitem, list);
		this.context = context;
		this.list = list;
		Log.d(TAG, " ---- [Bdd] Connection ---- ");
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
		holder.titre.setText(list.get(position).getmRoleName() + " - "
				+ list.get(position).getmPpn());
		holder.description.setText(list.get(position).getCitation());

		// change d'image en fonction du Role de Auteur
		int flag = 0;
		if (list.get(position).getmRoleName().trim().startsWith("Auteur")) {
			holder.imgItem.setImageResource(R.drawable.pen);
			flag = 1;
		}

		if (list.get(position).getmRoleName().contains("Préfacier")) {
			holder.imgItem.setImageResource(R.drawable.user_prefacier);
			flag = 1;
		}
		if (list.get(position).getmRoleName().matches(".*Collaborateur.*")) {
			holder.imgItem.setImageResource(R.drawable.user_collaborate);
			flag = 1;
		}
		if (list.get(position).getmRoleName().matches(".*Editeur.*")) {
			holder.imgItem.setImageResource(R.drawable.user_editeur);
			flag = 1;
		}
		if (list.get(position).getmRoleName().matches(".*Directeur.*")) {
			holder.imgItem.setImageResource(R.drawable.user_directeur);
			flag = 1;
		}
		if (list.get(position).getmRoleName().matches(".*Traducteur.*")) {
			holder.imgItem.setImageResource(R.drawable.user_traducteur);
			flag = 1;
		}
		if (flag == 0) {
			holder.imgItem.setImageResource(R.drawable.book2);
		}

		// holder.imgItem.setBackgroundResource(R.drawable.book2);
		holder.check.setChecked(list.get(position).isSelected());

		holder.check
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton checkView,
							boolean isChecked) {
						Document element = (Document) holder.check.getTag();
						element.setSelected(checkView.isChecked());
						// Création d'une instance de ma classe LivresBDD
						LivresBDD livreBdd = new LivresBDD(context);
						livreBdd.open();// On ouvre la base de données pour

						// Action on Checked Box
						Livre livre = new Livre(list.get(position).getmPpn(),
								list.get(position).getmRoleName(), list.get(
										position).getCitation(), list.get(
										position).getmAuteur(), 1);
						try {
							livreBdd.insertLivre(livre);
						} catch (SQLiteConstraintException e) {
							Log.i(getClass().getName(),
									"Constraintes execption or records already exist");
						}
						Log.d(TAG,
								"[BDD-Bibliographie] -> Insert record Livre in BDD");
						livreBdd.close();
					}
				});
		Log.d(TAG, "[Bdd] Fermeture BDD");
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