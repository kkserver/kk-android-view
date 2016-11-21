package cn.kkserver.view.element;

import android.view.DragEvent;
import android.view.View;

import cn.kkserver.view.style.Style;
import cn.kkserver.view.value.Rect;

/**
 * Created by zhanghailong on 2016/11/7.
 */

public class ViewElementDragEvent extends ElementEvent {

    public final static String DRAG = "view.drag";

    public final Object object;
    public final DragEvent event;

    public ViewElementDragEvent(Element element, DragEvent event) {
        super(element);
        this.event = event;
        object = event.getLocalState();
    }


    public Rect elementFrame(Element element) {

        Rect r = (Rect) element.get(Layout.Frame,Rect.class,new Rect()).clone();
        Element p = element.parent();

        while(p != null && p != this.element) {
            Rect v = p.get(Layout.Frame,Rect.class);
            if(v != null) {
                r.origin.x += v.origin.x;
                r.origin.y += v.origin.y;
            }
            p = p.parent();
        }

        return r;
    }

    public Rect elementVisibleFrame(Element element) {

        Rect r = (Rect) element.get(Layout.Frame,Rect.class,new Rect()).clone();
        Element p = element.parent();

        while(p != null && p != this.element) {
            Rect v = p.get(Layout.Frame,Rect.class);
            if(v != null) {
                r.origin.x += v.origin.x;
                r.origin.y += v.origin.y;
                if(p instanceof ViewElement ) {
                    View view = ((ViewElement) p).view();
                    r.origin.x -= view.getScrollX();
                    r.origin.y -= view.getScrollY();
                }
            }
            p = p.parent();
        }

        return r;
    }

    public boolean isOverElemenet(Element element) {

        if(element == this.element) {
            return true;
        }

        if(! element.get(Style.Hidden,Boolean.class,false)) {

            Rect r = elementVisibleFrame(element);

            if(event.getX() >= r.origin.x && event.getX() < r.right()
                    && event.getY() >= r.origin.y && event.getY() < r.bottom()) {
                return true;
            }

        }

        return false;
     }
}
