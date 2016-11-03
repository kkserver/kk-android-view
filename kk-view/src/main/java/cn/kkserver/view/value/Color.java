package cn.kkserver.view.value;

/**
 * Created by zhanghailong on 2016/11/1.
 */

public class Color extends Object {

    public int value;

    public Color(int value) {
        this.value = value;
    }

    public Color() {
        this.value = 0;
    }

    public Color(float r,float g,float b,float a) {
        this.value = ( (int) (a * 0xff) << 24 ) | ( (int) (r * 0xff) << 16 ) | ( (int) (g * 0xff) << 8 ) | ( (int) (b * 0xff) );
    }

    public Color(float r,float g,float b) {
        this.value = ( (int) (0xff) << 24 ) | ( (int) (r * 0xff) << 16 ) | ( (int) (g * 0xff) << 8 ) | ( (int) (b * 0xff) );
    }

    public float r() {
        return (float)( (this.value >> 16) & 0x0ff) / 255.0f;
    }

    public float g() {
        return (float)( (this.value >> 8) & 0x0ff) / 255.0f;
    }

    public float b() {
        return (float)( (this.value ) & 0x0ff) / 255.0f;
    }

    public float a() {
        return (float)( (this.value >> 24 ) & 0x0ff) / 255.0f;
    }

    public final static Color Clear = new Color(0);
    public final static Color Blank = new Color(0x0ff000000);

    public static Color valueOf(String value) {

        if(value == null) {
            return null;
        }

        if(value.equals("clear")) {
            return Color.Clear;
        }

        String[] vs = value.split(" ");
        String vv = vs[0];
        Color v = new Color();
        int i;

        if(vv.startsWith("#")) {
            if(vv.length() == 4) {
                i = Integer.valueOf(vv.substring(1,2),16);
                v.value = v.value | (((i << 4) | i) << 16);
                i = Integer.valueOf(vv.substring(2,3),16);
                v.value = v.value | (((i << 4) | i) << 8);
                i = Integer.valueOf(vv.substring(3,4),16);
                v.value = v.value | (((i << 4) | i));
                v.value = v.value | 0x0ff000000;
            }
            else if(vv.length() == 7) {
                i = Integer.valueOf(vv.substring(1,3),16);
                v.value = v.value | (i << 16);
                i = Integer.valueOf(vv.substring(3,5),16);
                v.value = v.value | (i  << 8);
                i = Integer.valueOf(vv.substring(5,7),16);
                v.value = v.value | i;
                v.value = v.value | 0x0ff000000;
            }
            else if(vv.length() == 9) {
                i = Integer.valueOf(vv.substring(1,3),16);
                v.value = v.value | (i << 24);
                i = Integer.valueOf(vv.substring(3,5),16);
                v.value = v.value | (i << 16);
                i = Integer.valueOf(vv.substring(5,7),16);
                v.value = v.value | (i  << 8);
                i = Integer.valueOf(vv.substring(7,9),16);
                v.value = v.value | i;
            }
        }

        if(vs.length > 1) {
            i = (int) (Float.valueOf(vs[1]).floatValue() * 0xff);
            v.value = (v.value & 0x0ffffff) | (i << 24);
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

                if( value instanceof Color) {
                    return value;
                }

                if( value instanceof String) {
                    return Color.valueOf((String) value);
                }
            }
            return null;
        }
    }
}
