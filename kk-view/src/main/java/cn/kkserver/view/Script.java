package cn.kkserver.view;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.List;

import cn.kkserver.observer.IObserver;
import cn.kkserver.observer.IWithObserver;
import cn.kkserver.observer.Listener;
import cn.kkserver.observer.Object;

/**
 * Created by zhanghailong on 2016/11/2.
 */

public class Script extends Object {

    public static class OutletScript implements IScript {

        protected java.lang.Object onValue(IObserver observer, Element element, java.lang.Object weakObject, java.lang.Object value, String[] keys) {
            return value;
        }

        protected boolean onObserverElement(IObserver observer, Element element, java.lang.Object weakObject,String[] keys) {
            return false;
        }

        @Override
        public boolean onRunScript(IObserver observer, final Element element, java.lang.Object weakObject) {

            String key = element.get(Style.Key,String.class);
            String property = element.get(Style.Property,String.class);

            if(key != null && property != null) {

                final Property prop = Style.get(property);

                if(prop != null) {

                    final String[] keys = key.split("\\.");
                    final Element target = element.parent();

                    if(target != null) {

                        observer.on(keys,new Listener<java.lang.Object>() {

                            @Override
                            public void onChanged(IObserver observer, String[] changedKeys, java.lang.Object weakObject) {

                                java.lang.Object v = observer.get(keys);

                                v = OutletScript.this.onValue(observer,target,weakObject,v,keys);

                                if(v != null) {
                                    if (target instanceof IOutletElement) {
                                        ((IOutletElement) target).setOutletValue(observer, keys, weakObject, v);
                                    } else {
                                        target.set(prop, v);
                                    }
                                }

                            }

                        }, weakObject);

                        return onObserverElement(observer,target,weakObject,keys);

                    }

                }
            }

            return false;
        }
    }

    public final static IScript Outlet = new OutletScript();

}
