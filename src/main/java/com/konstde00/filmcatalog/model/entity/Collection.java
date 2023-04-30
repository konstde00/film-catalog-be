package com.konstde00.filmcatalog.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
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
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "collections")
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = PRIVATE)
public class Collection {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "collections_id_seq")
    @SequenceGenerator(name = "collections_id_seq",
            sequenceName = "collections_id_seq", allocationSize = 1)
    Long id;

    String name;

    String description;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    User creator;

    @ManyToMany
    @JoinTable(
            name = "films_collections",
            joinColumns = @JoinColumn(name = "collection_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "film_id", referencedColumnName = "id")
    )
    List<Film> films;

    @CreationTimestamp
    @Column(name = "created_at")
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    public Collection(String name) {
        this.name = name;
    }
}
