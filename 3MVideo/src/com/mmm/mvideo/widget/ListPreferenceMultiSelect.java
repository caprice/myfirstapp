package com.mmm.mvideo.widget;

import java.util.ArrayList;
import java.util.Iterator;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.mmm.mvideo.R;
import com.mmm.mvideo.common.ApplicationCommon;

/**
 * Strangely, the Android framework does not include a widget which allows
 * multiple selections in Preference Screens. Extend ListPreference is to do
 * this.
 */
public class ListPreferenceMultiSelect extends ListPreference {
	private static final String LOG_TAG = "ListPreferenceMultiSelect";
	// private String checkAllKey = "checkAll";
	private boolean[] mClickedDialogEntryIndices;
	private Drawable mIcon;

	// Constructor
	public ListPreferenceMultiSelect(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mIcon = context.getResources().getDrawable(R.drawable.right);
		// Initialize the array of boolean to the same size as number of entries
		// mClickedDialogEntryIndices = new boolean[getEntries().length];
	}

	@Override
	public void setEntries(CharSequence[] entries) {
		super.setEntries(entries);
		// Initialize the array of boolean to the same size as number of entries
		mClickedDialogEntryIndices = new boolean[entries.length];
	}

	public ListPreferenceMultiSelect(Context context) {
		this(context, null);
	}

	@Override
	protected void onPrepareDialogBuilder(Builder builder) {
		CharSequence[] entries = getEntries();
		CharSequence[] entryValues = getEntryValues();
		Log.d(LOG_TAG, "onPrepareDialogBuilder entries = " + entries.toString() + " entryValues = " + entryValues.toString());
		if (entries == null || entryValues == null || entries.length != entryValues.length) {
			throw new IllegalStateException("ListPreference requires an entries array and an entryValues array which are both the same length");
		}
		restoreCheckedEntries();
		builder.setMultiChoiceItems(entries, mClickedDialogEntryIndices, new DialogInterface.OnMultiChoiceClickListener() {
			public void onClick(DialogInterface dialog, int which, boolean val) {
				// if (isCheckAllValue(which)) {
				// checkAll(dialog, val);
				// }
				mClickedDialogEntryIndices[which] = val;
			}
		});
	}

	/*****
	 * private boolean isCheckAllValue(int which) { final CharSequence[]
	 * entryValues = getEntryValues(); if (checkAllKey != null) { return
	 * entryValues[which].equals(checkAllKey); } return false; }
	 * 
	 * private void checkAll(DialogInterface dialog, boolean val) { ListView lv
	 * = ((AlertDialog) dialog).getListView(); int size = lv.getCount(); for
	 * (int i = 0; i < size; i++) { lv.setItemChecked(i, val);
	 * mClickedDialogEntryIndices[i] = val; } }
	 *******/

	public String[] parseStoredValue(String val) {
		if ("".equals(val)) {
			return null;
		} else {
			return val.split(ApplicationCommon.SEPARATOR);
		}
	}

	private void restoreCheckedEntries() {
		CharSequence[] entryValues = getEntryValues();

		// Explode the string read in sharedpreferences
		String[] vals = parseStoredValue(getValue());

		if (vals != null) {
			for (int j = 0; j < vals.length; j++) {
				String val = vals[j].trim();
				for (int i = 0; i < entryValues.length; i++) {
					CharSequence entry = entryValues[i];
					if (entry.equals(val)) {
						mClickedDialogEntryIndices[i] = true;
						break;
					}
				}
			}
		}
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		// super.onDialogClosed(positiveResult);

		CharSequence[] entryValues = getEntryValues();
		if (positiveResult && entryValues != null) {
			ArrayList<String> values = new ArrayList<String>();
			for (int i = 0; i < entryValues.length; i++) {

				if (mClickedDialogEntryIndices[i] == true) {
					// Don't save the state of check all option - if any
					String val = (String) entryValues[i];
					// if (checkAllKey == null || (val.equals(checkAllKey) ==
					// false)) {
					values.add(val);
					// }
				}
			}

			if (callChangeListener(values)) {
				setValue(join(values, ApplicationCommon.SEPARATOR));
			}
		}
	}

	@Override
	protected void onBindView(final View view) {
		super.onBindView(view);
		final ImageView imageView = (ImageView) view.findViewById(R.id.icon);
		if ((imageView != null) && (this.mIcon != null)) {
			imageView.setImageDrawable(this.mIcon);
		}
	}

	/**
	 * Sets the icon for this Preference with a Drawable.
	 * 
	 * @param icon
	 *            The icon for this Preference
	 */
	public void setIcon(final Drawable icon) {
		if (((icon == null) && (this.mIcon != null)) || ((icon != null) && (!icon.equals(this.mIcon)))) {
			this.mIcon = icon;
			this.notifyChanged();
		}
	}

	public void setIcon(int iconRes) {
		if (R.drawable.right != iconRes) {
			this.mIcon = getContext().getResources().getDrawable(iconRes);
			this.notifyChanged();
		}
	}

	/**
	 * Returns the icon of this Preference.
	 * 
	 * @return The icon.
	 * @see #setIcon(Drawable)
	 */
	public Drawable getIcon() {
		return this.mIcon;
	}

	/**
	 * @param pColl
	 * @param separator
	 * @return
	 */
	protected static String join(Iterable<? extends Object> pColl, String separator) {
		Iterator<? extends Object> oIter;
		if (pColl == null || (!(oIter = pColl.iterator()).hasNext()))
			return "";
		StringBuilder oBuilder = new StringBuilder(String.valueOf(oIter.next()));
		while (oIter.hasNext())
			oBuilder.append(separator).append(oIter.next());
		return oBuilder.toString();
	}

	/**
	 * 
	 * @param straw
	 *            String to be found
	 * @param haystack
	 *            Raw string that can be read direct from preferences
	 * @param separator
	 *            Separator string. If null, static default separator will be
	 *            used
	 * @return boolean True if the straw was found in the haystack
	 */
	public static boolean contains(String straw, String haystack, String separator) {
		if (separator == null) {
			separator = ApplicationCommon.DEFAULT_SEPARATOR;
		}
		String[] vals = haystack.split(separator);
		for (int i = 0; i < vals.length; i++) {
			if (vals[i].equals(straw)) {
				return true;
			}
		}
		return false;
	}
}
