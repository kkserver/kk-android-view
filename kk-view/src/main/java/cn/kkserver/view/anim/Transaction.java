package cn.kkserver.view.anim;


import android.os.Build;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import cn.kkserver.view.value.Rect;

import static android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH;

/**
 * Created by zhanghailong on 2016/11/10.
 */

public class Transaction extends Object {

    private static List<PropertyAnimation<?>> _animations ;
    private static long _duration;
    private static long _afterDelay;
    private static int _repeatMode;
    private static int _repeatCount;

    public static void begin() {
        _animations = new ArrayList<>();
        _duration = 0;
        _afterDelay = 0;
        _repeatMode = 0;
        _repeatCount = 0;
    }

    public static void cancel() {
        _animations = null;
    }

    public static void setDuration(long value) {
        _duration = value;
    }

    public static void setAfterDelay(long value) {
        _afterDelay = value;
    }

    public void setRepeatMode(int repeatMode) {
        _repeatMode = repeatMode;
    }


    public void setRepeatCount(int repeatCount) {
        _repeatCount = repeatCount;
    }

    public static void commit() {

        if(_animations != null) {

            List<View> views = new ArrayList<>();
            Map<View,AnimationSet> anims = new HashMap<>();

            for(PropertyAnimation<?> v : _animations) {

                AnimationSet anim;

                if(anims.containsKey(v.view)) {
                    anim = anims.get(v.view);
                }
                else {
                    anim = new TransactionAnimationSet();
                    anim.setDuration(_duration);
                    anim.setStartOffset(_afterDelay);
                    anim.setRepeatMode(_repeatMode);
                    anim.setRepeatCount(_repeatCount);
                    anims.put(v.view,anim);
                    views.add(v.view);
                }

                v.addAnimation(anim);

            }

            for(View view : views) {
                view.startAnimation(anims.get(view));
            }
        }
    }

    public static void setFrame(View view,Rect fromValue,Rect toValue) {
        if(_animations != null && fromValue != null && ! fromValue.equals(toValue)) {
            _animations.add(new PropertyFrameAnimation(view,fromValue,toValue));
        }
    }

    public static void setAlpha(View view,float fromValue,float toValue) {
        if(_animations != null && fromValue != toValue) {
            _animations.add(new PropertyAlphaAnimation(view,fromValue,toValue));
        }
    }

    public static void setScale(View view,float fromValue,float toValue) {
        if(_animations != null && fromValue != toValue) {
            _animations.add(new PropertyScaleAnimation(view,fromValue,toValue));
        }
    }

    private static abstract class PropertyAnimation<T extends Object> {

        public final View view;
        public final T fromValue;
        public final T toValue;

        public PropertyAnimation(View view,T fromValue,T toValue) {
            this.view = view;
            this.fromValue = fromValue;
            this.toValue = toValue;
        }

        public abstract void addAnimation(AnimationSet anim);

    }

    private static class TransactionAnimationSet extends AnimationSet {

        public TransactionAnimationSet() {
            super(true);
        }

        public void addAnimation(Animation anim) {
            anim.setDuration(getDuration());
            anim.setRepeatMode(getRepeatMode());
            anim.setRepeatCount(getRepeatCount());
            anim.setStartOffset(getStartOffset());
            super.addAnimation(anim);
        }
    }

    private static class PropertyFrameAnimation extends PropertyAnimation<Rect>{


        public PropertyFrameAnimation(View view, Rect fromValue, Rect toValue) {
            super(view, fromValue, toValue);
        }

        @Override
        public void addAnimation(AnimationSet anim) {

            anim.addAnimation(new TranslateAnimation(
                    Animation.ABSOLUTE,fromValue.origin.x - toValue.origin.x,Animation.ABSOLUTE,0.0f
                    ,Animation.ABSOLUTE,fromValue.origin.y - toValue.origin.y,Animation.ABSOLUTE,0.0f));

            anim.addAnimation(new ScaleAnimation(
                    1.0f,(float) fromValue.size.width / (float) toValue.size.width,
                    1.0f,(float) fromValue.size.height / (float) toValue.size.height));

        }
    }

    private static class PropertyAlphaAnimation extends PropertyAnimation<Float> {

        public PropertyAlphaAnimation(View view, Float fromValue, Float toValue) {
            super(view, fromValue, toValue);
        }

        @Override
        public void addAnimation(AnimationSet anim) {
            anim.addAnimation(new AlphaAnimation(fromValue,toValue));
        }
    }

    private static class PropertyScaleAnimation extends PropertyAnimation<Float> {

        public PropertyScaleAnimation(View view, Float fromValue, Float toValue) {
            super(view, fromValue, toValue);
        }

        @Override
        public void addAnimation(AnimationSet anim) {
            anim.addAnimation(new ScaleAnimation(fromValue,toValue,fromValue,toValue));
        }
    }

}
