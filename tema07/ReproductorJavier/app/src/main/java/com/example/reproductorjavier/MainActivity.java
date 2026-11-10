package com.example.reproductorjavier;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ExoPlayer player;

    private SeekBar seekBar;
    private TextView tvActual;
    private TextView tvTotal;
    private boolean arrastrando = false;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable actualizarBarra = new Runnable() {
        @Override
        public void run() {
            if (player != null && !arrastrando) {
                long posicion = player.getCurrentPosition();
                seekBar.setProgress((int) posicion);
                tvActual.setText(formatear(posicion));
            }
            handler.postDelayed(this, 500);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Reproductor
        Button btnPlay = findViewById(R.id.btnPlay);
        Button btnPausa = findViewById(R.id.btnPausa);

        player = new ExoPlayer.Builder(this).build();
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.cancion);
        player.setMediaItem(MediaItem.fromUri(uri));
        player.prepare();

        btnPlay.setOnClickListener(v -> {
            if (player.getPlaybackState() == Player.STATE_ENDED) {
                player.seekTo(0);
            }
            player.play();
        });
        btnPausa.setOnClickListener(v -> player.pause());

        // Barra de progreso
        seekBar = findViewById(R.id.seekBar);
        tvActual = findViewById(R.id.tvActual);
        tvTotal = findViewById(R.id.tvTotal);

        player.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int estado) {
                if (estado == Player.STATE_READY) {
                    long duracion = player.getDuration();
                    seekBar.setMax((int) duracion);
                    tvTotal.setText(formatear(duracion));
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar barra, int progreso, boolean delUsuario) {
                if (delUsuario) {
                    tvActual.setText(formatear(progreso));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar barra) {
                arrastrando = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar barra) {
                player.seekTo(barra.getProgress());
                arrastrando = false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        handler.post(actualizarBarra);
    }

    private String formatear(long ms) {
        long segundos = ms / 1000;
        return String.format(Locale.getDefault(), "%02d:%02d", segundos / 60, segundos % 60);
    }
}