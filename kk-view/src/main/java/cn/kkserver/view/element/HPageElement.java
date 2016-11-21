package cn.kkserver.view.element;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;
import cn.kkserver.view.style.Style;
import cn.kkserver.view.value.Rect;
import cn.kkserver.view.value.Size;

/**
 * Created by zhanghailong on 2016/11/14.
 */

public class HPageElement extends HScrollElement {

    public HPageElement(HorizontalScrollView view) {
        super(view);
    }

    public HPageElement(Context context) {
        super(context);
    }

    @Override
    protected void initHScrollElement() {
        super.initHScrollElement();
        HorizontalScrollView view = scrollView();
        view.setHorizontalScrollBarEnabled(false);
        set(Style.Layout,"flow-nowarp");
    }

    private float _touchX;
    private float _touchDX;

    public int pageIndex() {
        HorizontalScrollView view = scrollView();
        Rect frame = get(Layout.Frame,Rect.class);
        Size contentSize = get(Layout.ContentSize,Size.class);
        if(frame != null && contentSize != null) {
            int pageSize = contentSize.width / frame.size.width;
            int pageIndex = (view.getScrollX() / frame.size.width);
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
        HorizontalScrollView view = scrollView();
        Rect frame = get(Layout.Frame,Rect.class);
        Size contentSize = get(Layout.ContentSize,Size.class);
        if(frame != null && contentSize != null) {
            int pageSize = contentSize.width / frame.size.width;
            if(pageIndex >= pageSize) {
                pageIndex = pageSize - 1;
            }
            if(pageIndex < 0) {
                pageIndex = 0;
            }
            if(animated) {
                view.smoothScrollTo(pageIndex * frame.size.width, 0);
            }
            else {
                view.scrollTo(pageIndex * frame.size.width, 0);
            }
        }
    }

    @Override
    protected boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                _touchX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                _touchDX = event.getX() - _touchX;
                _touchX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:

            {
                final HorizontalScrollView view = scrollView();
                Rect frame = get(Layout.Frame,Rect.class);
                Size contentSize = get(Layout.ContentSize,Size.class);

                if(frame != null && contentSize != null) {
                    int pageSize = contentSize.width / frame.size.width;
                    int pageIndex = (view.getScrollX() / frame.size.width);
                    if(_touchDX < 0) {
                        pageIndex ++;
                    }
                    if(pageIndex >= pageSize) {
                        pageIndex = pageSize - 1;
                    }
                    if(pageIndex < 0) {
                        pageIndex = 0;
                    }
                    final int toScrollX = pageIndex * frame.size.width;
                    view.post(new Runnable() {
                        @Override
                        public void run() {
                            view.smoothScrollTo(toScrollX,0);
                        }
                    });

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
