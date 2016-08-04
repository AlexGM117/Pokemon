package com.example.alejandro.pokemon.pokeapi;

import com.example.alejandro.pokemon.models.PokemonResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by alejandro on 04/08/16.
 */
public interface PokemonService {
    @GET("pokemon")
    //@Query Cambia los valores de algun fracmento especifico de de la url
        // http://pokeapi.co/api/v2/pokemon/?limit=20&offset=20 en este caso limit y offset
    //que indican cuantos elementos se muestran
    Call<PokemonResponse> getPokemonList(@Query("limit") int limit, @Query("offset") int offset);
}
