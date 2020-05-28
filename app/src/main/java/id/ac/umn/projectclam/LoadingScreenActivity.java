package id.ac.umn.projectclam;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoadingScreenActivity extends AppCompatActivity {
    ProgressBar loadingBar;
    TextView loadingPercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        loadingBar = findViewById(R.id.loadingBar);
        loadingPercent = findViewById(R.id.loadingPercent);

        loadingBar.setMax(100);
        loadingBar.setScaleY(3f);

        progressAnimation();
    }

    public void progressAnimation(){
        ProgressBarAnimation anim = new ProgressBarAnimation(this, loadingBar, loadingPercent, 0f, 100f);
        anim.setDuration(5000);
        loadingBar.setAnimation(anim);
    }
}
