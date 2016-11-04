package cn.kkserver.demo;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import cn.kkserver.observer.IObserver;
import cn.kkserver.observer.Observer;
import cn.kkserver.view.element.Element;
import cn.kkserver.view.event.Event;
import cn.kkserver.view.event.EventFunction;
import cn.kkserver.view.KK;
import cn.kkserver.view.KKDocumentView;
import cn.kkserver.view.value.KKValue;

public class MainActivity extends Activity {

    private final Observer observer = new Observer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics metrics = getResources().getDisplayMetrics();

        KKValue.UNIT_DP = metrics.widthPixels / 380.0f;

        KKDocumentView documentView;

        try {
            documentView = ((KKDocumentView) findViewById(R.id.activity_main));
            documentView.load(R.xml.main);
            Element.obtainObserver(documentView.bodyElement(),observer);
            documentView.bodyElement().on("action.editing", new EventFunction<IObserver>() {

                @Override
                public void onEvent(String name, Event event, IObserver weakObject) {

                    weakObject.set(new String[]{"editing"},"editing");

                }

            },observer);

        } catch (Throwable e) {
            Log.d(KK.TAG,e.getMessage(),e);
        }

        List<Object> apps = new ArrayList<>();

        for(ApplicationInfo app : getPackageManager().getInstalledApplications(0)) {
            if((app.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                TreeMap<String, Object> v = new TreeMap<>();
                v.put("icon", app.loadIcon(getPackageManager()));
                v.put("label", app.loadLabel(getPackageManager()));
                v.put("name", app.packageName);
                apps.add(v);
            }
        }

        observer.set(new String[]{"apps"},apps);

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK) {
            observer.set(new String[]{"editing"},null);
        }

        return false;
    }


}
