package apps4christ.android.nhicapp.prefsfrag;

import apps4christ.android.nhicapp.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LicenseFragment extends Fragment {

	private static View licenseFragView;
	public LicenseFragment() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.eula_page, container,
				false);

		licenseFragView = rootView;

		return rootView;
	}
	
}
