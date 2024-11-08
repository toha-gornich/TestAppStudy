package com.cl.test.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("test_results")
public class TestResult {
    @Id
    private Long id;

    @Column("test_id")
    private Long testId;

    @Column("user_id")
    private Long userId;

    private Integer score;

    @Column("completion_date")
    private LocalDateTime completionDate;
}