package cn.kkserver.view;

import android.content.Context;
import android.os.Handler;
import java.net.URL;
import java.util.TreeMap;

/**
 * Created by zhanghailong on 2016/11/2.
 */

public class KKResource extends Object {

    private final TreeMap<String,LoaderCallback> _cbs = new TreeMap<>();

    public void add(String protocol,LoaderCallback loader) {
        _cbs.put(protocol,loader);
    }

    public <T extends Object> T load(URL url,Class<T> type,Callback<T> cb) {
        if(_cbs.containsKey(url.getProtocol())) {
            LoaderCallback loader = _cbs.get(url.getProtocol());
            return loader.load(url,type,cb);
        }
        else {
            final Callback<T> v = cb;
            final Exception ex = new Exception("Not Found Protocol " + url.getProtocol());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    v.onFail(ex);
                }
            },0);
            return null;
        }
    }

    public static final KKResource Shared = new KKResource();

    public static interface Callback<T extends Object> {

        public void onLoaded(T object);

        public void onFail(Throwable ex);

    }

    public static interface LoaderCallback {

        public <T extends Object> T load(URL url,Class<T> type,Callback<T> cb);

    }

    public static class ResourceLoaderCallback implements LoaderCallback {

        private final Context _context;

        public ResourceLoaderCallback(Context context) {
            _context = context;
        }

        @Override
        public <T> T load(URL url, Class<T> type, Callback<T> cb) {
            return null;
        }
    }
}
