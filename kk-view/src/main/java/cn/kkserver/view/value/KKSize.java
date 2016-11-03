package cn.kkserver.view.value;

/**
 * Created by zhanghailong on 16/7/14.
 */
public class KKSize {

    public KKValue width;
    public KKValue height;

    public KKSize() {

        this.width = new KKValue();
        this.height = new KKValue();

    }

    public KKSize(KKValue width, KKValue height) {
        this.width = width;
        this.height = height;
    }

    public static KKSize Zero = new KKSize();

    public static class Property extends cn.kkserver.view.Property {

        public Property(String name) {
            super(name);
        }

        @Override
        public Object valueOf(Object value) {

            if(value != null) {

                if( value instanceof KKSize) {
                    return value;
                }

                if( value instanceof Size) {
                    return new KKSize(new KKValue(((Size) value).width),new KKValue(((Size) value).height));
                }

                if( value instanceof String) {
                    String[] vs = ((String) value).split(" ");
                    return new KKSize(KKValue.valueOf(vs[0]),vs.length > 1 ? KKValue.valueOf(vs[1]) : new KKValue());
                }

            }

            return null;
        }
    }

}
