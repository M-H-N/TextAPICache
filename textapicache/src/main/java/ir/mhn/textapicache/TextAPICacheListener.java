package ir.mhn.textapicache;

public interface TextAPICacheListener {
    void onCacheLoaded(String url, String cacheName, String cacheData);

    void onCacheError(String url, String cacheName, Exception e);
}
