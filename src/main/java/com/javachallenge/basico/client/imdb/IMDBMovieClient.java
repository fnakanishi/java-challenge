package com.javachallenge.basico.client.imdb;

import com.javachallenge.basico.client.builder.MovieFeignClientBuilder;
import com.javachallenge.basico.client.imdb.resources.ImdbMovieDTO;
import com.javachallenge.basico.client.imdb.resources.ImdbMovieList;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

import static com.javachallenge.basico.Constants.IMDB_BASE_URL;

@Headers("user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36")
public interface IMDBMovieClient {

    static IMDBMovieClient buildMovieClient() {
        MovieFeignClientBuilder movieFeignClientBuilder = new MovieFeignClientBuilder();
        return movieFeignClientBuilder.createClient(IMDBMovieClient.class, IMDB_BASE_URL);
    }

    @RequestLine("GET /Title/{apiKey}/{imdbId}")
    ImdbMovieDTO findByImdbId(@Param String apiKey, @Param String imdbId);

    @RequestLine("GET /Top250Movies/{apiKey}")
    ImdbMovieList findAll(@Param String apiKey);
}