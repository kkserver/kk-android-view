package cn.kkserver.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import cn.kkserver.view.value.Rect;

/**
 * Created by zhanghailong on 2016/11/1.
 */

public class KKView extends ViewGroup {

    public KKView(Context context) {
        super(context);
    }

    public KKView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int count = getChildCount();

        for(int i= 0 ;i<count; i++ ){
            View v = getChildAt(i);
            if(v.getVisibility() == View.VISIBLE) {
                Object f = v.getTag(R.id.Frame);
                if (f != null && f instanceof Rect) {
                    Rect frame = (Rect) f;
                    v.layout(frame.left(), frame.top(), frame.right(), frame.bottom());
                }
            }
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = getDefaultSize(Integer.MAX_VALUE,widthMeasureSpec);
        int height = getDefaultSize(Integer.MAX_VALUE,heightMeasureSpec);

        setMeasuredDimension(width,height);

        measureChildren(widthMeasureSpec,heightMeasureSpec);

    }

}
