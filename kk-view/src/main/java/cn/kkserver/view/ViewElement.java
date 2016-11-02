package cn.kkserver.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.FloatRange;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;


/**
 * Created by zhanghailong on 2016/11/1.
 */

public class ViewElement extends Element {

    private View _view;

    public View view() {
        return _view;
    }

    public ViewElement(View view) {
        _view = view;
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
    }

    @Override
    protected void onPropertyChanged(Property property,Object value,Object newValue) {

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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                @FloatRange(from=0.0, to=1.0) float v = newValue == null ? 1.0f : ((Float) newValue).floatValue();
                view.setAlpha(v);
            }
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
            if(newValue != null ) {
                if(newValue instanceof AnimationElement) {
                    view.startAnimation(((AnimationElement) newValue).getAnimation());
                }
                else if(newValue instanceof String) {
                    AnimationElement anim = null;
                    Element p = parent();
                    while(p != null) {
                        if(p instanceof KKDocumentView.DocumentElement){
                            anim = ((KKDocumentView.DocumentElement) p).animation((String) newValue);
                        }
                        p = p.parent();
                    }
                    if(anim != null) {
                        view.startAnimation(anim.getAnimation());
                    }
                    else {
                        view.clearAnimation();
                    }
                }
                else {
                    view.clearAnimation();
                }
            }
            else {
                view.clearAnimation();
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
        View view = null;
        try {
            view = _view == null ? null : _view.getClass().getConstructor(Context.class).newInstance(_view.getContext());
        } catch (Throwable e) {
        }
        return new ViewElement(view);
    }

}
