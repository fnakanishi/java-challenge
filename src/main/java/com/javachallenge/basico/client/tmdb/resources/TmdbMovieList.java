package com.javachallenge.basico.client.tmdb.resources;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TmdbMovieList {
    private List<TmdbMovieDTO> results;
}