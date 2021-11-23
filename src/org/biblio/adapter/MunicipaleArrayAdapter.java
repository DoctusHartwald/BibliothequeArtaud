package org.biblio.adapter;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mapping.municipale.paris.EntryRow;

import org.biblio.R;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MunicipaleArrayAdapter extends ArrayAdapter<EntryRow> {
	List<EntryRow> list;
	private final Activity context;
	private final String TAG = getClass().getSimpleName();

	public MunicipaleArrayAdapter(Activity context, List<EntryRow> list) {
		super(context, R.layout.search_municipale_item, list);
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
			convertView = inflator.inflate(R.layout.search_municipale_item,
					null);
			holder = new ViewHolder();

			holder.titre = (TextView) convertView.findViewById(R.id.titreMuni);
			holder.hiddenTitle = (TextView) convertView
					.findViewById(R.id.hiddenTitle);
			holder.hiddenCreator = (TextView) convertView
					.findViewById(R.id.hiddenCreator);
			holder.auteur = (TextView) convertView.findViewById(R.id.auteur);
			holder.editeur = (TextView) convertView.findViewById(R.id.editeur);
			holder.itemMunicipaleLayout = (LinearLayout) convertView.findViewById(R.id.itemMunicipaleLayout);
			
			Pattern p = Pattern.compile("^([0-9]*).*");
			Matcher m = null ;
			if(list.get(position).getTitle()!=null)
			m = p.matcher(list.get(position).getTitle());
			int flag = 0 ;
			// cas ou titre a des valeurs numeric => prendre hidden Title
			if (m!=null && m.find()) {
				if (null != list.get(position).getHiddenTitle()) {
					holder.hiddenTitle.setText(list.get(position)
							.getHiddenTitle());
					holder.auteur.setText(list.get(position).getAuteur());
					holder.editeur.setText(list.get(position).getEditeur());
					flag=1;
				}

			} else {
				holder.titre.setText(list.get(position).getTitle());
				holder.hiddenCreator.setText(list.get(position)
						.getHiddenCreator());
				holder.auteur.setText(list.get(position).getAuteur());
				holder.editeur.setText(list.get(position).getEditeur());
				flag=1;
			}
			if(flag==0){
				holder.itemMunicipaleLayout.setVisibility(LinearLayout.INVISIBLE);
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		return convertView;
	}

	static class ViewHolder {
		TextView titre;
		TextView hiddenTitle;
		TextView hiddenCreator;
		TextView auteur;
		TextView editeur;
		
		LinearLayout itemMunicipaleLayout;

	}
}
