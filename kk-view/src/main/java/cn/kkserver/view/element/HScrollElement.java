package cn.kkserver.view.element;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import cn.kkserver.view.KKView;
import cn.kkserver.view.Property;
import cn.kkserver.view.R;
import cn.kkserver.view.value.Rect;
import cn.kkserver.view.value.Size;

/**
 * Created by zhanghailong on 2016/11/14.
 */

public class HScrollElement extends ForeachElement {

    private ViewGroup _viewGroup;

    public HorizontalScrollView scrollView() {
        return (HorizontalScrollView) view();
    }

    @Override
    public ViewGroup viewGroup() {
        return _viewGroup;
    }

    public HScrollElement(HorizontalScrollView view) {
        super(view);
        initHScrollElement();
    }

    public HScrollElement(Context context) {
        super(new HorizontalScrollView(context));
        initHScrollElement();
    }

    protected void initHScrollElement() {
        HorizontalScrollView view = scrollView();
        _viewGroup = new KKView(view.getContext());
        _viewGroup.setLayoutParams(new ScrollView.LayoutParams(
                ScrollView.LayoutParams.WRAP_CONTENT,ScrollView.LayoutParams.MATCH_PARENT
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
