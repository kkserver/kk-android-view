package cn.kkserver.view.value;

/**
 * Created by zhanghailong on 2016/11/1.
 */

public class KKValue extends Object implements Cloneable {

    public static float UNIT_PX = 1.0f;
    public static float UNIT_DP = 1.0f;
    public static float UNIT_SP = 1.0f;
    public final static float UNIT_AUTO = -1.0f;

    public float unit = 0.0f;
    public float ratio = 0.0f;
    public float value = 0.0f;

    public KKValue(float value , float ratio, float unit) {
        this.value = value;
        this.ratio = ratio;
        this.unit = unit;
    }

    public KKValue(float value ) {
        this.value = value;
        this.ratio = 0.0f;
        this.unit = UNIT_PX;
    }

    public KKValue() {
        this.value = 0.0f;
        this.ratio = 0.0f;
        this.unit = 0.0f;
    }

    public KKValue(KKValue value) {
        this.value = value.value;
        this.ratio = value.ratio;
        this.unit = value.unit;
    }

    public float floatValue() {
        return value * unit;
    }

    public float floatValue(float baseValue) {
        return baseValue * ratio * 0.01f + value * unit;
    }

    public int intValue() {
        return (int) floatValue();
    }

    public int intValue(float baseValue) {
        return (int) floatValue(baseValue);
    }

    public boolean isZero() {
        return unit == 0.0f && ratio == 0.0f && value == 0.0f;
    }

    public boolean isAuto() {
        return unit == UNIT_AUTO;
    }

    public final static KKValue Auto = new KKValue(0.0f,0.0f,UNIT_AUTO);
    public final static KKValue Zero = new KKValue();

    @Override
    public int hashCode() {
        return Float.valueOf(100.0f * ratio * 0.01f + value * unit).hashCode();
    }

    @Override
    public boolean equals(Object value) {
        KKValue v = (KKValue) value;
        return v.value == v.value && v.ratio == v.ratio && v.unit == v.unit;
    }

    public static KKValue valueOf(String value) {

        if(value == null) {
            return null;
        }

        if("auto".equals(value)) {
            return Auto;
        }

        KKValue v = new KKValue();

        if(value.endsWith("%")) {
            v.ratio = Float.valueOf(value.substring(0,value.length() - 1));
            v.unit = UNIT_PX;
            v.value = 0;
            return v;
        }

        String[] vs = value.split("%");
        String vv = vs[0];
        if(vs.length > 1) {
            vv= vs[1];
            v.ratio = Float.valueOf(vs[0]);
        }
        if(vv.endsWith("px")) {
            v.value = Float.valueOf(vv.substring(0,vv.length() - 2));
            v.unit = UNIT_PX;
        }
        else if(vv.endsWith("dp")) {
            v.value = Float.valueOf(vv.substring(0,vv.length() - 2));
            v.unit = UNIT_DP;
        }
        else if(vv.endsWith("sp")) {
            v.value = Float.valueOf(vv.substring(0,vv.length() - 2));
            v.unit = UNIT_SP;
        }
        else {
            v.value = Float.valueOf(vv);
            v.unit = UNIT_PX;
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

                if( value instanceof KKValue) {
                    return value;
                }

                if( value instanceof Number) {
                    return new KKValue(((Number) value).floatValue());
                }

                if( value instanceof String) {
                    return KKValue.valueOf((String) value);
                }

            }

            return null;
        }
    }

}
