package com.amgems.terrastoriesbackend.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Pokemon {
    @JsonProperty("id")
    private Integer pokemonId;
    @JsonProperty("name")
    private String pokemonName;
    @JsonProperty("base_experience")
    private Integer baseExperience;
    @JsonProperty("weight")
    private Integer weight;
}
