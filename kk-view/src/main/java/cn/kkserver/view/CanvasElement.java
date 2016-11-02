package cn.kkserver.view;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.util.PropertyPermission;

/**
 * Created by zhanghailong on 2016/11/1.
 */

public class CanvasElement extends ViewElement {

    private final OnCallbackImpl _cb = new OnCallbackImpl(this);

    public KKCanvasView canvasView() {
        return (KKCanvasView) view();
    }

    public CanvasElement(Context context) {
        super(new KKCanvasView(context));
        canvasView().setOnCallback(_cb);
    }


    protected void onDraw(Canvas canvas) {

    }

    protected boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    protected boolean needDisplayProperty(Property property) {
        return property == Style.BackgroundColor  || property == Style.BorderColor || property == Style.BorderWidth  || property == Style.BorderRadius;
    }

    @Override
    protected void onPropertyChanged(Property property,Object value,Object newValue) {

        KKCanvasView view = canvasView();

        if(view != null) {


            if (property == Style.BackgroundColor) {
                view.backgroundColor = newValue == null ? 0 : ((Color) newValue).value;
            } else if (property == Style.BorderColor) {
                view.borderColor = newValue == null ? 0 : ((Color) newValue).value;
            } else if (property == Style.BorderWidth) {
                view.borderWidth = (KKValue) newValue;
            } else if (property == Style.BorderRadius) {
                view.borderRadius = (KKValue) newValue;
            }

            if (needDisplayProperty(property)) {
                view.invalidate();
            }
        }

        super.onPropertyChanged(property,value,newValue);

    }

    @Override
    protected Element onCreateCloneElement() {
        return new CanvasElement(view().getContext());
    }

    private static class OnCallbackImpl implements KKCanvasView.OnCallback {

        private WeakReference<CanvasElement> _element;

        public OnCallbackImpl(CanvasElement element) {
            _element = new WeakReference<>(element);
        }

        @Override
        public void onDraw(Canvas canvas) {
            CanvasElement e = _element.get();
            if(e != null) {
                e.onDraw(canvas);
            }
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            CanvasElement e = _element.get();
            if(e != null) {
                return e.onTouchEvent(event);
            }
            return false;
        }
    }
}
