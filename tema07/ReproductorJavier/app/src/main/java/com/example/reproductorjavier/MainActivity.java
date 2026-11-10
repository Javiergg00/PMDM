package com.example.reproductorjavier;

import android.media.AudioAttributes;
import android.media.SoundPool;
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

    private SoundPool soundPool;
    private int idCascabeles, idGrito, idSampler, idTambor;
    private int sonidosCargados = 0;

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

        Button btnCascabeles = findViewById(R.id.btnCascabeles);
        Button btnGrito = findViewById(R.id.btnGrito);
        Button btnSampler = findViewById(R.id.btnSampler);
        Button btnTambor = findViewById(R.id.btnTambor);

        AudioAttributes atributos = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(4)
                .setAudioAttributes(atributos)
                .build();

        soundPool.setOnLoadCompleteListener((pool, idSonido, estado) -> {
            if (estado == 0) {
                sonidosCargados++;
                if (sonidosCargados == 4) {
                    btnCascabeles.setEnabled(true);
                    btnGrito.setEnabled(true);
                    btnSampler.setEnabled(true);
                    btnTambor.setEnabled(true);
                }
            }
        });

        idCascabeles = soundPool.load(this, R.raw.efecto_cascabeles, 1);
        idGrito = soundPool.load(this, R.raw.efecto_grito, 1);
        idSampler = soundPool.load(this, R.raw.efecto_sampler, 1);
        idTambor = soundPool.load(this, R.raw.efecto_tambor, 1);

        btnCascabeles.setOnClickListener(v -> soundPool.play(idCascabeles, 1f, 1f, 1, 0, 1f));
        btnGrito.setOnClickListener(v -> soundPool.play(idGrito, 1f, 1f, 1, 0, 1f));
        btnSampler.setOnClickListener(v -> soundPool.play(idSampler, 1f, 1f, 1, 0, 1f));
        btnTambor.setOnClickListener(v -> soundPool.play(idTambor, 1f, 1f, 1, 0, 1f));
    }

    @Override
    protected void onStart() {
        super.onStart();
        handler.post(actualizarBarra);
    }

    @Override
    protected void onStop() {
        super.onStop();
        player.pause();
        handler.removeCallbacks(actualizarBarra);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
        player = null;
        soundPool.release();
        soundPool = null;
    }

    private String formatear(long ms) {
        long segundos = ms / 1000;
        return String.format(Locale.getDefault(), "%02d:%02d", segundos / 60, segundos % 60);
    }
}