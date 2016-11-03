package cn.kkserver.view.value;

/**
 * Created by zhanghailong on 16/7/14.
 */
public class KKPoint {

    public KKValue x;
    public KKValue y;

    public KKPoint() {
        this.x = new KKValue();
        this.y = new KKValue();
    }

    public KKPoint(KKValue x, KKValue y) {
        this.x = x;
        this.y = y;
    }

    public final static KKPoint Zero = new KKPoint();

    public static class Property extends cn.kkserver.view.Property {

        public Property(String name) {
            super(name);
        }

        @Override
        public Object valueOf(Object value) {

            if(value != null) {

                if( value instanceof KKPoint) {
                    return value;
                }

                if( value instanceof Point) {
                    return new KKPoint(new KKValue(((Point) value).x),new KKValue(((Point) value).y));
                }

                if( value instanceof String) {
                    String[] vs = ((String) value).split(" ");
                    return new KKPoint(KKValue.valueOf(vs[0]),vs.length > 1 ? KKValue.valueOf(vs[1]) : new KKValue());
                }

            }

            return null;
        }
    }
}
