package cn.kkserver.view.element;

import cn.kkserver.view.event.Event;

/**
 * Created by zhanghailong on 2016/11/1.
 */

public class ElementEvent extends Event {

    public final Element element;
    private boolean _cancelBubble = false;

    public ElementEvent(Element element) {
        this.element = element;
    }

    public boolean isCancelBubble() {
        return _cancelBubble;
    }

    public void setCancelBubble(boolean cancelBubble) {
        _cancelBubble = cancelBubble;
    }

}
