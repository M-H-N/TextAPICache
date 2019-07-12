package ir.m_h_n.textapicacheauto;

import android.app.Activity;
import android.content.Context;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import ir.m_h_n.textapicacheauto.exceptions.InternetNotAvailableException;
import ir.m_h_n.textapicacheauto.exceptions.RequestUnsuccessfulException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TextAPICache {
    private static final String TAG = TextAPICache.class.getSimpleName();
    private final OkHttpClient client = new OkHttpClient();
    private Context context;
    private Activity activity;
    private TextAPICacheListener listener;
    private TextAPICacheMode mode;

    public TextAPICache(Activity activity, TextAPICacheListener listener, TextAPICacheMode mode) {
        if (activity == null) throw new NullPointerException("The value activity cannot be null!");
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.listener = listener;
        if (mode == null) throw new NullPointerException("The value mode cannot be null!");
        this.mode = mode;
    }

    public TextAPICache get(String url) {
        return get(url, TextAPICacheTools.convertUrlIntoCacheName(url, context), false);
    }

    public TextAPICache get(String url, boolean forceOnline) {
        return get(url, TextAPICacheTools.convertUrlIntoCacheName(url, context), forceOnline);
    }

    public TextAPICache get(String url, String cacheName) {
        return get(url, cacheName, false);
    }

    public TextAPICache get(String url, String cacheName, boolean forceOnline) {
        if (forceOnline) {
            if (TextAPICacheTools.isInternetAvailable(context)) {
                getDataFromServer(url, cacheName);
            } else {
                if (listener != null)
                    listener.onCacheError(url, cacheName, new InternetNotAvailableException());
            }
        } else {
            if (CacheManager.doesCacheExists(cacheName, context)) {
                String result = CacheManager.loadCacheIfValid(cacheName, context);
                if (CacheManager.isCacheValid(cacheName, context)) {
                    if (listener != null)
                        listener.onCacheLoaded(url, cacheName, result);
                } else {
                    if (TextAPICacheTools.isInternetAvailable(context)) {
                        getDataFromServer(url, cacheName);
                    } else {
                        switch (mode) {
                            case NORMAL_MODE:
                                if (listener != null)
                                    listener.onCacheError(url, cacheName, new InternetNotAvailableException());
                                break;
                            case MOST_OFFLINE_MODE:
                                if (CacheManager.doesCacheExists(cacheName, context)) {
                                    String response = CacheManager.loadCache(cacheName, context);
                                    if (response != null) {
                                        if (listener != null)
                                            listener.onCacheLoaded(url, cacheName, response);
                                    } else {
                                        if (listener != null)
                                            listener.onCacheError(url, cacheName, new RequestUnsuccessfulException());
                                    }
                                } else {
                                    if (listener != null)
                                        listener.onCacheError(url, cacheName, new RequestUnsuccessfulException());
                                }
                                break;
                        }
                    }
                }
            } else {
                if (TextAPICacheTools.isInternetAvailable(context)) {
                    getDataFromServer(url, cacheName);
                } else {
                    if (listener != null)
                        listener.onCacheError(url, cacheName, new InternetNotAvailableException());
                }
            }
        }
        return this;
    }

    public TextAPICache get(String url, long expireLimit) {
        return get(url, TextAPICacheTools.convertUrlIntoCacheName(url, context), false, expireLimit);
    }

    public TextAPICache get(String url, String cacheName, long expireLimit) {
        return get(url, cacheName, false, expireLimit);
    }

    public TextAPICache get(String url, boolean forceOnline, long expireLimit) {
        return get(url, TextAPICacheTools.convertUrlIntoCacheName(url, context), forceOnline, expireLimit);
    }

    public TextAPICache get(String url, String cacheName, boolean forceOnline, long expireLimit) {
        if (forceOnline) {
            if (TextAPICacheTools.isInternetAvailable(context)) {
                getDataFromServer(url, cacheName);
            } else {
                if (listener != null)
                    listener.onCacheError(url, cacheName, new InternetNotAvailableException());
            }
        } else {
            if (CacheManager.doesCacheExists(cacheName, context)) {
                String result = CacheManager.loadCacheIfValid(cacheName, context, expireLimit);
                if (CacheManager.isCacheValid(cacheName, context, expireLimit)) {
                    if (listener != null)
                        listener.onCacheLoaded(url, cacheName, result);
                } else {
                    if (TextAPICacheTools.isInternetAvailable(context)) {
                        getDataFromServer(url, cacheName);
                    } else {
                        switch (mode) {
                            case NORMAL_MODE:
                                if (listener != null)
                                    listener.onCacheError(url, cacheName, new InternetNotAvailableException());
                                break;
                            case MOST_OFFLINE_MODE:
                                if (CacheManager.doesCacheExists(cacheName, context)) {
                                    String response = CacheManager.loadCache(cacheName, context);
                                    if (response != null) {
                                        if (listener != null)
                                            listener.onCacheLoaded(url, cacheName, response);
                                    } else {
                                        if (listener != null)
                                            listener.onCacheError(url, cacheName, new RequestUnsuccessfulException());
                                    }
                                } else {
                                    if (listener != null)
                                        listener.onCacheError(url, cacheName, new RequestUnsuccessfulException());
                                }
                                break;
                        }
                    }
                }
            } else {
                if (TextAPICacheTools.isInternetAvailable(context)) {
                    getDataFromServer(url, cacheName);
                } else {
                    if (listener != null)
                        listener.onCacheError(url, cacheName, new InternetNotAvailableException());
                }
            }
        }
        return this;
    }

    private void getDataFromServer(final String url, final String cacheName) {
        try {
            final Request request = new Request.Builder()
                    .url(url).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull final IOException e) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (listener != null)
                                listener.onCacheError(url, cacheName, e);
                        }
                    });
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    try {
                        final String resultStringBody = response.body().string();
                        updateCache(cacheName, resultStringBody);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (listener != null)
                                    listener.onCacheLoaded(url, cacheName, resultStringBody);
                            }
                        });
                    } catch (final Exception e) {
                        e.printStackTrace();
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (listener != null)
                                    listener.onCacheError(url, cacheName, e);
                            }
                        });
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            if (listener != null)
                listener.onCacheError(url, cacheName, e);
        }
    }

    private void updateCache(String cacheName, String data) {
        CacheManager.saveCache(cacheName, data, context);
    }
}
