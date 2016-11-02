package cn.kkserver.view;

/**
 * Created by zhanghailong on 16/7/14.
 */
public class Point {

    public int x;
    public int y;

    public Point() {
        this.x = this.y = 0;
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int left() {
        return x;
    }

    public int top() {
        return y;
    }

}
