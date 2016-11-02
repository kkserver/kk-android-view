package cn.kkserver.view;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zhanghailong on 2016/11/1.
 */

public class EventEmitter extends Object {

    private List<Callback<?>> _callbacks = new ArrayList<>(4);

    public <T extends Object> EventEmitter on(String name,EventFunction<T> fn,T weakObject) {
        _callbacks.add(new Callback<T>(name,fn,weakObject));
        return this;
    }

    public <T extends Object> EventEmitter off(String name,EventFunction<T> fn,T weakObject) {
        if(name == null && fn ==null && weakObject == null){
            _callbacks.clear();
        }
        else {
            Iterator<Callback<?>> i = _callbacks.iterator();
            while(i.hasNext()) {
                Callback<?> v = i.next();
                if((name == null || v.name.equals(name))
                        && (fn == null || v.fn == fn )
                        && (weakObject == null || v.weakObject.get() == weakObject)) {
                    i.remove();
                }
            }
        }
        return this;
    }

    public EventEmitter emit(String name,Event event) {

        List<Callback<Object>> cbs = new ArrayList<>(4);

        for(Callback<?> cb : _callbacks) {
            if(name.startsWith(cb.name)) {
                cbs.add((Callback<Object>) cb);
            }
        }

        for(Callback<Object> cb : cbs) {
            cb.fn.onEvent(name,event,cb.weakObject.get());
        }

        return this;
    }

    private static class Callback <T extends Object> extends Object {

        public final String name;
        public final EventFunction<T> fn;
        public final WeakReference<T> weakObject;

        public Callback(String name,EventFunction<T> fn,T weakObject) {
            this.name = name;
            this.fn = fn;
            this.weakObject = new WeakReference<T>(weakObject);
        }

    }
}
