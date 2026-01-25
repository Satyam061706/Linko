package com.Linko.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "email_from")
    private String from;

    @Column(name = "user_name")
    private String name;

    @Column(name = "email_to")
    private String to;

    @Column(name = "email_subject")
    private String subject;

    @Column(length = 1000, name = "email_content",columnDefinition = "TEXT")
    private String content;

    // private MultipartFile sendingImage;

    @ManyToOne
    @JsonIgnore
    private User user;

    @CreationTimestamp
    private LocalDate createdAt;

    @Column(length = 1000)
    private String picture;

}
