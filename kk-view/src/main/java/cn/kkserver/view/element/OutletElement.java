package cn.kkserver.view.element;

import java.util.Map;
import java.util.TreeMap;

import cn.kkserver.observer.IObserver;
import cn.kkserver.observer.Listener;
import cn.kkserver.observer.Observer;
import cn.kkserver.view.Property;
import cn.kkserver.view.script.IScriptFunction;
import cn.kkserver.view.style.Style;

/**
 * Created by zhanghailong on 2016/11/4.
 */

public class OutletElement extends ScriptElement implements IObserverElement {

    public final static Object Done = new Object();

    @Override
    protected void onScriptFunctionCall(IScriptFunction func) throws Throwable {

    }

    protected Object onValue(Element element,IObserver observer,IObserver obs,String[] keys,Object value) {

        if(hasScriptFunction()) {

            Map<String, Object> object = new TreeMap<>();

            object.put("value", value);
            object.put("key", Observer.joinString(keys));
            object.put("observer",observer);
            object.put("obs",obs);
            object.put("element",element);
            object.put("done",Done);

            try {
                value = call(object);
            } catch (Throwable ex) {
            }
        }

        return value;
    }

    @Override
    public void obtainObserver(final IObserver observer) {

        String key = get(Style.Key,String.class);
        String property = get(Style.Property,String.class);

        IObserver obs = observer;

        while(key.startsWith("^") && obs != null) {
            key = key.substring(1);
            obs = obs.parent();
        }

        if(key != null && property != null && obs != null) {

            final Property prop = Style.get(property);

            if(prop != null) {

                final String[] keys = Observer.keys(key);
                final Element element = parent();

                if(element != null && obs != null) {

                    Object v = onValue(element,observer,obs,keys,obs.get(keys));

                    if(v != Done) {
                        element.set(prop, v);
                    }

                    obs.on(keys, new Listener<OutletElement>() {
                        @Override
                        public void onChanged(IObserver obs, String[] changedKeys, OutletElement outlet) {
                            if(outlet != null) {
                                Object v = outlet.onValue(element,observer,obs,keys, obs.get(keys));
                                if(v != Done) {
                                    element.set(prop,v);
                                }
                            }
                        }
                    },this);

                }

            }
        }

    }

    @Override
    public void recycleObserver(IObserver observer) {

        IObserver obs = observer;

        while(obs != null) {
            obs.off(null,null,this);
            obs = obs.parent();
        }
    }

    @Override
    protected Element onCreateCloneElement() {
        return new OutletElement();
    }

}
