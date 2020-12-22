package cdictv.englishfour.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import cdictv.englishfour.R;
import cdictv.englishfour.utils.CircleProgressbar;

public class guideActivity extends AppCompatActivity {

    private static final String TAG ="SplashActivity";

    private CircleProgressbar mCircleProgressbar;

    private boolean isClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        mCircleProgressbar = (CircleProgressbar) findViewById(R.id.tv_red_skip);
        mCircleProgressbar.setOutLineColor(Color.TRANSPARENT);
        mCircleProgressbar.setInCircleColor(Color.parseColor("#505559"));
        mCircleProgressbar.setProgressColor(Color.parseColor("#1BB079"));
        mCircleProgressbar.setProgressLineWidth(5);
        mCircleProgressbar.setProgressType(CircleProgressbar.ProgressType.COUNT);
        mCircleProgressbar.setTimeMillis(3000);
        mCircleProgressbar.reStart();

        mCircleProgressbar.setCountdownProgressListener(1,progressListener);

        mCircleProgressbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                isClick = true;
                Intent intent = new Intent(guideActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    private CircleProgressbar.OnCountdownProgressListener progressListener = new CircleProgressbar.OnCountdownProgressListener() {
        @Override
        public void onProgress(int what, int progress)
        {

            if(what==1 && progress==100 && !isClick)
            {
                Intent intent = new Intent(guideActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                Log.e(TAG, "onProgress: =="+progress );
            }

        }
    };

}
