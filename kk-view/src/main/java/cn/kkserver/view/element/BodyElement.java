package cn.kkserver.view.element;

import android.view.DragEvent;
import android.view.View;
import java.lang.ref.WeakReference;

/**
 * Created by zhanghailong on 2016/11/3.
 */

public class BodyElement extends ViewElement {

    public BodyElement(View view) {
        super(view,true);
        view.setOnDragListener(new OnCallback(this));
    }

    @Override
    protected Element onCreateCloneElement() {
        return new BodyElement(view());
    }

    private static class OnCallback implements View.OnDragListener {

        private WeakReference<BodyElement> _element;

        public OnCallback(BodyElement element) {
            _element = new WeakReference<>(element);
        }

        @Override
        public boolean onDrag(View v, DragEvent event) {

            BodyElement e = _element.get();

            if(e != null) {
                ViewElementDragEvent ev = new ViewElementDragEvent(e,event);
                e.dispatchEvent(ViewElementDragEvent.DRAG,ev);
                return true;
            }

            return false;
        }
    }
}
