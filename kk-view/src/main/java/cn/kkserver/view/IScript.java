package cn.kkserver.view;

import cn.kkserver.observer.IObserver;

/**
 * Created by zhanghailong on 2016/11/2.
 */

public interface IScript {

    public boolean onRunScript(IObserver observer,Element element,Object weakObject);

}
