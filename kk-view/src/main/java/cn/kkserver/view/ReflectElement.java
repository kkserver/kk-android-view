package cn.kkserver.view;

/**
 * Created by zhanghailong on 2016/11/1.
 */

public class ReflectElement extends Element {

    public final Element element;

    public ReflectElement(Element element) {
        this.element = element;
    }

    private Element _reflectElement;

    @Override
    public Object get(Property property) {
        if(propertys().contains(property)) {
            return super.get(property);
        }
        return element.get(property);
    }

    public void commitReflect(Element element) {

        _reflectElement = element;

        for(Property property : propertys()) {
            element.set(property,get(property));
        }

        Element e = firstChild();
        Element t = element.firstChild();

        while(e != null && t != null) {
            ((ReflectElement) e).commitReflect(t);
            e = e.nextSibling();
            t = t.nextSibling();
        }

    }

    public void cancelReflect(Element element) {
        if(_reflectElement == element) {
            _reflectElement = null;
        }
    }

    public void cancelReflect() {
        _reflectElement = null;
    }

    @Override
    protected void onPropertyChanged(Property property,Object value,Object newValue) {

        if(_reflectElement != null) {
            _reflectElement.set(property,newValue);
        }

        super.onPropertyChanged(property,value,newValue);
    }

}
