package cn.kkserver.view.element;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import cn.kkserver.view.style.Style;

/**
 * Created by zhanghailong on 2016/11/2.
 */

public abstract class AnimationElement extends Element {


    abstract public Animation getAnimation();

    public void setAnimation(Animation animation) {

        animation.setDuration(get(Style.Duration,Long.class,0L));
        animation.setRepeatMode(get(Style.Autoreverses,Boolean.class,false) ? Animation.REVERSE : Animation.RESTART);
        animation.setRepeatCount(get(Style.ReplayCount,Integer.class,0));
        animation.setStartOffset(get(Style.AfterDelay,Long.class,0L));

    }

    public static class SetElement extends AnimationElement {

        @Override
        public Animation getAnimation() {
            AnimationSet anim = new AnimationSet(true);
            setAnimation(anim);

            Element e = firstChild();

            while(e != null) {
                if(e instanceof AnimationElement) {
                    Animation v = ((AnimationElement) e).getAnimation();
                    setAnimation(v);
                    anim.addAnimation(v);
                }
                e = e.nextSibling();
            }

            return anim;
        }

        @Override
        protected Element onCreateCloneElement() {
            return new SetElement();
        }

    }

    public static class ScaleElement extends AnimationElement {

        @Override
        public Animation getAnimation() {
            ScaleAnimation anim = new ScaleAnimation(
                    get(Style.FromX,Float.class,0f),get(Style.ToX,Float.class,0f),
                    get(Style.FromY,Float.class,0f),get(Style.ToY,Float.class,0f),
                    Animation.RELATIVE_TO_SELF,get(Style.PivotX,Float.class,0.5f),
                    Animation.RELATIVE_TO_SELF,get(Style.PivotY,Float.class,0.5f)
            );
            setAnimation(anim);
            return anim;
        }

        @Override
        protected Element onCreateCloneElement() {
            return new ScaleElement();
        }
    }

    public static class TranslateElement extends AnimationElement {

        @Override
        public Animation getAnimation() {
            TranslateAnimation anim = new TranslateAnimation(
                    Animation.RELATIVE_TO_PARENT,get(Style.FromX,Float.class,0f),
                    Animation.RELATIVE_TO_PARENT,get(Style.ToX,Float.class,0f),
                    Animation.RELATIVE_TO_PARENT,get(Style.FromY,Float.class,0f),
                    Animation.RELATIVE_TO_PARENT,get(Style.ToY,Float.class,0f)
            );
            setAnimation(anim);
            return anim;
        }

        @Override
        protected Element onCreateCloneElement() {
            return new TranslateElement();
        }
    }

    public static class OpacityElement extends AnimationElement {

        @Override
        public Animation getAnimation() {
            AlphaAnimation anim = new AlphaAnimation(get(Style.FromOpacity,Float.class,0f),get(Style.ToOpacity,Float.class,0f));
            setAnimation(anim);
            return anim;
        }

        @Override
        protected Element onCreateCloneElement() {
            return new OpacityElement();
        }
    }

    public static class RotateElement extends AnimationElement {

        @Override
        public Animation getAnimation() {
            RotateAnimation anim = new RotateAnimation(
                    get(Style.FromDegrees,Float.class,0f),get(Style.ToDegrees,Float.class,0f),
                    Animation.RELATIVE_TO_SELF,get(Style.PivotX,Float.class,0.5f),
                    Animation.RELATIVE_TO_SELF,get(Style.PivotY,Float.class,0.5f));
            setAnimation(anim);
            return anim;
        }

        @Override
        protected Element onCreateCloneElement() {
            return new RotateElement();
        }
    }


}
