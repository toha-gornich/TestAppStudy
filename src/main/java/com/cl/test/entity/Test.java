package com.cl.test.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Data
@Table("tests")
public class Test {
    @Id
    private Long id;

    private String subject;
    private String title;
    private String description;

    @Column("tutor_id")
    private Long tutorId;

    @Column("is_active")
    private Boolean isActive;

    public void setActive(Boolean active) {
        this.isActive = active;
    }

    public Boolean isActive() {
        return isActive;
    }
}