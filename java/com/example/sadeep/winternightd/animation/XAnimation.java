package com.example.sadeep.winternightd.animation;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.HorizontalScrollView;

/**
 * Created by Sadeep on 6/18/2017.
 */
public class XAnimation {
    private XAnimation() {}

    public static final int DIMENSION_WIDTH = 0;
    public static final int DIMENSION_HEIGHT = 1;


    public static void scroll(final HorizontalScrollView view, int duration, int amount){
        scroll( view,  duration,  amount,0);
    }

    public static void scroll(final HorizontalScrollView view, int duration, int amount,int delay){
        ValueAnimator animator = ValueAnimator.ofInt(0,amount).setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            int preVal = 0;
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                view.scrollBy(value-preVal,0);
                preVal = value;
            }
        });
        AnimatorSet set = new AnimatorSet();
        set.play(animator);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.setStartDelay(delay);
        set.start();
    }



    public static void changeDimension(final View view, int duration, final int dimension, int start, int end){
        changeDimension(view, duration, dimension, start, end,0);
    }

    public static void changeDimension(final View view, int duration, final int dimension, int start, int end,int delay){

        ValueAnimator slideAnimator;
        if(dimension==0)slideAnimator = ValueAnimator.ofInt(start,end).setDuration(duration);
        else slideAnimator = ValueAnimator.ofInt(start,end).setDuration(duration);

        slideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                if(dimension==0)view.getLayoutParams().width = value.intValue();
                else if(dimension==1)view.getLayoutParams().height = value.intValue();
                view.requestLayout();
            }
        });
        AnimatorSet set = new AnimatorSet();
        set.play(slideAnimator);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.setStartDelay(delay);
        set.start();
    }


    public static void changeBackgroundColor(final View view, int duration, int start, int end, int delay){

        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), start, end);
        colorAnimation.setDuration(duration); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                view.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.setInterpolator(new AccelerateInterpolator(.8f));
        colorAnimation.setStartDelay(delay);
        colorAnimation.start();
    }

}
