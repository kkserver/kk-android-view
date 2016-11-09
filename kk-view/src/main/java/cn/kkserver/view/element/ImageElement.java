package cn.kkserver.view.element;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;

import cn.kkserver.view.Property;
import cn.kkserver.view.value.KKEdge;
import cn.kkserver.view.KKResource;
import cn.kkserver.view.value.KKValue;
import cn.kkserver.view.value.Rect;
import cn.kkserver.view.value.Size;
import cn.kkserver.view.style.Style;
import cn.kkserver.view.value.Font;

/**
 * Created by zhanghailong on 2016/11/2.
 */

public class ImageElement extends CanvasElement {

    private Drawable _defaultImage;
    private Drawable _image;
    private Drawable _failImage;
    private boolean _fail;
    private DefaultImageCallback _defaultCB;
    private FailImageCallback _failCB;
    private ImageCallback _cb;

    public void setFail(boolean fail) {
        _fail = fail;
        canvasView().invalidate();
    }

    public Drawable defaultImage() {

        if(_defaultImage == null) {
            String src = get(Style.DefaultSrc,String.class);
            if(src != null && _defaultCB == null) {
                try {
                    URL u = new URL(src);
                    _defaultCB = new DefaultImageCallback(this);
                    _defaultImage = KKResource.Shared.load(u,Drawable.class,_defaultCB);
                } catch (MalformedURLException e) {
                }
            }
        }

        return _defaultImage;
    }

    public Drawable failImage() {

        if(_failImage == null) {
            String src = get(Style.FailSrc,String.class);
            if(src != null && _failCB == null) {
                try {
                    URL u = new URL(src);
                    _failCB = new FailImageCallback(this);
                    _failImage = KKResource.Shared.load(u,Drawable.class,_failCB);
                } catch (MalformedURLException e) {
                }
            }
        }

        if(_failImage == null) {
            return defaultImage();
        }

        return _failImage;
    }

    public void setDefaultImage(Drawable image) {
        _defaultImage = image;
        canvasView().invalidate();
    }

    public void setImage(Drawable image) {
        _image = image;
        canvasView().invalidate();
    }

    public void setFailImage(Drawable image) {
        _failImage = image;
        canvasView().invalidate();
    }

    public Drawable image() {

        if(_image == null) {
            _image = get(Style.Image,Drawable.class);
        }

        if(_image == null) {

            String src = get(Style.Src,String.class);

            if(src != null && _cb == null) {
                try {
                    URL u = new URL(src);
                    _cb = new ImageCallback(this);
                    _image = KKResource.Shared.load(u, Drawable.class, _cb);
                } catch (MalformedURLException e) {
                }
            }
        }

        if(_image == null) {
            if (_fail) {
                return failImage();
            }

            return defaultImage();
        }

        return _image;
    }

    public ImageElement(Context context) {
        super(context);
        set(Style.Layout,new ImageElement.ImageLayout());
    }

    @Override
    protected boolean needDisplayProperty(Property property) {
        return property == Style.Src  || property == Style.DefaultSrc
                || property == Style.FailSrc || property == Style.Image
                || super.needDisplayProperty(property);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Drawable image = image();

        Rect frame = get(Layout.Frame,Rect.class);

        if(image != null && frame != null){

            float imageWidth = image.getIntrinsicWidth();
            float imageHeight = image.getIntrinsicHeight();
            float width = frame.size.width;
            float height = frame.size.height;

            float radius = get(Style.BorderRadius,KKValue.class,KKValue.Zero).floatValue(width);

            float tx = 0,ty = 0,rx = 1.0f,ry = 1.0f;

            String gravity = get(Style.Gravity,String.class,"aspect-fill");

            if("center".equals(gravity)){
                float dx = (imageWidth - width) / 2.0f;
                float dy = (imageHeight - height) / 2.0f;
                image.setBounds((int) (dx  ), (int) (dy  )
                        , (int) ( width  ), (int) (height  ));
            }
            else if("resize".equals(gravity)){
                rx = width / imageWidth;
                ry =  height / imageHeight;
                image.setBounds(0, 0, (int) ( imageWidth  ), (int) (imageHeight  ));
            }
            else if("top".equals(gravity)){
                float dx = (imageWidth - width) / 2.0f;
                float dy = 0;
                image.setBounds((int) (dx  ), (int) (dy  )
                        , (int) ( width  ), (int) (height  ));
            }
            else if("bottom".equals(gravity)){
                float dx = (imageWidth - width) / 2.0f;
                float dy = (imageHeight - height);
                image.setBounds((int) (dx  ), (int) (dy  )
                        , (int) ( width  ), (int) (height  ));
            }
            else if("left".equals(gravity)){
                float dx =0;
                float dy = (imageHeight - height) / 2.0f;
                image.setBounds((int) (dx  ), (int) (dy  )
                        , (int) ( width  ), (int) (height  ));
            }
            else if("right".equals(gravity)){
                float dx = (imageWidth - width) ;
                float dy = (imageHeight - height) / 2.0f;
                image.setBounds((int) (dx  ), (int) (dy  )
                        , (int) ( width  ), (int) (height  ));
            }
            else if("topleft".equals(gravity)){
                float dx = 0 ;
                float dy = 0;
                image.setBounds((int) (dx  ), (int) (dy  )
                        , (int) ( width  ), (int) (height  ));
            }
            else if("topright".equals(gravity)){
                float dx = (imageWidth - width) ;
                float dy = 0;
                image.setBounds((int) (dx  ), (int) (dy  )
                        , (int) ( width  ), (int) (height  ));
            }
            else if("bottomleft".equals(gravity)){
                float dx = 0 ;
                float dy = (imageHeight - height);
                image.setBounds((int) (dx  ), (int) (dy  )
                        , (int) ( width  ), (int) (height  ));
            }
            else if("bottomright".equals(gravity)){
                float dx = (imageWidth - width) ;
                float dy = (imageHeight - height);
                image.setBounds((int) (dx  ), (int) (dy  )
                        , (int) ( width  ), (int) (height  ));
            }
            else if("aspect".equals(gravity)){
                float r0 = imageWidth / imageHeight;
                float r1 = width / height;
                if(r0 == r1){
                    image.setBounds(0, 0, (int) ( imageWidth  ), (int) (imageHeight  ));
                }
                else if(r0 > r1){

                    rx = width / imageWidth;
                    ry = width / imageWidth;

                    imageHeight = width / r0;
                    imageWidth = width;

                    image.setBounds(0, (int) (imageHeight - height), (int) ( width  ), (int) (height  ));
                }
                else if(r0 < r1){

                    rx = height / imageHeight;
                    ry = height / imageHeight;

                    imageWidth = height * r0;
                    imageHeight = height;

                    image.setBounds((int) (imageWidth - width),0, (int) ( width  ), (int) (height  ));
                }
            }
            else if("aspect-top".equals(gravity)){
                float r0 = imageWidth / imageHeight;
                float r1 = width / height;
                if(r0 == r1){
                    rx = width / imageWidth;
                    ry = height / imageHeight;
                    image.setBounds(0, 0, (int) ( imageWidth  ), (int) (imageHeight  ));
                }
                else if(r0 < r1){

                    rx = width / imageWidth;
                    ry = width / imageWidth;

                    imageHeight = width / r0;
                    imageWidth = width;

                    image.setBounds(0, 0, (int) ( imageWidth  ), (int) (imageHeight  ));
                }
                else if(r0 > r1){

                    rx = height / imageHeight;
                    ry = height / imageHeight;

                    imageWidth = height * r0;
                    imageHeight = height;
                    image.setBounds(0,0, (int) ( imageWidth  ), (int) (imageHeight  ));
                }
            }
            else if("aspect-bottom".equals(gravity)){

                float r0 = imageWidth / imageHeight;
                float r1 = width / height;
                if(r0 == r1){
                    rx = width / imageWidth;
                    ry = height / imageHeight;
                    image.setBounds(0, 0, (int) ( imageWidth  ), (int) (imageHeight  ));
                }
                else if(r0 < r1){

                    rx = width / imageWidth;
                    ry = width / imageWidth;

                    imageHeight = width / r0;
                    imageWidth = width;

                    ty = (height - imageHeight)  ;

                    image.setBounds(0, 0, (int) ( imageWidth  ), (int) (imageHeight  ));
                }
                else if(r0 > r1){


                    rx = height / imageHeight;
                    ry = height / imageHeight;

                    imageWidth = height * r0;
                    imageHeight = height;

                    tx = (width - height)  ;

                    image.setBounds(0 , 0, (int) ( imageWidth  ), (int) (imageHeight  ));
                }

            }
            else {

                float r0 = imageWidth / imageHeight;
                float r1 = width / height;
                if(r0 == r1){
                    rx = width / imageWidth;
                    ry = height / imageHeight;
                    image.setBounds(0, 0, (int) ( imageWidth  ), (int) (imageHeight  ));
                }
                else if(r0 < r1){

                    rx = width / imageWidth;
                    ry = width / imageWidth;

                    ty = (height - width / r0)   * 0.5f;

                    image.setBounds(0, 0, (int) ( imageWidth  ), (int) (imageHeight  ));
                }
                else if(r0 > r1){

                    rx = height / imageHeight;
                    ry = height / imageHeight;

                    tx = (width - height * r0)   * 0.5f;

                    image.setBounds(0 , 0, (int) ( imageWidth  ), (int) (imageHeight  ));
                }

            }

            if(radius != 0.0f && image instanceof BitmapDrawable){

                int x = (int) (width );
                int y = (int) (height );
                float[] mOuter = new float[] { radius, radius, radius, radius,
                        radius, radius, radius, radius };


                // 新建一个矩形
                RectF outerRect = new RectF(0, 0, x, y);

                Paint paint = new Paint();

                paint.setAntiAlias(true);
                paint.setColor(0xffffffff);

                canvas.saveLayer(outerRect, paint, Canvas.CLIP_SAVE_FLAG);

                Path mPath = new Path();
                // 创建一个圆角矩形路径
                mPath.addRoundRect(outerRect, mOuter, Path.Direction.CW);

                canvas.clipPath(mPath);

                canvas.drawRoundRect(outerRect, radius, radius, paint);

                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

                canvas.translate(tx, ty);
                canvas.scale(rx, ry);

                canvas.drawBitmap(((BitmapDrawable) image).getBitmap(), 0, 0, paint);

                canvas.restore();

            }
            else{
                canvas.translate(tx, ty);
                canvas.scale(rx, ry);
                image.draw(canvas);
            }
        }
    }

    @Override
    protected void onPropertyChanged(Property property,Object value,Object newValue) {

        if(property == Style.Src || property == Style.Image) {
            _image = null;
            _fail = false;
            if(_cb != null) {
                _cb.recycle();
                _cb = null;
            }
        }
        else if(property == Style.DefaultSrc) {
            _defaultImage = null;
            if(_defaultCB != null) {
                _defaultCB.recycle();
                _defaultCB = null;
            }
        }
        else if(property == Style.FailSrc) {
            _failImage = null;
            if(_failCB != null) {
                _failCB.recycle();
                _failCB = null;
            }
        }

        super.onPropertyChanged(property,value,newValue);
    }

    private static class WeakCallback<T extends Object> implements KKResource.Callback<T>{

        private WeakReference<ImageElement> _element;

        public WeakCallback(ImageElement element) {
            _element = new WeakReference<>(element);
        }

        @Override
        public void onLoaded(T object) {
            ImageElement e ;
            if(_element != null && (e = _element.get()) != null) {
                onLoadedElement(e,object);
            }
        }

        @Override
        public void onFail(Throwable ex) {
            ImageElement e;
            if(_element != null && (e = _element.get()) != null) {
                onFailElement(e,ex);
            }
        }

        public void recycle() {
            _element = null;
        }

        protected void onLoadedElement(ImageElement element,T object) {

        }

        protected void onFailElement(ImageElement element,Throwable ex) {

        }
    }

    public static class DefaultImageCallback extends WeakCallback<Drawable> {

        public DefaultImageCallback(ImageElement element) {
            super(element);
        }

        @Override
        protected void onLoadedElement(ImageElement element,Drawable object) {
            element.setDefaultImage(object);
        }

    }

    public static class FailImageCallback extends WeakCallback<Drawable> {

        public FailImageCallback(ImageElement element) {
            super(element);
        }

        @Override
        protected void onLoadedElement(ImageElement element,Drawable object) {
            element.setFailImage(object);
        }

    }

    public static class ImageCallback extends WeakCallback<Drawable> {

        public ImageCallback(ImageElement element) {
            super(element);
        }

        @Override
        protected void onLoadedElement(ImageElement element,Drawable object) {
            element.setImage(object);
        }

        @Override
        protected void onFailElement(ImageElement element,Throwable ex) {
            element.setFail(true);
        }

    }

    public static class ImageLayout extends Layout {

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
        return new ImageElement(view().getContext());
    }
}
