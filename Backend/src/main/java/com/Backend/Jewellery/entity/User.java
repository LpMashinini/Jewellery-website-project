package com.Backend.Jewellery.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.print.attribute.standard.MediaSize;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "user_auth")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = false, length = 255)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "password", nullable = false, unique = true, length = 255)
    private String password;

    @Column(name = "isVerified", nullable = false)
    private Boolean isVerified;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;


    @ManyToMany
    @JoinTable(
            name = "user_otp",
            joinColumns = @JoinColumn(name = "otp"),
            inverseJoinColumns = @JoinColumn( name = "user_id")
    )
    List<Otp> otp = new ArrayList<>();
}
