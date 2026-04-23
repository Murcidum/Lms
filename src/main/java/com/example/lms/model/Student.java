package com.example.lms.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
    UUID id;

    @Column(nullable = false)
    String name;

    @Column(nullable = false)
    String surname;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    Group group;

}
