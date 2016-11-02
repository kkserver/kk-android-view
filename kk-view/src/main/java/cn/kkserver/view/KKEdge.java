package cn.kkserver.view;

/**
 * Created by zhanghailong on 2016/11/1.
 */

public class KKEdge extends Object {

    public KKValue left;
    public KKValue top;
    public KKValue right;
    public KKValue bottom;

    public KKEdge() {
        this.left = new KKValue();
        this.top = new KKValue();
        this.right = new KKValue();
        this.bottom = new KKValue();
    }

    public KKEdge(KKValue left, KKValue top, KKValue right, KKValue bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public KKEdge(float left, float top, float right, float bottom) {
        this.left = new KKValue(left);
        this.top = new KKValue(top);
        this.right = new KKValue(right);
        this.bottom = new KKValue(bottom);
    }

    public KKEdge(KKEdge value) {
        this.left = new KKValue(value.left);
        this.top = new KKValue(value.top);
        this.right = new KKValue(value.right);
        this.bottom = new KKValue(value.bottom);
    }

    @Override
    public int hashCode() {
        return this.left.hashCode() ^ this.top.hashCode() ^ this.right.hashCode() ^ this.bottom.hashCode();
    }

    @Override
    public boolean equals(Object value) {
        KKEdge v = (KKEdge) value;
        return v.left.equals(left) && v.top.equals(top) && v.right.equals(right) && v.bottom.equals(bottom);
    }

    public final static KKEdge Zero = new KKEdge();

    public static KKEdge valueOf(String value) {

        if(value == null) {
            return null;
        }

        String[] vs = value.split(" ");

        KKEdge v = new KKEdge();

        if(vs.length >0) {
            v.left = KKValue.valueOf(vs[0]);
            if(vs.length > 1) {
                v.top = KKValue.valueOf(vs[1]);
                if(vs.length > 2) {
                    v.right = KKValue.valueOf(vs[2]);
                    if(vs.length > 3) {
                        v.bottom = KKValue.valueOf(vs[3]);
                    }
                    else {
                        v.bottom = new KKValue(v.top);
                    }
                }
                else {
                    v.right = new KKValue(v.left);
                    v.bottom = new KKValue(v.top);
                }
            }
            else {
                v.top = new KKValue(v.left);
                v.right = new KKValue(v.left);
                v.bottom = new KKValue(v.left);
            }
        }

        return v;
    }

    public static class Property extends cn.kkserver.view.Property {

        public Property(String name) {
            super(name);
        }

        @Override
        public Object valueOf(Object value) {

            if(value != null) {

                if( value instanceof KKEdge) {
                    return value;
                }

                if( value instanceof String) {
                    return KKEdge.valueOf((String) value);
                }
            }
            return null;
        }
    }
}
