package com.javachallenge.basico.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.javachallenge.basico.client.imdb.resources.ImdbMovieDTO;
import com.javachallenge.basico.client.tmdb.resources.TmdbMovieDTO;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "TB_MOVIE", uniqueConstraints = @UniqueConstraint(columnNames = "IMDB_ID"))
@JsonIgnoreProperties("usersFavorited")
public class Movie implements Serializable {
    private Long id;
    private String imdbId;
    private String title;
    private String originalTitle;
    private String fullTitle;
    private String image;
    private Date releaseDate;
    private String runtime;
    private String plot;
    private String genres;
    private int favorited;
    private Set<User> usersFavorited;

    public Movie() {
    }

    public Movie(ImdbMovieDTO dto) {
        this.imdbId = dto.getId();
        this.title = dto.getTitle();
        this.originalTitle = dto.getOriginalTitle();
        this.fullTitle = dto.getFullTitle();
        this.image = dto.getImage();
        this.releaseDate = dto.getReleaseDate();
        this.runtime = dto.getRuntime();
        this.plot = dto.getPlot();
        this.genres = dto.getGenres();
    }

    public Movie(TmdbMovieDTO dto) {
        this.imdbId = dto.getImdbId();
        this.title = dto.getTitle();
        this.originalTitle = dto.getOriginalTitle();
        this.image = ("https://image.tmdb.org/t/p/original" + dto.getImage());
        this.releaseDate = dto.getReleaseDate();
        this.runtime = dto.getRuntime();
        this.plot = this.getPlot();
        this.genres = ""; // stream the shit out of it;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "IMDB_ID", nullable = false)
    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    @Column(nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "ORIGINAL_TITLE", nullable = false)
    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    @Column(name = "FULL_TITLE")
    public String getFullTitle() {
        return fullTitle;
    }

    public void setFullTitle(String fullTitle) {
        this.fullTitle = fullTitle;
    }

    @Column(name = "IMAGE_URL")
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Column(name = "RELEASE_DATE", nullable = false)
    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Column()
    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    @Column()
    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    @Column()
    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    @Column(nullable = false)
    public int getFavorited() {
        return favorited;
    }

    public void setFavorited(int favorited) {
        this.favorited = favorited;
    }

    @ManyToMany(mappedBy = "favorites", fetch = FetchType.LAZY)
    public Set<User> getUsersFavorited() {
        return usersFavorited;
    }

    public void setUsersFavorited(Set<User> usersFavorited) {
        this.usersFavorited = usersFavorited;
    }
}
