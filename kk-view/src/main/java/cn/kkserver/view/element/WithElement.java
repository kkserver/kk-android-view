package cn.kkserver.view.element;

import android.content.Context;
import android.view.View;

import cn.kkserver.observer.IObserver;
import cn.kkserver.observer.IWithObserver;
import cn.kkserver.observer.Observer;
import cn.kkserver.view.style.Style;

/**
 * Created by zhanghailong on 2016/11/4.
 */

public class WithElement extends ViewElement implements IObserverElement{

    public WithElement(Context context) {
        super(context);
    }

    public WithElement(View view) {
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

        IWithObserver withObserver = get(Style.Observer,IWithObserver.class);

        if(withObserver != null){
            withObserver.recycle();
        }

        if(key != null && obs != null) {

            withObserver = obs.with(Observer.keys(key));

            set(Style.Observer,withObserver);

            Element e = firstChild();

            while(e != null) {

                Element.obtainObserver(e,withObserver);

                e = e.nextSibling();

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

        IWithObserver withObserver = get(Style.Observer,IWithObserver.class);

        if(withObserver != null){
            withObserver.recycle();
        }

    }

}
