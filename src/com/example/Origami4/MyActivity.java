package com.example.Origami4;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

public class MyActivity extends Activity {

    PerspectiveView perspectiveView;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ViewGroup rootView = (ViewGroup) findViewById(R.id.rootView);

        perspectiveView = new PerspectiveView(this);
        perspectiveView.setTextures(
                BitmapFactory.decodeResource(getResources(), R.drawable.artbook_cover),
                BitmapFactory.decodeResource(getResources(), R.drawable.spine),
                BitmapFactory.decodeResource(getResources(), R.drawable.loadings_1)
        );
        rootView.addView(perspectiveView);

        rootView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onPause() {
        if (perspectiveView != null) {
            perspectiveView.onPause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (perspectiveView != null) {
            perspectiveView.onResume();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    perspectiveView.startAnimation();
                }
            }, 2000);
        }
        super.onResume();
    }
}
