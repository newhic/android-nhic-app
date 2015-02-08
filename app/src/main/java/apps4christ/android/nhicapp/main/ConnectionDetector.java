package apps4christ.android.nhicapp.main;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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
                  for (int i = 0; i < info.length; i++) 
                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
                      {
                          return true;
                      }
 
          }
          return false;
    }
    
    public void showAlertDialog()
    {
    	AlertDialog alertDialog = new AlertDialog.Builder(_context).create();
    	 
        // Setting Dialog Title
        alertDialog.setTitle("No Internet Connection");
 
        // Setting Dialog Message
        alertDialog.setMessage("Please connect to the internet to see content");
         
 
        // Showing Alert Message
        alertDialog.show();
    }
}
