package cn.kkserver.view.element;

import android.content.Context;
import android.view.View;
import java.util.Iterator;
import java.util.TreeMap;

import cn.kkserver.core.Value;
import cn.kkserver.observer.ArrayIterator;
import cn.kkserver.observer.IObserver;
import cn.kkserver.observer.IWithObserver;
import cn.kkserver.observer.Listener;
import cn.kkserver.view.Property;
import cn.kkserver.view.style.Style;
import cn.kkserver.view.value.KKValue;

/**
 * Created by zhanghailong on 2016/11/4.
 */

public class ForeachElement extends ViewElement {


    private final static Listener<ForeachElement> _L = new Listener<ForeachElement>() {

        @Override
        public void onChanged(IObserver observer, String[] changedKeys, ForeachElement weakObject) {
            weakObject.onValueChanged(observer,observer.get(new String[0]));
        }

    };

    public ForeachElement(Context context) {
        super(context);
    }

    public ForeachElement(View view) {
        super(view);
    }

    @Override
    protected void obtainObserver(IObserver observer) {

    }

    private TreeMap<String,Element> _elementsWithReuse;

    @Override
    protected void onPropertyChanged(Property property, Object value, Object newValue) {
        super.onPropertyChanged(property,value,newValue);

        if(property == Style.WithObserver) {

            final IWithObserver withObserver = (IWithObserver) newValue;

            if(withObserver != null) {

                _elementsWithReuse = new TreeMap<>();

                Element e = firstChild();

                while(e != null) {
                    Element n = e.nextSibling();
                    String reuse = e.get(Style.Reuse,String.class,"");
                    if(! _elementsWithReuse.containsKey(reuse)) {
                        _elementsWithReuse.put(reuse,e);
                    }
                    e.remove();
                    e = n;
                }

                withObserver.on(new String[0],_L,this);

                view().post(new Runnable() {
                    @Override
                    public void run() {
                        withObserver.change(new String[0]);
                    }
                });

            }
        }
    }

    protected void onValueChanged(IObserver observer, Object value) {

        Element p = firstChild();

        Iterator<Object> i = new ArrayIterator<>(value);

        int idx = 0;

        while(i.hasNext()) {

            Object object = i.next();

            String reuse = Value.stringValue(Value.get(object,"reuse"),"");

            if(p == null || !reuse.equals(p.get(Style.Reuse,String.class,""))) {

                Element n;

                if(_elementsWithReuse.containsKey(reuse)) {
                    n = _elementsWithReuse.get(reuse).clone();
                }
                else if(_elementsWithReuse.containsKey("")) {
                    n = _elementsWithReuse.get("").clone();
                }
                else {
                    n = new Element();
                }

                if(p == null) {
                    n.appendTo(this);
                }
                else {
                    n.beforeTo(p);
                    p.remove();
                }
                p = n;
            }

            p.set(Style.Key,String.valueOf(idx));
            p.set(Style.Observer,observer);

            idx ++;

            p = p.nextSibling();
        }

        while(p != null) {
            Element n = p.nextSibling();
            p.set(Style.Observer,null);
            p.remove();
            p = n;
        }

        if(get(Style.Width,KKValue.class,KKValue.Zero).isAuto() || get(Style.Height,KKValue.class,KKValue.Zero).isAuto()) {
            Layout.elementLayoutChildren(parent());
        }
        else {
            Layout.elementLayoutChildren(this);
        }

    }

}
