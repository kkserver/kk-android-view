package cn.kkserver.view.element;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.List;
import cn.kkserver.observer.IObserver;
import cn.kkserver.observer.IWithObserver;
import cn.kkserver.observer.Object;
import cn.kkserver.view.script.IScript;
import cn.kkserver.view.KK;
import cn.kkserver.view.script.Script;
import cn.kkserver.view.style.Style;

/**
 * Created by zhanghailong on 2016/11/2.
 */

public class ForeachElement extends ScriptElement {

    public final static IScript Script = new cn.kkserver.view.script.Script.OutletScript() {

        protected Element foreachElement(IObserver observer,Element element,Element p,java.lang.Object object,String[] keys) {

            {
                Element e = p.nextSibling();
                if(e != null && e.get(Style.Element) == element) {
                    p = e;
                }
                else {
                    e = element.clone();
                    p.after(e);
                    e.set(Style.Hidden,false);
                    e.set(Style.Element,element);
                    p = e;
                }
            }

            IWithObserver withObserver = p.get(Style.Observer,IWithObserver.class);

            if(withObserver != null) {
                withObserver.recycle();
            }

            withObserver = observer.with(keys,object);

            p.set(Style.Observer,withObserver);

            Element e = p.firstChild();

            IObserver obs = observer;

            while (e != null) {
                if(e instanceof ForeachElement) {
                    obs = withObserver;
                }
                else {
                    try {
                        if(ScriptElement.runScript(e,obs,withObserver)) {
                            break;
                        }
                    } catch (Throwable ex) {
                        Log.d(KK.TAG,ex.getMessage(),ex);
                        break;
                    }
                }

                e = e.nextSibling();
            }

            return p;
        }

        @Override
        protected java.lang.Object onValue(IObserver observer, Element element, java.lang.Object weakObject, java.lang.Object value,String[] keys) {

            Element p = element,n;

            if(value != null) {

                if( value instanceof List) {

                    int idx = 0;

                    for(java.lang.Object object : (List<?>) value) {
                        p = foreachElement(observer,element,p,object, Object.join(keys,new String[]{String.valueOf(idx)}));
                        idx ++;
                    }
                }
                else if(value.getClass().isArray()) {

                    int count = Array.getLength(value);
                    for(int idx = 0;idx < count;idx ++ ){
                        p = foreachElement(observer,element,p,Array.get(value,idx),Object.join(keys,new String[]{String.valueOf(idx)}));
                    }
                }
            }

            if(p != null) {
                p = p.nextSibling();
            }

            while(p != null) {
                if(p.get(Style.Element) == element) {
                    n = p.nextSibling();
                    p.remove();
                    p = n;
                }
                else {
                    p = p.nextSibling();
                }
            }

            Layout.elementLayoutChildren(element.parent());

            return cn.kkserver.view.script.Script.Done;
        }

        @Override
        protected boolean onObserverElement(IObserver observer, Element element, java.lang.Object weakObject,String[] keys) {

            element.set(Style.Hidden,true);

            return true;

        }

    };

    public ForeachElement(Context context) {
        super(context);
    }

    @Override
    public boolean runScript(IObserver observer, java.lang.Object weakObject) throws Throwable {
        return Script.onRunScript(observer,this,weakObject);
    }

    @Override
    protected Element onCreateCloneElement() {
        return new ForeachElement(context);
    }
}
