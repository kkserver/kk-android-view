package cn.kkserver.view.element;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import java.lang.ref.WeakReference;

import cn.kkserver.view.KKView;
import cn.kkserver.view.Property;
import cn.kkserver.view.R;
import cn.kkserver.view.value.Rect;
import cn.kkserver.view.value.Size;

/**
 * Created by zhanghailong on 2016/11/14.
 */

public class VScrollElement extends ForeachElement {

    private ViewGroup _viewGroup;

    public ScrollView scrollView() {
        return (ScrollView) view();
    }

    @Override
    public ViewGroup viewGroup() {
        return _viewGroup;
    }

    public VScrollElement(ScrollView view) {
        super(view);
        initVScrollElement();
    }

    public VScrollElement(Context context) {
        this(new ScrollView(context));
        initVScrollElement();
    }

    protected void initVScrollElement() {
        ScrollView view = scrollView();
        _viewGroup = new KKView(view.getContext());
        _viewGroup.setLayoutParams(new ScrollView.LayoutParams(
                ScrollView.LayoutParams.MATCH_PARENT,ScrollView.LayoutParams.WRAP_CONTENT
        ));
        view.addView(_viewGroup);
    }

    @Override
    protected void onPropertyChanged(Property property, Object value, Object newValue) {

        if(property == Layout.ContentSize) {
            Size size = (Size) newValue;
            if(size != null) {
                _viewGroup.setTag(R.id.Frame,new Rect(0,0,size.width,size.height));
            }
            else {
                _viewGroup.setTag(R.id.Frame,null);
            }
            _viewGroup.requestLayout();
        }

        super.onPropertyChanged(property,value,newValue);
    }

}
