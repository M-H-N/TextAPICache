package ir.mhn.textapicache;

import android.content.Context;
import android.content.SharedPreferences;

public class TextAPICachePreferencesManager {

    private static final String PREFERENCES_NAME = "TEXT_API_CACHE_PREFS";

    static void putString(Context context, String key, String value) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    static String getString(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }

    static void remove(Context context, String key) {
        if (contains(context, key)) {
            SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.remove(key);
            editor.commit();
        }
    }

    static boolean contains(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return pref.contains(key);
    }

    public static void removeAllPreferences(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
}
