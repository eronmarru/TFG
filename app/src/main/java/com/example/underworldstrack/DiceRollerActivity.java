package com.example.underworldstrack;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DiceRollerActivity extends AppCompatActivity implements SensorEventListener {

    private RadioGroup rgDiceType;
    private RadioButton rbAttack;
    private RadioButton rbDefense;
    private SeekBar sbDiceCount;
    private TextView tvDiceCountLabel;
    private Button btnRollDice;
    private GridLayout glDiceResults;
    private Button btnBack;
    private Random random;
    private List<Integer> lastRollResults = new ArrayList<>();
    private List<Float> lastRollRotations = new ArrayList<>();
    private boolean hasLastRoll = false;
    private boolean lastRollIsAttack = true;

    // Sensor variables
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float acceleration;
    private float currentAcceleration;
    private float lastAcceleration;
    private long lastShakeTime;
    private static final int SHAKE_THRESHOLD = 12;
    private static final int SHAKE_COOLDOWN = 1000; // ms

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice_roller);

        rgDiceType = findViewById(R.id.rgDiceType);
        rbAttack = findViewById(R.id.rbAttack);
        rbDefense = findViewById(R.id.rbDefense);
        sbDiceCount = findViewById(R.id.sbDiceCount);
        tvDiceCountLabel = findViewById(R.id.tvDiceCountLabel);
        btnRollDice = findViewById(R.id.btnRollDice);
        glDiceResults = findViewById(R.id.glDiceResults);
        btnBack = findViewById(R.id.btnBack);
        
        random = new Random();

        // Initialize sensor
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
        acceleration = 0.00f;
        currentAcceleration = SensorManager.GRAVITY_EARTH;
        lastAcceleration = SensorManager.GRAVITY_EARTH;

        sbDiceCount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Ensure minimum is 1 since SeekBar min attribute requires API 26+
                if (progress < 1) {
                    seekBar.setProgress(1);
                    return;
                }
                tvDiceCountLabel.setText("Cantidad de Dados: " + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btnRollDice.setOnClickListener(v -> rollDice());
        btnBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null && accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            lastAcceleration = currentAcceleration;
            currentAcceleration = (float) Math.sqrt(x * x + y * y + z * z);
            float delta = currentAcceleration - lastAcceleration;
            acceleration = acceleration * 0.9f + delta;

            if (acceleration > SHAKE_THRESHOLD) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastShakeTime > SHAKE_COOLDOWN) {
                    lastShakeTime = currentTime;
                    rollDice();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void rollDice() {
        glDiceResults.removeAllViews();
        int count = sbDiceCount.getProgress();
        if (count < 1) count = 1;

        boolean isAttack = rbAttack.isChecked();
        List<Integer> newResults = new ArrayList<>();
        List<Float> newRotations = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Integer previousResId = null;
            Float previousRotation = 0f;
            if (hasLastRoll && lastRollIsAttack == isAttack && i < lastRollResults.size()) {
                previousResId = lastRollResults.get(i);
                if (i < lastRollRotations.size()) {
                    previousRotation = lastRollRotations.get(i);
                }
            }
            ImageView diceView = createDiceView(isAttack, previousResId, previousRotation);
            Object tag = diceView.getTag();
            if (tag instanceof Integer) {
                newResults.add((Integer) tag);
            } else {
                newResults.add(0);
            }
            newRotations.add(diceView.getRotation());
            glDiceResults.addView(diceView);
        }
        lastRollResults = newResults;
        lastRollRotations = newRotations;
        lastRollIsAttack = isAttack;
        hasLastRoll = true;
    }

    private ImageView createDiceView(boolean isAttack, Integer previousResId, Float previousRotation) {
        ImageView dice = new ImageView(this);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 250;
        params.height = 250;
        params.setMargins(16, 16, 16, 16);
        dice.setLayoutParams(params);
        dice.setScaleType(ImageView.ScaleType.FIT_CENTER);
        int resId = setRandomFace(dice, isAttack, previousResId, previousRotation);
        dice.setTag(resId);
        dice.setOnClickListener(v -> {
            Object tag = dice.getTag();
            Integer prev = tag instanceof Integer ? (Integer) tag : null;
            float rotationBefore = dice.getRotation();
            int newResId = setRandomFace(dice, isAttack, prev, rotationBefore);
            dice.setTag(newResId);
        });
        return dice;
    }

    private int setRandomFace(ImageView dice, boolean isAttack, Integer previousResId, Float previousRotation) {
        int roll = random.nextInt(6) + 1;
        int resId = 0;

        if (isAttack) {
            switch (roll) {
                case 1:
                case 2:
                    resId = R.drawable.martillo_atq;
                    break;
                case 3:
                    resId = R.drawable.espadas_atq;
                    break;
                case 4:
                    resId = R.drawable.critico_atq;
                    break;
                case 5:
                    resId = R.drawable.rodeado_atq;
                    break;
                case 6:
                    resId = R.drawable.flanqueado_atq;
                    break;
            }
        } else {
            switch (roll) {
                case 1:
                case 2:
                    resId = R.drawable.escudo_def;
                    break;
                case 3:
                    resId = R.drawable.esquiva_def;
                    break;
                case 4:
                    resId = R.drawable.critico_def;
                    break;
                case 5:
                    resId = R.drawable.rodeado_def;
                    break;
                case 6:
                    resId = R.drawable.flanqueado_def;
                    break;
            }
        }

        if (resId != 0) {
            dice.setImageResource(resId);
        } else {
            dice.setImageResource(android.R.drawable.ic_menu_help);
        }
        float newRotation = 0f;
        if (previousResId != null && previousResId == resId) {
            float baseRotation = previousRotation != null ? previousRotation : dice.getRotation();
            newRotation = (baseRotation + 90f) % 360f;
        }
        dice.setRotation(newRotation);
        return resId;
    }
}
