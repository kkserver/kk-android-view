package cn.kkserver.view.element;

import android.view.View;
/**
 * Created by zhanghailong on 2016/11/3.
 */

public class BodyElement extends ViewElement {

    public BodyElement(View view) {
        super(view,true);
    }

    @Override
    protected Element onCreateCloneElement() {
        return new BodyElement(view());
    }

}
