package ir.mhn.textapicache;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Random;

class TextAPICacheTools {
    static boolean isInternetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable());
    }

    static String convertUrlIntoCacheName(String url, Context context) {
        if (TextAPICachePreferencesManager.contains(context, url)) {
            return TextAPICachePreferencesManager.getString(context, url);
        } else {
            String cacheName = new Random().nextInt() + "";
            TextAPICachePreferencesManager.putString(context, url, cacheName);
            return cacheName;
        }
    }
}