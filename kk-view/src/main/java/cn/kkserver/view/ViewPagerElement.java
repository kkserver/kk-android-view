package cn.kkserver.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanghailong on 2016/11/1.
 */

public class ViewPagerElement extends ViewElement  {

    private final ViewPagerElementAdpater _adapter = new ViewPagerElementAdpater(this);
    private boolean _editing;

    public ViewPager viewPager() {
        return (ViewPager) view();
    }

    public ViewPagerElement(Context context) {
        super(new ViewPager(context));
        viewPager().setAdapter(_adapter);
    }

    public ViewPagerElement(ViewPager view) {
        super(view);
        view.setAdapter(_adapter);
    }

    @Override
    protected void onPropertyChanged(Property property, Object value, Object newValue) {
        super.onPropertyChanged(property,value,newValue);
    }


    public boolean isEditing() {
        return _editing;
    }

    public void beginEditing() {
        _editing = true;
    }

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

    public PageElement addPageElement(View view,String reuse) {
        PageElement page = new PageElement(view);
        page.set(Style.Reuse,reuse);
        append(page);
        return page;
    }

    public RowElement addRowElement(String reuse) {

        Element p = firstChild();

        while(p != null) {
            if(p instanceof PageElement) {
                String v = p.get(Style.Reuse,String.class);
                if((v == reuse) || (reuse != null && reuse.equals(v))) {
                    RowElement row = (RowElement) p.reflect();
                    append(row);
                    return row;
                }
                break;
            }
            p = p.nextSibling();
        }

        return null;
    }

    private static class ViewPagerElementAdpater extends PagerAdapter {

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
            return row.element != null && ((PageElement) row.element).view() == view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            RowElement row = (RowElement) object;

            if(row.element != null) {
                PageElement page = (PageElement) row.element;
                container.removeView(page.view());
                row.cancelReflect();
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

                rowElement.commitReflect(page);
                View view = page.view();
                ViewParent pv = view.getParent();
                if(p != null && pv instanceof ViewGroup) {
                    ((ViewGroup) pv).removeView(view);
                }

                container.addView(view);

            }

            return rowElement;
        }
    }


    public static class PageElement extends ViewElement {

        public PageElement(View view) {
            super(view);
        }

        @Override
        protected ReflectElement onCreateReflectElement() {
            return new RowElement(this);
        }

        @Override
        protected void onRemoveFromParent(Element element) {

        }

        @Override
        protected void onAddToParent(Element element) {

        }
    }

    public static class RowElement extends ReflectElement {

        public RowElement(PageElement element) {
            super(element);
        }

    }

}
