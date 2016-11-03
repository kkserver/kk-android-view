package cn.kkserver.view.value;

/**
 * Created by zhanghailong on 2016/11/1.
 */

public enum TextAlignment {

    LEFT,CENTER,RIGHT;

    public static class Property extends cn.kkserver.view.Property {

        public Property(String name) {
            super(name);
        }

        @Override
        public Object valueOf(Object value) {

            if(value != null) {

                if( value instanceof TextAlignment) {
                    return value;
                }

                if( value instanceof String) {
                    if("center".equals(name)) {
                        return CENTER;
                    }
                    if("right".equals(name)) {
                        return RIGHT;
                    }
                    return LEFT;
                }
            }
            return null;
        }
    }
}
