package com.example.danton.comutelogger;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity
{


    Button btnStart, btnPause, btnStop;
    TextView txtTimer;
    Handler customHandler = new Handler();
    LinearLayout container;

    long startTime = 0L, timeMs = 0L, timeSwapBuff = 0L, updateTime= 0L;

    Runnable updateTimerThread = new Runnable()
    {
        @Override
        public void run()
        {
            timeMs      = SystemClock.uptimeMillis() - startTime;
            updateTime  = timeSwapBuff + timeMs;
            int seconds = (int)(updateTime / 1000);
            int minutes = seconds/60;
            int hours   = minutes/60;
            int milliseconds = (int)(updateTime % 1000);
            txtTimer.setText("" + minutes + ":" +
                             "" + String.format("%02d", seconds) + ":" +
                             "" + String.format("%03d", milliseconds)
            );

            customHandler.postDelayed(this, 0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart  = (Button)findViewById(R.id.startButton);
        btnPause  = (Button)findViewById(R.id.pauseButton);
        btnStop   = (Button)findViewById(R.id.stopButton);
        txtTimer  = (TextView)findViewById(R.id.timerValue);
        container = (LinearLayout)findViewById(R.id.container);

        btnStart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startTime = SystemClock.uptimeMillis();
                customHandler.postDelayed(updateTimerThread, 0);
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                timeSwapBuff += timeMs;
                customHandler.removeCallbacks(updateTimerThread);
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                LayoutInflater inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View addView = inflater.inflate(R.layout.raw, null);
                TextView txtValue = (TextView)addView.findViewById(R.id.txtContent);
                txtValue.setText(txtTimer.getText());
                container.addView(addView);
            }
        });

    }
}
