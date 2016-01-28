package com.arpaul.movieapp.Utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Description Class : Checking Network Connections
 * @author Neeraj
 *
 */
public class NetworkUtility 
{
	/**
	 * Method to check Network Connections 
	 * @param context
	 * @return boolean value
	 */
	public static boolean isConnectionAvailable(Context context) {
		if(isWifiConnected(context))
			return true;
		else if(isNetworkConnectionAvailable(context))
			return true;
		else
			return false;
	}

	private static boolean isNetworkConnectionAvailable(Context context)
	{
		boolean isNetworkConnectionAvailable = false;
		
		ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE/*"connectivity"*/);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		
		if(activeNetworkInfo != null) 
		{
		    isNetworkConnectionAvailable = activeNetworkInfo.getState() == NetworkInfo.State.CONNECTED;
		}
		return isNetworkConnectionAvailable;
	}

	private static boolean isWifiConnected(Context context)
	{
		boolean isNetworkConnectionAvailable = false;
		ConnectivityManager connManager = (ConnectivityManager)context. getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (mWifi.isConnected()) {
			isNetworkConnectionAvailable = true;
		}
		return isNetworkConnectionAvailable;
	}
	
	
	
}
