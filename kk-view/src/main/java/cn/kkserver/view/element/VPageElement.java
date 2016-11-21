package cn.kkserver.view.element;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.ScrollView;

import cn.kkserver.view.style.Style;
import cn.kkserver.view.value.Rect;
import cn.kkserver.view.value.Size;

/**
 * Created by zhanghailong on 2016/11/14.
 */

public class VPageElement extends VScrollElement {

    public VPageElement(ScrollView view) {
        super(view);
    }

    public VPageElement(Context context) {
        super(context);
    }

    @Override
    protected void initVScrollElement() {
        super.initVScrollElement();
        ScrollView view = scrollView();
        view.setVerticalScrollBarEnabled(false);
        set(Style.Layout,"flow");
    }


    private float _touchY;
    private float _touchDY;

    public int pageIndex() {
        ScrollView view = scrollView();
        Rect frame = get(Layout.Frame,Rect.class);
        Size contentSize = get(Layout.ContentSize,Size.class);
        if(frame != null && contentSize != null) {
            int pageSize = contentSize.height / frame.size.height;
            int pageIndex = (view.getScrollY() / frame.size.height);
            if(pageIndex >= pageSize) {
                pageIndex = pageSize - 1;
            }
            if(pageIndex < 0) {
                pageIndex = 0;
            }
            return pageIndex;
        }
        return 0;
    }

    public void setPageIndex(int pageIndex,boolean animated) {
        ScrollView view = scrollView();
        Rect frame = get(Layout.Frame,Rect.class);
        Size contentSize = get(Layout.ContentSize,Size.class);
        if(frame != null && contentSize != null) {
            int pageSize = contentSize.height / frame.size.height;
            if(pageIndex >= pageSize) {
                pageIndex = pageSize - 1;
            }
            if(pageIndex < 0) {
                pageIndex = 0;
            }
            if(animated) {
                view.smoothScrollTo(0, pageIndex * frame.size.height);
            }
            else {
                view.scrollTo(0, pageIndex * frame.size.height);
            }
        }
    }

    @Override
    protected boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                _touchY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                _touchDY = event.getY() - _touchY;
                _touchY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:

            {
                ScrollView view = scrollView();
                Rect frame = get(Layout.Frame,Rect.class);
                Size contentSize = get(Layout.ContentSize,Size.class);

                if(frame != null && contentSize != null) {
                    int pageSize = contentSize.height / frame.size.height;
                    int pageIndex = (view.getScrollY() / frame.size.width);
                    if(_touchDY < 0) {
                        pageIndex ++;
                    }
                    if(pageIndex >= pageSize) {
                        pageIndex = pageSize - 1;
                    }
                    if(pageIndex < 0) {
                        pageIndex = 0;
                    }
                    view.smoothScrollTo(0,pageIndex * frame.size.height);
                    PageElementEvent ev = new PageElementEvent(this);
                    ev.pageIndex = pageIndex;
                    ev.pageCount = pageSize;
                    sendEvent(PageElementEvent.CHANGED,ev);
                }
            }

            break;
        }

        return super.onTouchEvent(event);

    }

}
