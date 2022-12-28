package ru.practicum.user.model;

import lombok.*;
import org.springframework.data.annotation.ReadOnlyProperty;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ReadOnlyProperty
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email", unique = true)
    private String email;
}