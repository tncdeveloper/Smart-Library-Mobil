package com.smart.smartLibraryWeb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Schema(hidden = true)
public class BookRating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int rating; // 1–5 arasında

    @ManyToOne
    @JsonIgnore
    private Book book;

    @ManyToOne
    @JsonIgnore
    private Student student;

    private LocalDateTime createdAt;

    @Column(length = 1000) // Uzun yorumlara izin vermek için
    private String comment;

}
