package org.biblio.adapter;

import java.util.List;

import org.biblio.R;
import org.biblio.record.Record;
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

public class SudocRecordsAdapter extends ArrayAdapter<Record> {

	private List<Record> list;
	private final Activity context;
	private final String TAG = getClass().getName();

	public SudocRecordsAdapter(Activity context, List<Record> list) {
		super(context, R.layout.affichageitem, list);
		this.context = context;
		this.list = list;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			// LayoutInflater inflator = context.getLayoutInflater();
			// LayoutInflater inflator = (LayoutInflater) context
			// .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			LayoutInflater inflator = LayoutInflater.from(context);

			view = inflator.inflate(R.layout.affichageitem, null);
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.titre = (TextView) view.findViewById(R.id.titre);
			viewHolder.titre.setTextSize(14);
			
			viewHolder.description = (TextView) view
					.findViewById(R.id.description);
			viewHolder.description.setTextSize(12);
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
		if (list.get(position).getLines() != null
				&& list.get(position).getLines().size() > 0)
			holder.titre.setText(list.get(position).getLines().get(0));
		holder.description.setText(list.get(position).getLine());
		
		// change d'image en fonction du type de support
		int flag=0;
		if("Livre".equals(list.get(position).getTypeSupport())){
			holder.imgItem.setImageResource(R.drawable.book2);
			flag=1;
		}
		if("Article".equals(list.get(position).getTypeSupport())){
			holder.imgItem.setImageResource(R.drawable.pen);
			flag=1;
		}
		if("These".equals(list.get(position).getTypeSupport())){
			holder.imgItem.setImageResource(R.drawable.graduation2);
			flag=1;
		}
		if("Electronique".equals(list.get(position).getTypeSupport())){
			holder.imgItem.setImageResource(R.drawable.electronic_network);
			flag=1;
		}
		if("Periodique".equals(list.get(position).getTypeSupport())){
			holder.imgItem.setImageResource(R.drawable.periodique);
			flag=1;
		}
		if("Partitions".equals(list.get(position).getTypeSupport())){
			holder.imgItem.setImageResource(R.drawable.musique_bleu);
			flag=1;
		}
		if(flag==0){
			holder.imgItem.setImageResource(R.drawable.book_others);
		}
		
		
		

		// holder.imgItem.setBackgroundResource(R.drawable.book2);
		holder.check.setChecked(list.get(position).isSelected());

		holder.check
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton checkView,
							boolean isChecked) {
						Record element = (Record) holder.check.getTag();
						element.setSelected(checkView.isChecked());
						// Création d'une instance de ma classe LivresBDD
						LivresBDD livreBdd = new LivresBDD(context);
						livreBdd.open();// On ouvre la base de données pour

						// Action on Checked Box
						Livre livre = new Livre(list.get(position).getPpn(),
												list.get(position).getLines().get(0), //titre
												list.get(position).getLine(), //description
												list.get(position).getTypeSupport(), //auteur
												1);
						//auteur remplacer par type de support 
						
						try {
							livreBdd.insertLivre(livre);
						} catch (SQLiteConstraintException e) {
							Log.w(getClass().getName(),
									"Constraintes execption or records already exist");
						}

						livreBdd.close();
					}
				});
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
