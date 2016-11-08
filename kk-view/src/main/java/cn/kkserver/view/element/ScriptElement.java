package cn.kkserver.view.element;

import cn.kkserver.view.Property;
import cn.kkserver.view.script.IScriptElement;
import cn.kkserver.view.script.IScriptFunction;
import cn.kkserver.view.script.ScriptContext;
import cn.kkserver.view.script.ScriptException;
import cn.kkserver.view.style.Style;

/**
 * Created by zhanghailong on 2016/11/8.
 */

public class ScriptElement extends Element implements IScriptElement {

    public final static Property ScriptFunction = new Property("script-function");

    private IScriptFunction _func;

    @Override
    public void onScript(ScriptContext context) throws Throwable{
        String type = get(Style.Type,String.class);
        String code = get(Style.Text,String.class);
        if(type != null && code != null) {
            IScriptFunction fn = context.compile(type,code);
            if(fn != null) {
                set(ScriptFunction,fn);
                onScriptFunctionCall(fn);
            }
        }
    }

    protected void onScriptFunctionCall(IScriptFunction func) throws Throwable {
        func.call(this);
    }

    public boolean hasScriptFunction() {
        return get(ScriptFunction,IScriptFunction.class) != null;
    }

    public Object call(Object object) throws Throwable {
        IScriptFunction fn = get(ScriptFunction,IScriptFunction.class);
        if(fn != null) {
            return fn.call(object);
        }
        throw new ScriptException("Not Found Script Function");
    }

    public static void runScript(ScriptContext context,Element element) throws Throwable {
        if(element instanceof IScriptElement) {
            ((IScriptElement) element).onScript(context);
        }
        else {
            Element e = element.firstChild();
            while(e != null) {
                runScript(context,e);
                e = e.nextSibling();
            }
        }
    }
}
