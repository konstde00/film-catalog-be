package com.konstde00.filmcatalog.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@Table(name = "reviews")
@FieldDefaults(level = PRIVATE)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reviews_id_seq")
    @SequenceGenerator(name = "reviews_id_seq",
            sequenceName = "reviews_id_seq", allocationSize = 1)
    Long id;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    User creator;

    @OneToOne
    @JoinColumn(name = "film_id")
    Film film;

    String comment;

    Integer rating;

    @CreationTimestamp
    @Column(name = "created_at")
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updatedAt")
    LocalDateTime updatedAt;
}
