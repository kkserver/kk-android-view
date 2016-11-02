package cn.kkserver.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by zhanghailong on 2016/11/2.
 */

public class TextElement extends CanvasElement {

    public TextElement(Context context) {
        super(context);
        set(Style.Layout,new TextLayout());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        CharSequence text = get(Style.Text,CharSequence.class);

        if(text != null && text.length() > 0){

            KKEdge padding = get(Style.Padding,KKEdge.class,KKEdge.Zero);

            Font font = get(Style.Font,Font.class,Font.defaultFont);

            Color textColor = get(Style.Color,Color.class,Color.Blank);

            Paint paint = new Paint();

            paint.setAntiAlias(true);
            paint.setTextSize(font.size);
            paint.setFakeBoldText(font.bold);
            paint.setColor(textColor.value & 0x0ffffff);
            paint.setAlpha((textColor.value >> 24) & 0x0ff);

            float width = canvas.getWidth();
            float height = canvas.getHeight();
            KKValue maxWidth = get(Style.MaxWidth,KKValue.class,KKValue.Zero);
            KKValue maxHeight = get(Style.MaxHeight,KKValue.class,KKValue.Zero);

            Size textSize = getTextSize(text,paint,maxWidth.isZero() ? width : maxWidth.floatValue());

            float w = textSize.width;
            float h = textSize.height;

            if(!maxHeight.isZero() && h > maxHeight.floatValue()){
                h = maxHeight.floatValue();
            }

            float dy = padding.top.floatValue();

            VerticalAlignment valign = get(Style.VerticalAlign,VerticalAlignment.class,VerticalAlignment.TOP);

            if(valign == VerticalAlignment.MIDDLE){
                dy = ( height - h ) * 0.5f;
            }
            else if(valign == VerticalAlignment.BOTTOM){
                dy = ( height - h ) - padding.bottom.floatValue() ;
            }

            TextAlignment align = get(Style.TextAlign,TextAlignment.class,TextAlignment.LEFT);

            if(align == TextAlignment.LEFT){

                {

                    float[] widths = new float[1];

                    int start = 0;
                    int end = text.length();
                    int len;

                    while(start < end ){

                        len = paint.breakText(text, start, end, false, w, widths);

                        canvas.drawText(text, start, start + len, (width - widths[0]) / 2.0f, dy - paint.ascent(), paint);

                        dy +=  - paint.ascent() + paint.descent();

                        start += len;

                    }
                }

            }
            else if(align == TextAlignment.RIGHT){

                {

                    float[] widths = new float[1];

                    int start = 0;
                    int end = text.length();
                    int len;

                    while(start < end ){

                        len = paint.breakText(text, start, end, false, w, widths);

                        canvas.drawText(text, start, start +  len, width - widths[0] - padding.right.floatValue(), dy - paint.ascent(), paint);

                        dy +=  - paint.ascent() + paint.descent();

                        start += len;

                    }
                }

            }
            else {
                {

                    float[] widths = new float[1];

                    int start = 0;
                    int end = text.length();
                    int len;

                    while(start < end ){

                        len = paint.breakText(text, start, end, false, w, widths);

                        canvas.drawText(text, start, start +  len, padding.left.floatValue(), dy - paint.ascent(), paint);

                        dy +=  - paint.ascent() + paint.descent();

                        start += len;

                    }
                }
            }


        }

    }

    @Override
    protected boolean needDisplayProperty(Property property) {
        return property == Style.Text  || property == Style.Color
                || property == Style.Font  || property == Style.TextAlign
                || property == Style.VerticalAlign
                || super.needDisplayProperty(property);
    }

    public static Size getTextSize(CharSequence text, Paint paint, float maxWidth){

        Size size = new Size(0,0);

        float[] widths = new float[1];

        int start = 0;
        int end = text.length();
        int len;

        while(start < end ){

            len = paint.breakText(text, start, end, false, maxWidth, widths);

            if(widths[0] > size.width){
                size.width = (int) Math.ceil( widths[0]);
            }

            size.height = (int) Math.ceil(size.height - paint.ascent() + paint.descent());

            start += len;
        }

        return size;

    }

    public static class TextLayout extends Layout {

        @Override
        public Size layoutChildren(Element element) {

            Size size = new Size();
            KKEdge padding = element.get(Style.Padding,KKEdge.class, KKEdge.Zero);
            Rect frame = element.get(Frame,Rect.class,new Rect());
            int paddingLeft = padding.left.intValue(frame.size.width);
            int paddingTop = padding.top.intValue(frame.size.height);
            int paddingRight = padding.right.intValue(frame.size.width);
            int paddingBottom = padding.bottom.intValue(frame.size.height);
            KKValue width = element.get(Style.Width,KKValue.class, KKValue.Zero);
            KKValue height = element.get(Style.Height,KKValue.class, KKValue.Zero);

            if(width.isAuto()) {
                frame.size.width = Integer.MAX_VALUE;
            }

            if(height.isAuto()) {
                frame.size.height = Integer.MAX_VALUE;
            }

            if(width.isAuto() || height.isAuto()){

                CharSequence text = element.get(Style.Text,CharSequence.class);

                if(text != null && text.length() > 0){

                    Font font = element.get(Style.Font,Font.class,Font.defaultFont);

                    Paint paint = new Paint();

                    paint.setTextSize(font.size);
                    paint.setFakeBoldText(font.bold);

                    paint.breakText(text, 0, 0, false, 0, null);

                    KKValue maxWidth = element.get(Style.MaxWidth,KKValue.class,KKValue.Zero);
                    KKValue maxHeight = element.get(Style.MaxHeight,KKValue.class,KKValue.Zero);

                    Size textSize = TextElement.getTextSize(text,paint,maxWidth.isZero() ? frame.size.width : maxWidth.floatValue());

                    float w = textSize.width + paddingLeft + paddingRight;
                    float h = textSize.height + paddingTop + paddingBottom;

                    if(!maxHeight.isZero() && h > maxHeight.floatValue()){
                        h = maxHeight.floatValue();
                    }

                    if(width.isAuto()){
                        size.width = (int) Math.ceil(w + paddingLeft + paddingRight);
                        KKValue minWidth = element.get(Style.MinWidth,KKValue.class,KKValue.Zero);
                        if(!maxWidth.isZero() && size.width > maxWidth.intValue()){
                            size.width = maxWidth.intValue();
                        }
                        if(!minWidth.isZero() && size.width < minWidth.intValue()){
                            size.width = minWidth.intValue();
                        }
                    }

                    if(height.isAuto()){

                        size.height = (int) Math.ceil(h + paddingTop + paddingBottom);
                        KKValue minHeight = element.get(Style.MinHeight,KKValue.class,KKValue.Zero);
                        if(!maxHeight.isZero() && size.height > maxHeight.intValue()){
                            size.height = maxHeight.intValue();
                        }
                        if(!minHeight.isZero() && size.height < minHeight.intValue()){
                            size.height = minHeight.intValue();
                        }

                    }
                }
                else {

                    KKValue minWidth = element.get(Style.MinWidth,KKValue.class,KKValue.Zero);
                    KKValue minHeight = element.get(Style.MinHeight,KKValue.class,KKValue.Zero);

                    if(width.isAuto()){
                        size.width = minWidth.intValue();
                    }

                    if(height.isAuto()){
                        size.height = minHeight.intValue();
                    }
                }

            }
            else {
                size.width = frame.size.width;
                size.height = frame.size.height;
            }

            element.set(ContentSize,size);

            return size;
        }
    }

    @Override
    protected Element onCreateCloneElement() {
        return new TextElement(view().getContext());
    }
}
