package cn.kkserver.view;

import android.content.Context;
import java.lang.reflect.Field;

import cn.kkserver.observer.IObserver;

/**
 * Created by zhanghailong on 2016/11/2.
 */

public class ScriptElement extends Element {

    public final Context context;

    public ScriptElement(Context context) {
        this.context = context;
    }

    private IScript _javaScript;

    public boolean runScript(IObserver observer,Object weakObject) throws Throwable {

        if(_javaScript == null) {

            String type = get(Style.Type,String.class);
            String name = get(Style.Name,String.class);

            if(type != null && name != null) {

                Class<?> clazz = context.getClassLoader().loadClass(type);

                Field fd = clazz.getField(name);

                Object v = fd.get(null);

                if( v instanceof  IScript) {
                    _javaScript = (IScript) v;
                }
            }
        }

        if(_javaScript != null) {
            return _javaScript.onRunScript(observer,this,weakObject);
        }

        return false;
    }

    public static boolean runScript(Element element,IObserver observer,Object weakObject) throws Throwable {

        if(element instanceof ScriptElement) {
            return ((ScriptElement) element).runScript(observer,weakObject);
        }

        Element p = element.firstChild();

        while(p != null) {
            if(runScript(p,observer,weakObject)) {
                return true;
            }
            p = p.nextSibling();
        }

        return false;

    }

    @Override
    protected Element onCreateCloneElement() {
        return new ScriptElement(context);
    }

}
