package com.trs.tickets.model.entity;

import com.trs.tickets.model.PlaceType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Place extends BaseEntity {
//    @ManyToOne(optional = false)
//    private Hall hall;

    @ManyToOne(optional = false)
    private Session session;

    @Column(nullable = false)
    private Integer row;

    @Column(nullable = false)
    private Integer number;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private PlaceType placeType;

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Place place = (Place) o;
        return getId() != null && Objects.equals(getId(), place.getId());
    }
}
