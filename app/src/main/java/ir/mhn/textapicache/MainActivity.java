package ir.mhn.textapicache;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import ir.m_h_n.textapicacheauto.TextAPICache;
import ir.m_h_n.textapicacheauto.TextAPICacheListener;
import ir.m_h_n.textapicacheauto.TextAPICacheMode;
import ir.mhn.textapicache.R;

public class MainActivity extends AppCompatActivity implements TextAPICacheListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView txtTest;
//    private TextAPICache apiCache = new TextAPICache(this, this, TextAPICacheMode.NORMAL_MODE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTest = findViewById(R.id.txtTest);
//        apiCache = new TextAPICache(this, this, TextAPICacheMode.MOST_OFFLINE_MODE);
        TextAPICache textAPICache = new TextAPICache(this, new TextAPICacheListener() {
            @Override
            public void onCacheLoaded(String url, String cacheName, String cacheData) {

            }

            @Override
            public void onCacheError(String url, String cacheName, Exception e) {

            }
        }, TextAPICacheMode.NORMAL_MODE);

//        textAPICache
//                .get("https://en-maktoob.yahoo.com/?p=us", "YahooCache", false)
//                .get("https://github.com", "GitHubCache")
//                .get("https://www.microsoft.com/en-us/", true)
//                .get("https://www.apple.com/")
//                .get("https://jitpack.io/", "JitPackCache", false, 10000);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                apiCache.get("https://en-maktoob.yahoo.com/?p=us");
            }
        });
    }

    @Override
    public void onCacheLoaded(String url, String cacheName, String cacheData) {

    }

    @Override
    public void onCacheError(String url, String cacheName, Exception e) {

    }
}