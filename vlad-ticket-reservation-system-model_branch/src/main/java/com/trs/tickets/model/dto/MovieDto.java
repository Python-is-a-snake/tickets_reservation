package com.trs.tickets.model.dto;

import com.trs.tickets.model.entity.Session;
import com.trs.tickets.model.entity.User;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MovieDto extends BaseDto {
    @NotEmpty(message = "Title can not be empty")
    @Length(min = 3, message = "Too short")
    private String    title;

    @Length(max = 1000, min = 10, message = "Too long or too short")
    private String description;

    @NotEmpty(message = "Genre can not be empty")
    private String    genre;

    @NotEmpty(message = "Director can not be empty")
    private String    director;

    @NotEmpty(message = "Actors can not be empty")
    private String    actors;

    @NotNull(message = "Duration can not be empty")
    @Min(message = "Duration must be longer than 0 mins", value = 0)
    @Max(message = "Duration must be not longer than 900 mins", value = 900)
    private Integer   duration;

    @NotEmpty(message = "Country can not be empty")
    private String    country;

    @NotNull(message = "Release date can not be empty")
    @Past(message = "Release Date must be in past")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @NotEmpty(message = "Poster Url can not be empty")
    @URL(message = "Poster Url must be URL")
    private String    posterUrl;

    @NotEmpty(message = "Trailer Url can not be empty")
    @URL(message = "Trailer Url must be an URL")
    private String    trailerUrl;

    private List<Session> sessions = new ArrayList<>();

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public String getTrailerCode() {
        String videoCode = null;

        String[] parts = trailerUrl.split("\\?v=");
        if (parts.length > 1) {
            videoCode = parts[1];
            int ampersandIndex = videoCode.indexOf('&');
            if (ampersandIndex != -1) {
                videoCode = videoCode.substring(0, ampersandIndex);
            }
        }
        return videoCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    public Map<LocalDate, List<Session>> getSessionsGroupedByDateTime() {
        return sessions.stream()
                .sorted(Comparator.comparing(Session::getSessionDateTime))
                .collect(Collectors.groupingBy((Session session) -> session.getSessionDateTime().toLocalDate(), TreeMap::new, Collectors.toList()));
    }
}
