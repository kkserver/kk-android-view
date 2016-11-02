package cn.kkserver.view;

/**
 * Created by zhanghailong on 2016/11/1.
 */

public class Property extends Object implements Comparable{

    public final String name;

    public Property(String name) {
        this.name = name;
    }

    public Object valueOf(Object value) {
        return value;
    }

    public <T extends Object> T valueOf(Object value,Class<T> tClass) {
        Object v = valueOf(value);
        if(v != null && v.getClass().isAssignableFrom(tClass)) {
            return (T) v;
        }
        return null;
    }

    public Object animationValueOf(Object[] values,float ratio) {
        return values[(int)(values.length * ratio)];
    }

    @Override
    public int compareTo(Object value) {
        Property v = (Property) value;
        return name.compareTo(v.name);
    }

    public static class BooleanProperty extends Property {

        public BooleanProperty(String name) {
            super(name);
        }

        @Override
        public Object valueOf(Object value) {

            if(value != null) {

                if(value instanceof Boolean) {
                    return value;
                }

                if(value instanceof Number) {
                    return ((Number) value).doubleValue() != 0.0;
                }

                if(value instanceof String) {
                    if("false".equals(value) || "no".equals(value) || "0".equals(value) || "".equals(value)) {
                        return false;
                    }
                    else if("true".equals(value) || "yes".equals(value) ) {
                        return true;
                    }
                    else {
                        return null;
                    }
                }
            }

            return null;
        }
    }

    public static class StringProperty extends Property {

        public StringProperty(String name) {
            super(name);
        }

        @Override
        public Object valueOf(Object value) {

            if(value != null) {

                if(value instanceof String) {
                    return value;
                }

                return value.toString();

            }

            return null;
        }
    }

    public static class FloatProperty extends Property {

        public FloatProperty(String name) {
            super(name);
        }

        @Override
        public Object valueOf(Object value) {

            if(value != null) {

                if(value instanceof Float) {
                    return value;
                }

                if(value instanceof Number) {
                    return ((Number) value).floatValue();
                }

                if(value instanceof String) {
                    return Float.valueOf((String) value);
                }

            }

            return null;
        }
    }

    public static class LongProperty extends Property {

        public LongProperty(String name) {
            super(name);
        }

        @Override
        public Object valueOf(Object value) {

            if(value != null) {

                if(value instanceof Long) {
                    return value;
                }

                if(value instanceof Number) {
                    return ((Number) value).longValue();
                }

                if(value instanceof String) {
                    return Long.valueOf((String) value);
                }

            }

            return null;
        }
    }

    public static class IntegerProperty extends Property {

        public IntegerProperty(String name) {
            super(name);
        }

        @Override
        public Object valueOf(Object value) {

            if(value != null) {

                if(value instanceof Integer) {
                    return value;
                }

                if(value instanceof Number) {
                    return ((Number) value).intValue();
                }

                if(value instanceof String) {
                    return Integer.valueOf((String) value);
                }

            }

            return null;
        }
    }

    public static class CharSequenceProperty extends Property {

        public CharSequenceProperty(String name) {
            super(name);
        }

        @Override
        public Object valueOf(Object value) {

            if(value != null) {

                if(value instanceof CharSequence) {
                    return value;
                }

                return value.toString();

            }

            return null;
        }
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object value) {
        Property v = (Property) value;
        return name.equals(v.name);
    }

}
