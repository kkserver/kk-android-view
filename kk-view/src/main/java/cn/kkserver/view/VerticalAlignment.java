package cn.kkserver.view;

/**
 * Created by zhanghailong on 2016/11/1.
 */

public enum VerticalAlignment {

    TOP,MIDDLE,BOTTOM;

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
                    if("middle".equals(name)) {
                        return MIDDLE;
                    }
                    if("bottom".equals(name)) {
                        return BOTTOM;
                    }
                    return TOP;
                }
            }
            return null;
        }
    }

}
