package com.example.alejandro.pokemon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.alejandro.pokemon.models.Pokemon;
import com.example.alejandro.pokemon.models.PokemonResponse;
import com.example.alejandro.pokemon.pokeapi.PokemonService;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "POKEDEX: ";

    private RecyclerView mRecyclerView;

    private AdaptadorPokemon adaptadorPokemon;

    private int offset;

    private boolean readyToRefresh;
    ArrayList<Pokemon> listPokemon;

    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        adaptadorPokemon = new AdaptadorPokemon(this);

        mRecyclerView.setAdapter(adaptadorPokemon);
        mRecyclerView.setHasFixedSize(true);

        final GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 ) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                    if (readyToRefresh){
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount){
                            Log.i(TAG, "Final del Recycler...");

                            readyToRefresh = false;
                            offset += 20;
                            obtenerDatos(offset);
                        }
                    }
                }

                super.onScrolled(recyclerView, dx, dy);
            }
        });

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        readyToRefresh = true;
        offset = 0;
        obtenerDatos(offset);
    }

    private void obtenerDatos(int offset) {
        PokemonService service = retrofit.create(PokemonService.class);
        Call<PokemonResponse> pokemonResponseCall = service.getPokemonList(20, offset);

        pokemonResponseCall.enqueue(new Callback<PokemonResponse>() {
            @Override
            public void onResponse(Call<PokemonResponse> call, Response<PokemonResponse> response) {
                Log.d(TAG, response.body().getResults().toString());

                readyToRefresh = true;
                if (response.isSuccessful()){
                    PokemonResponse pokemonResponse = response.body();
                    listPokemon = pokemonResponse.getResults();

                    adaptadorPokemon.addListPokemon(listPokemon);

                } else {
                    Log.e(TAG, "onResponse: " + response.errorBody());
                }

//                for (Pokemon pokemon : listPokemon) {
////                    Log.d(TAG, "\n" + pokemon);
//                    pokemon.setNumber(pokemon.getNumber());
//                    Log.d(TAG, "\n" + pokemon);
//                }

                adaptadorPokemon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Log.d(TAG, "Click: Posicion del layout " + mRecyclerView.getChildLayoutPosition(view));
                            int pokedexNumber = mRecyclerView.getChildAdapterPosition(view);
                            Pokemon pokemon = listPokemon.get(pokedexNumber);
                            Log.d(TAG, "Nombre: " + pokemon.getName());
                            Intent intent = new Intent(MainActivity.this, PokeInfo.class);
                            intent.putExtra("pokeNum", pokedexNumber);
                            intent.putExtra("pokeName", pokemon.getName());
                            startActivity(intent);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<PokemonResponse> call, Throwable t) {
                readyToRefresh = true;
                Log.e(TAG, "onFailure: " +t.getMessage());
            }
        });
    }
}
