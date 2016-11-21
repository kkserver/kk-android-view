package cn.kkserver.view.element;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import cn.kkserver.observer.ArrayIterator;
import cn.kkserver.observer.IObserver;
import cn.kkserver.observer.IWithObserver;
import cn.kkserver.observer.Listener;
import cn.kkserver.observer.Observer;
import cn.kkserver.view.KK;
import cn.kkserver.view.Property;
import cn.kkserver.view.style.Style;
import cn.kkserver.view.value.Rect;
import cn.kkserver.view.value.Size;

/**
 * Created by zhanghailong on 2016/11/1.
 */

public class ViewPagerElement extends ViewElement implements IEditingElement,IElementCreator {

    private final ViewPagerElementAdpater _adapter = new ViewPagerElementAdpater(this);
    private boolean _editing;

    public ViewPager viewPager() {
        return (ViewPager) view();
    }

    public ViewPagerElement(Context context) {
        super(new ViewPager(context));
        viewPager().setAdapter(_adapter);
        viewPager().addOnPageChangeListener(_adapter);
        set(Style.Layout,new PagerLayout());
    }

    public ViewPagerElement(ViewPager view) {
        super(view);
        view.setAdapter(_adapter);
        view.addOnPageChangeListener(_adapter);
        set(Style.Layout,new PagerLayout());
    }

    @Override
    public boolean isEditing() {
        return _editing;
    }

    @Override
    public void beginEditing() {
        _editing = true;
    }

    @Override
    public void endEditing() {
        if(_editing) {
            _editing = false;
            _adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onAddChildren(Element element) {
        if(!_editing && element instanceof RowElement) {
            _adapter.notifyDataSetChanged();
        }
        super.onAddChildren(element);
    }

    @Override
    protected void onRemoveChildren(Element element) {
        if(!_editing && element instanceof RowElement) {
            _adapter.notifyDataSetChanged();
        }
        super.onRemoveChildren(element);
    }

    private final static Listener<ViewPagerElement> _L = new Listener<ViewPagerElement>() {

        @Override
        public void onChanged(IObserver observer, String[] changedKeys, ViewPagerElement weakObject) {
            weakObject.onValueChanged(observer,observer.get(new String[0]));
        }

    };

    @Override
    protected void obtainObserver(IObserver observer) {

    }

    @Override
    protected void onPropertyChanged(Property property, Object value, Object newValue) {
        super.onPropertyChanged(property,value,newValue);

        if(property == Style.WithObserver) {
            IWithObserver withObserver = (IWithObserver) newValue;
            if(withObserver != null) {
                _L.onChanged(withObserver,new String[0],this);
                withObserver.on(new String[0],_L,this);
            }
        }
    }

    protected void onValueChanged(IObserver observer,Object value) {

        Iterator<Object> i = new ArrayIterator<>(value);

        beginEditing();

        Element p = firstChild();

        while(p!= null && ! (p instanceof RowElement)) {
            p = p.nextSibling();
        }

        int idx = 0;

        while(i.hasNext()) {

            Object object = i.next();

            if(p == null) {
                p = new RowElement();
                append(p);
            }

            p.set(Style.Observer,observer);
            p.set(Style.Key, String.valueOf(idx));
            p.set(Style.Object,object);

            idx ++;

            p = p.nextSibling();
            while(p!= null && ! (p instanceof RowElement)) {
                p = p.nextSibling();
            }
        }

        while(p != null) {

            if(p instanceof RowElement) {

                Element n = p.nextSibling();
                p.remove();
                p = n;
            }
            else {
                p = p.nextSibling();
            }
        }

        endEditing();

    }

    @Override
    public Element onCreateElement(String name) throws Throwable {

        if("page".equals(name)) {
            return new ViewPagerElement.PageElement(view().getContext());
        }

        if("row".equals(name)) {
            return new ViewPagerElement.RowElement();
        }

        return null;
    }

    public boolean hasPrevPage() {
        return viewPager().getCurrentItem()  > 0;
    }

    public void prevPage(boolean animated) {
        if(hasPrevPage()) {
            viewPager().setCurrentItem(viewPager().getCurrentItem() - 1,animated);
        }
    }

    public boolean hasNextPage() {
        int count = _adapter.getCount();
        return viewPager().getCurrentItem() + 1 < count;
    }

    public void nextPage(boolean animated) {
        if(hasNextPage()) {
            viewPager().setCurrentItem(viewPager().getCurrentItem() + 1,animated);
        }
    }

    private static class ViewPagerElementAdpater extends PagerAdapter implements ViewPager.OnPageChangeListener{

        private final WeakReference<ViewPagerElement> _element;

        private List<RowElement> _rowElements;

        public ViewPagerElementAdpater(ViewPagerElement element) {
            _element = new WeakReference<>(element);
        }

        @Override
        public void notifyDataSetChanged() {
            _rowElements = null;
            super.notifyDataSetChanged();
        }

        protected List<RowElement> rowElements() {
            if(_rowElements == null) {

                _rowElements = new ArrayList<>(4);

                ViewPagerElement pager = _element.get();

                if(pager != null) {

                    Element p = pager.firstChild();

                    while (p != null) {

                        if(p instanceof RowElement) {
                            _rowElements.add((RowElement) p);
                        }

                        p = p.nextSibling();
                    }
                }
            }
            return _rowElements;
        }

        @Override
        public int getCount() {
            return rowElements().size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            RowElement row = (RowElement) object;
            return row.element != null && row.element.view() == view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            RowElement row = (RowElement) object;

            if(row.element != null) {

                PageElement page = row.element;

                page.set(Style.Observer,null);

                row.element = null;

            }

        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            RowElement rowElement = rowElements().get(position);

            String reuse = rowElement.get(Style.Reuse,String.class);

            ViewPagerElement pager = _element.get();

            if(pager != null) {

                PageElement page = null;

                Element p = pager.firstChild();

                while(p != null) {
                    if(p instanceof PageElement && ((ViewElement) p).view().getParent() == null) {
                        String v = p.get(Style.Reuse,String.class);
                        if((v == reuse) || (reuse != null && reuse.equals(v))) {
                            page = (PageElement) p;
                        }
                        break;
                    }
                    p = p.nextSibling();
                }

                if(page == null) {

                    p = pager.firstChild();

                    while(p != null) {
                        if(p instanceof PageElement) {
                            String v = p.get(Style.Reuse,String.class);
                            if((v == reuse) || (reuse != null && reuse.equals(v))) {
                                page = (PageElement) p.clone();
                                pager.append(page);
                            }
                            break;
                        }
                        p = p.nextSibling();
                    }
                }

                rowElement.element = page;

                {

                    IObserver observer = rowElement.get(Style.Observer,IObserver.class);
                    String key = rowElement.get(Style.Key,String.class);

                    page.set(Style.Key,key);
                    page.set(Style.Observer,observer);

                }

                View view = page.view();
                ViewParent pv = view.getParent();
                if(p != null && pv instanceof ViewGroup) {
                    ((ViewGroup) pv).removeView(view);
                }

                container.addView(view);

            }

            return rowElement;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            ViewPagerElement e = _element.get();

            if(e != null) {

                PageElementEvent ev = new PageElementEvent(e);

                ev.pageIndex = position;
                ev.pageCount = getCount();

                e.set(Style.PageIndex,position);
                e.set(Style.PageCount,getCount());

                e.sendEvent(PageElementEvent.CHANGED,ev);

            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


    public static class PageElement extends ViewElement {

        public PageElement(Context context) {
            super(context);
        }

        public PageElement(View view) {
            super(view);
        }

        @Override
        protected void onRemoveFromParent(Element element) {

        }

        @Override
        protected void onAddToParent(Element element) {

        }

    }

    public static class RowElement extends Element {

        public PageElement element;

        public RowElement() {

        }

        @Override
        protected void onPropertyChanged(Property property, Object value, Object newValue) {

            if(property == Style.Observer) {

                return ;
            }

            super.onPropertyChanged(property,value,newValue);
        }


        @Override
        protected Element onCreateCloneElement() {
            return new RowElement();
        }
    }

    public static class PagerLayout extends Layout {

        public Size layoutChildren(Element element) {

            Rect v = element.get(Frame,Rect.class);

            if(v != null) {

                Element p = element.firstChild();

                while(p != null) {

                    if(p instanceof PageElement) {
                        Layout.elementLayout(p,v.size);
                    }

                    p = p.nextSibling();
                }

                return v.size;
            }

            return Size.Zero;
        }
    }

}
