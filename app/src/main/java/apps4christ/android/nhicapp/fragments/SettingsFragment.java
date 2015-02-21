package apps4christ.android.nhicapp.fragments;

import apps4christ.android.nhicapp.prefsfrag.AboutFragment;
import apps4christ.android.nhicapp.prefsfrag.AcknFragment;
import apps4christ.android.nhicapp.prefsfrag.LicenseFragment;
import apps4christ.android.nhicapp.R;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {

	public SettingsFragment() {
	}

	public Context context;
	public Fragment acknFrag = new AcknFragment();
	public Fragment aboutFrag = new AboutFragment();
	public Fragment licenseFrag = new LicenseFragment();

	@Override
	public void onCreate(Bundle savedInstanceState) {

		Preference aboutPref;
		Preference acknPref;
		Preference eulaPref;

		super.onCreate(savedInstanceState);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);

		aboutPref = (Preference) findPreference("aboutPrefKey");
		acknPref = (Preference) findPreference("ackPrefKey");
		eulaPref = (Preference) findPreference("eulaPrefKey");

		aboutPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction ft = fragmentManager.beginTransaction();
				ft.replace(R.id.frame_container, aboutFrag);
				ft.addToBackStack(null);
				ft.commit();

				return true;
			}
		});

		acknPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction ft = fragmentManager.beginTransaction();
				ft.replace(R.id.frame_container, acknFrag);
				ft.addToBackStack(null);
				ft.commit();

				return true;
			}
		});

		eulaPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction ft = fragmentManager.beginTransaction();
				ft.replace(R.id.frame_container, licenseFrag);
				ft.addToBackStack(null);
				ft.commit();

				return true;
			}
		});

		CreateVersionString();
	}

	// Create the NHIC App Version string
	public void CreateVersionString() {
		EditTextPreference versionPref;
		String version;
		String qualifier;

		versionPref = (EditTextPreference) findPreference("version");
		version = "";
		qualifier = "";
		try {
			version = getActivity().getPackageManager().getPackageInfo(
					getActivity().getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		versionPref.setTitle(getString(R.string.prefVersion) + ": " + version + " "
				+ qualifier);
	}

}
