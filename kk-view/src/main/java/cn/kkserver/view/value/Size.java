package cn.kkserver.view.value;

/**
 * Created by zhanghailong on 16/7/14.
 */
public class Size implements Cloneable {

    public int width;
    public int height;

    public Size() {
        this.width = this.height = 0;
    }

    public Size(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public static Size Zero = new Size(0,0);

    public static class Property extends cn.kkserver.view.Property {

        public Property(String name) {
            super(name);
        }

        @Override
        public Object valueOf(Object value) {

            if(value != null) {

                if( value instanceof Size) {
                    return value;
                }

            }

            return null;
        }
    }

    @Override
    public int hashCode() {
        return width ^ height;
    }

    @Override
    public boolean equals(Object value) {
        if(value != null && value instanceof Size) {
            Size v = (Size) value;
            return v.width == width && v.height == height;
        }
        return false;
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {}
        return null;
    }

}
