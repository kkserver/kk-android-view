package cn.kkserver.view.value;

/**
 * Created by zhanghailong on 16/7/14.
 */
public class Rect implements Cloneable {

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

    public float centerX() {
        return origin.x + size.width * 0.5f;
    }

    public float centerY() {
        return origin.y + size.height * 0.5f;
    }

    @Override
    public int hashCode() {
        return size.hashCode() ^ origin.hashCode();
    }

    @Override
    public boolean equals(Object value) {
        if(value != null && value instanceof Rect) {
            Rect v = (Rect) value;
            return (origin == v.origin || (origin != null && origin.equals(v.origin)))
                    &&  (size == v.size || (size != null && size.equals(v.size)));
        }
        return false;
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


    @Override
    public Object clone() {
        try {
            Rect v = (Rect) super.clone();
            if(origin != null) {
                v.origin = (Point) origin.clone();
            }
            if(size != null) {
                v.size = (Size) size.clone();
            }
            return v;
        } catch (CloneNotSupportedException e) {}
        return null;
    }

}
