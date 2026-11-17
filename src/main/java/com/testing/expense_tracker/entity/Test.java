package com.testing.expense_tracker.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Test")
@Getter
@Setter
@NoArgsConstructor
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name" , nullable = false, length = 150)
    private String name;

    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "address", nullable = true, length = 1000)
    private String address;
}
