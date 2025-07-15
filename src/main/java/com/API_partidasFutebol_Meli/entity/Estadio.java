package com.API_partidasFutebol_Meli.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "estadios") //uniqueConstraints = @UniqueConstraint(ColumnNames = "nome"))
public class Estadio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
