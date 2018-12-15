package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import com.app.AppConstant;

public abstract class AbsSwipeActivity extends AppCompatActivity implements SimpleGestureFilter.SimpleGestureListener {

    private SimpleGestureFilter detector;
    @Override
    public boolean dispatchTouchEvent(MotionEvent event){

        this.detector.onTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onSwipe(int direction) {
        String str = "";

        switch (direction) {

            case SimpleGestureFilter.SWIPE_RIGHT:
                str = "Swipe Right";
                Intent j = new Intent(this, SwitchingSide.class);
                startActivity(j);
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
                finish();
                break;
            case SimpleGestureFilter.SWIPE_LEFT:
                str = "Swipe Left";
//                Intent i = new Intent(ProfilePage.this, ProfilePage.class);
//                i.putExtra("userId", id);
//                i.putExtra("address", address);
//                i.putExtra("city", city);
//                i.putExtra("state", state);
//                i.putExtra("zipcode", zipcode);
//                startActivity(i);
//                overridePendingTransition(R.anim.slide_from_right ,R.anim.slide_to_left);
//                finish();

                break;
           /* case SimpleGestureFilter.SWIPE_DOWN:
                str = "Swipe Down";
                break;
            case SimpleGestureFilter.SWIPE_UP:
                str = "Swipe Up";
                break;*/
        }
    }

    @Override
    public void onDoubleTap() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detector = new SimpleGestureFilter(this,this);
    }

    abstract AppConstant.SWIPETYPE SwipeType();
}
