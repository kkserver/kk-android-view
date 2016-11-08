package cn.kkserver.view.element;

import android.content.Context;
import android.graphics.Matrix;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.lang.ref.WeakReference;

import cn.kkserver.view.Property;
import cn.kkserver.view.value.Rect;
import cn.kkserver.view.style.Style;

/**
 * Created by zhanghailong on 2016/11/2.
 */

public class ButtonElement extends CanvasElement {


    private final LongActionRunnable _longActionRunnable = new LongActionRunnable(this);

    public ButtonElement(Context context) {
        super(context);
    }


    @Override
    protected boolean onTouchEvent(MotionEvent event) {

        if(super.onTouchEvent(event) == false) {
            Boolean enabled = get(Style.Enabled, Boolean.class, true);
            Boolean selected = get(Style.Selected, Boolean.class, false);

            if (enabled && !selected) {

                int action = event.getAction() & MotionEvent.ACTION_MASK;

                if (action == MotionEvent.ACTION_DOWN) {

                    ViewElementEvent ev = new ViewElementEvent(this,event);

                    ev.returnResult = true;

                    sendEvent(ViewElementEvent.HOVER,ev);

                    if(ev.returnResult) {

                        setHover(true);

                        final String name = get(Style.LongAction, String.class);

                        if (name != null) {

                            view().getHandler().postDelayed(_longActionRunnable, 800);

                        }

                        return true;
                    }

                    return false;

                } else if (action == MotionEvent.ACTION_UP) {

                    if (isHover()) {

                        view().getHandler().removeCallbacks(_longActionRunnable);

                        String v = get(Style.Action, String.class);

                        if (v != null) {

                            final ElementEvent e = new ElementEvent(this);
                            final String name = v;

                            view().getHandler().post(new Runnable() {
                                @Override
                                public void run() {
                                    e.element.sendEvent(name, e);
                                }
                            });

                        }

                        setHover(false);
                    }

                    return true;
                } else if (action == MotionEvent.ACTION_MOVE) {

                    Rect frame = get(Layout.Frame, Rect.class);
                    if (frame != null && event.getX() >= 0 && event.getX() < frame.size.width
                            && event.getY() >= 0 && event.getY() < frame.size.height) {
                        setHover(true);
                    } else {
                        setHover(false);
                        view().getHandler().removeCallbacks(_longActionRunnable);
                    }
                    return true;
                }
                else {
                    view().getHandler().removeCallbacks(_longActionRunnable);
                    setHover(false);
                    return true;
                }

            }
            return false;
        }

        return true;
    }

    @Override
    protected void onStatusChanged() {

        View view = view();

        if(view == null) {
            return ;
        }

        if(! view.isEnabled()) {
            set(Style.Status,"disabled");
        }
        else if(isSelected()) {
            set(Style.Status,"selected");
        }
        else if(isHover()) {
            set(Style.Status,"hover");
        }
        else {
            removeProperty(Style.Status);
        }
    }

    private boolean _hover;

    public boolean isHover() {
        return _hover;
    }

    public void setHover(boolean value) {
        _hover = value;
        onStatusChanged();
    }

    private boolean _selected;

    public boolean isSelected() {
        return _selected;
    }

    public void setSelected(boolean value) {
        _selected = value;
        onStatusChanged();
    }

    @Override
    protected void onPropertyChanged(Property property, Object value, Object newValue) {

        if(property == Style.Selected) {
            setSelected(newValue == null ? false: (Boolean) newValue);
        }

        super.onPropertyChanged(property,value,newValue);
    }

    @Override
    protected Element onCreateCloneElement() {
        return new ButtonElement(view().getContext());
    }

    private static class LongActionRunnable implements Runnable {

        private final WeakReference<ButtonElement> _element;

        public LongActionRunnable(ButtonElement element) {
            _element = new WeakReference<>(element);
        }

        @Override
        public void run() {
            ButtonElement e = _element.get();

            if(e != null) {

                String name = e.get(Style.LongAction,String.class);

                if(name != null) {
                    e.setHover(false);
                    e.sendEvent(name,new ElementEvent(e));
                }
            }
        }
    }
}
