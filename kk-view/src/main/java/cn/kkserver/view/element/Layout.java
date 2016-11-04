package cn.kkserver.view.element;

import cn.kkserver.view.style.Style;
import cn.kkserver.view.value.KKEdge;
import cn.kkserver.view.value.KKValue;
import cn.kkserver.view.value.Point;
import cn.kkserver.view.value.Rect;
import cn.kkserver.view.value.Size;

/**
 * Created by zhanghailong on 2016/11/1.
 */

public class Layout extends Object {

    public final static cn.kkserver.view.Property Frame = new Rect.Property("frame");
    public final static cn.kkserver.view.Property ContentSize = new Size.Property("content-size");

    public static void elementLayoutChildren(Element element) {
        Layout layout = element.get(Style.Layout,Layout.class);
        if(layout != null) {
            layout.layoutChildren(element);
        }
    }

    public static void elementLayout(Element element,Size size) {
        Layout layout = element.get(Style.Layout,Layout.class);
        if(layout != null) {
            layout.layout(element,size);
        }
    }

    public void layout(Element element, Size size) {

        KKValue width = element.get(Style.Width,KKValue.class, KKValue.Zero);
        KKValue height = element.get(Style.Height,KKValue.class, KKValue.Zero);

        Rect r = element.get(Frame,Rect.class,new Rect());

        r.size.width = width.isAuto() ? 0 : width.intValue(size.width);
        r.size.height = height.isAuto() ? 0 : height.intValue(size.height);

        if(width.isAuto() || height.isAuto()){

            element.set(Frame,r);

            Layout layout = element.get(Style.Layout,Layout.class);

            Size contentSize = layout == null ? Size.Zero : layout.layoutChildren(element);

            if(width.isAuto()){

                KKValue maxWidth = element.get(Style.MaxWidth,KKValue.class, KKValue.Zero);
                KKValue minWidth = element.get(Style.MinWidth,KKValue.class, KKValue.Zero);

                r.size.width = contentSize.width;

                if(! maxWidth.isZero()) {
                    int v = maxWidth.intValue(size.width);
                    if(r.size.width > v) {
                        r.size.width = v;
                    }
                }

                if(! minWidth.isZero()) {
                    int v = minWidth.intValue( size.width);
                    if(r.size.width < v) {
                        r.size.width = v;
                    }
                }

            }

            if(height.isAuto()){

                KKValue maxHeight = element.get(Style.MaxHeight,KKValue.class, KKValue.Zero);
                KKValue minHeight = element.get(Style.MinHeight,KKValue.class, KKValue.Zero);

                r.size.height = contentSize.height;

                if(! maxHeight.isZero()) {
                    int v = maxHeight.intValue(size.height);
                    if(r.size.height > v) {
                        r.size.height = v;
                    }
                }

                if(! minHeight.isZero()) {
                    int v = minHeight.intValue(size.height);
                    if(r.size.height < v) {
                        r.size.height = v;
                    }
                }
            }
            element.set(Frame,r);
        }
        else {

            element.set(Frame,r);

            Layout layout = element.get(Style.Layout,Layout.class);

            if(layout != null) {
                layout.layoutChildren(element);
            }

        }

    }

    public Size layoutChildren(Element element) {
        Rect v = element.get(Frame,Rect.class);
        if(v != null) {
            return v.size;
        }
        return Size.Zero;
    }

    public static class RelativeLayout extends Layout {

        @Override
        public Size layoutChildren(Element element) {

            Size size = new Size(0,0);
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

            Size inSize = new Size(frame.size.width - paddingLeft - paddingRight, frame.size.height - paddingTop - paddingBottom);

            size.width = paddingLeft + paddingRight;
            size.height = paddingTop + paddingBottom;

            Element p = element.firstChild();

            while(p != null) {

                Layout layout = p.get(Style.Layout,Layout.class);

                Boolean hidden = p.get(Style.Hidden,Boolean.class,false);

                if(layout != null && ! hidden){

                    layout.layout(p,inSize);

                    Rect r = p.get(Frame,Rect.class,new Rect());

                    KKValue left = p.get(Style.Left,KKValue.class, KKValue.Zero);
                    KKValue right = p.get(Style.Right,KKValue.class, KKValue.Zero);
                    KKValue top = p.get(Style.Top,KKValue.class, KKValue.Zero);
                    KKValue bottom = p.get(Style.Bottom,KKValue.class, KKValue.Zero);

                    if(left.isAuto()){
                        if(right.isAuto()){
                            r.origin.x = paddingLeft + (int) ((inSize.width - r.size.width) * 0.5f);
                        }
                        else{
                            r.origin.x = paddingLeft + (inSize.width - r.size.width - right.intValue(inSize.width));
                        }
                    }
                    else {
                        r.origin.x = paddingLeft + left.intValue(inSize.width);
                    }

                    if(top.isAuto()){
                        if(bottom.isAuto()){
                            r.origin.y = paddingTop + (int) ((inSize.height - r.size.height) * 0.5f);
                        }
                        else {
                            r.origin.y = paddingTop + (inSize.height - r.size.height - bottom.intValue(inSize.height));
                        }
                    }
                    else {
                        r.origin.y = paddingTop + top.intValue(inSize.height);
                    }

                    if(r.origin.x + r.size.width + paddingRight > size.width){
                        size.width = r.origin.x + r.size.width + paddingRight;
                    }

                    if(r.origin.y + r.size.height + paddingBottom > size.height ){
                        size.height = r.origin.y + r.size.height +paddingBottom;
                    }

                    p.set(Frame,r);

                }

                p = p.nextSibling();
            }

            element.set(ContentSize,size);

            return size;
        }

    }

    public static class FlowLayout extends Layout {

        public final boolean nowarp;

        public FlowLayout(boolean nowarp) {
            this.nowarp = nowarp;
        }

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

            Size inSize = new Size(frame.size.width - paddingLeft - paddingRight, frame.size.height - paddingTop - paddingBottom);

            Point p = new Point(paddingLeft, paddingTop);
            int lineHeight = 0;
            size.width = paddingLeft + paddingRight;
            int maxWidth = frame.size.width;

            KKValue v = element.get(Style.MaxWidth,KKValue.class, KKValue.Zero);

            if(width.isAuto() && ! v.isZero()) {
                maxWidth = v.intValue(frame.size.width);
            }

            Element e = element.firstChild();

            while(e != null) {

                Layout layout = e.get(Style.Layout,Layout.class);
                Boolean hidden = e.get(Style.Hidden,Boolean.class,false);

                if(layout != null && !hidden){

                    KKEdge margin = e.get(Style.Margin,KKEdge.class, KKEdge.Zero);
                    int marginLeft =  margin.left.intValue(inSize.width);
                    int marginTop = margin.left.intValue(inSize.height);
                    int marginRight = margin.left.intValue(inSize.width);
                    int marginBottom = margin.left.intValue(inSize.height);;

                    layout.layout(e,new Size(inSize.width - marginLeft - marginRight,inSize.height - marginTop - marginBottom));

                    Rect r = e.get(Frame,Rect.class,new Rect());

                    if(nowarp || ( p.x + r.size.width + marginLeft + marginRight <= maxWidth - paddingRight)){

                        r.origin.x = p.x + marginLeft;
                        r.origin.y = p.y + marginTop;

                        p.x += r.size.width + marginLeft + marginRight;

                        if(lineHeight < r.size.height + marginTop + marginBottom){
                            lineHeight = r.size.height + marginTop + marginBottom;
                        }
                        if(size.width < p.x + paddingRight){
                            size.width = p.x + paddingRight;
                        }
                    }
                    else {
                        p.x = paddingLeft;
                        p.y += lineHeight;
                        lineHeight = r.size.height + marginTop + marginBottom;
                        r.origin.x = p.x + marginLeft;
                        r.origin.y = p.y + marginTop;
                        p.x += r.size.width + marginLeft + marginRight;
                        if(size.width < p.x + paddingRight){
                            size.width = p.x + paddingRight;
                        }
                    }

                    e.set(Frame,r);

                }

                e = e.nextSibling();
            }

            size.height = p.y + lineHeight + paddingBottom;

            element.set(ContentSize,size);

            return size;
        }

    }

    public static class Property extends cn.kkserver.view.Property {

        public Property(String name) {
            super(name);
        }

        @Override
        public Object valueOf(Object value) {

            if(value != null) {

                if( value instanceof Layout) {
                    return value;
                }

                if( value instanceof String) {
                    if("relative".equals(value)) {
                        return new RelativeLayout();
                    }
                    if("flow".equals(value)) {
                        return new FlowLayout(false);
                    }
                    if("flow-nowarp".equals(value)) {
                        return new FlowLayout(true);
                    }
                    if("none".equals(value)) {
                        return new Layout();
                    }
                }
            }

            return null;
        }
    }

}
