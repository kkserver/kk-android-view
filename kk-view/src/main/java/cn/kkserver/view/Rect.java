package cn.kkserver.view;

/**
 * Created by zhanghailong on 16/7/14.
 */
public class Rect {

    public Point origin;
    public Size size;

    public Rect() {
        this.origin = new Point();
        this.size = new Size();
    }

    public Rect(int x,int y,int width,int height) {
        this.origin = new Point(x,y);
        this.size = new Size(width,height);
    }

    public static Rect Zero = new Rect();

    public int right() {
        return origin.x + size.width;
    }

    public int bottom() {
        return origin.y + size.height;
    }

    public int left() {
        return origin.x;
    }

    public int top() {
        return origin.y;
    }

    public static class Property extends cn.kkserver.view.Property {

        public Property(String name) {
            super(name);
        }

        @Override
        public Object valueOf(Object value) {

            if(value != null) {

                if( value instanceof Rect) {
                    return value;
                }
            }

            return null;
        }
    }
}
