package com.example.alejandro.pokemon.models;

import java.util.ArrayList;

/**
 * Created by alejandro on 04/08/16.
 */
public class PokemonResponse {

    private ArrayList<Pokemon> results;

    public ArrayList<Pokemon> getResults() {
        return results;
    }

    public void setResults(ArrayList<Pokemon> results) {
        this.results = results;
    }
}
