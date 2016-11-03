package cn.kkserver.view.element;

import cn.kkserver.observer.IObserver;

/**
 * Created by zhanghailong on 2016/11/2.
 */

public interface IOutletElement {

    public void setOutletValue(IObserver observer,String[] keys,Object weakObject,Object value);

}
