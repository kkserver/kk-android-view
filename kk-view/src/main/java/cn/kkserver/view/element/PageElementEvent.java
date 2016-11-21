package cn.kkserver.view.element;

/**
 * Created by zhanghailong on 2016/11/14.
 */

public class PageElementEvent extends ElementEvent {

    public final static String CHANGED = "page.changed";

    public int pageCount;
    public int pageIndex;


    public PageElementEvent(Element element) {
        super(element);
    }
}
