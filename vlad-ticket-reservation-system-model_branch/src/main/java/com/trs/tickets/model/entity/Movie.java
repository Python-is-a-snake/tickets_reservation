package com.trs.tickets.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Movie extends BaseEntity {

    @Column(nullable = false)
    private String    title;

    @Column(length = 1000)
    private String    description;

    private String    genre;

    private String    director;

    private String    actors;

    private Integer   duration;

    private String    country;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    private String    posterUrl;

    private String    trailerUrl;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<Session> sessions = new ArrayList<>();

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Movie movie = (Movie) o;
        return getId() != null && Objects.equals(getId(), movie.getId());
    }
}
