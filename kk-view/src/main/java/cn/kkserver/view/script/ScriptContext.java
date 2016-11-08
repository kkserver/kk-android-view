package cn.kkserver.view.script;

import android.content.Context;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by zhanghailong on 2016/11/8.
 */

public class ScriptContext extends Object {

    public final Context context;

    public ScriptContext(Context context) {
        this.context = context;
    }

    private final Map<String,IScriptRunnable> _runnables = new TreeMap<String,IScriptRunnable>();

    public void set(String type,IScriptRunnable runnable) {
        _runnables.put(type,runnable);
    }

    public IScriptRunnable get(String type) throws Throwable {

        if(type != null && _runnables.containsKey(type)) {

            return _runnables.get(type);

        }

        return null;
    }

    public IScriptFunction compile(String type,String code) throws Throwable {

        if(type != null && _runnables.containsKey(type)) {

            IScriptRunnable runnable = _runnables.get(type);

            return runnable.compile(this,code);

        }

        return null;
    }

}
