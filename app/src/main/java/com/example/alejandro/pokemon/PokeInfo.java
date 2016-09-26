package com.example.alejandro.pokemon;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.alejandro.pokemon.models.Pokemon;

public class PokeInfo extends AppCompatActivity {
    private TextView pokeName;
    private ImageView pokeImage;
    private Pokemon pokemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poke_info);

        pokeName = (TextView) findViewById(R.id.nombre_pokemon);

        pokeImage = (ImageView) findViewById(R.id.imagen_pokemon);

        Bundle bundle = getIntent().getExtras();

        int pokeNum = bundle.getInt("pokeNum") + 1;

        pokeName.setText(bundle.getString("pokeName"));

        Glide.with(this).load("http://pokeapi.co/media/sprites/pokemon/" + pokeNum +".png").into(pokeImage);
    }
}
