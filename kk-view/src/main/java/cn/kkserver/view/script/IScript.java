package cn.kkserver.view.script;

import cn.kkserver.observer.IObserver;
import cn.kkserver.view.element.Element;

/**
 * Created by zhanghailong on 2016/11/2.
 */

public interface IScript {

    public boolean onRunScript(IObserver observer, Element element, Object weakObject);

}
