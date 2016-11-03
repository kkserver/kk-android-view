package cn.kkserver.view.element;

import cn.kkserver.view.Property;

/**
 * Created by zhanghailong on 2016/11/1.
 */

public interface IElementView {

    public void onElementPropertyChanged(Element element, Property property, Object value, Object newValue);

}
