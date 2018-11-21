package org.phantancy.fgocalc.activity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.phantancy.fgocalc.R;
import org.phantancy.fgocalc.base.BaseActy;
import org.phantancy.fgocalc.view.CompassView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompassActy extends BaseActy {

    @BindView(R.id.ac_cv_compass)
    CompassView acCvCompass;
    @BindView(R.id.ac_iv_guda)
    ImageView acIvGuda;
    @BindView(R.id.ac_btn_summon)
    Button acBtnSummon;

    private SensorManager mSensorManager;
    private SensorEventListener mSensorEventListener;
    float val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acty_compass);
        ButterKnife.bind(this);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);


        mSensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                val = event.values[0];
                acCvCompass.setVal(val);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };

        mSensorManager.registerListener(mSensorEventListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);

        acBtnSummon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("angle",val);
                setResult(RESULT_OK,i);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(mSensorEventListener);
    }
}
