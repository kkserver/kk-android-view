package cn.kkserver.view.script;

/**
 * Created by zhanghailong on 2016/11/8.
 */

public class ScriptException extends Exception {
    /**
     * Constructs a new {@code Exception} that includes the current stack trace.
     */
    public ScriptException() {
    }

    public ScriptException(String detailMessage) {
        super(detailMessage);
    }


    public ScriptException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ScriptException(Throwable throwable) {
        super(throwable);
    }
}
