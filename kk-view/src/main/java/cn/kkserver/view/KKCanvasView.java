package cn.kkserver.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by zhanghailong on 2016/11/1.
 */

public class KKCanvasView extends KKView {

    public int backgroundColor;
    public KKValue borderRadius;
    public KKValue borderWidth;
    public int borderColor;

    private OnCallback _cb;

    public void setOnCallback(OnCallback cb) {
        _cb = cb;
    }

    public KKCanvasView(Context context) {
        super(context);
        setWillNotDraw(false);
    }

    public KKCanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        onDrawBackground(canvas);

        if(_cb != null) {
            _cb.onDraw(canvas);
        }

        onDrawBorder(canvas);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(_cb != null) {
            return _cb.onTouchEvent(event) || super.onTouchEvent(event);
        }

        return super.onTouchEvent(event);
    }

    protected void onDrawBackground(Canvas canvas){

        if((backgroundColor & 0x0ff000000) != 0 ){

            float width = canvas.getWidth();
            float height = canvas.getHeight();

            Paint paint = new Paint();

            paint.setAntiAlias(true);
            paint.setColor(backgroundColor & 0x0ffffff);
            paint.setAlpha((backgroundColor >> 24 ) & 0x0ff);
            paint.setStyle(Paint.Style.FILL);

            float radius = borderRadius != null ? borderRadius.floatValue(width) : 0f;
            float border = borderWidth != null ? borderWidth.floatValue(width) : 0f;
            float dw = border * 0.5f;

            if(radius == 0){
                canvas.drawRect(dw,dw,width -dw,height - dw,paint);
            }
            else {
                canvas.drawRoundRect(new RectF(dw,dw,width- dw,height- dw)
                        ,radius,radius  ,paint);
            }

        }
    }

    protected void onDrawBorder(Canvas canvas){

        float width = canvas.getWidth();

        float border = borderWidth != null ? borderWidth.floatValue(width) : 0f;

        if(border > 0f && (borderColor & 0x0ff000000) != 0){

            float height = canvas.getHeight();

            float radius = borderRadius != null ? borderRadius.floatValue(width) : 0f;

            Paint paint = new Paint();

            paint.setAntiAlias(true);
            paint.setColor(borderColor & 0x0ffffff);
            paint.setAlpha((borderColor >> 24) & 0x0ff);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(border);
            float dw = border * 0.5f;

            if(radius == 0){
                canvas.drawRect(dw,dw,width - dw ,height - dw,paint);
            }
            else {
                canvas.drawRoundRect(new RectF(dw,dw,width - dw,height - dw )
                        ,radius ,radius ,paint);
            }

        }

    }

    public static interface OnCallback {

        public void onDraw(Canvas canvas);

        boolean onTouchEvent(MotionEvent event);

    }
}
