package com.example.listajavier;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

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

        // 1. Los datos: al menos 8 elementos
        ArrayList<Videojuego> videojuegos = new ArrayList<>();
        videojuegos.add(new Videojuego("Zelda", "Nintendo Switch", R.drawable.ic_estrella));
        videojuegos.add(new Videojuego("Mario Kart", "Nintendo Switch", R.drawable.ic_mando));
        videojuegos.add(new Videojuego("FIFA", "PlayStation", R.drawable.ic_mando));
        videojuegos.add(new Videojuego("Minecraft", "PC", R.drawable.ic_libro));
        videojuegos.add(new Videojuego("Fortnite", "Xbox", R.drawable.ic_sombrilla));
        videojuegos.add(new Videojuego("Hollow Knight", "PC", R.drawable.ic_estrella));
        videojuegos.add(new Videojuego("God of War", "PlayStation", R.drawable.ic_libro));
        videojuegos.add(new Videojuego("Stardew Valley", "PC", R.drawable.ic_sombrilla));

        // 2. La lista: cómo se colocan las filas y quién las pinta
        RecyclerView rvLista = findViewById(R.id.rvLista);
        rvLista.setLayoutManager(new LinearLayoutManager(this));
        rvLista.setAdapter(new AdaptadorVideojuegos(videojuegos));
    }
}