package cn.kkserver.view.element;

import cn.kkserver.observer.IObserver;
import cn.kkserver.observer.Listener;
import cn.kkserver.observer.Observer;
import cn.kkserver.view.Property;
import cn.kkserver.view.style.Style;

/**
 * Created by zhanghailong on 2016/11/4.
 */

public class OutletElement extends Element implements IObserverElement {


    @Override
    public void obtainObserver(IObserver observer) {

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

                    element.set(prop,obs.get(keys));

                    obs.on(keys, new Listener<OutletElement>() {
                        @Override
                        public void onChanged(IObserver observer, String[] changedKeys, OutletElement outlet) {
                            element.set(prop,observer.get(keys));
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
