package id.ac.umn.projectclam;

import android.content.Context;
import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProgressBarAnimation extends Animation {
    private Context context;
    private ProgressBar loadingBar;
    private TextView loadingPercent;
    private float from, to;

    public ProgressBarAnimation(Context context, ProgressBar loadingBar, TextView loadingPercent, float from, float to){
        this.context = context;
        this.loadingBar = loadingBar;
        this.loadingPercent = loadingPercent;
        this.from = from;
        this.to = to;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t){
        super.applyTransformation(interpolatedTime, t);
        float value = from + (to - from) * interpolatedTime;
        loadingBar.setProgress((int)value);
        loadingPercent.setText((int)value + " %");

        if(value == to){
            context.startActivity(new Intent(context, MainActivity.class));
        }
    }

}
