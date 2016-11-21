package cn.kkserver.view.element;

import java.util.Map;
import java.util.TreeMap;
import cn.kkserver.observer.IObserver;
import cn.kkserver.observer.IWithObserver;
import cn.kkserver.observer.Listener;
import cn.kkserver.view.Property;
import cn.kkserver.view.script.IScriptFunction;
import cn.kkserver.view.style.Style;

/**
 * Created by zhanghailong on 2016/11/4.
 */

public class OutletElement extends ScriptElement {

    public final static Object Done = new Object();

    @Override
    protected void onScriptFunctionCall(IScriptFunction func) throws Throwable {

    }

    protected Object onValue(Element element,IObserver observer,IObserver obs,String key,Object value) {

        if(hasScriptFunction()) {

            Map<String, Object> object = new TreeMap<>();

            object.put("value", value);
            object.put("key", key);
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

    private final static Listener<OutletElement> _L = new Listener<OutletElement>() {

        @Override
        public void onChanged(IObserver observer, String[] changedKeys, OutletElement weakObject) {
            weakObject.onValueChanged(observer,new String[0],observer.get(new String[0]));
        }

    };

    @Override
    protected void onPropertyChanged(Property property, Object value, Object newValue) {
        super.onPropertyChanged(property,value,newValue);

        if(property == Style.WithObserver) {
            IWithObserver withObserver = (IWithObserver) newValue;
            if(withObserver != null) {
                _L.onChanged(withObserver,new String[0],this);
                withObserver.on(new String[0],_L,this);
            }
        }
    }

    protected void onValueChanged(IObserver observer,String[] baseKeys, Object value) {

        String property = get(Style.Property,String.class);

        if(property != null) {

            Property prop = Style.get(property);

            if(prop != null) {

                Element p = parent();

                if(p != null) {

                    Object v = onValue(p,get(Style.Observer,IObserver.class),observer,get(Style.Key,String.class),value);

                    if(v != Done) {
                        p.set(prop, v);
                    }

                }
            }
        }
    }

    @Override
    protected Element onCreateCloneElement() {
        return new OutletElement();
    }

}
