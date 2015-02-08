package apps4christ.android.nhicapp.prefsfrag;

import apps4christ.android.nhicapp.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AcknFragment extends Fragment {

	private static View acknFragView;

	public AcknFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.ackn_page, container, false);

		acknFragView = rootView;

		return rootView;
	}

}