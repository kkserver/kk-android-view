package cn.kkserver.view.element;

import android.view.MotionEvent;

/**
 * Created by zhanghailong on 2016/11/7.
 */

public class ViewElementEvent extends ElementEvent {

    public final static String TOUCH = "view.touch";
    public final static String HOVER = "view.hover";

    public final MotionEvent event;
    public boolean returnResult = false;

    public ViewElementEvent(Element element,MotionEvent event) {
        super(element);
        this.event = event;
    }


}
