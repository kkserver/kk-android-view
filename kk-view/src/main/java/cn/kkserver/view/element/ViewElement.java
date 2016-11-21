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
import cn.kkserver.view.anim.Transaction;
import cn.kkserver.view.style.Style;
import cn.kkserver.view.value.Color;
import cn.kkserver.view.value.Rect;


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

    public View view() {
        if(_view == null && _weakView != null) {
            return _weakView.get();
        }
        return _view;
    }

    public ViewGroup viewGroup() {
        View v = view();
        if(v != null && v instanceof ViewGroup) {
            return (ViewGroup) v;
        }
        return null;
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
            if(value != null) {
                Transaction.setAlpha(view,(Float) value,newValue == null ? 1.0f : ((Float) newValue).floatValue());
            }
        }
        else if(property == Style.Scale) {

            float v = newValue == null ? 1.0f : ((Float) newValue).floatValue();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                view.setScaleX(v);
                view.setScaleY(v);
            }

            if(value != null) {
                Transaction.setScale(view,(Float) value,newValue == null ? 1.0f : ((Float) newValue).floatValue());
            }

        }
        else if(property == Layout.Frame) {

            Rect v = (Rect) view.getTag(R.id.Frame);
            Rect frame = newValue == null ? null : (Rect) ((Rect) newValue).clone();

            if(value != null && v != null && frame != null && ! v.equals(frame)) {
                Transaction.setFrame(view,v,frame);
            }

            view.setTag(R.id.Frame,frame);

            if (frame != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                view.setPivotX(frame.size.width * 0.5f);
                view.setPivotY(frame.size.height * 0.5f);
            }

            if(v == null || !v.equals(frame)) {
                view.requestLayout();
            }

        }
        else if(property == Style.Animation) {
            if(newValue != null  && !newValue.equals(value) ) {
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
            else if(newValue == null) {
                view.clearAnimation();
            }
        }
        else if(property == Style.Selected) {
            onStatusChanged();
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
            ViewGroup pp = ((ViewElement) element).viewGroup();
            if(pp != null) {
                View v = view();
                ViewParent p = v.getParent();
                if(p != null && p instanceof ViewGroup) {
                    ((ViewGroup)p).removeView(v);
                }

                pp.addView(v);

                Object newValue = get(Style.Animation);

                if(newValue != null) {
                    if (newValue instanceof AnimationElement) {
                        v.startAnimation(((AnimationElement) newValue).getAnimation());
                    } else if (newValue instanceof String) {
                        Animation anim = DocumentElement.getAnimation(this, (String) newValue);
                        if (anim != null) {
                            v.startAnimation(anim);
                        }
                    }
                }
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

    private static class OnCallback implements View.OnTouchListener{

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

    }

    public void startDraggable() {

        View v = view();

        if(v != null) {

            // Instantiates the drag shadow builder.
            View.DragShadowBuilder myShadow = new View.DragShadowBuilder(v);

            // Starts the drag
            v.startDrag(null,  // the data to be dragged
                    myShadow,  // the drag shadow builder
                    get(Style.Object),      // no need to use local data
                    0          // flags (not currently used, set to 0)
            );

        }

    }
}
