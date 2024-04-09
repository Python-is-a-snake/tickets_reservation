package com.trs.tickets.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Session extends BaseEntity {
    @ManyToOne(optional = false)
    private Movie movie;

    @ManyToOne(optional = false)
    private Hall hall;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime sessionDateTime;

    @ToString.Exclude
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Place> places = new ArrayList<>();

    @Column
    private Boolean isActive;

    public boolean isAvailable() {
        return LocalDateTime.now().isBefore(sessionDateTime);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Session session = (Session) o;
        return getId() != null && Objects.equals(getId(), session.getId());
    }
}
