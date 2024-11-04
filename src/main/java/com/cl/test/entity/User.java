package com.cl.test.entity;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("users")
public class User {
    @Id
    private Long id;

    @NotNull
    @Column("first_name")
    private String firstName;

    @NotNull
    @Column("last_name")
    private String lastName;

    @NotNull
    @Column("email")
    private String email;

    @NotNull
    @Column("role")
    private UserRole role;
}