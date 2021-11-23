package org.biblio;

import org.biblio.help.HelpAdapter;
import org.biblio.viewpagertabs.ViewPagerTabs;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class InternationalDetailActivity extends Activity {
	private ViewPager mPager;
	private ViewPagerTabs mTabs;
	private HelpAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sudoc_help_layout);

		mAdapter = new HelpAdapter(this);

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		mTabs = (ViewPagerTabs) findViewById(R.id.tabs);
		mTabs.setViewPager(mPager);

		/*((Button) findViewById(R.id.style))
				.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
						mTabs.setBackgroundColor(0x00FFFFFF);
						mTabs.setBackgroundColorPressed(0x33333333);
						mTabs.setTextColor(0x44A80000);
						mTabs.setTextColorCenter(0xFFA80000);
						mTabs.setLineColorCenter(0xFFA80000);
						mTabs.setLineHeight(5);
						mTabs.setTextSize(22);
						mTabs.setTabPadding(5, 1, 5, 10);
						mTabs.setOutsideOffset(200);

						// mTabs.refreshTitles();

						mAdapter.changeData();
						mTabs.notifyDatasetChanged();

						findViewById(R.id.colorline).setBackgroundColor(
								0xFFA80000);

						v.setVisibility(View.GONE);
					}
				});*/

	}
}
