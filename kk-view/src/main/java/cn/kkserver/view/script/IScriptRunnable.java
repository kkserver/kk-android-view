package cn.kkserver.view.script;

/**
 * Created by zhanghailong on 2016/11/8.
 */

public interface IScriptRunnable {


    public IScriptFunction compile(ScriptContext context,String code) throws Throwable;


}
