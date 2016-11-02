package cn.kkserver.view;

/**
 * Created by zhanghailong on 16/7/14.
 */
public class Size {

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

}
