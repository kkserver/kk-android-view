package cn.kkserver.view.style;

import java.util.TreeMap;

import cn.kkserver.view.value.KKEdge;
import cn.kkserver.view.value.KKPoint;
import cn.kkserver.view.value.KKValue;
import cn.kkserver.view.element.Layout;
import cn.kkserver.view.Property;
import cn.kkserver.view.value.TextAlignment;
import cn.kkserver.view.value.VerticalAlignment;
import cn.kkserver.view.element.Element;
import cn.kkserver.view.value.Color;
import cn.kkserver.view.value.Font;

/**
 * Created by zhanghailong on 2016/11/1.
 */

public class Style extends Object {

    public final static cn.kkserver.view.Property BackgroundColor = new Color.Property("background-color");
    public final static Property BorderColor = new Color.Property("border-color");
    public final static Property BorderWidth = new KKValue.Property("border-width");
    public final static Property BorderRadius = new KKValue.Property("border-radius");
    public final static Property ShadowColor = new Color.Property("shadow-color");
    public final static Property ShadowOffset = new KKPoint.Property("shadow-offset");
    public final static Property ShadowRadius = new KKValue.Property("shadow-radius");
    public final static Property ShadowOpacity = new KKValue.Property("shadow-opacity");

    public final static Property Font = new Font.Property("font");
    public final static Property Color = new Color.Property("color");
    public final static Property TextAlign = new TextAlignment.Property("text-align");
    public final static Property VerticalAlign = new VerticalAlignment.Property("vertical-align");
    public final static Property Hidden = new Property.BooleanProperty("hidden");
    public final static Property Enabled = new Property.BooleanProperty("enabled");
    public final static Property Selected = new Property.BooleanProperty("selected");

    public final static Property Opacity = new Property.FloatProperty("opacity");

    public final static Property Left = new KKValue.Property("left");
    public final static Property Top = new KKValue.Property("top");
    public final static Property Right = new KKValue.Property("right");
    public final static Property Bottom = new KKValue.Property("bottom");

    public final static Property Width = new KKValue.Property("width");
    public final static Property MinWidth = new KKValue.Property("min-width");
    public final static Property MaxWidth = new KKValue.Property("max-width");

    public final static Property Height = new KKValue.Property("height");
    public final static Property MinHeight = new KKValue.Property("min-height");
    public final static Property MaxHeight = new KKValue.Property("max-height");

    public final static Property Padding = new KKEdge.Property("padding");
    public final static Property Margin = new KKEdge.Property("margin");

    public final static Property Layout = new Layout.Property("layout");

    public final static Property Status = new Property.StringProperty("status");

    public final static Property InStatus = new Property.StringProperty("in-status");

    public final static Property Reuse = new Property.StringProperty("reuse");

    public final static Property Style = new StyleProperty("style");

    public final static Property Text = new Property.CharSequenceProperty("text");

    public final static Property Class = new Property.StringProperty("class");

    public final static Property Src = new Property.StringProperty("src");

    public final static Property DefaultSrc = new Property.StringProperty("default-src");

    public final static Property FailSrc = new Property.StringProperty("fail-src");

    public final static Property Key = new Property.StringProperty("key");

    public final static Property Name = new Property.StringProperty("name");

    public final static Property Property = new Property.StringProperty("property");

    public final static Property Type = new Property.StringProperty("type");

    public final static Property Observer = new Property("observer");

    public final static Property Element = new Property("element");

    public final static Property Object = new Property("object");

    public final static Property Action = new Property.StringProperty("action");

    public final static Property LongAction = new Property.StringProperty("long-action");

    public final static Property Scale =  new Property.FloatProperty("scale");

    public final static Property FromX =  new Property.FloatProperty("fromX");

    public final static Property FromY =  new Property.FloatProperty("fromY");

    public final static Property ToX =  new Property.FloatProperty("toX");

    public final static Property ToY =  new Property.FloatProperty("toY");

    public final static Property PivotX =  new Property.FloatProperty("pivotX");

    public final static Property PivotY =  new Property.FloatProperty("pivotY");

    public final static Property Duration =  new Property.LongProperty("duration");

    public final static Property Autoreverses = new Property.BooleanProperty("autoreverses");

    public final static Property ReplayCount = new Property.IntegerProperty("replayCount");

    public final static Property AfterDelay = new Property.LongProperty("afterDelay");

    public final static Property FromOpacity = new Property.FloatProperty("fromOpacity");

    public final static Property ToOpacity = new Property.FloatProperty("toOpacity");

    public final static Property FromDegrees = new Property.FloatProperty("fromDegrees");

    public final static Property ToDegrees = new Property.FloatProperty("toDegrees");

    public final static Property Animation = new Property("animation");

    public final static Property Image = new Property("image");

    public final static Property Gravity = new Property.StringProperty("gravity");

    public final static Property[] Propertys = new Property[]{
            BackgroundColor,
            BorderColor,
            BorderWidth,
            BorderRadius,
            ShadowColor,
            ShadowOffset,
            ShadowRadius,
            ShadowOpacity,
            Font,
            Color,
            TextAlign,
            VerticalAlign,
            Hidden,
            Enabled,
            Selected,
            Opacity,
            Left,
            Top,
            Right,
            Bottom,
            Width,
            MinWidth,
            MaxWidth,
            Height,
            MinHeight,
            MaxHeight,
            Padding,
            Margin,
            Layout,
            Status,
            Reuse,
            Class,
            Src,
            DefaultSrc,
            FailSrc,
            Key,
            Name,
            Type,
            Property,
            Object,
            Action,
            LongAction,
            Scale,
            FromX,
            FromY,
            ToX,
            ToY,
            PivotX,
            PivotY,
            Duration,
            Autoreverses,
            ReplayCount,
            AfterDelay,
            FromOpacity,
            ToOpacity,
            FromDegrees,
            ToDegrees,
            Animation,
            Image,
            Text,
            Gravity
    };

    private final static TreeMap<String,Property> _propertysMap;

    static {
        _propertysMap = new TreeMap();
        for(Property p : Propertys) {
            _propertysMap.put(p.name,p);
        }
    }

    public static Property get(String name) {
        return _propertysMap.containsKey(name) ? _propertysMap.get(name) : null;
    }

    public static Style valueOf(String value,String status) {

        Style style = new Style();

        style.loadCSSContent(value,status);

        return style;
    }

    public static class StyleProperty extends cn.kkserver.view.Property {

        public StyleProperty(String name) {
            super(name);
        }

        @Override
        public Object valueOf(Object value) {

            if(value != null) {

                if( value instanceof Style) {
                    return value;
                }

                if( value instanceof String) {
                    return Style.valueOf((String) value,null);
                }

            }

            return null;
        }
    }

    private TreeMap<String,TreeMap<Property,Object>> _propertyValues = new TreeMap<>();

    public void loadCSSContent(String cssContent,String status) {

        String[] items = cssContent.split(";");

        for(String item : items) {

            String[] kv = item.trim().split(":");

            String key = kv[0].trim();

            if(_propertysMap.containsKey(key)) {

                Property propery = _propertysMap.get(key);

                set(propery,kv.length > 1 ? kv[1].trim() : "",status);

            }
        }
    }

    public Object get(Property property,String status) {

        if(status == null) {
            status = "";
        }

        if(_propertyValues.containsKey(status)) {
            TreeMap<Property,Object> values = _propertyValues.get(status);
            if(values.containsKey(property)) {
                return values.get(property);
            }
        }

        return null;
    }

    public void set(Property property,Object value,String status) {

        if(status == null) {
            status = "";
        }

        TreeMap<Property,Object> values;

        if(_propertyValues.containsKey(status)) {
           values = _propertyValues.get(status);
        }
        else {
            values = new TreeMap<>();
            _propertyValues.put(status,values);
        }

        values.put(property,property.valueOf(value));

    }

    public void remove(Property property,String status) {

        if(status == null) {
            status = "";
        }

        if(_propertyValues.containsKey(status)) {
            TreeMap<Property,Object> values = _propertyValues.get(status);
            if(values.containsKey(property)) {
                values.remove(property);
            }
        }

    }

    public void applyElement(cn.kkserver.view.element.Element element, String status) {
        if(_propertyValues.containsKey(status)) {
            TreeMap<Property,Object> values = _propertyValues.get(status);
            for(Property property : values.keySet()) {
                element.set(property,values.get(property));
            }
        }
    }

    public void applyElement(Element element) {

        String status = element.get(Status,String.class);

        if(status == null) {
            status = element.get(InStatus,String.class);
        }

        if(status == null) {
            status = "";
        }

        applyElement(element,status);

    }

    public void changedElement(Element element,String status) {

        if(_propertyValues.containsKey(status)) {
            TreeMap<Property,Object> values = _propertyValues.get(status);
            for(Property property : values.keySet()) {
                if(property != Style || property != Status) {
                    element.changeProperty(property);
                }
            }
        }

    }

    public void changedElement(Element element) {

        String status = element.get(Status,String.class);

        if(status == null) {
            status = element.get(InStatus,String.class);
        }

        if(status == null) {
            status = "";
        }

        changedElement(element,status);

    }
}
