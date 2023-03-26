package com.javachallenge.basico.client.tmdb;

import com.javachallenge.basico.client.builder.MovieFeignClientBuilder;
import com.javachallenge.basico.client.tmdb.resources.TmdbMovieList;
import com.javachallenge.basico.client.tmdb.resources.TmdbMovieDTO;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import static com.javachallenge.basico.Constants.TMDB_BASE_URL;

@Headers("user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36")
public interface TMDBMovieClient {

    static TMDBMovieClient buildMovieClient() {
        MovieFeignClientBuilder movieFeignClientBuilder = new MovieFeignClientBuilder();
        return movieFeignClientBuilder.createClient(TMDBMovieClient.class, TMDB_BASE_URL);
    }

    @RequestLine("GET /find/{imdbId}?external_source=imdb_id&api_key={apiKey}")
    TmdbMovieDTO findByImdbId(@Param String apiKey, @Param String imdbId);

    @RequestLine("GET /movie/{tmdbId}?api_key={apiKey}")
    TmdbMovieDTO findByTmdbId(@Param String apiKey, @Param Long tmdbId);

    @RequestLine("GET /movie/top_rated?api_key={apiKey}")
    TmdbMovieList findAll(@Param String apiKey);
}