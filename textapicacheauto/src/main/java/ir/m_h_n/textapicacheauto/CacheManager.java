package ir.m_h_n.textapicacheauto;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class CacheManager {
    private static final String TAG = CacheManager.class.getSimpleName();

    private static final int BUFFER_SIZE = 1024;
    private static final long EXPIRE_LIMIT = 300000;
    private static final String CACHE_DIRECTORY_NAME = "TextAPICache";
    private static final String TIME_POSTFIX = "<LONG-TIME-END/>";


    /**
     * This method tries to load the previously save data from file specified with <i>cacheName</i>
     *
     * @param cacheName The name of the cache(not it's path or uri)
     * @param context   The context of your program
     * @return Returns the saved data to the cache if exist and returns <b>null</b> if error occurs or cache don't exits
     */
    public static String loadCache(String cacheName, Context context) {
        return loadCache(generateCacheFile(cacheName, context));
    }

    /**
     * This method tries to save the given data to file specified with <i>cacheName</i>
     *
     * @param cacheName The name of the cache(not it's path or uri)
     * @param data      The data(string data) which needs to be save into the file
     * @param context   The context of your program
     * @return returns <b>true</b> if the save process is successful and <b>false</b> if not
     */
    public static boolean saveCache(String cacheName, String data, Context context) {
        return saveCache(data, generateCacheFile(cacheName, context));
    }


    /**
     * This method specifies if the cache file specified with <i>cacheName</i> exists or not
     *
     * @param cacheName The name of the cache(not it's path or uri)
     * @param context   The context of your program
     * @return returns <b>true</b> if the cache file specified with <i>cacheName</i> exits and <b>false</b> if doesn't
     */
    public static boolean doesCacheExists(String cacheName, Context context) {
        return doesCacheExists(generateCacheFile(cacheName, context));
    }

    public static boolean deleteCache(String cacheName, Context context) {
        File file = generateCacheFile(cacheName, context);
        if (!doesCacheExists(file)) return true;
        return file.delete();
    }

    public static boolean deleteWholeCache(Context context) {
        File cacheDirectory = getCacheDirectory(context);
        if (cacheDirectory == null) return false;
        boolean result = true;
        for (File file : cacheDirectory.listFiles()) {
            if (!file.delete()) result = false;
        }
        return result;
    }

    public static String loadCacheIfValid(String cacheName, Context context) {
        return loadCacheIfValid(cacheName, context, EXPIRE_LIMIT);
    }

    public static String loadCacheIfValid(String cacheName, Context context, long limit) {
        try {
            File cacheFile = generateCacheFile(cacheName, context);
            if (!doesCacheExists(cacheFile)) return null;
            String data = loadCache(cacheFile);
            if (data == null) return null;
            if (!data.contains(TIME_POSTFIX)) return null;
            long fileTime = Long.parseLong(data.substring(0, data.indexOf(TIME_POSTFIX)));
            long currentTime = new Date().getTime();
            if (Math.abs(currentTime - fileTime) <= limit)
                return data;
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isCacheValid(String cacheName, Context context, long limit) {
        try {
            Log.d(TAG, "isCacheValid: flag-01");
            File cacheFile = generateCacheFile(cacheName, context);
            if (!doesCacheExists(cacheFile)) return false;
            Log.d(TAG, "isCacheValid: flag-02");
            String data = loadCache(cacheFile);
            if (data == null) return false;
            Log.d(TAG, "isCacheValid: flag-03");
            if (!data.contains(TIME_POSTFIX)) return false;
            Log.d(TAG, "isCacheValid: flag-04");
            long fileTime = Long.parseLong(data.substring(0, data.indexOf(TIME_POSTFIX)));
            long currentTime = new Date().getTime();
            Log.d(TAG, "isCacheValid: flag-05-fileTime:" + fileTime);
            Log.d(TAG, "isCacheValid: flag-05-currentTime:" + currentTime);
            return (Math.abs(currentTime - fileTime) <= limit);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isCacheValid(String cacheName, Context context) {
        return isCacheValid(cacheName, context, EXPIRE_LIMIT);
    }

    public static boolean isCacheExpired(String cacheName, Context context, long limit) {
        return !isCacheValid(cacheName, context, limit);
    }

    public static boolean isCacheExpired(String cacheName, Context context) {
        return !isCacheValid(cacheName, context, EXPIRE_LIMIT);
    }

    private static File generateCacheFile(String cacheName, Context context) {
        if (context == null)
            throw new NullPointerException("The context cannot be null!");
        if (cacheName == null || TextUtils.isEmpty(cacheName))
            throw new NullPointerException("The value cacheName shouldn't be null or empty!");
        String cacheFileNamePure = String.format("%s_TEXT.cache", cacheName);
        return new File(getCacheDirectory(context), cacheFileNamePure);
    }

    private static String loadCache(File cacheFile) {
        if (!doesCacheExists(cacheFile)) return null;
        FileInputStream fileInputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            fileInputStream = new FileInputStream(cacheFile);
            int read = 0;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((read = fileInputStream.read(buffer)) > 0)
                byteArrayOutputStream.write(buffer, 0, read);
            String result = byteArrayOutputStream.toString();
            return result.substring(result.indexOf('\n') + 1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean saveCache(StringBuilder data, File cacheFile) {
        return saveCache(data.toString(), cacheFile);
    }

    private static boolean saveCache(String data, File cacheFile) {
        if (data == null)
            throw new NullPointerException("The value data cannot be null!");
        String fetchedData = (new Date().getTime()) + TIME_POSTFIX + "\n" + data;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(cacheFile);
            fileOutputStream.write(fetchedData.getBytes());
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (fileOutputStream != null)
                    fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean doesCacheExists(File cacheFile) {
        return cacheFile.exists();
    }

    private static File getCacheDirectory(Context context) {
        if (!context.getCacheDir().exists())
            if (context.getCacheDir().mkdirs())
                return null;
        File cacheDirectory = new File(context.getCacheDir(), CACHE_DIRECTORY_NAME);
        if (!cacheDirectory.exists())
            if (!cacheDirectory.mkdirs())
                return null;
        return cacheDirectory;
    }

}
