package com.example.enrollingservice.model;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

}
