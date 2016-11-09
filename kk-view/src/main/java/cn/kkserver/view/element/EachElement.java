package cn.kkserver.view.element;

import android.content.Context;
import android.util.Log;
import android.view.View;

import java.util.Iterator;
import cn.kkserver.observer.ArrayIterator;
import cn.kkserver.observer.IObserver;
import cn.kkserver.observer.IWithObserver;
import cn.kkserver.observer.Listener;
import cn.kkserver.observer.Observer;
import cn.kkserver.view.Property;
import cn.kkserver.view.style.Style;

/**
 * Created by zhanghailong on 2016/11/4.
 */

public class EachElement extends ViewElement implements IObserverElement {


    public EachElement(Context context) {
        super(context);
    }

    public EachElement(View view) {
        super(view);
    }

    @Override
    public void obtainObserver(IObserver observer) {

        String key = get(Style.Key,String.class);
        IObserver obs = observer;

        while(key != null && key.startsWith("^") && obs != null) {
            key = key.substring(1);
            obs = obs.parent();
        }

        if(key != null && obs != null) {

            set(Style.Hidden,true);

            final String[] keys = Observer.keys(key);

            onValueChanged(obs,keys,obs.get(keys));

            obs.on(keys, new Listener<EachElement>() {
                @Override
                public void onChanged(IObserver observer, String[] changedKeys, EachElement weakObject) {
                    if(weakObject != null) {
                        weakObject.onValueChanged(observer,keys,observer.get(keys));
                    }
                }
            },this);

        }
    }

    protected EachRowElement createRowElement() {

        EachRowElement row = new EachRowElement(view().getContext());

        for(Property p : propertys()) {
            row.set(p,get(p));
        }

        Element e = firstChild();
        while(e != null) {
            row.append(e.clone());
            e = e.nextSibling();
        }
        return row;
    }

    protected void onValueChanged(IObserver observer,String[] baseKeys, Object value) {

        Element p = this,e;

        Iterator<Object> i = new ArrayIterator<>(value);

        int idx = 0;

        while(i.hasNext()) {

            java.lang.Object object = i.next();

            e = p.nextSibling();

            if(e != null && e.get(Style.Element) == this) {
                p = e;
            }
            else {
                e = createRowElement();
                p.after(e);
                e.set(Style.Hidden,false);
                e.set(Style.Element,this);
                p = e;
            }

            String[] keys = Observer.join(baseKeys,new String[]{String.valueOf(idx)});

            IWithObserver withObserver = p.get(Style.Observer,IWithObserver.class);

            if(withObserver != null) {

                Element n = p.firstChild();

                while(n != null) {

                    Element.recycleObserver(n,withObserver);

                    n = n.nextSibling();
                }

                withObserver.recycle();
            }

            withObserver = observer.with(keys,object);

            p.set(Style.Observer,withObserver);
            p.set(Style.Object,object);
            Element n = p.firstChild();

            while(n != null) {

                Element.obtainObserver(n,withObserver);

                n = n.nextSibling();
            }

            idx ++;
        }

        p = p.nextSibling();

        while(p != null) {
            if(p.get(Style.Element) == this) {

                e = p.nextSibling();

                IWithObserver withObserver = p.get(Style.Observer,IWithObserver.class);

                if(withObserver != null) {

                    Element n = p.firstChild();

                    while(n != null) {

                        Element.recycleObserver(n,withObserver);

                        n = n.nextSibling();
                    }

                    withObserver.recycle();
                }

                p.remove();
                p = e;
            }
            else {
                break;
            }
        }

        Layout.elementLayoutChildren(parent());

    }

    @Override
    public void recycleObserver(IObserver observer) {

        IObserver obs = observer;

        while(obs != null) {
            obs.off(null,null,this);
            obs = obs.parent();
        }

        Element p = nextSibling();

        while(p != null) {

            if(p.get(Style.Element) == this) {

                Element e = p.nextSibling();

                IWithObserver withObserver = p.get(Style.Observer,IWithObserver.class);

                if(withObserver != null) {

                    Element n = p.firstChild();

                    while(n != null) {

                        Element.recycleObserver(n,withObserver);

                        n = n.nextSibling();
                    }

                    withObserver.recycle();
                }

                p.remove();
                p = e;
            }
            else {
                break;
            }
        }
    }

    public static class EachRowElement extends ViewElement implements IObserverElement ,IReflectElement{

        public EachRowElement(Context context) {
            super(context);
        }

        public EachRowElement(View view) {
            super(view);
        }

        @Override
        public void obtainObserver(IObserver observer) {

        }

        @Override
        public void recycleObserver(IObserver observer) {

        }

        @Override
        public Element element() {
            return get(Style.Element,Element.class);
        }
    }
}
