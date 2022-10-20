package com.javachallenge.basico.client;

import com.javachallenge.basico.client.builder.MovieFeignClientBuilder;
import com.javachallenge.basico.client.resources.MovieListResource;
import com.javachallenge.basico.client.resources.dto.MovieDTO;
import com.javachallenge.basico.entity.Movie;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

@Headers("user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36")
public interface MovieClient {

    static MovieClient buildMovieClient() {
        MovieFeignClientBuilder movieFeignClientBuilder = new MovieFeignClientBuilder();
        return movieFeignClientBuilder.createClient(MovieClient.class);
    }

    @RequestLine("GET /Title/{apiKey}/{imdbId}")
    MovieDTO findById(@Param String apiKey, @Param String imdbId);

    @RequestLine("GET /Top250Movies/{apiKey}")
    MovieListResource findAll(@Param String apiKey);

    @RequestLine("POST")
    @Headers("Content-Type: application/json")
    void create(Movie movie);
}
