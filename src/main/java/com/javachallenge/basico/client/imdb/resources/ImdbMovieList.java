package com.javachallenge.basico.client.imdb.resources;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ImdbMovieList {
    private List<ImdbMovieDTO> items;
}
