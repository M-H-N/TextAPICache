package ir.m_h_n.textapicacheauto;

public interface TextAPICacheListener {
    void onCacheLoaded(String url, String cacheName, String cacheData);

    void onCacheError(String url, String cacheName, Exception e);
}
