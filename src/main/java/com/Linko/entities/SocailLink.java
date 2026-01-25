package com.Linko.entities;
import jakarta.persistence.*;
import lombok.*;


@Entity

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SocailLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String link;
    private String linkType; // e.g., "Facebook", "Twitter"
  
    @ManyToOne
    private Contact contact;

}
