package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

public class Progressbar extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progressbar);
        ProgressBar pb=(ProgressBar)findViewById(R.id.progressBar);
        pb.setVisibility(View.VISIBLE);


    }
}
