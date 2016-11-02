package cn.kkserver.view;

import java.util.TreeMap;

/**
 * Created by zhanghailong on 2016/11/1.
 */

public class StyleSheet extends Object {

    private final TreeMap<String,Style> _styles = new TreeMap<>();

    public Style get(String name) {
        if(_styles.containsKey(name)) {
            return _styles.get(name);
        }
        return null;
    }

    public void loadCSSContent(String cssContent) {

        String[] items = cssContent.split("\\}");

        for(String item : items) {

            String[] nstyle = item.split("\\{");

            if(nstyle.length > 1) {

                String[] nstatus = nstyle[0].trim().split(":");

                String name = nstatus[0];
                String status = nstatus.length > 1 ? nstatus[1] : "";

                Style style;

                if(_styles.containsKey(name)) {
                    style = _styles.get(name);
                }
                else {
                    style = new Style();
                    _styles.put(name,style);
                }

                style.loadCSSContent(nstyle[1],status);
            }

        }
    }

    public static StyleSheet valueOf(String value) {

        StyleSheet v = new StyleSheet();

        v.loadCSSContent(value);

        return v;
    }
}
