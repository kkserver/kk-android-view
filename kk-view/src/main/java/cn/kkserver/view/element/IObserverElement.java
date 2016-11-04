package cn.kkserver.view.element;

import cn.kkserver.observer.IObserver;

/**
 * Created by zhanghailong on 2016/11/4.
 */

public interface IObserverElement {

    public void obtainObserver(IObserver observer);

    public void recycleObserver(IObserver observer);

}
