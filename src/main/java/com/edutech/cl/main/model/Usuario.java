package com.edutech.cl.main.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "usuario")
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 20)
    private String rol;

    // Si necesitas relaciones con otras entidades, por ejemplo:
    // @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    // private List<Algo> algoList;

}