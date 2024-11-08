package com.cl.test.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

@Table(name = "roles_has_users")
public class RolesHasUsers {

    @Id
    private Long id;

    private Long roleId;
    private Long userId;
}