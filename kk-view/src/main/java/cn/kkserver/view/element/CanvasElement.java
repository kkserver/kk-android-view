package cn.kkserver.view.element;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

import java.lang.ref.WeakReference;

import cn.kkserver.view.KKCanvasView;
import cn.kkserver.view.Property;
import cn.kkserver.view.value.KKValue;
import cn.kkserver.view.style.Style;
import cn.kkserver.view.value.Color;

/**
 * Created by zhanghailong on 2016/11/1.
 */

public class CanvasElement extends ViewElement {

    public KKCanvasView canvasView() {
        return (KKCanvasView) view();
    }

    public CanvasElement(Context context) {
        super(new KKCanvasView(context));
        canvasView().setOnDrawCallback(new OnDrawCallbackImpl(this));
    }

    protected void onDraw(Canvas canvas) {

        Element e = firstChild();

        while(e != null) {
            if(e instanceof PaintElement) {
                ((PaintElement) e).draw(canvas);
            }
            e = e.nextSibling();
        }

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


    private static class OnDrawCallbackImpl implements KKCanvasView.OnDrawCallback {

        private WeakReference<CanvasElement> _element;

        public OnDrawCallbackImpl(CanvasElement element) {
            _element = new WeakReference<>(element);
        }

        @Override
        public void onDraw(Canvas canvas) {
            CanvasElement e = _element.get();
            if(e != null) {
                e.onDraw(canvas);
            }
        }

    }
}
