package org.biblio.help;

import org.biblio.R;
import org.biblio.viewpagertabs.ViewPagerTabProvider;

import android.app.Activity;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class HelpAdapter extends PagerAdapter implements ViewPagerTabProvider {
	ViewHolder viewHolder;
	final String TAG = getClass().getSimpleName();
	protected transient Activity mContext;

	private String[] mData = { "zero", "one", "two", "three", "four", "five",
			"six", "seven", "eight", "nine", "ten" };

	private String[] mTitles = new String[8];

	private String[] mTitlesNew = { "this", "is", "really", "awesome" };

	private boolean mUseNewData = false;

	public HelpAdapter(Activity context) {
		mContext = context;
		mTitles[0] = mContext.getResources().getString(R.string.AideCategory0);
		mTitles[1] = mContext.getResources().getString(R.string.AideCategory1);
		mTitles[2] = mContext.getResources().getString(R.string.AideCategory2);
		mTitles[3] = mContext.getResources().getString(R.string.AideCategory3);
		mTitles[4] = mContext.getResources().getString(R.string.AideCategory4);
		mTitles[5] = mContext.getResources().getString(R.string.AideCategory5);
		mTitles[6] = mContext.getResources().getString(R.string.AideCategory6);
		mTitles[7] = mContext.getResources().getString(R.string.AideCategory7);
	}

	@Override
	public int getCount() {
		return mData.length;
	}

	public void changeData() {
		mUseNewData = true;
	}

	@Override
	public Object instantiateItem(View convertView, int position) {
		View view = new View(mContext);

		LayoutInflater inflator = mContext.getLayoutInflater();
		view = inflator.inflate(R.layout.help_layout, null);

		viewHolder = new ViewHolder();
		viewHolder.textDescription = (TextView) view
				.findViewById(R.id.helptextDescription);

		viewHolder.textDescription2 = (TextView) view
				.findViewById(R.id.helptextDescription2);

		viewHolder.textDescription3 = (TextView) view
				.findViewById(R.id.helptextDescription3);

		viewHolder.textDescription4 = (TextView) view
				.findViewById(R.id.helptextDescription4);

		viewHolder.textDescription5 = (TextView) view
				.findViewById(R.id.helptextDescription5);

		viewHolder.imageViewHelp = (ImageView) view
				.findViewById(R.id.helpimageView);

		viewHolder.imageViewHelpDown = (ImageView) view
				.findViewById(R.id.helpimageViewDown);

		viewHolder.imageViewHelpDown2 = (ImageView) view
				.findViewById(R.id.helpimageViewDown2);

		viewHolder.imageViewHelpDown3 = (ImageView) view
				.findViewById(R.id.helpimageViewDown3);

		viewHolder.imageViewHelpDown4 = (ImageView) view
				.findViewById(R.id.helpimageViewDown4);

		viewHolder.imageViewHelpDown5 = (ImageView) view
				.findViewById(R.id.helpimageViewDown5);

		// Aide Sudoc
		if (position == 0) {
			viewHolder.imageViewHelp.setImageResource(R.drawable.sudoc);
			viewHolder.textDescription
					.setText(R.string.AideAboutSudocDescription);
			viewHolder.textDescription3
					.setText(R.string.AideAboutSudocDescription3);
		}
		// Aide But de lapplication
		if (position == 1) {
			viewHolder.textDescription.setText(R.string.AideButApplication);
			viewHolder.imageViewHelp.setImageResource(R.drawable.books_search);
		}
		// Aide recherche tous
		if (position == 2) {
			viewHolder.textDescription.setText(R.string.AideRechercheTous);
			viewHolder.imageViewHelpDown
					.setImageResource(R.drawable.aiderecherchetous);
			viewHolder.textDescription2.setText(R.string.AideRechercheDetails);
			viewHolder.imageViewHelpDown2
					.setImageResource(R.drawable.aiderecherchedetails);

			viewHolder.textDescription3
					.setText(R.string.AideRechercheDetailsBibliotheque);
			viewHolder.imageViewHelpDown3
					.setImageResource(R.drawable.aidelocation);
			viewHolder.textDescription4
					.setText(R.string.AideRechercheDetailsBibliothequeDispo);

			viewHolder.imageViewHelpDown4
					.setImageResource(R.drawable.aiderecherchebibli);
		}

		// Aide recherche Auteur
		if (position == 3) {
			viewHolder.textDescription = (TextView) view
					.findViewById(R.id.helptextDescription);
			viewHolder.imageViewHelpDown = (ImageView) view
					.findViewById(R.id.helpimageViewDown);
			viewHolder.imageViewHelpDown2 = (ImageView) view
					.findViewById(R.id.helpimageViewDown2);

			viewHolder.textDescription.setText(R.string.AideRechercheAuteur);
			viewHolder.imageViewHelpDown
					.setImageResource(R.drawable.aiderechercheauteur);
			viewHolder.textDescription2.setText(R.string.AideRechercheAuteur2);
			viewHolder.imageViewHelpDown2
					.setImageResource(R.drawable.aide_bibliographie);
		}
		// /Bibli municipale
		if (position == 5) {
			viewHolder.imageViewHelpDown
					.setImageResource(R.drawable.aide_municipale);

			viewHolder.textDescription
					.setText(R.string.AideBiblioMunicipaleDescription);
			viewHolder.textDescription2
					.setText(R.string.AideBiblioMunicipaleDetails);
			viewHolder.imageViewHelpDown2
					.setImageResource(R.drawable.aide_municipale_details);
			viewHolder.textDescription3
					.setText(R.string.AideBiblioMunicipaleLocation);
			viewHolder.imageViewHelpDown3
					.setImageResource(R.drawable.aide_municipale_location);
		}
		// autre type de recherche
		if (position == 4) {
			viewHolder.imageViewHelp.setImageResource(R.drawable.books_search);
			viewHolder.textDescription
					.setText(R.string.AideAutreTypedeRecherche);
		}
		// A propos
		if (position == 6) {
			viewHolder.textDescription.setText(R.string.AidePropos);
			viewHolder.imageViewHelp.setImageResource(R.drawable.abes);

			viewHolder.textDescription2.setText(R.string.AidePropos2);
			viewHolder.imageViewHelpDown2.setImageResource(R.drawable.star2);

			viewHolder.textDescription3.setText(R.string.AidePropos3);
			viewHolder.imageViewHelpDown3.setImageResource(R.drawable.calames);

			viewHolder.textDescription4.setText(R.string.AidePropos4);
			viewHolder.imageViewHelpDown4.setImageResource(R.drawable.theses);

			viewHolder.textDescription4.setText(R.string.AidePropos5);
			viewHolder.imageViewHelpDown4
					.setImageResource(R.drawable.worldcat_icon);

		}
		// remerciements
		if (position == 7) {
			viewHolder.imageViewHelp.setImageResource(R.drawable.books_search);
			viewHolder.textDescription.setText(R.string.AideRemerciements);
		}

		view.setTag(viewHolder);

		((ViewPager) convertView).addView(view, 0);

		return view;
	}

	/*
	 * @Override public void destroyItem(View container, int position, Object
	 * view) { ((ViewPager) container).removeView((View) view); }
	 */

	@Override
	public void destroyItem(View collection, int position, Object o) {
		View view = (View) o;
		Log.d(TAG, "destroying view");
		((ViewPager) collection).removeView(view);
		view = null;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((View) object);
	}

	@Override
	public void finishUpdate(View container) {
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View container) {
	}

	public String getTitle(int position) {

		final int len = mUseNewData ? mTitlesNew.length : mTitles.length;

		if (position >= 0 && position < len)
			return mUseNewData ? mTitlesNew[position] : mTitles[position]
					.toUpperCase();
		else
			return "";

	}

	static class ViewHolder {

		TextView textDescription;
		TextView textDescription2;
		TextView textDescription3;
		TextView textDescription4;
		TextView textDescription5;

		ImageView imageViewHelp;
		ImageView imageViewHelpDown;
		ImageView imageViewHelpDown2;
		ImageView imageViewHelpDown3;
		ImageView imageViewHelpDown4;
		ImageView imageViewHelpDown5;
	}
}
