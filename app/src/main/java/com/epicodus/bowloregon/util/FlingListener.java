package com.epicodus.bowloregon.util;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.epicodus.bowloregon.R;

/**
 * Created by Guest on 5/16/16.
 */
public class FlingListener extends GestureDetector.SimpleOnGestureListener {

    private ImageView imageView;
    private ImageView pinView;
    private Animation flingAnimation;



    public FlingListener(ImageView imageView, Context context) {
        this.imageView = imageView;
        flingAnimation = AnimationUtils.loadAnimation(context, R.anim.ball_animation);


    }

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2,
                           float velocityX, float velocityY) {
        Log.d("fling listener", "fling!");

        if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE &&
                Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            imageView.startAnimation(flingAnimation);

            return true;
        }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE &&
                Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            //From Left to Right
            return true;
        }

        if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE &&
                Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
            //From Bottom to Top
            return true;
        }  else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE &&
                Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
            //From Top to Bottom
            return true;
        }
        return false;
    }
    @Override
    public boolean onDown(MotionEvent e) {
        Log.d("fling listener", "onDown");
        //always return true since all gestures always begin with onDown and

        //if this returns false, the framework won't try to pick up onFling for example.
        return true;
    }
}
