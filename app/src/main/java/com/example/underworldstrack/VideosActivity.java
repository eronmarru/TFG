package com.example.underworldstrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class VideosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);

        View itemTrailer = findViewById(R.id.item_trailer);
        View itemReview = findViewById(R.id.item_review);
        View btnTrailerPlay = findViewById(R.id.btn_trailer_play);
        View btnReviewPlay = findViewById(R.id.btn_review_play);
        Button btnBack = findViewById(R.id.btn_back_videos);

        View.OnClickListener playTrailerListener = v -> {
            Intent intent = new Intent(this, VideoActivity.class);
            intent.putExtra(VideoActivity.EXTRA_URI, "https://raw.githubusercontent.com/eronmarru/Proyecto/main/Trailer.mp4");
            startActivity(intent);
        };

        View.OnClickListener playReviewListener = v -> {
            Intent intent = new Intent(this, VideoActivity.class);
            intent.putExtra(VideoActivity.EXTRA_URI, "https://raw.githubusercontent.com/eronmarru/Proyecto/main/review.mp4");
            startActivity(intent);
        };

        itemTrailer.setOnClickListener(playTrailerListener);
        btnTrailerPlay.setOnClickListener(playTrailerListener);

        itemReview.setOnClickListener(playReviewListener);
        btnReviewPlay.setOnClickListener(playReviewListener);

        btnBack.setOnClickListener(v -> finish());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }
}
