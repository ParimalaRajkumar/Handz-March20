package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

/**
 * Created by IZ-Parimala on 26-06-2018.
 */

public class MapActivity extends AppCompatActivity {
    Fragment fragment = null;

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
}
