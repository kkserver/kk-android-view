package cn.kkserver.view.value;

/**
 * Created by zhanghailong on 16/7/14.
 */
public class Point implements Cloneable {

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

    @Override
    public int hashCode() {
        return x ^ y;
    }

    @Override
    public boolean equals(Object value) {
        if(value != null && value instanceof Point) {
            Point v = (Point) value;
            return v.x == x && v.y == y;
        }
        return false;
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {}
        return null;
    }
}
