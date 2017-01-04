package com.shenhua.pc_controller;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Shenhua on 1/3/2017.
 * e-mail shenhuanet@126.com
 */
public class TouchView extends View {

    private OnViewTouchListener listener;

    public TouchView(Context context) {
        this(context, null);
    }

    public TouchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TouchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClickable(true);
        setFocusable(true);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                System.out.println("shenhua sout:--------------------->" + "down");
//                return true;
//            case MotionEvent.ACTION_MOVE:
//                System.out.println("shenhua sout:" + "move");
//                return false;
//            case MotionEvent.ACTION_UP:
//
//                break;
//        }
        return super.dispatchTouchEvent(event);
    }

    private float x;
    private float y;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                y = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //System.out.println("shenhua sout:x: " + (event.getX() - x));
                //System.out.println("shenhua sout:y: " + (event.getY() - y));
                if (listener != null) {
                    listener.onMove((int) (x - event.getX()), (int) (y - event.getY()));
                }
                break;
            case MotionEvent.ACTION_UP:
                if (x == event.getX() && y == event.getY()) {
                    if (listener != null) {
                        listener.onClick();
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    public void setOnViewTouchListener(OnViewTouchListener listener) {
        this.listener = listener;
    }

    public interface OnViewTouchListener {
        void onMove(int dx, int dy);

        void onClick();
    }

}
