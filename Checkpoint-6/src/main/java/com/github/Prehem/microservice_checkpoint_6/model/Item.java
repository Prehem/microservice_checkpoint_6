package com.prehem.checkpoint6.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private int quantidade;

    // Construtores, Getters e Setters (omitidos para brevidade)
    public Item() {}

    public Item(String nome, int quantidade) {
        this.nome = nome;
        this.quantidade = quantidade;
    }
    // Getters and Setters...
}