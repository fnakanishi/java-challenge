package com.javachallenge.basico.client.builder;

import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.slf4j.Slf4jLogger;

public class MovieFeignClientBuilder {

    public static <T> T createClient(Class<T> type, String clientURL) {
        return Feign.builder()
            .encoder(new GsonEncoder())
            .decoder(new GsonDecoder())
            .logger(new Slf4jLogger(type))
            .logLevel(Logger.Level.HEADERS)
            .target(type, clientURL);
    }
}
