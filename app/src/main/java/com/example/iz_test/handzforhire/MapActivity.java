package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import com.listeners.Callback;

/**
 * Created by IZ-Parimala on 26-06-2018.
 */

public class MapActivity extends AppCompatActivity implements Callback<Location> {
    FindJobMap fragment = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==LocationTrack.REQUEST_CHECK_SETTINGS) {

            if(fragment != null)
            fragment.getLocation();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_mapactivity);
        fragment=new FindJobMap();
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.popBackStackImmediate ("map", 0)) {
        }else{
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment).addToBackStack("map");
            fragmentTransaction.commit();
        }

    }

    @Override
    public void onUpdate(Location location) {

        fragment.UpdateLocation(location);
    }
}
