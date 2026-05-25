package com.amgems.terrastoriesbackend;

import com.amgems.terrastoriesbackend.domain.Pokemon;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "pokemon-example", url = "https://pokeapi.co/api/v2/pokemon")
public interface IPokemonExample {

    @GetMapping("/{pokemonName}")
    public Pokemon getPokemon(@PathVariable("pokemonName") String pokemonName);

}
