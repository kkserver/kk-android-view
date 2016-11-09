package cn.kkserver.view.element;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.FloatRange;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import java.lang.ref.WeakReference;
import cn.kkserver.view.KK;
import cn.kkserver.view.KKView;
import cn.kkserver.view.Property;
import cn.kkserver.view.R;
import cn.kkserver.view.style.Style;
import cn.kkserver.view.value.Color;


/**
 * Created by zhanghailong on 2016/11/1.
 */

public class ViewElement extends Element {

    private View _view;
    private WeakReference<View> _weakView;

    protected boolean onTouchEvent(MotionEvent event) {

        ViewElementMotionEvent e = new ViewElementMotionEvent(this,event);

        sendEvent(ViewElementMotionEvent.TOUCH,e);

        return e.returnResult;

    }

    protected boolean onDragEvent(DragEvent event) {

        ViewElementDragEvent ev = new ViewElementDragEvent(this,event);

        sendEvent(ViewElementDragEvent.DRAG,ev);

        return ev.returnResult;
    }

    public View view() {
        if(_view == null && _weakView != null) {
            return _weakView.get();
        }
        return _view;
    }

    public ViewElement(View view) {
        this(view,false);
    }

    public ViewElement(View view,boolean weakView) {
        if(weakView){
            _weakView = new WeakReference<>(view);
        }
        else {
            _view = view;
        }

        OnCallback cb = new OnCallback(this);

        view.setOnTouchListener(cb);

        set(Style.Layout,"none");
    }

    public ViewElement(Context context) {
        this(new KKView(context));
    }

    protected void onStatusChanged() {

        View view = view();

        if(view == null) {
            return ;
        }

        if(! view.isEnabled()) {
            set(Style.Status,"disabled");
        }
        else if(get(Style.Selected,Boolean.class,false)) {
            set(Style.Status,"selected");
        }
        else {
            removeProperty(Style.Status);
        }
    }

    @Override
    protected void onPropertyChanged(Property property, Object value, Object newValue) {

        View view = view();

        if(view == null) {
            return ;
        }

        if(property == Style.BackgroundColor) {
            view.setBackgroundColor(newValue == null ? 0 : ((Color) newValue).value);
        }
        else if(property == Style.Enabled) {
            view.setEnabled(newValue == null ? true : (Boolean) newValue);
            onStatusChanged();
        }
        else if(property == Style.Hidden) {
            view.setVisibility(newValue == null || (Boolean) newValue ? View.GONE : View.VISIBLE);
        }
        else if(property == Style.Opacity) {
            @FloatRange(from=0.0, to=1.0) float v = newValue == null ? 1.0f : ((Float) newValue).floatValue();
            view.setAlpha(v);
        }
        else if(property == Style.Scale) {

            float v = newValue == null ? 1.0f : ((Float) newValue).floatValue();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                view.setScaleX(v);
                view.setScaleY(v);
            }

        }
        else if(property == Layout.Frame) {
            view.setTag(R.id.Frame,newValue);
            view.requestLayout();
        }
        else if(property == Style.Animation) {
            view.clearAnimation();
            if(newValue != null ) {
                if(newValue instanceof AnimationElement) {
                    view.startAnimation(((AnimationElement) newValue).getAnimation());
                }
                else if(newValue instanceof String) {
                    Animation anim = DocumentElement.getAnimation(this,(String) newValue);
                    if(anim != null) {
                        view.startAnimation(anim);
                    }
                }
            }
        }
        else if(property == Style.Selected) {
            onStatusChanged();
        }
        else if(property == Style.Droppable) {
            if(newValue != null && (Boolean) newValue) {
                view.setOnDragListener(new OnCallback(this));
            }
            else {
                view.setOnDragListener(null);
            }
        }

        if(view instanceof IElementView) {
            ((IElementView) view).onElementPropertyChanged(this,property,value,newValue);
        }

        super.onPropertyChanged(property,value,newValue);

    }

    @Override
    protected void onRemoveFromParent(Element element) {
        View v = view();
        ViewParent p = v.getParent();
        if(p != null && p instanceof ViewGroup) {
            v.clearAnimation();
            ((ViewGroup)p).removeView(v);
        }
    }

    @Override
    protected void onAddToParent(Element element) {

        if(element instanceof ViewElement) {
            View pp = ((ViewElement) element).view();
            if(pp instanceof ViewGroup) {
                View v = view();
                ViewParent p = v.getParent();
                if(p != null && p instanceof ViewGroup) {
                    ((ViewGroup)p).removeView(v);
                }
                ((ViewGroup)pp).addView(v);
            }
        }

    }

    @Override
    protected Element onCreateCloneElement() {
        try {
            View view = _view == null ? null : _view.getClass().getConstructor(Context.class).newInstance(_view.getContext());
            return getClass().getConstructor(View.class).newInstance(view);
        } catch (Throwable e) {
            Log.d(KK.TAG,e.getMessage(),e);
            return null;
        }
    }

    private static class OnCallback implements View.OnTouchListener,View.OnDragListener {

        private WeakReference<ViewElement> _element;

        public OnCallback(ViewElement element) {
            _element = new WeakReference<>(element);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            ViewElement e = _element.get();
            if(e != null) {
                return e.onTouchEvent(event);
            }
            return false;
        }

        @Override
        public boolean onDrag(View v, DragEvent event) {
            ViewElement e = _element.get();
            if(e != null) {
                return e.onDragEvent(event);
            }
            return false;
        }


    }

    public void startDraggable() {

        View v = view();

        if(v != null) {

            // Instantiates the drag shadow builder.
            View.DragShadowBuilder myShadow = new View.DragShadowBuilder(v);

            // Starts the drag
            v.startDrag(null,  // the data to be dragged
                    myShadow,  // the drag shadow builder
                    this,      // no need to use local data
                    0          // flags (not currently used, set to 0)
            );

        }

    }
}
