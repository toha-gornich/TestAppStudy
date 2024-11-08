package com.cl.test.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Data
@Table("questions")
public class Question {
    @Id
    private Long id;

    private String text;

    @Column("type")
    private QuestionType type;

    @Column("test_id")
    private Long testId;
}