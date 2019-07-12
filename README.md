# TextAPICache
API Text Caching Library for Android

This library will help you to cahe your API responses(usually JSON) into file-based database.

Some features of TextAPICache:
 * Optional names for cahe files.
 * Optional modes (NORMAL_MODE , MOST_OFFLINE_MODE)
 * ...

## Diagram
| Flowchart    |
| ------------- |
| ![Flowchart](https://github.com/M-H-N/TextAPICache/blob/master/Diagram.jpg) |

## Using TextAPICache Library in your Android application
Add this in your root `build.gradle` file (**not** your module `build.gradle` file):

```gradle
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```
Add this in your `build.gradle`
```groovy
implementation 'com.github.M-H-N:TextAPICache:v1.3'
```
Do not forget to add internet and accessing network state permissions in manifest if already not present
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```
Then create an object of `TextAPICache` class in your activity :
```java
TextAPICache textAPICache = new TextAPICache(this, new TextAPICacheListener() {
            @Override
            public void onCacheLoaded(String url, String cacheName, String cacheData) {
                //Is called when the data is loaded (from cache or server)
            }

            @Override
            public void onCacheError(String url, String cacheName, Exception e) {
                //Is called when an error occurs during load process
            }
        }, TextAPICacheMode.NORMAL_MODE);
```

After that whenever you need data (from cache or server) you just need to do this :
```java
textAPICache.get("https://en-maktoob.yahoo.com/?p=us", "YahooCache", false);
```

You can also call `get` method continuously :
```java
textAPICache
            .get("https://en-maktoob.yahoo.com/?p=us", "YahooCache", false)
            .get("https://github.com", "GitHubCache")
            .get("https://www.microsoft.com/en-us/", true)
            .get("https://www.apple.com/")
            .get("https://jitpack.io/", "JitPackCache", false, 10000);
```
After calling `get` method, the results (loaded or failed) will be on implemented methods on `TextAPICacheListener` which are `onCacheLoaded` and `onCacheError`.
So it is recommended to implement `TextAPICacheListener` in your activity and enter your activity instead of implementing `TextAPICacheListener` there on constructor.
```java
public class MainActivity extends AppCompatActivity implements TextAPICacheListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    
    ...
    
    private TextAPICache apiCache = new TextAPICache(this, this, TextAPICacheMode.NORMAL_MODE);
  
    ...
  
    @Override
    public void onCacheLoaded(String url, String cacheName, String cacheData) {
                //Is called when the data is loaded (from cache or server)   
    }

    @Override
    public void onCacheError(String url, String cacheName, Exception e) {
                //Is called when an error occurs during load process
    }
}
```

## Modes
| **Mode**      | **Description**  |
| ------------- | ------------- | 
| NORMAL_MODE     | Returns error if the cache is expired and the internet is not available | 
| MOST_OFFLINE_MODE     | Returns the expired cache if the internet is not available, Recommended to use in applications with much offline use | 

## Parameters
| **Parameter**      | **Description**  |
| ------------- | ------------- | 
| url     | The main parameter that determines the URL of server API (NOTE: For now it only supports get requests) | 
| cacheName     | Specifies the cache file name (By default it generates **random number** for each given URL)  | 
| forceOnline     | Determines if the data must be loaded from server or not (By default it is **false**) | 
| expireLimit     | Defines the custom expire time limitation (By default it is **300000 ms**) | 


## License:
```
    Copyright 2019 Mahmoud HodaeeNia

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
```
