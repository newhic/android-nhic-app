package apps4christ.android.nhicapp.main;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import apps4christ.android.nhicapp.R;

public class ConnectionDetector {
	private Context _context;
    
    public ConnectionDetector(Context context){
        this._context = context;
    }
 
    public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
          if (connectivity != null) 
          {
              NetworkInfo[] info = connectivity.getAllNetworkInfo();
              if (info != null) 
                  for (NetworkInfo i : info)
                      if (i.getState() == NetworkInfo.State.CONNECTED)
                      {
                          return true;
                      }
 
          }
          return false;
    }
    
    public void showAlertDialog()
    {
    	new AlertDialog.Builder(_context)
				.setTitle(R.string.noInternetConnectionTitle)
				.setMessage(R.string.pleaseConnectToInternetMessage)
				.show();
    }
}
