package cn.kkserver.view.element;

import android.view.DragEvent;
/**
 * Created by zhanghailong on 2016/11/7.
 */

public class ViewElementDragEvent extends ElementEvent {

    public final static String DRAG = "view.drag";

    public final ViewElement draggable;
    public final DragEvent event;
    public boolean returnResult = false;

    public ViewElementDragEvent(Element element, DragEvent event) {
        super(element);
        this.event = event;
        draggable = (ViewElement) event.getLocalState();
    }


}
