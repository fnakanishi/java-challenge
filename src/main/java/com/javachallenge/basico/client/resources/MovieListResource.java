package com.javachallenge.basico.client.resources;

import com.javachallenge.basico.client.resources.dto.MovieDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MovieListResource {
    private List<MovieDTO> items;
}
