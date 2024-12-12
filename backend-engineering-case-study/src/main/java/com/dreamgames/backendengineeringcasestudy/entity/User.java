package com.dreamgames.backendengineeringcasestudy.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "ab_group", nullable = false, length = 1)
    private AbGroup abGroup; // Enum: A or B

    @Column(name = "level", nullable = false)
    private int level = 1;

    @Column(name = "coins", nullable = false)
    private int coins = 2000;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Constructors, getters, and setters
    public User() {}

    public User(Integer id, AbGroup abGroup, int level, int coins, LocalDateTime createdAt) {
        this.id = id;
        this.abGroup = abGroup;
        this.level = level;
        this.coins = coins;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AbGroup getAbGroup() {
        return abGroup;
    }

    public void setAbGroup(AbGroup abGroup) {
        this.abGroup = abGroup;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}