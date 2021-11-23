package org.biblio.adapter.page;

import org.biblio.viewpagertabs.ViewPagerTabProvider;

import android.app.Activity;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class InternationalDetailAdapter extends PagerAdapter implements
		ViewPagerTabProvider {

	protected transient Activity mContext;

	private String[] mData = { "zero", "one", "two", "three", "four", "five",
			"six", "seven" };

	private String[] mTitles = { "kategorien", "vorgestellt",
			"top kostenpflichtig", "top kostenlos", "erfolgreichste",
			"neu kostenpflichtig", "top kostenlos - neu", "trends" };

	private String[] mTitlesNew = { "this", "is", "really", "awesome" };

	private boolean mUseNewData = false;

	public InternationalDetailAdapter(Activity context) {
		mContext = context;
	}

	@Override
	public int getCount() {
		return mData.length;
	}

	public void changeData() {
		mUseNewData = true;
	}

	@Override
	public Object instantiateItem(View container, int position) {

		RelativeLayout v = new RelativeLayout(mContext);

		TextView t = new TextView(mContext);
		t.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		t.setText(mData[position]);
		t.setTextSize(30);
		t.setGravity(Gravity.CENTER);

		v.addView(t);

		((ViewPager) container).addView(v, 0);

		return v;
	}

	@Override
	public void destroyItem(View container, int position, Object view) {
		((ViewPager) container).removeView((View) view);
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

}
