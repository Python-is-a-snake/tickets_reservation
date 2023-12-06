package com.trs.tickets.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.trs.tickets.model.HallType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIdentityInfo(property = "id", generator = ObjectIdGenerators.PropertyGenerator.class)
public class Hall extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(optional = false)
    private Cinema cinema;

    @Enumerated(value = EnumType.STRING)
    private HallType type;

//    @ToString.Exclude
//    @OneToMany(mappedBy = "hall")
//    private List<Place> places = new ArrayList<>();

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Hall hall = (Hall) o;
        return getId() != null && Objects.equals(getId(), hall.getId());
    }
}
