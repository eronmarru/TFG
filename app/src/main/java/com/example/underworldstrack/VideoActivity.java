package com.example.underworldstrack;

import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity para reproducir videos.
 * Soporta reproducción desde recursos locales o URLs remotas.
 * Ajusta automáticamente el tamaño del video para mantener la proporción.
 */
public class VideoActivity extends AppCompatActivity {

    public static final String EXTRA_URI = "extra_uri"; // Clave para pasar la URI del video

    private ProgressBar progressBar;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        videoView = findViewById(R.id.videoView);
        progressBar = findViewById(R.id.progressBar);
        Button btnBack = findViewById(R.id.btn_back_video);

        btnBack.setOnClickListener(v -> finish());

        String uriString = getIntent().getStringExtra(EXTRA_URI);

        if (uriString != null) {
            Uri uri;
            if (uriString.startsWith("http")) {
                // Es una URL remota
                uri = Uri.parse(uriString);
                progressBar.setVisibility(View.VISIBLE);
            } else {
                // Es un recurso local
                int resId = getResources().getIdentifier(uriString, "raw", getPackageName());
                uri = Uri.parse("android.resource://" + getPackageName() + "/" + resId);
                progressBar.setVisibility(View.GONE);
            }

            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            videoView.setVideoURI(uri);

            // Listener cuando el video está listo para reproducirse
            videoView.setOnPreparedListener(mp -> {
                int videoWidth = mp.getVideoWidth();
                int videoHeight = mp.getVideoHeight();

                if (videoWidth > 0 && videoHeight > 0) {
                    DisplayMetrics metrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(metrics);

                    int screenWidth = metrics.widthPixels;
                    float videoProportion = (float) videoWidth / (float) videoHeight;

                    // Ajustar altura manteniendo proporción
                    int newWidth = screenWidth;
                    int newHeight = (int) (screenWidth / videoProportion);

                    android.view.ViewGroup.LayoutParams lp = videoView.getLayoutParams();
                    lp.width = newWidth;
                    lp.height = newHeight;
                    videoView.setLayoutParams(lp);
                }

                progressBar.setVisibility(View.GONE);
                videoView.start();
            });

            videoView.setOnErrorListener((mp, what, extra) -> {
                progressBar.setVisibility(View.GONE);
                return false;
            });
        }
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
