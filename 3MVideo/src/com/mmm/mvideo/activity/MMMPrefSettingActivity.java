package com.mmm.mvideo.activity;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;

import com.mmm.mvideo.R;
import com.mmm.mvideo.activity.fragment.NavigationFragment.FragmentParameterKey;
import com.mmm.mvideo.business.entity.MMMVideoItem;
import com.mmm.mvideo.common.ApplicationCommon;
import com.mmm.mvideo.widget.ListPreferenceMultiSelect;

/**
 * @author Eric Liu
 * 
 */
public class MMMPrefSettingActivity extends PreferenceActivity implements OnPreferenceChangeListener {
	Dialog dialog;
	CharSequence[] entries = null;
	CharSequence[] entryValues = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mmm.mvideo.activity.MMMActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setEntriesAndValues();
		addPreferencesFromResource(R.xml.preferences);
		dialog = new Dialog(this);
		ListPreferenceMultiSelect pre = (ListPreferenceMultiSelect) findPreference(ApplicationCommon.TAB_KEY_IN_SHARED_PREF);
		pre.setEntries(this.entries);
		pre.setEntryValues(this.entryValues);
		pre.setOnPreferenceChangeListener(this);

	}

	/**
	 * init entries and values
	 */
	private void setEntriesAndValues() {
		Bundle args = getIntent().getExtras();
		ArrayList<MMMVideoItem> items = (ArrayList<MMMVideoItem>) args.get(FragmentParameterKey.NAV_ITEMS);
		if (items != null) {
			entries = new CharSequence[items.size()];
			entryValues = new CharSequence[items.size()];
			for (int i = 0; i < items.size(); i++) {
				entries[i] = items.get(i).getTitle();
				entryValues[i] = items.get(i).getTitle();
			}
		}
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {

		if (preference.getKey().equals(ApplicationCommon.TAB_KEY_IN_SHARED_PREF)) {
			Intent intent = new Intent(this, HomeActivity.class);
			intent.setAction("HomeActivity");
			startActivity(intent);
			finish();
			// true indicate that not allow to change
			return true;
		} else {
			// true indicate that not allow to change
			return false;
		}
	}

}
