package cn.kkserver.view.element;

import java.lang.ref.WeakReference;
import java.util.Set;
import java.util.TreeMap;

import cn.kkserver.observer.IObserver;
import cn.kkserver.observer.IWithObserver;
import cn.kkserver.observer.Observer;
import cn.kkserver.view.Property;
import cn.kkserver.view.style.Style;
import cn.kkserver.view.event.Event;
import cn.kkserver.view.event.EventEmitter;
import cn.kkserver.view.value.Rect;

/**
 * Created by zhanghailong on 2016/11/1.
 */

public class Element extends EventEmitter {

    private WeakReference<Element> _parent;
    private Element _firstChild;
    private Element _lastChild;
    private Element _nextSibling;
    private WeakReference<Element> _prevSibling;

    public Element parent() {
        return _parent == null ? null : _parent.get();
    }

    public Element firstChild() {
        return _firstChild;
    }

    public Element lastChild() {
        return _lastChild;
    }

    public Element nextSibling() {
        return _nextSibling;
    }

    public Element prevSibling() {
        return _prevSibling == null ? null :_prevSibling.get();
    }

    public Element append(Element element) {

        element.remove();

        if(_lastChild != null) {
            _lastChild._nextSibling = element;
            element._prevSibling = new WeakReference<>(_lastChild);
            _lastChild = element;
        } else {
            _firstChild = element;
            _lastChild = element;
        }

        element._parent = new WeakReference<>(this);

        onAddChildren(element);

        return this;
    }

    public Element appendTo(Element element) {
        element.append(this);
        return this;
    }

    public Element before(Element element) {

        element.remove();

        Element p = parent();

        if(p != null) {

            element._parent = new WeakReference<>(p);

            Element prev = prevSibling();

            if(prev != null) {
                prev._nextSibling = element;
                element._prevSibling = new WeakReference<>(prev);
                element._nextSibling = this;
                _prevSibling = new WeakReference<>(element);
            } else {
                p._firstChild = element;
                element._nextSibling = this;
                _prevSibling = new WeakReference<>(element);
            }

            p.onAddChildren(element);

        }

        return this;
    }

    public Element beforeTo(Element element) {
        element.before(this);
        return this;
    }

    public Element after(Element element) {

        element.remove();

        Element p = parent();

        if( p != null ) {

            element._parent = new WeakReference<>(p);

            if(_nextSibling != null) {
                element._nextSibling = _nextSibling;
                element._prevSibling = new WeakReference<>(this);
                _nextSibling._prevSibling = new WeakReference<>(element);
                _nextSibling = element;
            } else {
                p._lastChild = element;
                element._prevSibling = new WeakReference<>(this);
                _nextSibling = element;
            }

            p.onAddChildren(element);

        }

        return this;
    }

    public Element afterTo(Element element) {
        element.after(this);
        return this;
    }

    protected void onAddChildren(Element element) {
        element.onAddToParent(this);
    }

    protected void onRemoveChildren(Element element) {
        element.onRemoveFromParent(this);
    }

    protected void onRemoveFromParent(Element element) {

    }

    protected void onAddToParent(Element element) {

    }

    public Element remove() {

        Element prev = prevSibling();
        Element p = parent();

        if(prev != null) {

            prev._nextSibling = _nextSibling;

            if(_nextSibling != null) {
                _nextSibling._prevSibling = new WeakReference<>(prev);
            } else if(p != null) {
                p._lastChild = prev;
            }

        } else if(p != null) {
            p._firstChild = _nextSibling;
            if(_nextSibling != null) {
                _nextSibling._prevSibling = null;
            } else {
                p._lastChild = _nextSibling;
            }
        }

        _parent = null;
        _nextSibling = null;
        _prevSibling = null;

        if (p != null) {
            p.onRemoveChildren(this);
        }

        return this;
    }

    public void removeAllChildren() {
        Element p = _firstChild;
        while(p != null) {
            Element n = p.nextSibling();
            p.remove();
            p = n;
        }
    }

    public void exchangeTo(Element element) {

        Element fp = parent();
        Element tp = element.parent();

        if(fp != null && tp != null) {

            if(fp == tp) {
                if(nextSibling() == element) {
                    afterTo(element);
                }
                else if(nextSibling() != null){
                    Element fn = nextSibling();
                    element.beforeTo(this);
                    beforeTo(fn);
                }
                else {
                    beforeTo(element);
                    element.appendTo(this);
                }
            }
            else {
                Element fn = nextSibling();
                Element tn = element.nextSibling();

                if (tn != null) {
                    beforeTo(tn);
                } else {
                    appendTo(tp);
                }

                if (fn != null) {
                    element.beforeTo(fn);
                } else {
                    element.appendTo(fp);
                }
            }
        }
    }

    public void sendEvent(String name, Event event) {
        emit(name, event);
        if(event instanceof ElementEvent && !((ElementEvent) event).isCancelBubble()) {
            Element p = parent();
            if(p != null) {
                p.sendEvent(name, event);
            }
        }
    }

    public Element dispatchEvent(String name, Event event) {

        Element r = null;

        Element p = _lastChild;

        while(p != null) {

            r = dispatchChildrenEvent(p, name, event);

            if(r != null) {
                return r;
            }

            p = p.prevSibling();
        }

        return r;
    }

    public Element dispatchChildrenEvent(Element element, String name, Event event) {
        return element.dispatchEvent(name, event);
    }

    private TreeMap<Property,Object> _propertyValues = new TreeMap<>();

    public Set<Property> propertys() {
        return _propertyValues.keySet();
    }

    public Element set(Property property,Object value) {
        Object newValue = property.valueOf(value);
        Object v = _propertyValues.put(property,newValue);
        onPropertyChanged(property,v,newValue);
        return this;
    }

    public Object get(Property property) {

        if(_propertyValues.containsKey(property)) {
            return _propertyValues.get(property);
        }

        if(property != Style.Status && property!= Style.Style && _propertyValues.containsKey(Style.Style)) {
            Style style = (Style) _propertyValues.get(Style.Style);
            String status = _propertyValues.containsKey(Style.Status) ? (String) _propertyValues.get(Style.Status) : null;
            if(status == null) {
                status = _propertyValues.containsKey(Style.InStatus) ? (String) _propertyValues.get(Style.InStatus) : null;
            }
            return style.get(property,status);
        }

        return null;
    }

    public <T extends Object> T get(Property property,Class<T> tClass) {
        return get(property,tClass,null);
    }

    public <T extends Object> T get(Property property,Class<T> tClass,T defaultValue) {
        Object v = get(property);
        if(v != null ) {
            Class<?> type = v.getClass();
            if(tClass == type || tClass.isAssignableFrom(type)) {
                return (T) v;
            }
        }
        return defaultValue;
    }

    public Element removeProperty(Property property) {
        if(_propertyValues.containsKey(property)) {
            Object v = _propertyValues.remove(property);
            onPropertyChanged(property,v,null);
        }
        return this;
    }

    public Element changeProperty(Property property) {
        Object v = get(property);
        onPropertyChanged(property,v,v);
        return this;
    }

    protected void onPropertyChanged(Property property,Object value,Object newValue) {

        if(property == Style.Status || property == Style.InStatus) {

            Style v = get(Style.Style,Style.class);

            if(v != null) {

                String status = get(Style.Status,String.class);

                if(status == null) {
                    status = get(Style.InStatus,String.class);
                }

                for(Property prop : v.propertys()) {
                    if(prop != Style.Status && prop != Style.Style && prop != Style.InStatus) {
                        set(prop,v.get(prop,status));
                    }
                }
            }

            Element p = firstChild();

            while(p != null) {
                if(newValue == null) {
                    p.removeProperty(Style.InStatus);
                }
                else {
                    p.set(Style.InStatus, newValue);
                }
                p = p.nextSibling();
            }

        }
        else if(property == Style.Style) {

            if(newValue instanceof Style) {

                Style v = (Style) newValue;

                String status = get(Style.Status,String.class);

                if(status == null) {
                    status = get(Style.InStatus,String.class);
                }

                for(Property prop : v.propertys()) {
                    if(prop != Style.Status && prop != Style.Status && prop != Style.InStatus) {
                        set(prop,v.get(prop,status));
                    }
                }
            }

        }
        else if(property == Style.Observer) {

            IObserver observer = (IObserver) newValue;
            String key = get(Style.Key,String.class);

            if(key == null) {

                obtainObserver(observer);

            }
            else {

                IObserver obs = observer;

                if(key.startsWith("~")) {
                    while(obs.parent() != null) {
                        obs = obs.parent();
                    }
                    key = key.substring(1);
                }
                else {
                    while (key.startsWith("^") && obs != null) {
                        key = key.substring(1);
                        obs = obs.parent();
                    }
                }

                String[] keys = Observer.keys(key);

                IWithObserver withObserver = get(Style.WithObserver,IWithObserver.class);

                if(obs == null) {
                    if(withObserver != null) {
                        withObserver.recycle();
                    }
                    set(Style.Object,null);
                }
                else {
                    if(withObserver == null) {
                        withObserver = obs.with(keys);
                        set(Style.WithObserver,withObserver);
                    }
                    else {
                        withObserver.obtainObserver(obs,keys);
                    }
                    set(Style.Object,withObserver.get(new String[0]));
                }

                obtainObserver(withObserver);

            }
        }

    }

    protected void obtainObserver(IObserver observer) {

        Element e = firstChild();

        while (e != null) {
            if(e instanceof IReflectElement) {

            }
            else {
                e.set(Style.Observer, observer);
            }
            e = e.nextSibling();
        }

    }

    public Element clone() {

        Element v = onCreateCloneElement();

        for(Property property : propertys()) {
            if(property instanceof Property.ReflectProperty) {

            }
            else {
                v.set(property, get(property));
            }
        }

        Element p = firstChild();

        while(p != null) {

            if(!(p instanceof IReflectElement)) {
                v.append(p.clone());
            }

            p = p.nextSibling();
        }

        return v;
    }

    protected Element onCreateCloneElement() {
        return new Element();
    }

}
