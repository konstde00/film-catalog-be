package com.konstde00.filmcatalog.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.konstde00.filmcatalog.model.enums.Genre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "films")
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = PRIVATE)
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "films_id_seq")
    @SequenceGenerator(name = "films_id_seq",
            sequenceName = "films_id_seq", allocationSize = 1)
    Long id;

    String name;

    @Enumerated(EnumType.STRING)
    Genre genre;

    @Column(name = "duration_mins")
    Integer durationMins;

    String company;

    String director;

    String producers;

    String writers;

    @Column(name = "film_cast")
    String cast;

    @Column(name = "trailer_url")
    String trailerUrl;

    String synopsis;

    @Column(name = "completion_year")
    Integer completionYear;

    @ManyToMany
    @JoinTable(
            name = "films_collections",
            joinColumns = @JoinColumn(name = "film_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "collection_id", referencedColumnName = "id")
    )
    List<Collection> collections = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "created_by")
    User createdBy;

    @CreationTimestamp
    @Column(name = "created_at")
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;
}
