package apps4christ.android.nhicapp.prefsfrag;

import apps4christ.android.nhicapp.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AboutFragment extends Fragment {
	private static View aboutFragView;
	
	public AboutFragment() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.about_page, container,
				false);

		aboutFragView = rootView;

		return rootView;
	}
	
}
