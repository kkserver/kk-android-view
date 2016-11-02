package cn.kkserver.view;

/**
 * Created by zhanghailong on 16/7/6.
 */
public class Font {

    public String name;
    public int size;
    public boolean bold;
    public boolean italic;

    public Font(String name,int size,boolean bold,boolean italic) {
        this.name = name;
        this.size = size;
        this.bold = bold;
        this.italic = italic;
    }

    public Font(int size) {
        this(null,size,false,false);
    }

    public final static Font defaultFont = new Font(14);

    public static Font valueOf(String value) {

        if(value != null) {

            Font v = new Font(14);
            String[] vs = value.split(" ");


            for(String vv : vs) {
                if(vv.equals("bold")) {
                    v.bold =true;
                }
                else if(vv.equals("italic")) {
                    v.italic =true;
                }
                else if(vv.equals("italic")) {
                    v.italic =true;
                }
                else if(vv.endsWith("px") || vv.endsWith("dp") || vv.endsWith("sp")) {
                    v.size = (int) KKValue.valueOf(vv).floatValue();
                }
                else {
                    v.name = vv;
                }
            }

            return v;

        }

        return null;
    }

    public static class Property extends cn.kkserver.view.Property {

        public Property(String name) {
            super(name);
        }

        @Override
        public Object valueOf(Object value) {

            if(value != null) {

                if( value instanceof Font) {
                    return value;
                }

                if( value instanceof String) {
                    return Font.valueOf((String) value);
                }
            }
            return null;
        }
    }

}
