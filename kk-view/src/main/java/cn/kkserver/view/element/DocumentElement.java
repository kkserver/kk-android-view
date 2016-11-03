package cn.kkserver.view.element;

import android.content.Context;
import android.view.animation.Animation;

import java.util.Set;
import java.util.TreeMap;

import cn.kkserver.view.KKDocumentView;
import cn.kkserver.view.R;
import cn.kkserver.view.style.Style;
import cn.kkserver.view.style.StyleSheet;

/**
 * Created by zhanghailong on 2016/11/3.
 */

public class DocumentElement extends Element {

    public final StyleSheet styleSheet;
    public final Context context;

    public DocumentElement(Context context) {
        this.context = context;
        this.styleSheet = new StyleSheet();
        this.styleSheet.loadCSSContent(context.getResources().getString(R.string.styleSheet));
    }

    private TreeMap<String,AnimationElement> _animations = new TreeMap<>();

    public Set<String> animationKeys() {
        return _animations.keySet();
    }

    public AnimationElement animation(String key) {
        return _animations.containsKey(key) ? _animations.get(key) : null;
    }

    public void addStyleElement(StyleElement element) {
        styleSheet.loadCSSContent(element.get(Style.Text,String.class,""));
    }

    public void addAnimationElement(AnimationElement element) {

        String name = element.get(Style.Name,String.class);

        if(name != null) {
            _animations.put(name, element);
        }
    }

    public static DocumentElement getDocumentElement(Element element) {

        if(element == null) {
            return null;
        }

        if(element instanceof DocumentElement) {
            return (DocumentElement) element;
        }
        else {
            return getDocumentElement(element.parent());
        }

    }

    public static Animation getAnimation(Element element,String key) {

        DocumentElement e = getDocumentElement(element);

        if(e != null) {
            AnimationElement anim = e.animation(key);
            if(anim != null) {
                return anim.getAnimation();
            }
        }

        return null;
    }

    public static Style getStyle(Element element,String name) {

        DocumentElement e = getDocumentElement(element);

        if(e != null) {
            return e.styleSheet.get(name);
        }

        return null;
    }

    @Override
    protected Element onCreateCloneElement() {
        return new DocumentElement(context);
    }
}
