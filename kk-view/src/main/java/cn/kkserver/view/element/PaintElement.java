package cn.kkserver.view.element;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import cn.kkserver.view.style.Style;
import cn.kkserver.view.value.Color;
import cn.kkserver.view.value.KKValue;
import cn.kkserver.view.value.Rect;

/**
 * Created by zhanghailong on 2016/11/7.
 */

public class PaintElement extends Element implements IElementCreator {

    @Override
    protected Element onCreateCloneElement() {
        return new PaintElement();
    }

    public void draw(Canvas canvas) {

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        Color color = get(Style.Color, Color.class,Color.Clear);

        paint.setColor( color.value & 0x0ffffff);
        paint.setAlpha( (color.value >> 24) & 0x0ff);
        paint.setStrokeWidth(get(Style.Width, KKValue.class,KKValue.Zero).floatValue());

        Path path = new Path();

        Rect frame = parent().get(Layout.Frame,Rect.class);

        float width = frame == null ? canvas.getWidth() : frame.size.width;
        float height = frame == null ? canvas.getHeight() : frame.size.height;

        Element e = firstChild();

        while(e != null) {

            if(e instanceof MoveToElement) {
                float x = e.get(Style.Left,KKValue.class,KKValue.Zero).floatValue(width);
                float y = e.get(Style.Top,KKValue.class,KKValue.Zero).floatValue(width);
                path.moveTo(x,y);
            }
            else if(e instanceof LineToElement) {
                float x = e.get(Style.Left,KKValue.class,KKValue.Zero).floatValue(width);
                float y = e.get(Style.Top,KKValue.class,KKValue.Zero).floatValue(height);
                path.lineTo(x,y);
            }
            else if(e instanceof ArcToElement) {
                float left = e.get(Style.Left,KKValue.class,KKValue.Zero).floatValue(width);
                float top = e.get(Style.Top,KKValue.class,KKValue.Zero).floatValue(height);
                float right = e.get(Style.Right,KKValue.class,KKValue.Zero).floatValue(width);
                float bottom = e.get(Style.Bottom,KKValue.class,KKValue.Zero).floatValue(height);
                path.arcTo(new RectF(left,top,right,bottom)
                        ,(float) (e.get(Style.FromDegrees,Float.class,0f) * Math.PI / 180f)
                        ,(float) (e.get(Style.ToDegrees,Float.class,0f) * Math.PI / 180f)
                        ,true
                );
            }
            else if(e instanceof DrawElement) {

                String type = e.get(Style.Type,String.class);

                if("fill".equals(type)) {
                    paint.setStyle(Paint.Style.FILL);
                }
                else if("stroke".equals(type)) {
                    paint.setStyle(Paint.Style.STROKE);
                }
                else {
                    paint.setStyle(Paint.Style.FILL_AND_STROKE);
                }

                canvas.drawPath(path,paint);

                path.reset();
            }

            e = e.nextSibling();
        }

    }

    @Override
    public Element onCreateElement(String name) throws Throwable {
        if("moveto".equals(name)) {
            return new MoveToElement();
        }
        else if("lineto".equals(name)) {
            return new LineToElement();
        }
        else if("arcto".equals(name)) {
            return new ArcToElement();
        }
        else if("draw".equals(name)) {
            return new DrawElement();
        }
        return null;
    }

    public static class MoveToElement extends Element {

        @Override
        protected Element onCreateCloneElement() {
            return new MoveToElement();
        }

    }

    public static class LineToElement extends Element {

        @Override
        protected Element onCreateCloneElement() {
            return new LineToElement();
        }

    }

    public static class ArcToElement extends Element {

        @Override
        protected Element onCreateCloneElement() {
            return new ArcToElement();
        }

    }

    public static class DrawElement extends Element {

        @Override
        protected Element onCreateCloneElement() {
            return new DrawElement();
        }

    }
}
