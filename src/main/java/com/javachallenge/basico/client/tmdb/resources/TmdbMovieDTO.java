package com.javachallenge.basico.client.tmdb.resources;


import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class TmdbMovieDTO {
    private Long id;
    @SerializedName("imdb_id")
    private String imdbId;
    private String title;
    @SerializedName("original_title")
    private String originalTitle;
    @SerializedName("poster_path")
    private String image;
    @SerializedName("release_date")
    private Date releaseDate;
    private String runtime;
    @SerializedName("overview")
    private String plot;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }
}
