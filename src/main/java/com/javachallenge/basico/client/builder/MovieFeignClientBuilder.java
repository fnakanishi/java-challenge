package com.javachallenge.basico.client.builder;

import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.slf4j.Slf4jLogger;

import static com.javachallenge.basico.Constants.IMDB_BASE_URL;


public class MovieFeignClientBuilder {

    public static <T> T createClient(Class<T> type) {
        return Feign.builder()
            .encoder(new GsonEncoder())
            .decoder(new GsonDecoder())
            .logger(new Slf4jLogger(type))
            .logLevel(Logger.Level.HEADERS)
            .target(type, IMDB_BASE_URL);
    }
}
