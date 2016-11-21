package cn.kkserver.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;

import cn.kkserver.view.value.KKValue;
import cn.kkserver.view.value.Rect;

/**
 * Created by zhanghailong on 2016/11/1.
 */

public class KKCanvasView extends KKView {

    public int backgroundColor;
    public KKValue borderRadius;
    public KKValue borderWidth;
    public int borderColor;

    private OnDrawCallback _drawCallback;

    public void setOnDrawCallback(OnDrawCallback cb) {
        _drawCallback = cb;
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

        if(_drawCallback != null) {
            _drawCallback.onDraw(canvas);
        }

        onDrawBorder(canvas);

    }

    protected void onDrawBackground(Canvas canvas){

        if(backgroundColor == 0xffdddddd) {
            System.out.println();
        }
        if((backgroundColor & 0x0ff000000) != 0 ){

            Rect frame = (Rect) getTag(R.id.Frame);
            float width = frame == null ? canvas.getWidth() : frame.size.width;
            float height = frame == null ? canvas.getHeight() : frame.size.height;

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
                canvas.drawRoundRect(new RectF(dw,dw,width - dw,height - dw )
                        ,radius ,radius ,paint);
            }

        }
    }

    protected void onDrawBorder(Canvas canvas){

        Rect frame = (Rect) getTag(R.id.Frame);
        float width = frame == null ? canvas.getWidth() : frame.size.width;
        float height = frame == null ? canvas.getHeight() : frame.size.height;

        float border = borderWidth != null ? borderWidth.floatValue(width) : 0f;

        if(border > 0f && (borderColor & 0x0ff000000) != 0){

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

    @Override
    public void setBackgroundColor(int value) {
        backgroundColor = value;
    }

    public static interface OnDrawCallback {

        public void onDraw(Canvas canvas);

    }
}
