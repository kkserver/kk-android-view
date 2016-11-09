package cn.kkserver.view.element;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import cn.kkserver.view.Property;
import cn.kkserver.view.style.Style;
import cn.kkserver.view.value.Color;
import cn.kkserver.view.value.KKValue;
import cn.kkserver.view.value.Rect;

/**
 * Created by zhanghailong on 2016/11/9.
 */

public class PageControlElement extends CanvasElement {


    public PageControlElement(Context context) {
        super(context);
    }

    @Override
    protected Element onCreateCloneElement() {
        return new PageControlElement(view().getContext());
    }

    @Override
    protected boolean needDisplayProperty(Property property) {
        return property == Style.TintColor || property == Style.Color || property == Style.PageIndex || property == Style.PageCount  || super.needDisplayProperty(property);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Rect frame = get(Layout.Frame,Rect.class);

        if(frame != null) {

            float width = 7 * KKValue.UNIT_DP;
            float radius = width * 0.5f;
            float padding = 10 * KKValue.UNIT_DP;

            Color color = get(Style.Color,Color.class,new Color(1.0f,1.0f,1.0f,0.5f));
            Color tintColor = get(Style.TintColor,Color.class,new Color(1.0f,1.0f,1.0f,1.0f));
            int pageCount = get(Style.PageCount,Integer.class,1);
            int pageIndex = get(Style.PageIndex,Integer.class,0);

            if(pageCount > 1) {

                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

                paint.setStyle(Paint.Style.FILL);

                float x = (frame.size.width - (width * pageCount + padding * (pageCount - 1)) ) * 0.5f;
                float y = (frame.size.height - width) *0.5f;

                for(int i=0;i<pageCount;i++ ){

                    if(pageIndex == i) {
                        paint.setColor( tintColor.value & 0x0ffffff);
                        paint.setAlpha( (tintColor.value >> 24) & 0x0ff);
                    }
                    else {
                        paint.setColor( color.value & 0x0ffffff);
                        paint.setAlpha( (color.value >> 24) & 0x0ff);
                    }

                    canvas.drawRoundRect(new RectF(x,y,x + width,y + width),radius,radius,paint);

                    x += width + padding;

                }

            }

        }
    }
}
