package cn.kkserver.view;

import android.content.Context;
import android.util.AttributeSet;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import cn.kkserver.observer.IObserver;
import cn.kkserver.view.element.AnimationElement;
import cn.kkserver.view.element.BodyElement;
import cn.kkserver.view.element.DocumentElement;
import cn.kkserver.view.element.Element;
import cn.kkserver.view.element.Layout;
import cn.kkserver.view.element.ScriptElement;
import cn.kkserver.view.element.StyleElement;
import cn.kkserver.view.style.Style;
import cn.kkserver.view.value.Size;

/**
 * Created by zhanghailong on 2016/11/2.
 */

public class KKDocumentView extends KKView {

    private BodyElement _bodyElement;
    private DocumentElement _documentElement;
    private Size _layoutSize;

    public KKDocumentView(Context context) {
        super(context);
    }

    public KKDocumentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DocumentElement documentElement() {
        return _documentElement;
    }

    public BodyElement bodyElement() {
        return _bodyElement;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Layout layout = _bodyElement.get(Style.Layout,Layout.class);
        if(layout != null && (_layoutSize == null || _layoutSize.width != r- l || _layoutSize.height != b-t)) {
            if(_layoutSize == null) {
                _layoutSize = new Size(r-l,b-t);
            }
            else {
                _layoutSize.width = r-l;
                _layoutSize.height = b-t;
            }
            layout.layout(_bodyElement,_layoutSize);
        }
        super.onLayout(changed,l,t,r,b);
    }

    protected void onStartDocument(XmlPullParser parser) {
        _documentElement = new DocumentElement(getContext());
        _bodyElement = new BodyElement(this);
        _bodyElement.set(Style.Layout,"relative");
        _bodyElement.set(Style.Width,"100%");
        _bodyElement.set(Style.Height,"100%");
        _layoutSize = null;
        removeAllViews();
    }

    protected void onEndDocument(XmlPullParser parser) {

    }


    protected void onStartElement(Element element) {

    }


    protected void onEndElement(Element element) {

        if(element instanceof StyleElement) {
            _documentElement.addStyleElement((StyleElement) element);
        }
        else if(element instanceof AnimationElement) {
            _documentElement.addAnimationElement((AnimationElement) element);
        }

    }

    public boolean runScript(IObserver observer,Object weakObject) throws Throwable {

        if(_documentElement != null) {
            return ScriptElement.runScript(_documentElement,observer,weakObject);
        }

        return false;
    }

    public void load(int id) throws ClassNotFoundException, XmlPullParserException, InstantiationException, IllegalAccessException, IOException, InvocationTargetException {
        loadXML(getResources().getXml(id));
    }

    public void loadXML(XmlPullParser parser) throws IOException, XmlPullParserException, IllegalAccessException, InstantiationException, ClassNotFoundException, InvocationTargetException {

        onStartDocument(parser);

        Element element = null;

        StringBuilder text = null;

        int type = parser.next();

        while(type != XmlPullParser.END_DOCUMENT) {
            switch (type) {
                case XmlPullParser.START_TAG:
                {

                    if(element == null) {
                        element = _documentElement;
                        for (int i = 0; i < parser.getAttributeCount(); i++) {
                            setElementAttribute(element, parser.getAttributeName(i), parser.getAttributeValue(i));
                        }
                    }
                    else {

                        Element el = onCreateElement(parser.getName(), element);

                        for (int i = 0; i < parser.getAttributeCount(); i++) {
                            setElementAttribute(el, parser.getAttributeName(i), parser.getAttributeValue(i));
                        }

                        element.append(el);
                        element = el;

                    }

                    onStartElement(element);

                    if(text != null) {
                        text.delete(0,text.length());
                    }
                }
                break;
                case XmlPullParser.END_TAG:
                {
                    if(element != null) {

                        if(element.firstChild() == null && text != null) {
                            element.set(Style.Text,text.toString());
                        }

                        onEndElement(element);

                        element = element.parent();

                    }

                    if(text != null) {
                        text.delete(0,text.length());
                    }
                }
                break;
                case XmlPullParser.TEXT:
                {
                    if(text == null) {
                        text = new StringBuilder();
                    }
                    text.append(parser.getText());
                }
                break;
            }

            type = parser.next();
        }

        onEndDocument(parser);

    }

    protected Element onCreateElement(String name,Element parent) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException {

        if("body".equals(name)) {
            return _bodyElement;
        }

        String[] ns = name.split(":");
        Style v = _documentElement.styleSheet.get(ns[0]);
        Element element = null;

        if(v != null) {

            Object className = v.get(Style.Class,ns.length > 1? ns[1] : "");

            if(className != null && className instanceof String) {

                Class<?> clazz = getContext().getClassLoader().loadClass((String) className);

                try {
                    Constructor<?> constructor = clazz.getConstructor(Context.class);
                    element = (Element) constructor.newInstance(getContext());
                } catch (NoSuchMethodException e) {
                    element = (Element) clazz.newInstance();
                }

            }
        }

        if(element == null) {
            element = new Element();
        }

        if(v != null) {
            v.applyElement(element,ns.length > 1? ns[1] : "");
        }

        return element;
    }

    protected void setElementAttribute(Element element,String name,String value) {
        if("style".equals(name)) {
            Style v = _documentElement.styleSheet.get(value);
            if(v != null) {
                element.set(Style.Style, v);
            }
        }
        else if("animation".equals(name)) {
            AnimationElement anim = _documentElement.animation(value);
            if(anim != null) {
                element.set(Style.Animation, anim);
            }
        }
        else {
            Property property = Style.get(name);
            if(property != null) {
                element.set(property,value);
            }
        }
    }
}
