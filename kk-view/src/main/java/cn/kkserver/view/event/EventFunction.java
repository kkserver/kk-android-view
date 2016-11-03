package cn.kkserver.view.event;

/**
 * Created by zhanghailong on 2016/11/1.
 */

public interface EventFunction<T extends Object> {

    public void onEvent(String name,Event event, T weakObject);

}
